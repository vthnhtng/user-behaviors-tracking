package org.pinebell.app.tracking.exception;

public class EventSerializationException extends EventPersistenceException {

    public EventSerializationException(String message, Throwable cause, String userId, String eventType) {
        super(message, cause, userId, eventType);
    }
}
