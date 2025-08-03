package com.raved.notification.service.impl;

import com.raved.notification.dto.request.CreateTemplateRequest;
import com.raved.notification.dto.request.UpdateTemplateRequest;
import com.raved.notification.dto.response.NotificationTemplateResponse;
import com.raved.notification.exception.TemplateNotFoundException;
import com.raved.notification.exception.TemplateProcessingException;
import com.raved.notification.mapper.NotificationTemplateMapper;
import com.raved.notification.model.NotificationTemplate;
import com.raved.notification.repository.NotificationTemplateRepository;
import com.raved.notification.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implementation of TemplateService
 */
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*\\}\\}");

    @Autowired
    private NotificationTemplateRepository templateRepository;

    @Autowired
    private NotificationTemplateMapper templateMapper;

    @Override
    @Transactional(readOnly = true)
    public String processTemplate(String templateName, Map<String, Object> templateData) {
        logger.debug("Processing template: {} with data", templateName);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findByName(templateName);
        if (templateOpt.isEmpty()) {
            logger.warn("Template not found: {}", templateName);
            throw new TemplateNotFoundException("Template not found: " + templateName);
        }
        
        NotificationTemplate template = templateOpt.get();
        if (!template.getIsActive()) {
            logger.warn("Template is inactive: {}", templateName);
            throw new TemplateProcessingException("Template is inactive: " + templateName);
        }
        
        return processTemplateContent(template.getContent(), templateData);
    }

    @Override
    @Transactional(readOnly = true)
    public String processTemplate(Long templateId, Map<String, Object> templateData) {
        logger.debug("Processing template ID: {} with data", templateId);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findById(templateId);
        if (templateOpt.isEmpty()) {
            logger.warn("Template not found with ID: {}", templateId);
            throw new TemplateNotFoundException("Template not found with ID: " + templateId);
        }
        
        NotificationTemplate template = templateOpt.get();
        if (!template.getIsActive()) {
            logger.warn("Template is inactive with ID: {}", templateId);
            throw new TemplateProcessingException("Template is inactive with ID: " + templateId);
        }
        
        return processTemplateContent(template.getContent(), templateData);
    }

    @Override
    public NotificationTemplateResponse createTemplate(CreateTemplateRequest request) {
        logger.info("Creating new template: {}", request.getName());
        
        // Check if template name already exists
        if (templateRepository.existsByName(request.getName())) {
            throw new TemplateProcessingException("Template with name already exists: " + request.getName());
        }
        
        NotificationTemplate template = templateMapper.toNotificationTemplate(request);
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        template.setIsActive(true);
        template.setVersion("1.0");
        
        NotificationTemplate savedTemplate = templateRepository.save(template);
        logger.info("Template created successfully: {}", savedTemplate.getId());
        
        return templateMapper.toNotificationTemplateResponse(savedTemplate);
    }

    @Override
    public NotificationTemplateResponse updateTemplate(Long templateId, UpdateTemplateRequest request) {
        logger.info("Updating template: {}", templateId);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findById(templateId);
        if (templateOpt.isEmpty()) {
            throw new TemplateNotFoundException("Template not found with ID: " + templateId);
        }
        
        NotificationTemplate template = templateOpt.get();
        templateMapper.updateTemplateFromRequest(template, request);
        template.setUpdatedAt(LocalDateTime.now());
        
        NotificationTemplate savedTemplate = templateRepository.save(template);
        logger.info("Template updated successfully: {}", templateId);
        
        return templateMapper.toNotificationTemplateResponse(savedTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationTemplateResponse> getTemplateById(Long templateId) {
        logger.debug("Getting template by ID: {}", templateId);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findById(templateId);
        return templateOpt.map(templateMapper::toNotificationTemplateResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationTemplateResponse> getTemplateByName(String templateName) {
        logger.debug("Getting template by name: {}", templateName);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findByName(templateName);
        return templateOpt.map(templateMapper::toNotificationTemplateResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationTemplateResponse> getTemplateByNameAndLanguage(String templateName, String language) {
        logger.debug("Getting template by name: {} and language: {}", templateName, language);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findByNameAndLanguage(templateName, language);
        return templateOpt.map(templateMapper::toNotificationTemplateResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getAllActiveTemplates() {
        logger.debug("Getting all active templates");
        
        List<NotificationTemplate> templates = templateRepository.findByIsActiveTrue();
        return templates.stream()
                .map(templateMapper::toNotificationTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getTemplatesByType(NotificationTemplate.TemplateType type) {
        logger.debug("Getting templates by type: {}", type);
        
        List<NotificationTemplate> templates = templateRepository.findByTypeAndIsActiveTrueOrderByCreatedAtDesc(type);
        return templates.stream()
                .map(templateMapper::toNotificationTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationTemplateResponse> getTemplates(Pageable pageable) {
        logger.debug("Getting paginated templates");
        
        Page<NotificationTemplate> templates = templateRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);
        return templates.map(templateMapper::toNotificationTemplateResponse);
    }

    @Override
    public void deleteTemplate(Long templateId) {
        logger.info("Deleting template: {}", templateId);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findById(templateId);
        if (templateOpt.isPresent()) {
            NotificationTemplate template = templateOpt.get();
            template.setIsActive(false);
            template.setUpdatedAt(LocalDateTime.now());
            templateRepository.save(template);
            logger.info("Template deactivated: {}", templateId);
        } else {
            logger.warn("Template not found for deletion: {}", templateId);
        }
    }

    @Override
    public NotificationTemplateResponse toggleTemplateStatus(Long templateId, boolean isActive) {
        logger.info("Toggling template status: {} to {}", templateId, isActive);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findById(templateId);
        if (templateOpt.isEmpty()) {
            throw new TemplateNotFoundException("Template not found with ID: " + templateId);
        }
        
        NotificationTemplate template = templateOpt.get();
        template.setIsActive(isActive);
        template.setUpdatedAt(LocalDateTime.now());
        
        NotificationTemplate savedTemplate = templateRepository.save(template);
        logger.info("Template status updated: {}", templateId);
        
        return templateMapper.toNotificationTemplateResponse(savedTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateTemplate(String templateContent) {
        logger.debug("Validating template content");
        
        if (templateContent == null || templateContent.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Check for balanced braces
            int openBraces = 0;
            for (int i = 0; i < templateContent.length() - 1; i++) {
                if (templateContent.charAt(i) == '{' && templateContent.charAt(i + 1) == '{') {
                    openBraces++;
                    i++; // Skip next character
                } else if (templateContent.charAt(i) == '}' && templateContent.charAt(i + 1) == '}') {
                    openBraces--;
                    i++; // Skip next character
                }
            }
            
            return openBraces == 0;
        } catch (Exception e) {
            logger.error("Error validating template", e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getTemplateVariables(String templateContent) {
        logger.debug("Extracting template variables");
        
        List<String> variables = new ArrayList<>();
        if (templateContent == null) {
            return variables;
        }
        
        Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);
        while (matcher.find()) {
            String variable = matcher.group(1);
            if (!variables.contains(variable)) {
                variables.add(variable);
            }
        }
        
        return variables;
    }

    @Override
    public NotificationTemplateResponse cloneTemplate(Long templateId, String newName) {
        logger.info("Cloning template: {} with new name: {}", templateId, newName);
        
        Optional<NotificationTemplate> templateOpt = templateRepository.findById(templateId);
        if (templateOpt.isEmpty()) {
            throw new TemplateNotFoundException("Template not found with ID: " + templateId);
        }
        
        // Check if new name already exists
        if (templateRepository.existsByName(newName)) {
            throw new TemplateProcessingException("Template with name already exists: " + newName);
        }
        
        NotificationTemplate originalTemplate = templateOpt.get();
        NotificationTemplate clonedTemplate = new NotificationTemplate();
        
        // Copy properties
        clonedTemplate.setName(newName);
        clonedTemplate.setType(originalTemplate.getType());
        clonedTemplate.setLanguage(originalTemplate.getLanguage());
        clonedTemplate.setSubject(originalTemplate.getSubject());
        clonedTemplate.setContent(originalTemplate.getContent());
        clonedTemplate.setDescription("Cloned from: " + originalTemplate.getName());
        clonedTemplate.setIsActive(true);
        clonedTemplate.setVersion("1.0");
        clonedTemplate.setCreatedAt(LocalDateTime.now());
        clonedTemplate.setUpdatedAt(LocalDateTime.now());
        
        NotificationTemplate savedTemplate = templateRepository.save(clonedTemplate);
        logger.info("Template cloned successfully: {}", savedTemplate.getId());
        
        return templateMapper.toNotificationTemplateResponse(savedTemplate);
    }

    /**
     * Process template content by replacing variables with actual values
     */
    private String processTemplateContent(String templateContent, Map<String, Object> templateData) {
        if (templateContent == null || templateContent.isEmpty()) {
            return templateContent;
        }
        
        if (templateData == null || templateData.isEmpty()) {
            logger.warn("No template data provided for processing");
            return templateContent;
        }
        
        String processedContent = templateContent;
        
        try {
            Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);
            while (matcher.find()) {
                String variable = matcher.group(1);
                String placeholder = matcher.group(0); // Full match including braces
                
                Object value = templateData.get(variable);
                String replacement = value != null ? value.toString() : "";
                
                processedContent = processedContent.replace(placeholder, replacement);
            }
            
            logger.debug("Template processed successfully");
            return processedContent;
            
        } catch (Exception e) {
            logger.error("Error processing template content", e);
            throw new TemplateProcessingException("Error processing template: " + e.getMessage());
        }
    }
}
