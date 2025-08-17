package com.raved.notification.dto.response;

import com.raved.notification.model.NotificationTemplate;
import java.time.LocalDateTime;
import java.util.Map;

public class NotificationTemplateResponse {
    private String id; // Changed to String for MongoDB
    private String templateName;
    private NotificationTemplate.TemplateType templateType;
    private String subjectTemplate;
    private String bodyTemplate;
    private Map<String, Object> variables; // Changed to Map for MongoDB
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public NotificationTemplate.TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(NotificationTemplate.TemplateType templateType) {
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

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
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

    @Override
    public String toString() {
        return "NotificationTemplateResponse{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                ", templateType=" + templateType +
                ", subjectTemplate='" + subjectTemplate + '\'' +
                ", bodyTemplate='" + bodyTemplate + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}