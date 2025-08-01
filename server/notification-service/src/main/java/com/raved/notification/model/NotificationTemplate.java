package com.raved.notification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * NotificationTemplate Entity for TheRavedApp
 *
 * Represents notification templates for different types of notifications.
 * Based on the notification_templates table schema.
 */
@Entity
@Table(name = "notification_templates", indexes = {
        @Index(name = "idx_notification_templates_type", columnList = "template_type"),
        @Index(name = "idx_notification_templates_name", columnList = "template_name"),
        @Index(name = "idx_notification_templates_active", columnList = "is_active")
})
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Template name is required")
    @Size(max = 255, message = "Template name must not exceed 255 characters")
    @Column(name = "template_name", nullable = false, unique = true)
    private String templateName;

    @Enumerated(EnumType.STRING)
    @Column(name = "template_type", nullable = false)
    private TemplateType templateType;

    @NotBlank(message = "Subject template is required")
    @Size(max = 500, message = "Subject template must not exceed 500 characters")
    @Column(name = "subject_template", nullable = false)
    private String subjectTemplate;

    @NotBlank(message = "Body template is required")
    @Column(name = "body_template", columnDefinition = "TEXT", nullable = false)
    private String bodyTemplate;

    @Column(name = "sms_template", columnDefinition = "TEXT")
    private String smsTemplate;

    @Column(name = "push_template", columnDefinition = "TEXT")
    private String pushTemplate;

    @Size(max = 1000, message = "Variables must not exceed 1000 characters")
    @Column(name = "template_variables", columnDefinition = "TEXT")
    private String templateVariables; // JSON string of available variables

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    // Enums
    public enum TemplateType {
        EMAIL, SMS, PUSH_NOTIFICATION, IN_APP
    }

    // Constructors
    public NotificationTemplate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public NotificationTemplate(String templateName, TemplateType templateType, String subjectTemplate,
            String bodyTemplate) {
        this();
        this.templateName = templateName;
        this.templateType = templateType;
        this.subjectTemplate = subjectTemplate;
        this.bodyTemplate = bodyTemplate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public void setSubjectTemplate(String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public String getPushTemplate() {
        return pushTemplate;
    }

    public void setPushTemplate(String pushTemplate) {
        this.pushTemplate = pushTemplate;
    }

    public String getTemplateVariables() {
        return templateVariables;
    }

    public void setTemplateVariables(String templateVariables) {
        this.templateVariables = templateVariables;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Utility methods
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean supportsEmail() {
        return templateType == TemplateType.EMAIL && bodyTemplate != null;
    }

    public boolean supportsSms() {
        return templateType == TemplateType.SMS && smsTemplate != null;
    }

    public boolean supportsPush() {
        return templateType == TemplateType.PUSH_NOTIFICATION && pushTemplate != null;
    }

    @Override
    public String toString() {
        return "NotificationTemplate{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                ", templateType=" + templateType +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
