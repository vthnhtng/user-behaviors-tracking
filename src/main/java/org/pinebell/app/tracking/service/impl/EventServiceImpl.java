package org.pinebell.app.tracking.service.impl;

import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public EventServiceImpl(KafkaTemplate<String, String> kafkaTemplate,
                           ObjectMapper objectMapper,
                           @Value("${kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    @Override
    public void publishEvent(UserEvent event) {
        String data = serializeEvent(event);

        kafkaTemplate.send(topic, event.getUserId(), data)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to publish event for user {}", event.getUserId(), ex);
                } else {
                    logger.info("Event published successfully to topic {} for user {}", topic, event.getUserId());
                }
            });
    }

    private String serializeEvent(UserEvent event) throws JacksonException {
        return objectMapper.writeValueAsString(event);
    }
}
