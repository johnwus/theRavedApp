package com.raved.notification.exception;

/**
 * Exception thrown when a notification is not found
 */
public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationNotFoundException(Long notificationId) {
        super("Notification not found with ID: " + notificationId);
    }
}
