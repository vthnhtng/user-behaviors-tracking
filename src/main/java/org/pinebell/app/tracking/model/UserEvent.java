package org.pinebell.app.tracking.model;

import java.time.LocalDateTime;
import java.util.Map;

public class UserEvent {
    private String userId;
    private EventType eventType;
    private LocalDateTime timestamp;
    private Map<String, Object> payload;

    public UserEvent() {
    }

    public UserEvent(String userId, EventType eventType, LocalDateTime timestamp, Map<String, Object> payload) {
        this.userId = userId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}