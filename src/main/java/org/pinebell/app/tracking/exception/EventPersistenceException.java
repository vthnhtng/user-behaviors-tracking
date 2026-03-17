package org.pinebell.app.tracking.exception;

public class EventPersistenceException extends RuntimeException {

    private final String userId;
    private final String eventType;

    public EventPersistenceException(String message, Throwable cause, String userId, String eventType) {
        super(message, cause);
        this.userId = userId;
        this.eventType = eventType;
    }

    public EventPersistenceException(String message, String userId, String eventType) {
        super(message);
        this.userId = userId;
        this.eventType = eventType;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventType() {
        return eventType;
    }
}
