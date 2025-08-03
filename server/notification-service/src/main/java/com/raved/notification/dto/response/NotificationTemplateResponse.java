package com.raved.notification.dto.response;

import com.raved.notification.model.NotificationTemplate;

import java.time.LocalDateTime;

/**
 * Response DTO for notification templates
 */
public class NotificationTemplateResponse {

    private Long id;
    private String name;
    private NotificationTemplate.TemplateType type;
    private String language;
    private String subject;
    private String content;
    private String description;
    private Boolean isActive;
    private String version;
    private String tags;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public NotificationTemplateResponse() {}

    public NotificationTemplateResponse(Long id, String name, NotificationTemplate.TemplateType type,
                                      String language, String subject, String content) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.language = language;
        this.subject = subject;
        this.content = content;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
