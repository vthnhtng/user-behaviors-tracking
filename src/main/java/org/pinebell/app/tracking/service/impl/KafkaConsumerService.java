package org.pinebell.app.tracking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService implements ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final ObjectMapper objectMapper;

    public KafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
        topics = "${spring.kafka.producer.topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @Override
    public void consume(String message) {
        logger.info("Raw message received: {}", message);

        try {
            UserEvent event = objectMapper.readValue(message, UserEvent.class);
            logger.info("Event deserialized successfully: userId={}, eventType={}, timestamp={}",
                    event.getUserId(),
                    event.getEventType(),
                    event.getTimestamp());

            // Process the event - business logic would go here
            processEvent(event);

        } catch (Exception e) {
            logger.error("Failed to parse message: error={}, message={}",
                    e.getMessage(),
                    message,
                    e);
        }
    }

    private void processEvent(UserEvent event) {
        logger.debug("Processing event for userId={}, eventType={}",
                event.getUserId(),
                event.getEventType());
        // Add business logic here
    }
}
