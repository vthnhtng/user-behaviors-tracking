package org.pinebell.app.tracking.model;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserEvent {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "eventType is required")
    private EventType eventType;

    private LocalDateTime timestamp;

    @Size(max = 10000, message = "Payload too large")
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