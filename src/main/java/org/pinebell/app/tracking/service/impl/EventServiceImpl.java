package org.pinebell.app.tracking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.pinebell.app.tracking.exception.EventProcessingException;
import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.service.EventService;

@Service
public class EventServiceImpl implements EventService {

    // private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    // private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    // @Value("${spring.kafka.producer.topic}")
    // private String topic;

    // public EventServiceImpl(KafkaTemplate<String, UserEvent> kafkaTemplate) {
    //     this.kafkaTemplate = kafkaTemplate;
    // }

    @Override
    public void processEvent(UserEvent userEvent) {
        return;
        // try {
        //     // Use sessionId as partition key for ordering within sessions
        //     String partitionKey = userEvent.sessionId();

        //     kafkaTemplate.send(topic, partitionKey, userEvent)
        //         .whenComplete((result, ex) -> {
        //             if (ex != null) {
        //                 log.error("Failed to send event {} to Kafka: {}",
        //                     userEvent.eventId(), ex.getMessage());
        //             } else {
        //                 log.debug("Event {} sent to topic {} partition {} at offset {}",
        //                     userEvent.eventId(),
        //                     result.getRecordMetadata().topic(),
        //                     result.getRecordMetadata().partition(),
        //                     result.getRecordMetadata().offset());
        //             }
        //         });
        // } catch (Exception e) {
        //     log.error("Error processing event {}: {}", userEvent.eventId(), e.getMessage(), e);
        //     throw new EventProcessingException("Failed to process event: " + userEvent.eventId(), e);
        // }
    }
}