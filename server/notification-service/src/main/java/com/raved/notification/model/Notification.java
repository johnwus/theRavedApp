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
import java.util.Map;

/**
 * Notification Document for TheRavedApp MongoDB
 *
 * Represents individual notifications sent to users.
 * Converted from JPA entity
 * to MongoDB document.
 */
@Document(collection = "notifications")
@CompoundIndexes({
    @CompoundIndex(name = "idx_user_type", def = "{'userId': 1, 'notificationType': 1}"),
    @CompoundIndex(name = "idx_user_read", def = "{'userId': 1, 'isRead': 1}"),
    @CompoundIndex(name = "idx_user_created", def = "{'userId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "idx_delivery_scheduled", def = "{'deliveryStatus': 1, 'scheduledAt': 1}")
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
    private String id;

    @Field("userId")
    @Indexed
    @NotNull
    private String userId; // Reference to user service (changed to String for MongoDB)

    @Field("notificationType")
    @Indexed
    @NotBlank
    private String notificationType; // LIKE, COMMENT, FOLLOW, ORDER_UPDATE, etc.

    @Field("title")
    @NotBlank
    private String title;

    @Field("body")
    @NotBlank
    private String body;

    // Notification Data
    @Field("data")
    private Map<String, Object> data; // Additional notification data (changed to Map for MongoDB)

    @Field("actionUrl")
    private String actionUrl; // Deep link URL

    @Field("imageUrl")
    private String imageUrl;

    // Delivery Status
    @Field("isRead")
    @Indexed
    private Boolean isRead = false;

    @Field("isSent")
    private Boolean isSent = false;

    @Field("deliveryStatus")
    @Indexed
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    // Channels
    @Field("pushSent")
    private Boolean pushSent = false;

    @Field("emailSent")
    private Boolean emailSent = false;

    @Field("smsSent")
    private Boolean smsSent = false;

    // Timeline
    @Field("scheduledAt")
    @Indexed
    private LocalDateTime scheduledAt;

    @Field("sentAt")
    private LocalDateTime sentAt;

    @Field("readAt")
    private LocalDateTime readAt;

    @Field("expiresAt")
    private LocalDateTime expiresAt;

    @Field("createdAt")
    @Indexed
    private LocalDateTime createdAt;

    // Constructors
    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.deliveryStatus = DeliveryStatus.PENDING;
    }

    public Notification(String userId, String notificationType, String title, String body) {
        this();
        this.userId = userId;
        this.notificationType = notificationType;
        this.title = title;
        this.body = body;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
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

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
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
        this.deliveryStatus = DeliveryStatus.SENT;
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return DeliveryStatus.PENDING.equals(this.deliveryStatus);
    }

    public boolean isSent() {
        return DeliveryStatus.SENT.equals(this.deliveryStatus);
    }

    public boolean isFailed() {
        return DeliveryStatus.FAILED.equals(this.deliveryStatus);
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
