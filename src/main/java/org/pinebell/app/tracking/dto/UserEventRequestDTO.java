package org.pinebell.app.tracking.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserEventRequestDTO {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "eventType is required")
    private String eventType;

    @Size(max = 10000, message = "Payload too large")
    private Map<String, Object> payload;

    public UserEventRequestDTO() {
    }

    public UserEventRequestDTO(String userId, String eventType, Map<String, Object> payload) {
        this.userId = userId;
        this.eventType = eventType;
        this.payload = payload;
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

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
