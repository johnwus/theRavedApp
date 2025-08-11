package com.raved.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * NotificationDeliveryLog Entity for TheRavedApp
 * 
 * Represents delivery logs for notifications across different channels.
 * Based on the notification_delivery_logs table schema.
 */
@Entity
@Table(name = "notification_delivery_logs", indexes = {
    @Index(name = "idx_notification_delivery_logs_notification", columnList = "notification_id"),
    @Index(name = "idx_notification_delivery_logs_channel", columnList = "delivery_channel"),
    @Index(name = "idx_notification_delivery_logs_status", columnList = "delivery_status"),
    @Index(name = "idx_notification_delivery_logs_recipient", columnList = "recipient"),
    @Index(name = "idx_notification_delivery_logs_created_at", columnList = "created_at")
})
public class NotificationDeliveryLog {

    /**
     * Enum representing the delivery channel
     */
    public enum DeliveryChannel {
        EMAIL, SMS, PUSH
    }

    /**
     * Enum representing the delivery status
     */
    public enum DeliveryStatus {
        PENDING, SENT, DELIVERED, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_id", nullable = false)
    private Long notificationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_channel", nullable = false, length = 20)
    private DeliveryChannel deliveryChannel;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false, length = 20)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false, length = 255)
    private String recipient; // Email address, phone number, or device token

    @Column(name = "attempt_count")
    private Integer attemptCount = 1;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @Column(name = "retry_after")
    private LocalDateTime retryAfter;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructors
    public NotificationDeliveryLog() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public NotificationDeliveryLog(Long notificationId, DeliveryChannel deliveryChannel, String recipient) {
        this();
        this.notificationId = notificationId;
        this.deliveryChannel = deliveryChannel;
        this.recipient = recipient;
        this.deliveryStatus = DeliveryStatus.PENDING;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public DeliveryChannel getDeliveryChannel() {
        return deliveryChannel;
    }

    public void setDeliveryChannel(DeliveryChannel deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(LocalDateTime failedAt) {
        this.failedAt = failedAt;
    }

    public LocalDateTime getRetryAfter() {
        return retryAfter;
    }

    public void setRetryAfter(LocalDateTime retryAfter) {
        this.retryAfter = retryAfter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Lifecycle methods
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void markAsSent() {
        this.deliveryStatus = DeliveryStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void markAsDelivered() {
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void markAsFailed(String errorMessage) {
        this.deliveryStatus = DeliveryStatus.FAILED;
        this.failedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
        this.attemptCount++;
    }

    public void incrementAttemptCount() {
        this.attemptCount++;
    }

    public boolean canRetry() {
        return this.deliveryStatus == DeliveryStatus.FAILED && 
               (this.retryAfter == null || LocalDateTime.now().isAfter(this.retryAfter));
    }

    @Override
    public String toString() {
        return "NotificationDeliveryLog{" +
                "id=" + id +
                ", notificationId=" + notificationId +
                ", deliveryChannel=" + deliveryChannel +
                ", deliveryStatus=" + deliveryStatus +
                ", recipient='" + recipient + '\'' +
                ", attemptCount=" + attemptCount +
                ", createdAt=" + createdAt +
                '}';
    }
}


