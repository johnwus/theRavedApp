package com.raved.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Notification Entity for TheRavedApp
 *
 * Represents individual notifications sent to users.
 * Based on the notifications table schema.
 */
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notifications_user", columnList = "user_id"),
        @Index(name = "idx_notifications_type", columnList = "notification_type"),
        @Index(name = "idx_notifications_read", columnList = "is_read"),
        @Index(name = "idx_notifications_created_at", columnList = "created_at"),
        @Index(name = "idx_notifications_delivery_status", columnList = "delivery_status"),
        @Index(name = "idx_notifications_scheduled", columnList = "scheduled_at")
})
public class Notification {

    /**
     * Enum representing the type of notification
     */
    public enum NotificationType {
        LIKED, COMMENTED, FOLLOWED, ORDER_CONFIRMED, ORDER_SHIPPED, ORDER_DELIVERED,
        PAYMENT_SUCCESS, PAYMENT_FAILED, WELCOME, PASSWORD_RESET, EMAIL_VERIFICATION,
        SYSTEM_MAINTENANCE, PROMOTIONAL
    }

    /**
     * Enum representing the delivery status of a notification
     */
    public enum DeliveryStatus {
        PENDING, SENT, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @Column(name = "notification_type", nullable = false, length = 50)
    private String notificationType; // LIKE, COMMENT, FOLLOW, ORDER_UPDATE, etc.

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    // Notification Data
    @Column(columnDefinition = "JSONB")
    private String data; // Additional notification data

    @Column(name = "action_url", columnDefinition = "TEXT")
    private String actionUrl; // Deep link URL

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    // Delivery Status
    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "is_sent")
    private Boolean isSent = false;

    @Column(name = "delivery_status", length = 20)
    private String deliveryStatus = "PENDING"; // PENDING, SENT, FAILED

    // Channels
    @Column(name = "push_sent")
    private Boolean pushSent = false;

    @Column(name = "email_sent")
    private Boolean emailSent = false;

    @Column(name = "sms_sent")
    private Boolean smsSent = false;

    // Timeline
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructors
    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(Long userId, String notificationType, String title, String body) {
        this();
        this.userId = userId;
        this.notificationType = notificationType;
        this.title = title;
        this.body = body;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Boolean getPushSent() {
        return pushSent;
    }

    public void setPushSent(Boolean pushSent) {
        this.pushSent = pushSent;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    public Boolean getSmsSent() {
        return smsSent;
    }

    public void setSmsSent(Boolean smsSent) {
        this.smsSent = smsSent;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Business Methods
    public void markAsSent() {
        this.isSent = true;
        this.sentAt = LocalDateTime.now();
        this.deliveryStatus = "SENT";
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return "PENDING".equals(this.deliveryStatus);
    }

    public boolean isSent() {
        return "SENT".equals(this.deliveryStatus);
    }

    public boolean isFailed() {
        return "FAILED".equals(this.deliveryStatus);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", notificationType='" + notificationType + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", isRead=" + isRead +
                ", isSent=" + isSent +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
