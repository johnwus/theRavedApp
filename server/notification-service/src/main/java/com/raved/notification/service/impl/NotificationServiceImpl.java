package com.raved.notification.service.impl;

import com.raved.notification.dto.request.CreateNotificationRequest;
import com.raved.notification.dto.request.SendBulkNotificationRequest;
import com.raved.notification.dto.response.DeliveryStats;
import com.raved.notification.dto.response.NotificationResponse;
import com.raved.notification.dto.response.NotificationStats;
import com.raved.notification.exception.NotificationNotFoundException;
import com.raved.notification.kafka.NotificationProducer;
import com.raved.notification.mapper.NotificationMapper;
import com.raved.notification.model.Notification;
import com.raved.notification.repository.NotificationRepository;
import com.raved.notification.service.EmailService;
import com.raved.notification.service.NotificationService;
import com.raved.notification.service.PushNotificationService;
import com.raved.notification.service.SmsService;
import com.raved.notification.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of NotificationService
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private NotificationProducer notificationProducer;

    @Override
    public NotificationResponse createAndSendNotification(CreateNotificationRequest request) {
        logger.info("Creating and sending notification to user: {}", request.getUserId());
        
        Notification notification = notificationMapper.toNotification(request);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
        
        Notification savedNotification = notificationRepository.save(notification);
        
        // Send notification asynchronously
        sendNotificationAsync(savedNotification);
        
        logger.info("Notification created and queued for delivery: {}", savedNotification.getId());
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    public List<NotificationResponse> sendBulkNotifications(SendBulkNotificationRequest request) {
        logger.info("Sending bulk notifications to {} users", request.getRecipientUserIds().size());
        
        List<Notification> notifications = request.getRecipientUserIds().stream()
                .map(userId -> {
                    Notification notification = new Notification();
                    notification.setUserId(userId);
                    notification.setNotificationType(request.getNotificationType());
                    notification.setTitle(request.getTitle());
                    notification.setBody(request.getBody());
                    notification.setCreatedAt(LocalDateTime.now());
            notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
                    return notification;
                })
                .collect(Collectors.toList());
        
        List<Notification> savedNotifications = notificationRepository.saveAll(notifications);
        
        // Send notifications asynchronously
        savedNotifications.forEach(this::sendNotificationAsync);
        
        logger.info("Bulk notifications created and queued: {}", savedNotifications.size());
        return savedNotifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationResponse> getNotificationById(String id) {
        logger.debug("Getting notification by ID: {}", id);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        return notificationOpt.map(notificationMapper::toNotificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(String userId, Pageable pageable) {
        logger.debug("Getting notifications for user: {}", userId);
        
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(notificationMapper::toNotificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(String userId, Pageable pageable) {
        logger.debug("Getting unread notifications for user: {}", userId);

        Page<Notification> notifications = notificationRepository.findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(notificationMapper::toNotificationResponse);
    }

    @Override
    public NotificationResponse markAsRead(String notificationId) {
        logger.info("Marking notification as read: {}", notificationId);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isEmpty()) {
            throw new NotificationNotFoundException("Notification not found with ID: " + notificationId);
        }
        
        Notification notification = notificationOpt.get();
        notification.setReadAt(LocalDateTime.now());
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification marked as read: {}", notificationId);
        
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    public void markAllAsRead(String userId) {
        logger.info("Marking all notifications as read for user: {}", userId);
        
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadAtIsNull(userId);
        unreadNotifications.forEach(notification -> notification.setReadAt(LocalDateTime.now()));
        
        notificationRepository.saveAll(unreadNotifications);
        logger.info("Marked {} notifications as read for user: {}", unreadNotifications.size(), userId);
    }

    @Override
    public void deleteNotification(String notificationId) {
        logger.info("Deleting notification: {}", notificationId);
        
        if (!notificationRepository.existsById(notificationId)) {
            throw new NotificationNotFoundException("Notification not found with ID: " + notificationId);
        }
        
        notificationRepository.deleteById(notificationId);
        logger.info("Notification deleted: {}", notificationId);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationStats getNotificationStats(String userId) {
        logger.debug("Getting notification stats for user: {}", userId);
        
        long totalNotifications = notificationRepository.countByUserId(userId);
        long unreadNotifications = notificationRepository.countByUserIdAndReadAtIsNull(userId);
        long sentNotifications = notificationRepository.countByUserIdAndIsSentTrue(userId);
        
        return new NotificationStats(totalNotifications, unreadNotifications, sentNotifications);
    }

    @Override
    public NotificationResponse scheduleNotification(CreateNotificationRequest request, LocalDateTime scheduledAt) {
        logger.info("Scheduling notification for user: {} at {}", request.getUserId(), scheduledAt);
        
        Notification notification = notificationMapper.toNotification(request);
        notification.setScheduledAt(scheduledAt);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
        
        Notification savedNotification = notificationRepository.save(notification);
        
        logger.info("Notification scheduled: {} for user: {}", savedNotification.getId(), request.getUserId());
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    public void cancelScheduledNotification(String notificationId) {
        logger.info("Cancelling scheduled notification: {}", notificationId);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isEmpty()) {
            throw new NotificationNotFoundException("Notification not found with ID: " + notificationId);
        }
        
        Notification notification = notificationOpt.get();
        if (Notification.DeliveryStatus.PENDING.equals(notification.getDeliveryStatus()) && notification.getScheduledAt() != null) {
            notification.setDeliveryStatus(Notification.DeliveryStatus.FAILED);
            notificationRepository.save(notification);
            logger.info("Scheduled notification cancelled: {}", notificationId);
        } else {
            logger.warn("Cannot cancel non-scheduled notification: {}", notificationId);
        }
    }

    @Override
    public void processScheduledNotifications() {
        logger.info("Processing scheduled notifications");
        
        List<Notification> scheduledNotifications = notificationRepository.findByDeliveryStatusAndScheduledAtBefore(Notification.DeliveryStatus.PENDING, LocalDateTime.now());
        
        scheduledNotifications.forEach(notification -> {
            notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
            notificationRepository.save(notification);
            sendNotificationAsync(notification);
        });
        
        logger.info("Processed {} scheduled notifications", scheduledNotifications.size());
    }

    @Override
    public NotificationResponse sendNotificationByType(String userId, String notificationType, Map<String, Object> templateData) {
        logger.info("Sending notification of type: {} to user: {}", notificationType, userId);
        
        // Generate content based on notification type
        String title = generateTitleForType(notificationType);
        String body = generateBodyForType(notificationType, templateData);
        
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType(notificationType);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
        
        Notification savedNotification = notificationRepository.save(notification);
        
        // Send notification asynchronously
        sendNotificationAsync(savedNotification);
        
        logger.info("Notification sent: {} to user: {}", savedNotification.getId(), userId);
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByType(String userId, String notificationType, Pageable pageable) {
        logger.debug("Getting notifications of type: {} for user: {}", notificationType, userId);
        
        Page<Notification> notifications = notificationRepository.findByUserIdAndNotificationTypeOrderByCreatedAtDesc(userId, notificationType, pageable);
        return notifications.map(notificationMapper::toNotificationResponse);
    }

    @Override
    public NotificationResponse resendNotification(String notificationId) {
        logger.info("Resending notification: {}", notificationId);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isEmpty()) {
            throw new NotificationNotFoundException("Notification not found with ID: " + notificationId);
        }
        
        Notification notification = notificationOpt.get();
        notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
        notification.setIsSent(false);
        notification.setSentAt(null);
        
        Notification savedNotification = notificationRepository.save(notification);
        
        // Resend notification
        sendNotificationAsync(savedNotification);
        
        logger.info("Notification resent: {}", notificationId);
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryStats getDeliveryStats(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting delivery stats from {} to {}", startDate, endDate);
        
        long totalNotifications = notificationRepository.countByCreatedAtBetween(startDate, endDate);
        long sentNotifications = notificationRepository.countByCreatedAtBetweenAndIsSentTrue(startDate, endDate);
        long readNotifications = notificationRepository.countByCreatedAtBetweenAndReadAtIsNotNull(startDate, endDate);
        
        Map<String, Long> channelStats = getDeliveryStatsByChannel(startDate, endDate);
        
        return new DeliveryStats(totalNotifications, sentNotifications, readNotifications, channelStats);
    }

    @Async
    private void sendNotificationAsync(Notification notification) {
        try {
            logger.debug("Sending notification asynchronously: {}", notification.getId());
            
            // Send via different channels based on notification type
            // This is a simplified implementation - in reality, you'd check user preferences
            
            // For now, just mark as sent
            notification.setIsSent(true);
            notification.setSentAt(LocalDateTime.now());
            notification.setDeliveryStatus(Notification.DeliveryStatus.SENT);
            notificationRepository.save(notification);
            
            logger.info("Notification sent successfully: {}", notification.getId());
            
        } catch (Exception e) {
            logger.error("Error sending notification: {}", notification.getId(), e);
            notification.setDeliveryStatus(Notification.DeliveryStatus.FAILED);
            notificationRepository.save(notification);
        }
    }

    private String generateTitleForType(String notificationType) {
        switch (notificationType.toUpperCase()) {
            case "LIKED":
                return "Someone liked your content!";
            case "COMMENTED":
                return "New comment on your content";
            case "FOLLOWED":
                return "New follower!";
            case "ORDER_CONFIRMED":
                return "Order confirmed!";
            case "ORDER_SHIPPED":
                return "Your order has been shipped!";
            case "ORDER_DELIVERED":
                return "Your order has been delivered!";
            case "PAYMENT_SUCCESS":
                return "Payment successful!";
            case "PAYMENT_FAILED":
                return "Payment failed";
            case "WELCOME":
                return "Welcome to TheRavedApp!";
            case "PASSWORD_RESET":
                return "Password reset request";
            case "EMAIL_VERIFICATION":
                return "Verify your email";
            case "SYSTEM_MAINTENANCE":
                return "System maintenance notice";
            case "PROMOTIONAL":
                return "Special offer for you!";
            default:
                return "New notification";
        }
    }

    private String generateBodyForType(String notificationType, Map<String, Object> templateData) {
        // This would typically use a template engine
        String baseMessage = generateTitleForType(notificationType);
        
        if (templateData != null && !templateData.isEmpty()) {
            // Simple template substitution
            String message = baseMessage;
            for (Map.Entry<String, Object> entry : templateData.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
            return message;
        }
        
        return baseMessage;
    }

    private Map<String, Long> getDeliveryStatsByChannel(LocalDateTime startDate, LocalDateTime endDate) {
        // This would typically query delivery logs
        // For now, return empty stats
        return Map.of();
    }
}
