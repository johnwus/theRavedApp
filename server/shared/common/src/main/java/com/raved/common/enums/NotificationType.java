package com.raved.common.enums;

/**
 * Notification type enumeration
 */
public enum NotificationType {
    // Social notifications
    LIKE("like", "Like notification", "social"),
    COMMENT("comment", "Comment notification", "social"),
    FOLLOW("follow", "Follow notification", "social"),
    MENTION("mention", "Mention notification", "social"),
    SHARE("share", "Share notification", "social"),
    
    // E-commerce notifications
    ORDER_PLACED("order_placed", "Order placed", "ecommerce"),
    ORDER_CONFIRMED("order_confirmed", "Order confirmed", "ecommerce"),
    ORDER_SHIPPED("order_shipped", "Order shipped", "ecommerce"),
    ORDER_DELIVERED("order_delivered", "Order delivered", "ecommerce"),
    PAYMENT_SUCCESS("payment_success", "Payment successful", "ecommerce"),
    PAYMENT_FAILED("payment_failed", "Payment failed", "ecommerce"),
    
    // System notifications
    WELCOME("welcome", "Welcome message", "system"),
    VERIFICATION("verification", "Account verification", "system"),
    PASSWORD_RESET("password_reset", "Password reset", "system"),
    SECURITY_ALERT("security_alert", "Security alert", "system"),
    MAINTENANCE("maintenance", "System maintenance", "system"),
    
    // Content notifications
    POST_APPROVED("post_approved", "Post approved", "content"),
    POST_REJECTED("post_rejected", "Post rejected", "content"),
    CONTENT_FLAGGED("content_flagged", "Content flagged", "content"),
    
    // Chat notifications
    MESSAGE("message", "New message", "chat"),
    ROOM_INVITE("room_invite", "Room invitation", "chat"),
    
    // Marketing notifications
    PROMOTION("promotion", "Promotional offer", "marketing"),
    NEWSLETTER("newsletter", "Newsletter", "marketing"),
    ANNOUNCEMENT("announcement", "Announcement", "marketing");

    private final String code;
    private final String description;
    private final String category;

    NotificationType(String code, String description, String category) {
        this.code = code;
        this.description = description;
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public static NotificationType fromCode(String code) {
        for (NotificationType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown notification type code: " + code);
    }

    public boolean isSocialType() {
        return "social".equals(category);
    }

    public boolean isEcommerceType() {
        return "ecommerce".equals(category);
    }

    public boolean isSystemType() {
        return "system".equals(category);
    }

    public boolean isMarketingType() {
        return "marketing".equals(category);
    }

    public boolean isHighPriority() {
        return this == SECURITY_ALERT || this == PAYMENT_FAILED || this == ORDER_DELIVERED;
    }
}
