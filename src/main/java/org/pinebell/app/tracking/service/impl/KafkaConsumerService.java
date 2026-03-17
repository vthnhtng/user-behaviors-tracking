package org.pinebell.app.tracking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pinebell.app.tracking.exception.EventPersistenceException;
import org.pinebell.app.tracking.exception.EventSerializationException;
import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.repository.EventRepository;
import org.pinebell.app.tracking.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KafkaConsumerService implements ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final ObjectMapper objectMapper;
    private final EventRepository eventRepository;

    public KafkaConsumerService(ObjectMapper objectMapper, EventRepository eventRepository) {
        this.objectMapper = objectMapper;
        this.eventRepository = eventRepository;
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
            if (event.getTimestamp() == null) {
                event.setTimestamp(LocalDateTime.now());
            }
            logger.info("Event deserialized successfully: userId={}, eventType={}, timestamp={}",
                    event.getUserId(),
                    event.getEventType(),
                    event.getTimestamp());

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

        try {
            eventRepository.save(event);
            logger.info("Event persisted: userId={}, eventType={}",
                    event.getUserId(),
                    event.getEventType());
        } catch (EventSerializationException e) {
            logger.error("Event payload serialization failed (non-retryable): userId={}, eventType={}",
                    e.getUserId(), e.getEventType(), e);
        } catch (EventPersistenceException e) {
            logger.error("Event persistence failed: userId={}, eventType={}, cause={}",
                    e.getUserId(), e.getEventType(), e.getCause().getClass().getSimpleName(), e);
        }
    }
}
