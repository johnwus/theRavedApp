package com.raved.realtime.model;

/**
 * Message Type Enum for TheRavedApp
 * 
 * Defines the different types of messages that can be sent
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
public enum MessageType {
    TEXT("Text message"),
    IMAGE("Image message"),
    VIDEO("Video message"),
    AUDIO("Audio message"),
    FILE("File message"),
    SYSTEM("System message"),
    LOCATION("Location message"),
    CONTACT("Contact message");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}




