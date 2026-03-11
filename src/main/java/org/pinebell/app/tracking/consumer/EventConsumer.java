package org.pinebell.app.tracking.consumer;

import org.pinebell.app.tracking.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "${kafka.producer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(UserEvent event) {
        logger.info("Received event: userId={}, eventType={}, timestamp={}, payload={}",
                event.getUserId(),
                event.getEventType(),
                event.getTimestamp(),
                event.getPayload());
    }
}