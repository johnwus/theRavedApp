package com.raved.common.enums;

/**
 * User role enumeration
 */
public enum UserRole {
    ADMIN("ADMIN", "System Administrator", 100),
    MODERATOR("MODERATOR", "Content Moderator", 80),
    FACULTY("FACULTY", "Faculty Member", 60),
    STUDENT("STUDENT", "Student", 40),
    USER("USER", "Regular User", 20),
    GUEST("GUEST", "Guest User", 10);

    private final String code;
    private final String description;
    private final int priority;

    UserRole(String code, String description, int priority) {
        this.code = code;
        this.description = description;
        this.priority = priority;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown user role code: " + code);
    }

    public boolean hasHigherPriorityThan(UserRole other) {
        return this.priority > other.priority;
    }

    public boolean canModerate() {
        return this.priority >= MODERATOR.priority;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isAcademic() {
        return this == FACULTY || this == STUDENT;
    }
}
