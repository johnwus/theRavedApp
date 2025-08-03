package com.raved.notification.dto.request;

import com.raved.notification.model.NotificationTemplate;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating notification templates
 */
public class UpdateTemplateRequest {

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private NotificationTemplate.TemplateType type;

    @Size(max = 10, message = "Language code must not exceed 10 characters")
    private String language;

    @Size(max = 500, message = "Subject must not exceed 500 characters")
    private String subject;

    private String content;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Boolean isActive;

    @Size(max = 50, message = "Version must not exceed 50 characters")
    private String version;

    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;

    @Size(max = 255, message = "Category must not exceed 255 characters")
    private String category;

    // Constructors
    public UpdateTemplateRequest() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NotificationTemplate.TemplateType getType() {
        return type;
    }

    public void setType(NotificationTemplate.TemplateType type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
