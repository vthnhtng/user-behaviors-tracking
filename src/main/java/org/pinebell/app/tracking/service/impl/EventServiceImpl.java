package org.pinebell.app.tracking.service.impl;

import org.pinebell.app.tracking.model.UserEvent;
import org.pinebell.app.tracking.service.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final String topic;

    public EventServiceImpl(KafkaTemplate<String, UserEvent> kafkaTemplate,
                           @Value("${kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publishEvent(UserEvent event) {
        kafkaTemplate.send(topic, event.getUserId(), event);
    }
}