package com.raved.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Request DTO for sending bulk notifications
 */
public class SendBulkNotificationRequest {

    @NotEmpty(message = "Recipient user IDs are required")
    private List<String> recipientUserIds;

    @NotBlank(message = "Notification type is required")
    @Size(max = 50, message = "Notification type must not exceed 50 characters")
    private String notificationType;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Body is required")
    private String body;

    private String data; // Additional notification data as JSON string

    private String actionUrl; // Deep link URL

    private String imageUrl;

    private LocalDateTime scheduledAt;

    private LocalDateTime expiresAt;

    private Long templateId;

    private Map<String, Object> templateData;

    // Constructors
    public SendBulkNotificationRequest() {}

    public SendBulkNotificationRequest(List<String> recipientUserIds, String notificationType,
                                     String title, String body) {
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

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Map<String, Object> getTemplateData() {
        return templateData;
    }

    public void setTemplateData(Map<String, Object> templateData) {
        this.templateData = templateData;
    }

    @Override
    public String toString() {
        return "SendBulkNotificationRequest{" +
                "recipientUserIds=" + recipientUserIds +
                ", notificationType='" + notificationType + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", templateId=" + templateId +
                ", scheduledAt=" + scheduledAt +
                '}';
    }
}
