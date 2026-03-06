package org.pinebell.app.tracking.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.service.EventService;

@RestController
@RequestMapping("/api/v1")
public class TrackingController {

    private final EventService eventService;

    public TrackingController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/track")
    public ResponseEntity<Void> trackEvent(@Valid @RequestBody UserEvent userEvent) {
        eventService.processEvent(userEvent);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OKzzz");
    }
}
