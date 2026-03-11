package org.pinebell.app.tracking.controller;

import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TrackingController {

    private final EventService eventService;

    public TrackingController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/publish")
    public ResponseEntity<Map<String, Object>> publishEvent(@Valid @RequestBody UserEvent event) {
        eventService.publishEvent(event);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Event published successfully");
        response.put("userId", event.getUserId());
        response.put("eventType", event.getEventType());

        return ResponseEntity.ok(response);
    }
}
