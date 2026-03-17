package org.pinebell.app.tracking.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pinebell.app.tracking.exception.EventPersistenceException;
import org.pinebell.app.tracking.exception.EventSerializationException;
import org.pinebell.app.tracking.model.EventType;
import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class ClickHouseEventRepository implements EventRepository {

    private static final Logger logger = LoggerFactory.getLogger(ClickHouseEventRepository.class);

    private static final String INSERT_SQL =
            "INSERT INTO user_events (user_id, event_type, event_timestamp, payload) "
                    + "VALUES (:userId, :eventType, :eventTimestamp, :payload)";

    private static final String SELECT_BY_USER_SQL =
            "SELECT * FROM user_events WHERE user_id = :userId "
                    + "ORDER BY event_timestamp DESC LIMIT :limit";

    private static final String SELECT_BY_TYPE_SQL =
            "SELECT * FROM user_events WHERE event_type = :eventType "
                    + "AND event_timestamp BETWEEN :from AND :to "
                    + "ORDER BY event_timestamp DESC LIMIT :limit";

    private static final String COUNT_BY_TYPE_SQL =
            "SELECT count() FROM user_events WHERE event_type = :eventType "
                    + "AND event_timestamp BETWEEN :from AND :to";

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final ObjectMapper objectMapper;
    private final RowMapper<UserEvent> rowMapper;

    public ClickHouseEventRepository(
            @Qualifier("clickHouseNamedJdbcTemplate") NamedParameterJdbcTemplate namedJdbcTemplate,
            ObjectMapper objectMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.objectMapper = objectMapper;
        this.rowMapper = new UserEventRowMapper();
    }

    @Override
    public void save(UserEvent event) {
        try {
            namedJdbcTemplate.update(INSERT_SQL, toParameterSource(event));
        } catch (DataAccessException e) {
            throw translateException("Failed to persist event", e, event);
        }
    }

    @Override
    public void saveBatch(List<UserEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        SqlParameterSource[] batchParams = events.stream()
                .map(this::toParameterSource)
                .toArray(SqlParameterSource[]::new);

        try {
            namedJdbcTemplate.batchUpdate(INSERT_SQL, batchParams);
        } catch (DataAccessException e) {
            throw new EventPersistenceException(
                    "Failed to persist event batch of size " + events.size(),
                    e, null, null);
        }
    }

    @Override
    public List<UserEvent> findByUserId(String userId, int limit) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("limit", limit);

        try {
            return namedJdbcTemplate.query(SELECT_BY_USER_SQL, params, rowMapper);
        } catch (DataAccessException e) {
            throw new EventPersistenceException(
                    "Failed to query events by userId", e, userId, null);
        }
    }

    @Override
    public List<UserEvent> findByEventType(String eventType, LocalDateTime from, LocalDateTime to, int limit) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("eventType", eventType)
                .addValue("from", Timestamp.valueOf(from))
                .addValue("to", Timestamp.valueOf(to))
                .addValue("limit", limit);

        try {
            return namedJdbcTemplate.query(SELECT_BY_TYPE_SQL, params, rowMapper);
        } catch (DataAccessException e) {
            throw new EventPersistenceException(
                    "Failed to query events by eventType", e, null, eventType);
        }
    }

    @Override
    public long countByEventType(String eventType, LocalDateTime from, LocalDateTime to) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("eventType", eventType)
                .addValue("from", Timestamp.valueOf(from))
                .addValue("to", Timestamp.valueOf(to));

        try {
            Long count = namedJdbcTemplate.queryForObject(COUNT_BY_TYPE_SQL, params, Long.class);
            return count != null ? count : 0L;
        } catch (DataAccessException e) {
            throw new EventPersistenceException(
                    "Failed to count events by eventType", e, null, eventType);
        }
    }

    private MapSqlParameterSource toParameterSource(UserEvent event) {
        return new MapSqlParameterSource()
                .addValue("userId", event.getUserId())
                .addValue("eventType", event.getEventType().name())
                .addValue("eventTimestamp", Timestamp.valueOf(event.getTimestamp()))
                .addValue("payload", serializePayload(event));
    }

    private EventPersistenceException translateException(String message, DataAccessException cause, UserEvent event) {
        String userId = event != null ? event.getUserId() : null;
        String eventType = event != null ? event.getEventType().name() : null;

        if (cause instanceof TransientDataAccessException) {
            logger.warn("Transient storage error (retryable): {}", cause.getMessage());
        } else if (cause instanceof NonTransientDataAccessException) {
            logger.error("Non-transient storage error: {}", cause.getMessage());
        }

        return new EventPersistenceException(message, cause, userId, eventType);
    }

    private String serializePayload(UserEvent event) {
        Map<String, Object> payload = event.getPayload();
        if (payload == null || payload.isEmpty()) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new EventSerializationException(
                    "Failed to serialize event payload",
                    e,
                    event.getUserId(),
                    event.getEventType().name());
        }
    }

    private Map<String, Object> deserializePayload(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            logger.warn("Failed to deserialize payload, returning empty map: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    private class UserEventRowMapper implements RowMapper<UserEvent> {
        @Override
        public UserEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserEvent event = new UserEvent();
            event.setUserId(rs.getString("user_id"));
            event.setEventType(EventType.valueOf(rs.getString("event_type")));
            event.setTimestamp(rs.getTimestamp("event_timestamp").toLocalDateTime());
            event.setPayload(deserializePayload(rs.getString("payload")));
            return event;
        }
    }
}
