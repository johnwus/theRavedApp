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
import com.raved.notification.model.NotificationType;
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
import java.util.HashMap;
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
        logger.info("Creating and sending notification to user: {}", request.getRecipientUserId());
        
        Notification notification = notificationMapper.toNotification(request);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
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
                    notification.setRecipientUserId(userId);
                    notification.setNotificationType(NotificationType.valueOf(request.getNotificationType()));
                    notification.setSubject(request.getSubject());
                    notification.setContent(request.getContent());
                    notification.setCreatedAt(LocalDateTime.now());
                    notification.setUpdatedAt(LocalDateTime.now());
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
    public Optional<NotificationResponse> getNotificationById(Long id) {
        logger.debug("Getting notification by ID: {}", id);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        return notificationOpt.map(notificationMapper::toNotificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable) {
        logger.debug("Getting notifications for user: {}", userId);
        
        Page<Notification> notifications = notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(notificationMapper::toNotificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(Long userId, Pageable pageable) {
        logger.debug("Getting unread notifications for user: {}", userId);

        Page<Notification> notifications = notificationRepository.findByRecipientUserIdAndReadAtIsNullOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(notificationMapper::toNotificationResponse);
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {
        logger.info("Marking notification as read: {}", notificationId);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isEmpty()) {
            throw new NotificationNotFoundException("Notification not found with ID: " + notificationId);
        }
        
        Notification notification = notificationOpt.get();
        notification.setReadAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification marked as read: {}", notificationId);
        
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    public void markAllAsRead(Long userId) {
        logger.info("Marking all notifications as read for user: {}", userId);

        List<Notification> unreadNotifications = notificationRepository.findByRecipientUserIdAndReadAtIsNull(userId);
        LocalDateTime now = LocalDateTime.now();

        unreadNotifications.forEach(notification -> {
            notification.setReadAt(now);
            notification.setUpdatedAt(now);
        });

        notificationRepository.saveAll(unreadNotifications);
        logger.info("Marked {} notifications as read for user: {}", unreadNotifications.size(), userId);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        logger.info("Deleting notification: {}", notificationId);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            notificationRepository.delete(notificationOpt.get());
            logger.info("Notification deleted: {}", notificationId);
        } else {
            logger.warn("Notification not found for deletion: {}", notificationId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationStats getNotificationStats(Long userId) {
        logger.debug("Getting notification stats for user: {}", userId);
        
        long totalNotifications = notificationRepository.countByRecipientUserId(userId);
        long unreadNotifications = notificationRepository.countByRecipientUserIdAndReadAtIsNull(userId);
        long readNotifications = totalNotifications - unreadNotifications;

        // Get notifications by type - we'll need to implement this properly
        Map<NotificationType, Long> notificationsByType = getNotificationsByTypeForUser(userId);
        
        return new NotificationStats(totalNotifications, unreadNotifications, readNotifications, notificationsByType);
    }

    @Override
    public NotificationResponse scheduleNotification(CreateNotificationRequest request, LocalDateTime scheduledAt) {
        logger.info("Scheduling notification for user: {} at: {}", request.getRecipientUserId(), scheduledAt);
        
        Notification notification = notificationMapper.toNotification(request);
        notification.setScheduledAt(scheduledAt);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        notification.setDeliveryStatus(Notification.DeliveryStatus.SCHEDULED);
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification scheduled: {}", savedNotification.getId());
        
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    public void cancelScheduledNotification(Long notificationId) {
        logger.info("Cancelling scheduled notification: {}", notificationId);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            if (notification.getDeliveryStatus() == Notification.DeliveryStatus.SCHEDULED) {
                notification.setDeliveryStatus(Notification.DeliveryStatus.CANCELLED);
                notification.setUpdatedAt(LocalDateTime.now());
                notificationRepository.save(notification);
                logger.info("Scheduled notification cancelled: {}", notificationId);
            } else {
                logger.warn("Cannot cancel notification with status: {}", notification.getDeliveryStatus());
            }
        }
    }

    @Override
    public void processScheduledNotifications() {
        logger.debug("Processing scheduled notifications");
        
        LocalDateTime now = LocalDateTime.now();
        List<Notification> scheduledNotifications = notificationRepository.findScheduledNotificationsDue(now);
        
        logger.info("Found {} scheduled notifications to process", scheduledNotifications.size());
        
        scheduledNotifications.forEach(notification -> {
            notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
            notification.setUpdatedAt(now);
            notificationRepository.save(notification);
            
            sendNotificationAsync(notification);
        });
    }

    @Override
    public NotificationResponse sendNotificationByType(Long userId, NotificationType type, Map<String, Object> templateData) {
        logger.info("Sending notification by type: {} to user: {}", type, userId);
        
        // Get template for notification type
        String content = templateService.processTemplate(type.name(), templateData);
        
        Notification notification = new Notification();
        notification.setRecipientUserId(userId);
        notification.setNotificationType(type);
        notification.setSubject(generateSubjectForType(type));
        notification.setContent(content);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
        
        Notification savedNotification = notificationRepository.save(notification);
        sendNotificationAsync(savedNotification);
        
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByType(Long userId, NotificationType type, Pageable pageable) {
        logger.debug("Getting notifications by type: {} for user: {}", type, userId);
        
        Page<Notification> notifications = notificationRepository.findByRecipientUserIdAndNotificationTypeOrderByCreatedAtDesc(userId, type, pageable);
        return notifications.map(notificationMapper::toNotificationResponse);
    }

    @Override
    public NotificationResponse resendNotification(Long notificationId) {
        logger.info("Resending notification: {}", notificationId);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isEmpty()) {
            throw new NotificationNotFoundException("Notification not found with ID: " + notificationId);
        }
        
        Notification notification = notificationOpt.get();
        notification.setDeliveryStatus(Notification.DeliveryStatus.PENDING);
        notification.setUpdatedAt(LocalDateTime.now());
        
        Notification savedNotification = notificationRepository.save(notification);
        sendNotificationAsync(savedNotification);
        
        logger.info("Notification queued for resending: {}", notificationId);
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryStats getDeliveryStats(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting delivery stats from: {} to: {}", startDate, endDate);
        
        long totalSent = notificationRepository.countByCreatedAtBetween(startDate, endDate);
        long totalDelivered = notificationRepository.countByDeliveryStatusAndCreatedAtBetween(
                Notification.DeliveryStatus.DELIVERED, startDate, endDate);
        long totalFailed = notificationRepository.countByDeliveryStatusAndCreatedAtBetween(
                Notification.DeliveryStatus.FAILED, startDate, endDate);
        
        // Get delivery stats by channel - we'll implement this properly
        Map<String, Long> deliveryByChannel = getDeliveryStatsByChannel(startDate, endDate);
        
        return new DeliveryStats(totalSent, totalDelivered, totalFailed, deliveryByChannel);
    }

    @Async
    private void sendNotificationAsync(Notification notification) {
        logger.debug("Sending notification asynchronously: {}", notification.getId());
        
        // Send to Kafka for processing
        notificationProducer.sendNotificationEvent(notification);
    }

    private String generateSubjectForType(NotificationType type) {
        switch (type) {
            case LIKE: return "Someone liked your post";
            case COMMENT: return "New comment on your post";
            case FOLLOW: return "You have a new follower";
            case ORDER_CONFIRMATION: return "Order confirmed";
            case ORDER_SHIPPED: return "Your order has been shipped";
            case ORDER_DELIVERED: return "Your order has been delivered";
            case PAYMENT_SUCCESS: return "Payment successful";
            case PAYMENT_FAILED: return "Payment failed";
            case WELCOME: return "Welcome to RAvED!";
            case PASSWORD_RESET: return "Password reset request";
            case EMAIL_VERIFICATION: return "Verify your email";
            case SYSTEM_MAINTENANCE: return "System maintenance notification";
            case PROMOTIONAL: return "Special offer for you";
            default: return "Notification";
        }
    }

    /**
     * Helper method to get notifications by type for a user
     */
    private Map<NotificationType, Long> getNotificationsByTypeForUser(Long userId) {
        List<Object[]> results = notificationRepository.countNotificationsByTypeForUser(userId);
        Map<NotificationType, Long> notificationsByType = new HashMap<>();

        for (Object[] result : results) {
            NotificationType type = (NotificationType) result[0];
            Long count = (Long) result[1];
            notificationsByType.put(type, count);
        }

        return notificationsByType;
    }

    /**
     * Helper method to get delivery stats by channel
     */
    private Map<String, Long> getDeliveryStatsByChannel(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = notificationRepository.getDeliveryStatsByChannelAndDateRange(startDate, endDate);
        Map<String, Long> deliveryByChannel = new HashMap<>();

        for (Object[] result : results) {
            String channel = result[0].toString();
            Long count = (Long) result[1];
            deliveryByChannel.put(channel, count);
        }

        return deliveryByChannel;
    }
}
