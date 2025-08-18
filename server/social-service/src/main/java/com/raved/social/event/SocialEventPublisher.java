package com.raved.social.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Publisher for social events to Kafka topics
 */
@Component
public class SocialEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(SocialEventPublisher.class);
    
    private static final String LIKE_TOPIC = "social.likes";
    private static final String COMMENT_TOPIC = "social.comments";
    private static final String FOLLOW_TOPIC = "social.follows";
    private static final String ACTIVITY_TOPIC = "social.activities";
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public SocialEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Publish like event
     */
    public void publishLikeEvent(Long userId, Long postId, Long postAuthorId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "LIKE_CREATED");
        event.put("userId", userId);
        event.put("postId", postId);
        event.put("postAuthorId", postAuthorId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(LIKE_TOPIC, userId.toString(), event);
    }
    
    /**
     * Publish like removed event
     */
    public void publishLikeRemovedEvent(Long userId, Long postId, Long postAuthorId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "LIKE_REMOVED");
        event.put("userId", userId);
        event.put("postId", postId);
        event.put("postAuthorId", postAuthorId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(LIKE_TOPIC, userId.toString(), event);
    }
    
    /**
     * Publish comment created event
     */
    public void publishCommentCreatedEvent(Long userId, Long postId, Long commentId, Long postAuthorId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "COMMENT_CREATED");
        event.put("userId", userId);
        event.put("postId", postId);
        event.put("commentId", commentId);
        event.put("postAuthorId", postAuthorId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(COMMENT_TOPIC, userId.toString(), event);
    }
    
    /**
     * Publish comment updated event
     */
    public void publishCommentUpdatedEvent(Long userId, Long postId, Long commentId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "COMMENT_UPDATED");
        event.put("userId", userId);
        event.put("postId", postId);
        event.put("commentId", commentId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(COMMENT_TOPIC, userId.toString(), event);
    }
    
    /**
     * Publish comment deleted event
     */
    public void publishCommentDeletedEvent(Long userId, Long postId, Long commentId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "COMMENT_DELETED");
        event.put("userId", userId);
        event.put("postId", postId);
        event.put("commentId", commentId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(COMMENT_TOPIC, userId.toString(), event);
    }
    
    /**
     * Publish follow created event
     */
    public void publishFollowCreatedEvent(Long followerId, Long followingId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "FOLLOW_CREATED");
        event.put("followerId", followerId);
        event.put("followingId", followingId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(FOLLOW_TOPIC, followerId.toString(), event);
    }
    
    /**
     * Publish follow removed event
     */
    public void publishFollowRemovedEvent(Long followerId, Long followingId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "FOLLOW_REMOVED");
        event.put("followerId", followerId);
        event.put("followingId", followingId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(FOLLOW_TOPIC, followerId.toString(), event);
    }
    
    /**
     * Publish activity created event
     */
    public void publishActivityCreatedEvent(Long userId, String activityType, Long targetId, String targetType) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ACTIVITY_CREATED");
        event.put("userId", userId);
        event.put("activityType", activityType);
        event.put("targetId", targetId);
        event.put("targetType", targetType);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(ACTIVITY_TOPIC, userId.toString(), event);
    }
    
    /**
     * Generic method to publish event to Kafka
     */
    private void publishEvent(String topic, String key, Map<String, Object> event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, key, eventJson)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        logger.error("Failed to send event to topic {}: {}", topic, ex.getMessage());
                    } else {
                        logger.info("Event sent to topic {}: {}", topic, event.get("eventType"));
                    }
                });
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event: {}", e.getMessage());
        }
    }
}