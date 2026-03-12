package org.pinebell.app.tracking.service;

import org.pinebell.app.tracking.dto.UserEventRequestDTO;

public interface ProducerService {
    void publish(UserEventRequestDTO event);
}