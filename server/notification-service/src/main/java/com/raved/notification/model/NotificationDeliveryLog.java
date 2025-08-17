package com.raved.notification.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * NotificationDeliveryLog Document for TheRavedApp MongoDB
 *
 * Represents delivery logs for notifications across different channels.
 * Converted from JPA entity to MongoDB document.
 */
@Document(collection = "notification_delivery_logs")
@CompoundIndexes({
    @CompoundIndex(name = "idx_notification_channel", def = "{'notificationId': 1, 'deliveryChannel': 1}"),
    @CompoundIndex(name = "idx_status_created", def = "{'deliveryStatus': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "idx_recipient_channel", def = "{'recipient': 1, 'deliveryChannel': 1}")
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
    private String id;

    @Field("notificationId")
    @Indexed
    @NotNull
    private String notificationId; // Changed to String for MongoDB

    @Field("deliveryChannel")
    @Indexed
    @NotNull
    private DeliveryChannel deliveryChannel;

    @Field("deliveryStatus")
    @Indexed
    @NotNull
    private DeliveryStatus deliveryStatus;

    @Field("recipient")
    @Indexed
    @NotBlank
    private String recipient; // Email address, phone number, or device token

    @Field("attemptCount")
    private Integer attemptCount = 1;

    @Field("errorMessage")
    private String errorMessage;

    @Field("sentAt")
    private LocalDateTime sentAt;

    @Field("deliveredAt")
    private LocalDateTime deliveredAt;

    @Field("failedAt")
    private LocalDateTime failedAt;

    @Field("retryAfter")
    private LocalDateTime retryAfter;

    @Field("createdAt")
    @Indexed
    private LocalDateTime createdAt;

    @Field("updatedAt")
    private LocalDateTime updatedAt;

    // Constructors
    public NotificationDeliveryLog() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public NotificationDeliveryLog(String notificationId, DeliveryChannel deliveryChannel, String recipient) {
        this();
        this.notificationId = notificationId;
        this.deliveryChannel = deliveryChannel;
        this.recipient = recipient;
        this.deliveryStatus = DeliveryStatus.PENDING;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
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

    // Lifecycle methods for MongoDB
    public void updateTimestamp() {
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



