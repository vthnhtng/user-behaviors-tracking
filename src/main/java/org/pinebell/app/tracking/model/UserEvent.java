package org.pinebell.app.tracking.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;
import java.util.Map;

public record UserEvent(
    UUID eventId,
    @NotNull(message = "eventType is required")
    EventType eventType,
    @NotBlank(message = "userId is required")
    String userId,
    @NotBlank(message = "sessionId is required")
    String sessionId,
    String productId,
    long timestamp,
    String device,
    String ip,
    Map<String, Object> metadata
) {
}