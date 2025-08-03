package com.raved.notification.mapper;

import com.raved.notification.dto.request.CreateTemplateRequest;
import com.raved.notification.dto.request.UpdateTemplateRequest;
import com.raved.notification.dto.response.NotificationTemplateResponse;
import com.raved.notification.model.NotificationTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper for NotificationTemplate entities and DTOs
 */
@Component
public class NotificationTemplateMapper {

    /**
     * Convert CreateTemplateRequest to NotificationTemplate entity
     */
    public NotificationTemplate toNotificationTemplate(CreateTemplateRequest request) {
        if (request == null) {
            return null;
        }

        NotificationTemplate template = new NotificationTemplate();
        template.setName(request.getName());
        template.setType(request.getType());
        template.setLanguage(request.getLanguage() != null ? request.getLanguage() : "en");
        template.setSubject(request.getSubject());
        template.setContent(request.getContent());
        template.setDescription(request.getDescription());
        template.setTags(request.getTags());
        template.setCategory(request.getCategory());
        
        // Set defaults
        template.setIsActive(true);
        template.setVersion("1.0");
        
        LocalDateTime now = LocalDateTime.now();
        template.setCreatedAt(now);
        template.setUpdatedAt(now);

        return template;
    }

    /**
     * Convert NotificationTemplate entity to NotificationTemplateResponse DTO
     */
    public NotificationTemplateResponse toNotificationTemplateResponse(NotificationTemplate template) {
        if (template == null) {
            return null;
        }

        NotificationTemplateResponse response = new NotificationTemplateResponse();
        response.setId(template.getId());
        response.setName(template.getName());
        response.setType(template.getType());
        response.setLanguage(template.getLanguage());
        response.setSubject(template.getSubject());
        response.setContent(template.getContent());
        response.setDescription(template.getDescription());
        response.setIsActive(template.getIsActive());
        response.setVersion(template.getVersion());
        response.setTags(template.getTags());
        response.setCategory(template.getCategory());
        response.setCreatedAt(template.getCreatedAt());
        response.setUpdatedAt(template.getUpdatedAt());

        return response;
    }

    /**
     * Update NotificationTemplate entity from UpdateTemplateRequest
     */
    public void updateTemplateFromRequest(NotificationTemplate template, UpdateTemplateRequest request) {
        if (template == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            template.setName(request.getName());
        }
        
        if (request.getType() != null) {
            template.setType(request.getType());
        }
        
        if (request.getLanguage() != null) {
            template.setLanguage(request.getLanguage());
        }
        
        if (request.getSubject() != null) {
            template.setSubject(request.getSubject());
        }
        
        if (request.getContent() != null) {
            template.setContent(request.getContent());
        }
        
        if (request.getDescription() != null) {
            template.setDescription(request.getDescription());
        }
        
        if (request.getIsActive() != null) {
            template.setIsActive(request.getIsActive());
        }
        
        if (request.getVersion() != null) {
            template.setVersion(request.getVersion());
        }
        
        if (request.getTags() != null) {
            template.setTags(request.getTags());
        }
        
        if (request.getCategory() != null) {
            template.setCategory(request.getCategory());
        }
        
        template.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Create a copy of NotificationTemplate for cloning
     */
    public NotificationTemplate cloneTemplate(NotificationTemplate original, String newName) {
        if (original == null) {
            return null;
        }

        NotificationTemplate clone = new NotificationTemplate();
        clone.setName(newName);
        clone.setType(original.getType());
        clone.setLanguage(original.getLanguage());
        clone.setSubject(original.getSubject());
        clone.setContent(original.getContent());
        clone.setDescription("Cloned from: " + original.getName());
        clone.setIsActive(true);
        clone.setVersion("1.0");
        clone.setTags(original.getTags());
        clone.setCategory(original.getCategory());
        
        LocalDateTime now = LocalDateTime.now();
        clone.setCreatedAt(now);
        clone.setUpdatedAt(now);

        return clone;
    }
}
