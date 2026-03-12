package org.pinebell.app.tracking.service.impl;

import org.pinebell.app.tracking.service.ProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService implements ProducerService {

    @Value("${spring.kafka.producer.topic}")
    private String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Message sent: " + message);
    }
}
