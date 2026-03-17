package org.pinebell.app.tracking.repository;

import org.pinebell.app.tracking.model.UserEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository {

    void save(UserEvent event);

    void saveBatch(List<UserEvent> events);

    List<UserEvent> findByUserId(String userId, int limit);

    List<UserEvent> findByEventType(String eventType, LocalDateTime from, LocalDateTime to, int limit);

    long countByEventType(String eventType, LocalDateTime from, LocalDateTime to);
}
