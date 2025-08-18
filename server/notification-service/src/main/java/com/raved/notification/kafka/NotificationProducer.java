package com.raved.notification.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.notification.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka producer for notification events
 */
@Component
public class NotificationProducer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.topics.notification-events:notification-events}")
    private String notificationEventsTopic;

    @Value("${kafka.topics.user-events:user-events}")
    private String userEventsTopic;

    @Value("${kafka.topics.social-events:social-events}")
    private String socialEventsTopic;

    @Value("${kafka.topics.ecommerce-events:ecommerce-events}")
    private String ecommerceEventsTopic;

    /**
     * Send notification event to Kafka
     */
    public void sendNotificationEvent(Notification notification) {
        logger.info("Sending notification event to Kafka: {}", notification.getId());

        try {
            Map<String, Object> event = createNotificationEvent(notification);
            String eventJson = objectMapper.writeValueAsString(event);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
                    notificationEventsTopic,
                    notification.getId().toString(),
                    eventJson
            );

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Notification event sent successfully: {} to topic: {}",
                            notification.getId(), notificationEventsTopic);
                } else {
                    logger.error("Failed to send notification event: {} to topic: {}",
                            notification.getId(), notificationEventsTopic, ex);
                }
            });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize notification event: {}", notification.getId(), e);
        }
    }

    /**
     * Send user activity event
     */
    public void sendUserActivityEvent(Long userId, String activityType, Map<String, Object> activityData) {
        logger.info("Sending user activity event: {} for user: {}", activityType, userId);

        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "USER_ACTIVITY");
            event.put("userId", userId);
            event.put("activityType", activityType);
            event.put("activityData", activityData);
            event.put("timestamp", System.currentTimeMillis());

            String eventJson = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(userEventsTopic, userId.toString(), eventJson)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("User activity event sent: {} for user: {}", activityType, userId);
                    } else {
                        logger.error("Failed to send user activity event: {} for user: {}", activityType, userId, ex);
                    }
                });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize user activity event for user: {}", userId, e);
        }
    }

    /**
     * Send social interaction event
     */
    public void sendSocialInteractionEvent(Long userId, String interactionType, Long targetId, String targetType) {
        logger.info("Sending social interaction event: {} from user: {} to {}: {}",
                interactionType, userId, targetType, targetId);

        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "SOCIAL_INTERACTION");
            event.put("userId", userId);
            event.put("interactionType", interactionType);
            event.put("targetId", targetId);
            event.put("targetType", targetType);
            event.put("timestamp", System.currentTimeMillis());

            String eventJson = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(socialEventsTopic, userId.toString(), eventJson)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Social interaction event sent: {}", interactionType);
                    } else {
                        logger.error("Failed to send social interaction event: {}", interactionType, ex);
                    }
                });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize social interaction event", e);
        }
    }

    private Map<String, Object> createNotificationEvent(Notification notification) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "NOTIFICATION");
        event.put("notificationId", notification.getId());
        event.put("recipientUserId", notification.getUserId());
        event.put("notificationType", notification.getNotificationType());
        event.put("subject", notification.getTitle());
        event.put("content", notification.getBody());
        event.put("deliveryStatus", notification.getDeliveryStatus());
        event.put("scheduledAt", notification.getScheduledAt());
        event.put("createdAt", notification.getCreatedAt());
        event.put("timestamp", System.currentTimeMillis());

        return event;
    }
}
