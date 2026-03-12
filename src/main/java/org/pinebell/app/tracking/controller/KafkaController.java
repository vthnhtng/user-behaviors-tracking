package org.pinebell.app.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.pinebell.app.tracking.dto.UserEventRequestDTO;
import org.pinebell.app.tracking.dto.UserEventResponseDTO;
import org.pinebell.app.tracking.service.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class KafkaController {

    private final ProducerService producerService;

    @Autowired
    public KafkaController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public UserEventResponseDTO send(@Valid @RequestBody UserEventRequestDTO request) {
        try {
            producerService.publish(request);

            return new UserEventResponseDTO(
                    true,
                    "Event published successfully",
                    request.getUserId(),
                    request.getEventType(),
                    null,
                    request.getPayload()
            );
        } catch (Exception e) {
            return new UserEventResponseDTO(
                    false,
                    "Failed to publish event: " + e.getMessage(),
                    request.getUserId(),
                    request.getEventType(),
                    null,
                    request.getPayload()
            );
        }
    }
}
