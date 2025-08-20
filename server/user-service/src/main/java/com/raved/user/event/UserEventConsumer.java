package com.raved.user.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consumer for user-related events from other services
 */
@Component
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Handle content creation events to update user activity
     */
    @KafkaListener(topics = "${kafka.topics.content-created:content.created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleContentCreatedEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        logger.info("Received content created event from topic: {}, partition: {}, offset: {}", topic, partition, offset);
        
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            Long userId = Long.valueOf(event.get("userId").toString());
            String contentType = (String) event.get("contentType");
            
            logger.info("Processing content creation for user: {}, type: {}", userId, contentType);
            
            // Update user activity metrics
            // This could update user engagement scores, content creation counts, etc.
            updateUserActivityMetrics(userId, contentType);
            
            acknowledgment.acknowledge();
            logger.debug("Successfully processed content created event for user: {}", userId);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse content created event: {}", message, e);
            acknowledgment.acknowledge(); // Acknowledge to avoid reprocessing
        } catch (Exception e) {
            logger.error("Failed to process content created event: {}", message, e);
            acknowledgment.acknowledge(); // Acknowledge to avoid reprocessing
        }
    }

    /**
     * Handle social interaction events to update user engagement
     */
    @KafkaListener(topics = "${kafka.topics.social-interaction:social.interaction}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleSocialInteractionEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment acknowledgment) {
        
        logger.info("Received social interaction event from topic: {}", topic);
        
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            Long userId = Long.valueOf(event.get("userId").toString());
            String interactionType = (String) event.get("interactionType");
            
            logger.info("Processing social interaction for user: {}, type: {}", userId, interactionType);
            
            // Update user social engagement metrics
            updateUserSocialMetrics(userId, interactionType);
            
            acknowledgment.acknowledge();
            logger.debug("Successfully processed social interaction event for user: {}", userId);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse social interaction event: {}", message, e);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            logger.error("Failed to process social interaction event: {}", message, e);
            acknowledgment.acknowledge();
        }
    }

    /**
     * Handle subscription events to update user status
     */
    @KafkaListener(topics = "${kafka.topics.subscription-changed:subscription.changed}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleSubscriptionChangedEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment acknowledgment) {
        
        logger.info("Received subscription changed event from topic: {}", topic);
        
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            Long userId = Long.valueOf(event.get("userId").toString());
            String subscriptionStatus = (String) event.get("status");
            
            logger.info("Processing subscription change for user: {}, status: {}", userId, subscriptionStatus);
            
            // Update user subscription status
            updateUserSubscriptionStatus(userId, subscriptionStatus);
            
            acknowledgment.acknowledge();
            logger.debug("Successfully processed subscription changed event for user: {}", userId);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse subscription changed event: {}", message, e);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            logger.error("Failed to process subscription changed event: {}", message, e);
            acknowledgment.acknowledge();
        }
    }

    private void updateUserActivityMetrics(Long userId, String contentType) {
        // Implementation for updating user activity metrics
        // This could involve updating user statistics, engagement scores, etc.
        logger.debug("Updating activity metrics for user: {} with content type: {}", userId, contentType);
    }

    private void updateUserSocialMetrics(Long userId, String interactionType) {
        // Implementation for updating user social engagement metrics
        // This could involve updating social scores, interaction counts, etc.
        logger.debug("Updating social metrics for user: {} with interaction type: {}", userId, interactionType);
    }

    private void updateUserSubscriptionStatus(Long userId, String subscriptionStatus) {
        // Implementation for updating user subscription status
        // This could involve updating user roles, permissions, etc.
        logger.debug("Updating subscription status for user: {} to status: {}", userId, subscriptionStatus);
    }
}
