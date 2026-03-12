package org.pinebell.app.tracking.service.impl;

import org.pinebell.app.tracking.service.ConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService implements ConsumerService {

    @KafkaListener(
        topics = "${spring.kafka.producer.topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @Override
    public void consume(String message) {
        System.out.println("Message received: " + message);
    }
}
