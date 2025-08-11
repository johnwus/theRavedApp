package com.raved.notification.mapper;

import com.raved.notification.dto.request.CreateNotificationRequest;
import com.raved.notification.dto.response.NotificationResponse;
import com.raved.notification.model.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper for Notification entities and DTOs
 */
@Component
public class NotificationMapper {

    /**
     * Convert CreateNotificationRequest to Notification entity
     */
    public Notification toNotification(CreateNotificationRequest request) {
        if (request == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setNotificationType(request.getNotificationType());
        notification.setTitle(request.getTitle());
        notification.setBody(request.getBody());
        notification.setData(request.getData());
        notification.setActionUrl(request.getActionUrl());
        notification.setImageUrl(request.getImageUrl());
        notification.setScheduledAt(request.getScheduledAt());
        notification.setExpiresAt(request.getExpiresAt());
        
        // Set initial status
        if (request.getScheduledAt() != null && request.getScheduledAt().isAfter(LocalDateTime.now())) {
            notification.setDeliveryStatus("SCHEDULED");
        } else {
            notification.setDeliveryStatus("PENDING");
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
        response.setUserId(notification.getUserId());
        response.setNotificationType(notification.getNotificationType());
        response.setTitle(notification.getTitle());
        response.setBody(notification.getBody());
        response.setData(notification.getData());
        response.setActionUrl(notification.getActionUrl());
        response.setImageUrl(notification.getImageUrl());
        response.setIsRead(notification.getIsRead());
        response.setIsSent(notification.getIsSent());
        response.setDeliveryStatus(notification.getDeliveryStatus());
        response.setPushSent(notification.getPushSent());
        response.setEmailSent(notification.getEmailSent());
        response.setSmsSent(notification.getSmsSent());
        response.setScheduledAt(notification.getScheduledAt());
        response.setSentAt(notification.getSentAt());
        response.setReadAt(notification.getReadAt());
        response.setExpiresAt(notification.getExpiresAt());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }

    /**
     * Update Notification entity from CreateNotificationRequest
     */
    public void updateNotificationFromRequest(Notification notification, CreateNotificationRequest request) {
        if (notification == null || request == null) {
            return;
        }

        if (request.getUserId() != null) {
            notification.setUserId(request.getUserId());
        }
        if (request.getNotificationType() != null) {
            notification.setNotificationType(request.getNotificationType());
        }
        if (request.getTitle() != null) {
            notification.setTitle(request.getTitle());
        }
        if (request.getBody() != null) {
            notification.setBody(request.getBody());
        }
        if (request.getData() != null) {
            notification.setData(request.getData());
        }
        if (request.getActionUrl() != null) {
            notification.setActionUrl(request.getActionUrl());
        }
        if (request.getImageUrl() != null) {
            notification.setImageUrl(request.getImageUrl());
        }
        if (request.getScheduledAt() != null) {
            notification.setScheduledAt(request.getScheduledAt());
        }
        if (request.getExpiresAt() != null) {
            notification.setExpiresAt(request.getExpiresAt());
        }
    }
}
