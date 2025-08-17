package com.raved.notification.service;

import com.raved.notification.dto.request.CreateTemplateRequest;
import com.raved.notification.dto.request.UpdateTemplateRequest;
import com.raved.notification.dto.response.NotificationTemplateResponse;
import com.raved.notification.model.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TemplateService for TheRavedApp
 */
public interface TemplateService {

    /**
     * Process template with data by name
     */
    String processTemplateByName(String templateName, Map<String, Object> templateData);

    /**
     * Process template by ID with data
     */
    String processTemplate(String templateId, Map<String, Object> templateData);

    /**
     * Create new template
     */
    NotificationTemplateResponse createTemplate(CreateTemplateRequest request);

    /**
     * Update existing template
     */
    NotificationTemplateResponse updateTemplate(String templateId, UpdateTemplateRequest request);

    /**
     * Get template by ID
     */
    Optional<NotificationTemplateResponse> getTemplateById(String templateId);

    /**
     * Get template by name
     */
    Optional<NotificationTemplateResponse> getTemplateByName(String templateName);

    /**
     * Get all active templates
     */
    List<NotificationTemplateResponse> getAllActiveTemplates();

    /**
     * Get templates by type
     */
    List<NotificationTemplateResponse> getTemplatesByType(NotificationTemplate.TemplateType templateType);

    /**
     * Get paginated templates
     */
    Page<NotificationTemplateResponse> getTemplates(Pageable pageable);

    /**
     * Delete template
     */
    void deleteTemplate(String templateId);

    /**
     * Activate/Deactivate template
     */
    NotificationTemplateResponse toggleTemplateStatus(String templateId, boolean isActive);

    /**
     * Validate template syntax
     */
    boolean validateTemplate(String templateContent);

    /**
     * Get template variables
     */
    List<String> getTemplateVariables(String templateContent);

    /**
     * Clone template
     */
    NotificationTemplateResponse cloneTemplate(String templateId, String newName);
}
