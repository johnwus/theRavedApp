package com.raved.notification.dto.request;

import com.raved.notification.model.NotificationTemplate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

public class CreateTemplateRequest {
    @NotBlank(message = "Template name is required")
    @Size(max = 100, message = "Template name cannot exceed 100 characters")
    private String templateName;
    
    @NotNull(message = "Template type is required")
    private NotificationTemplate.TemplateType templateType;
    
    @Size(max = 255, message = "Subject template cannot exceed 255 characters")
    private String subjectTemplate;
    
    @NotBlank(message = "Body template is required")
    private String bodyTemplate;
    
    private String variables; // Template variables as JSON string
    
    private Boolean isActive = true;

    // Getters and setters
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

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "CreateTemplateRequest{" +
                "templateName='" + templateName + '\'' +
                ", templateType=" + templateType +
                ", subjectTemplate='" + subjectTemplate + '\'' +
                ", bodyTemplate='" + bodyTemplate + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}