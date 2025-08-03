package com.raved.user.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Publisher for user events to Kafka
 */
@Component
public class UserEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(UserEventPublisher.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.topics.user-events:user-events}")
    private String userEventsTopic;

    /**
     * Publish user registration event
     */
    public void publishUserRegisteredEvent(Long userId, String username, String email) {
        logger.info("Publishing user registered event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "USER_REGISTERED");
        event.put("username", username);
        event.put("email", email);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish password reset requested event
     */
    public void publishPasswordResetRequestedEvent(Long userId, String resetToken) {
        logger.info("Publishing password reset requested event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "PASSWORD_RESET_REQUESTED");
        event.put("resetToken", resetToken);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish email verification requested event
     */
    public void publishEmailVerificationRequestedEvent(Long userId, String verificationToken) {
        logger.info("Publishing email verification requested event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "EMAIL_VERIFICATION_REQUESTED");
        event.put("verificationToken", verificationToken);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish user profile updated event
     */
    public void publishUserProfileUpdatedEvent(Long userId, Map<String, Object> changes) {
        logger.info("Publishing user profile updated event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "PROFILE_UPDATED");
        event.put("changes", changes);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish user login event
     */
    public void publishUserLoginEvent(Long userId, String ipAddress, String userAgent) {
        logger.info("Publishing user login event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "USER_LOGIN");
        event.put("ipAddress", ipAddress);
        event.put("userAgent", userAgent);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish user logout event
     */
    public void publishUserLogoutEvent(Long userId) {
        logger.info("Publishing user logout event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "USER_LOGOUT");
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish account verification event
     */
    public void publishAccountVerifiedEvent(Long userId) {
        logger.info("Publishing account verified event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "ACCOUNT_VERIFIED");
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish account deactivated event
     */
    public void publishAccountDeactivatedEvent(Long userId, String reason) {
        logger.info("Publishing account deactivated event for user: {}", userId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_ACTIVITY");
        event.put("userId", userId);
        event.put("activityType", "ACCOUNT_DEACTIVATED");
        event.put("reason", reason);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    private void publishEvent(Map<String, Object> event, String key) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(userEventsTopic, key, eventJson);
            logger.debug("User event published successfully: {}", event.get("activityType"));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize user event: {}", event.get("activityType"), e);
        } catch (Exception e) {
            logger.error("Failed to publish user event: {}", event.get("activityType"), e);
        }
    }
}
