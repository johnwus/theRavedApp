package com.raved.user.model;

/**
 * User Roles for TheRavedApp
 */
public enum Role {
    USER("User"),
    ADMIN("Administrator"),
    MODERATOR("Moderator"),
    ARTIST("Artist"),
    ORGANIZER("Event Organizer"),
    VIP("VIP User"),
    FACULTY("Faculty Member"),
    STUDENT("Student");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
