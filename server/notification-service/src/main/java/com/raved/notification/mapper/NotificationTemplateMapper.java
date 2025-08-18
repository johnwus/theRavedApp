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
        template.setTemplateName(request.getTemplateName());
        template.setTemplateType(request.getTemplateType());
        template.setSubjectTemplate(request.getSubjectTemplate());
        template.setBodyTemplate(request.getBodyTemplate());
        template.setVariables(request.getVariables());
        template.setIsActive(request.getIsActive());
        
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
        response.setTemplateName(template.getTemplateName());
        response.setTemplateType(template.getTemplateType());
        response.setSubjectTemplate(template.getSubjectTemplate());
        response.setBodyTemplate(template.getBodyTemplate());
        response.setVariables(template.getVariables());
        response.setIsActive(template.getIsActive());
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

        if (request.getTemplateName() != null) {
            template.setTemplateName(request.getTemplateName());
        }
        
        if (request.getTemplateType() != null) {
            template.setTemplateType(request.getTemplateType());
        }
        
        if (request.getSubjectTemplate() != null) {
            template.setSubjectTemplate(request.getSubjectTemplate());
        }
        
        if (request.getBodyTemplate() != null) {
            template.setBodyTemplate(request.getBodyTemplate());
        }
        
        if (request.getVariables() != null) {
            template.setVariables(request.getVariables());
        }
        
        if (request.getIsActive() != null) {
            template.setIsActive(request.getIsActive());
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
        clone.setTemplateName(newName);
        clone.setTemplateType(original.getTemplateType());
        clone.setSubjectTemplate(original.getSubjectTemplate());
        clone.setBodyTemplate(original.getBodyTemplate());
        clone.setVariables(original.getVariables());
        clone.setIsActive(true);
        
        LocalDateTime now = LocalDateTime.now();
        clone.setCreatedAt(now);
        clone.setUpdatedAt(now);

        return clone;
    }
}
