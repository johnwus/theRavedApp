package com.raved.realtime.model;

/**
 * Message Status Enum for TheRavedApp
 * 
 * Defines the different statuses that a message can have
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
public enum MessageStatus {
    SENT("Message has been sent"),
    DELIVERED("Message has been delivered to recipient"),
    READ("Message has been read by recipient"),
    FAILED("Message failed to send"),
    PENDING("Message is pending to be sent");

    private final String description;

    MessageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}




