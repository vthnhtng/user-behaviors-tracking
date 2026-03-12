package org.pinebell.app.tracking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pinebell.app.tracking.dto.UserEventRequestDTO;
import org.pinebell.app.tracking.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService implements ProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(UserEventRequestDTO event) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(event);
            CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(topic, event.getUserId(), jsonMessage);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Event published successfully: userId={}, eventType={}, partition={}, offset={}",
                            event.getUserId(),
                            event.getEventType(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    logger.error("Failed to publish event: userId={}, eventType={}, error={}",
                            event.getUserId(),
                            event.getEventType(),
                            ex.getMessage(),
                            ex);
                }
            });
        } catch (Exception e) {
            logger.error("Failed to serialize event: userId={}, eventType={}, error={}",
                    event.getUserId(),
                    event.getEventType(),
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
