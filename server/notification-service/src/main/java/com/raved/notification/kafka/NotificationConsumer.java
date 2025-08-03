package com.raved.notification.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.notification.model.DeviceToken;
import com.raved.notification.model.Notification;
import com.raved.notification.repository.DeviceTokenRepository;
import com.raved.notification.repository.NotificationRepository;
import com.raved.notification.service.EmailService;
import com.raved.notification.service.PushNotificationService;
import com.raved.notification.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kafka consumer for processing notification events
 */
@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private SmsService smsService;

    /**
     * Process notification events
     */
    @KafkaListener(topics = "${kafka.topics.notification-events:notification-events}",
                   groupId = "${kafka.consumer.group-id:notification-service}")
    public void processNotificationEvent(@Payload String message,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Received notification event from topic: {}, partition: {}, offset: {}", topic, partition, offset);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            String eventType = (String) event.get("eventType");

            switch (eventType) {
                case "NOTIFICATION":
                    processNotification(event);
                    break;
                case "BULK_NOTIFICATION":
                    processBulkNotification(event);
                    break;
                case "SYSTEM":
                    processSystemEvent(event);
                    break;
                default:
                    logger.warn("Unknown event type: {}", eventType);
            }

        } catch (JsonProcessingException e) {
            logger.error("Failed to parse notification event: {}", message, e);
        } catch (Exception e) {
            logger.error("Error processing notification event: {}", message, e);
        }
    }

    /**
     * Process user activity events to trigger notifications
     */
    @KafkaListener(topics = "${kafka.topics.user-events:user-events}",
                   groupId = "${kafka.consumer.group-id:notification-service}")
    public void processUserActivityEvent(@Payload String message) {
        logger.info("Received user activity event");

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            String activityType = (String) event.get("activityType");
            Long userId = Long.valueOf(event.get("userId").toString());

            // Process different user activities
            switch (activityType) {
                case "USER_REGISTERED":
                    sendWelcomeNotification(userId);
                    break;
                case "PASSWORD_RESET_REQUESTED":
                    sendPasswordResetNotification(userId, (String) event.get("resetToken"));
                    break;
                case "EMAIL_VERIFICATION_REQUESTED":
                    sendEmailVerificationNotification(userId, (String) event.get("verificationToken"));
                    break;
                default:
                    logger.debug("No notification action for activity type: {}", activityType);
            }

        } catch (Exception e) {
            logger.error("Error processing user activity event: {}", message, e);
        }
    }

    private void processNotification(Map<String, Object> event) {
        logger.info("Processing notification event for notification: {}", event.get("notificationId"));

        Long notificationId = Long.valueOf(event.get("notificationId").toString());
        Notification notification = notificationRepository.findById(notificationId).orElse(null);

        if (notification == null) {
            logger.error("Notification not found: {}", notificationId);
            return;
        }

        // Update delivery status
        notification.setDeliveryStatus(Notification.DeliveryStatus.PROCESSING);
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        // Send notification through configured channels
        boolean delivered = false;

        if (notification.getChannels().contains("EMAIL")) {
            delivered |= sendEmailNotification(notification);
        }

        if (notification.getChannels().contains("PUSH")) {
            delivered |= sendPushNotification(notification);
        }

        if (notification.getChannels().contains("SMS")) {
            delivered |= sendSmsNotification(notification);
        }

        // Update final delivery status
        notification.setDeliveryStatus(delivered ?
                Notification.DeliveryStatus.DELIVERED : Notification.DeliveryStatus.FAILED);
        notification.setDeliveredAt(delivered ? LocalDateTime.now() : null);
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        logger.info("Notification processing completed: {} - Status: {}",
                notificationId, notification.getDeliveryStatus());
    }

    private void processBulkNotification(Map<String, Object> event) {
        logger.info("Processing bulk notification event: {}", event.get("bulkEventType"));
        // Implementation for bulk notifications
    }

    private void processSystemEvent(Map<String, Object> event) {
        logger.info("Processing system event: {}", event.get("systemEventType"));
        // Implementation for system events
    }

    private boolean sendEmailNotification(Notification notification) {
        try {
            // Get user email (this would typically come from user service)
            String userEmail = getUserEmail(notification.getRecipientUserId());
            if (userEmail != null) {
                return emailService.sendNotificationEmail(notification, userEmail);
            }
        } catch (Exception e) {
            logger.error("Failed to send email notification: {}", notification.getId(), e);
        }
        return false;
    }

    private boolean sendPushNotification(Notification notification) {
        try {
            List<DeviceToken> deviceTokens = deviceTokenRepository.findByUserIdAndIsActiveTrue(
                    notification.getRecipientUserId());

            if (!deviceTokens.isEmpty()) {
                List<String> tokens = deviceTokens.stream()
                        .map(DeviceToken::getToken)
                        .collect(Collectors.toList());

                return pushNotificationService.sendNotificationPush(notification, tokens);
            }
        } catch (Exception e) {
            logger.error("Failed to send push notification: {}", notification.getId(), e);
        }
        return false;
    }

    private boolean sendSmsNotification(Notification notification) {
        try {
            // Get user phone number (this would typically come from user service)
            String phoneNumber = getUserPhoneNumber(notification.getRecipientUserId());
            if (phoneNumber != null) {
                return smsService.sendSms(phoneNumber, notification.getContent());
            }
        } catch (Exception e) {
            logger.error("Failed to send SMS notification: {}", notification.getId(), e);
        }
        return false;
    }

    private void sendWelcomeNotification(Long userId) {
        logger.info("Sending welcome notification to user: {}", userId);
    }

    private void sendPasswordResetNotification(Long userId, String resetToken) {
        logger.info("Sending password reset notification to user: {}", userId);
    }

    private void sendEmailVerificationNotification(Long userId, String verificationToken) {
        logger.info("Sending email verification notification to user: {}", userId);
    }

    // Helper methods to get user information (would integrate with user service)
    private String getUserEmail(Long userId) {
        // TODO: Integrate with user service to get email
        return "user" + userId + "@example.com";
    }

    private String getUserPhoneNumber(Long userId) {
        // TODO: Integrate with user service to get phone number
        return null; // Return null if no phone number
    }
}
