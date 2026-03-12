package org.pinebell.app.tracking.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class UserEventResponseDTO {

    private boolean success;
    private String message;
    private String eventId;
    private String userId;
    private String eventType;
    private LocalDateTime timestamp;
    private Map<String, Object> payload;

    public UserEventResponseDTO() {
    }

    public UserEventResponseDTO(boolean success, String message, String userId, String eventType,
                                 String eventId, Map<String, Object> payload) {
        this.success = success;
        this.message = message;
        this.eventId = eventId;
        this.userId = userId;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
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
