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
 * NotificationTemplate Document for TheRavedApp MongoDB
 *
 * Represents templates for different types of notifications. Converted from JPA
 * entity to MongoDB document.
 */
@Document(collection = "notification_templates")
@CompoundIndexes({
    @CompoundIndex(name = "idx_type_active", def = "{'templateType': 1, 'isActive': 1}"),
    @CompoundIndex(name = "idx_name_unique", def = "{'templateName': 1}", unique = true)
})
public class NotificationTemplate {
    
    /**
     * Enum representing the type of notification template
     */
    public enum TemplateType {
        PUSH,      // Push notification
        EMAIL,     // Email notification
        SMS        // SMS notification
    }
    
    @Id
    private String id;

    @Field("templateName")
    @Indexed(unique = true)
    @NotBlank
    private String templateName;

    @Field("templateType")
    @Indexed
    @NotNull
    private TemplateType templateType; // PUSH, EMAIL, SMS

    @Field("subjectTemplate")
    private String subjectTemplate;

    @Field("bodyTemplate")
    @NotBlank
    private String bodyTemplate;

    @Field("variables")
    private Map<String, Object> variables; // Template variables (changed to Map for MongoDB)

    @Field("isActive")
    @Indexed
    private Boolean isActive = true;

    @Field("createdAt")
    @Indexed
    private LocalDateTime createdAt;

    @Field("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructors
    public NotificationTemplate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
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
        this.updatedAt = LocalDateTime.now();
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
        this.updatedAt = LocalDateTime.now();
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public void setSubjectTemplate(String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
        this.updatedAt = LocalDateTime.now();
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
        this.updatedAt = LocalDateTime.now();
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        this.updatedAt = LocalDateTime.now();
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
    
    // MongoDB lifecycle method
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}