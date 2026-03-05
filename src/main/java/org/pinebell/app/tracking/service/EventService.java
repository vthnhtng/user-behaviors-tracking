package org.pinebell.app.tracking.service;

import org.pinebell.app.tracking.model.UserEvent;

public interface EventService {
    void processEvent(UserEvent userEvent);
}