package com.raved.notification.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.notification.dto.request.CreateNotificationRequest;
import com.raved.notification.dto.response.NotificationResponse;
import com.raved.notification.model.Notification;
import com.raved.notification.model.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for Notification entities and DTOs
 */
@Component
public class NotificationMapper {

    private static final Logger logger = LoggerFactory.getLogger(NotificationMapper.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Convert CreateNotificationRequest to Notification entity
     */
    public Notification toNotification(CreateNotificationRequest request) {
        if (request == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setRecipientUserId(request.getRecipientUserId());
        notification.setNotificationType(request.getNotificationType());
        notification.setSubject(request.getSubject());
        notification.setContent(request.getContent());
        notification.setSmsContent(request.getSmsContent());
        notification.setPushContent(request.getPushContent());
        notification.setHtmlContent(request.getHtmlContent());
        
        // Set channels
        if (request.getChannels() != null && !request.getChannels().isEmpty()) {
            notification.setChannels(String.join(",", request.getChannels()));
        } else {
            // Default channels based on notification type
            notification.setChannels(getDefaultChannels(request.getNotificationType()));
        }
        
        // Set priority
        if (request.getPriority() != null) {
            try {
                notification.setPriority(Notification.Priority.valueOf(request.getPriority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid priority: {}, using NORMAL", request.getPriority());
                notification.setPriority(Notification.Priority.NORMAL);
            }
        } else {
            notification.setPriority(Notification.Priority.NORMAL);
        }
        
        notification.setScheduledAt(request.getScheduledAt());
        
        // Set metadata as JSON string
        if (request.getMetadata() != null && !request.getMetadata().isEmpty()) {
            try {
                notification.setMetadata(objectMapper.writeValueAsString(request.getMetadata()));
            } catch (JsonProcessingException e) {
                logger.error("Error serializing metadata", e);
            }
        }
        
        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        notification.setCreatedAt(now);
        notification.setUpdatedAt(now);
        
        // Set initial status
        if (request.getScheduledAt() != null && request.getScheduledAt().isAfter(now)) {
            notification.setDeliveryStatus(Notification.DeliveryStatus.SCHEDULED);
        } else {
            notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
        }

        return notification;
    }

    /**
     * Convert Notification entity to NotificationResponse DTO
     */
    public NotificationResponse toNotificationResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setRecipientUserId(notification.getRecipientUserId());
        response.setNotificationType(notification.getNotificationType().name());
        response.setSubject(notification.getSubject());
        response.setContent(notification.getContent());
        response.setSmsContent(notification.getSmsContent());
        response.setPushContent(notification.getPushContent());
        response.setHtmlContent(notification.getHtmlContent());
        response.setDeliveryStatus(notification.getDeliveryStatus().name());
        response.setPriority(notification.getPriority().name());
        
        // Parse channels
        if (notification.getChannels() != null && !notification.getChannels().isEmpty()) {
            response.setChannels(Arrays.asList(notification.getChannels().split(",")));
        }
        
        response.setScheduledAt(notification.getScheduledAt());
        response.setSentAt(notification.getSentAt());
        response.setDeliveredAt(notification.getDeliveredAt());
        response.setReadAt(notification.getReadAt());
        response.setRetryCount(notification.getRetryCount());
        response.setMaxRetries(notification.getMaxRetries());
        response.setFailureReason(notification.getFailureReason());
        
        // Parse metadata from JSON string
        if (notification.getMetadata() != null && !notification.getMetadata().isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> metadata = objectMapper.readValue(notification.getMetadata(), Map.class);
                response.setMetadata(metadata);
            } catch (JsonProcessingException e) {
                logger.error("Error deserializing metadata for notification: {}", notification.getId(), e);
                response.setMetadata(new HashMap<>());
            }
        }
        
        response.setCreatedAt(notification.getCreatedAt());
        response.setUpdatedAt(notification.getUpdatedAt());
        
        // Set template info if available
        if (notification.getTemplate() != null) {
            response.setTemplateId(notification.getTemplate().getId());
            response.setTemplateName(notification.getTemplate().getName());
        }
        
        // Set read status
        response.setIsRead(notification.getReadAt() != null);

        return response;
    }

    /**
     * Update notification entity from request (for partial updates)
     */
    public void updateNotificationFromRequest(Notification notification, CreateNotificationRequest request) {
        if (notification == null || request == null) {
            return;
        }

        if (request.getSubject() != null) {
            notification.setSubject(request.getSubject());
        }
        
        if (request.getContent() != null) {
            notification.setContent(request.getContent());
        }
        
        if (request.getSmsContent() != null) {
            notification.setSmsContent(request.getSmsContent());
        }
        
        if (request.getPushContent() != null) {
            notification.setPushContent(request.getPushContent());
        }
        
        if (request.getHtmlContent() != null) {
            notification.setHtmlContent(request.getHtmlContent());
        }
        
        if (request.getChannels() != null) {
            notification.setChannels(String.join(",", request.getChannels()));
        }
        
        if (request.getPriority() != null) {
            try {
                notification.setPriority(Notification.Priority.valueOf(request.getPriority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid priority: {}, keeping current", request.getPriority());
            }
        }
        
        if (request.getScheduledAt() != null) {
            notification.setScheduledAt(request.getScheduledAt());
        }
        
        if (request.getMetadata() != null) {
            try {
                notification.setMetadata(objectMapper.writeValueAsString(request.getMetadata()));
            } catch (JsonProcessingException e) {
                logger.error("Error serializing metadata", e);
            }
        }
        
        notification.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Get default channels based on notification type
     */
    private String getDefaultChannels(NotificationType notificationType) {
        switch (notificationType) {
            case EMAIL:
                return "EMAIL";
            case SMS:
                return "SMS";
            case PUSH_NOTIFICATION:
                return "PUSH";
            case IN_APP:
                return "PUSH";
            default:
                return "EMAIL,PUSH";
        }
    }
}
