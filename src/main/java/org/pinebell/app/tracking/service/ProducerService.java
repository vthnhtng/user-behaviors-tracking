package org.pinebell.app.tracking.service;

public interface ProducerService {
    void publish(String message);
}