package com.raved.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Request DTO for sending notifications
 */
public class SendNotificationRequest {

    @NotEmpty(message = "Recipient user IDs are required")
    private List<Long> recipientUserIds;

    @NotBlank(message = "Notification type is required")
    private String notificationType; // "push", "email", "sms", "in_app"

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    private String templateId; // Optional: use template instead of title/message

    private Map<String, Object> templateVariables; // Variables for template substitution

    private String priority; // "low", "normal", "high", "urgent"

    private String category; // "social", "ecommerce", "system", "marketing"

    private Map<String, Object> data; // Additional data payload

    private String actionUrl; // URL to navigate when notification is clicked

    private Boolean sendImmediately = true;

    private String scheduledAt; // ISO datetime string for scheduled notifications

    // Constructors
    public SendNotificationRequest() {
    }

    public SendNotificationRequest(List<Long> recipientUserIds, String notificationType, String title, String message) {
        this.recipientUserIds = recipientUserIds;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
    }

    // Getters and Setters
    public List<Long> getRecipientUserIds() {
        return recipientUserIds;
    }

    public void setRecipientUserIds(List<Long> recipientUserIds) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Boolean getSendImmediately() {
        return sendImmediately;
    }

    public void setSendImmediately(Boolean sendImmediately) {
        this.sendImmediately = sendImmediately;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
