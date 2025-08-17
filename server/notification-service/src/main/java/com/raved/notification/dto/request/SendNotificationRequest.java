package com.raved.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Request DTO for sending notifications
 */
public class SendNotificationRequest {

    @NotEmpty(message = "Recipient user IDs are required")
    private List<String> recipientUserIds; // Changed to String for MongoDB

    @NotBlank(message = "Notification type is required")
    @Size(max = 50, message = "Notification type must not exceed 50 characters")
    private String notificationType; // LIKE, COMMENT, FOLLOW, ORDER_UPDATE, etc.

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Body is required")
    private String body;

    private Map<String, Object> data; // Changed to Map for MongoDB

    private String actionUrl; // Deep link URL

    private String imageUrl;

    private String templateId; // Changed to String for MongoDB

    private Map<String, Object> templateVariables; // Variables for template substitution

    private LocalDateTime scheduledAt; // For scheduled notifications

    private LocalDateTime expiresAt; // When notification expires

    // Constructors
    public SendNotificationRequest() {
    }

    public SendNotificationRequest(List<String> recipientUserIds, String notificationType, String title, String body) {
        this.recipientUserIds = recipientUserIds;
        this.notificationType = notificationType;
        this.title = title;
        this.body = body;
    }

    // Getters and Setters
    public List<String> getRecipientUserIds() {
        return recipientUserIds;
    }

    public void setRecipientUserIds(List<String> recipientUserIds) {
        this.recipientUserIds = recipientUserIds;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, Object> getTemplateVariables() {
        return templateVariables;
    }

    public void setTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "SendNotificationRequest{" +
                "recipientUserIds=" + recipientUserIds +
                ", notificationType='" + notificationType + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", templateId=" + templateId +
                ", scheduledAt=" + scheduledAt +
                '}';
    }
}
