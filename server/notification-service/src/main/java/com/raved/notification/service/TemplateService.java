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
     * Process template with data
     */
    String processTemplate(String templateName, Map<String, Object> templateData);

    /**
     * Process template by ID with data
     */
    String processTemplate(Long templateId, Map<String, Object> templateData);

    /**
     * Create new template
     */
    NotificationTemplateResponse createTemplate(CreateTemplateRequest request);

    /**
     * Update existing template
     */
    NotificationTemplateResponse updateTemplate(Long templateId, UpdateTemplateRequest request);

    /**
     * Get template by ID
     */
    Optional<NotificationTemplateResponse> getTemplateById(Long templateId);

    /**
     * Get template by name
     */
    Optional<NotificationTemplateResponse> getTemplateByName(String templateName);

    /**
     * Get template by name and language
     */
    Optional<NotificationTemplateResponse> getTemplateByNameAndLanguage(String templateName, String language);

    /**
     * Get all active templates
     */
    List<NotificationTemplateResponse> getAllActiveTemplates();

    /**
     * Get templates by type
     */
    List<NotificationTemplateResponse> getTemplatesByType(NotificationTemplate.TemplateType type);

    /**
     * Get paginated templates
     */
    Page<NotificationTemplateResponse> getTemplates(Pageable pageable);

    /**
     * Delete template
     */
    void deleteTemplate(Long templateId);

    /**
     * Activate/Deactivate template
     */
    NotificationTemplateResponse toggleTemplateStatus(Long templateId, boolean isActive);

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
    NotificationTemplateResponse cloneTemplate(Long templateId, String newName);
}
