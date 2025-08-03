package com.raved.social.event;

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
 * Publisher for social events to Kafka
 */
@Component
public class SocialEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(SocialEventPublisher.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.topics.social-events:social-events}")
    private String socialEventsTopic;

    /**
     * Publish like event
     */
    public void publishLikeEvent(Long userId, Long postId, Long postAuthorId) {
        logger.info("Publishing like event: user {} liked post {} by user {}", userId, postId, postAuthorId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", userId);
        event.put("interactionType", "LIKE");
        event.put("targetId", postId);
        event.put("targetType", "POST");
        event.put("targetAuthorId", postAuthorId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish unlike event
     */
    public void publishUnlikeEvent(Long userId, Long postId, Long postAuthorId) {
        logger.info("Publishing unlike event: user {} unliked post {} by user {}", userId, postId, postAuthorId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", userId);
        event.put("interactionType", "UNLIKE");
        event.put("targetId", postId);
        event.put("targetType", "POST");
        event.put("targetAuthorId", postAuthorId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish comment event
     */
    public void publishCommentEvent(Long userId, Long postId, Long postAuthorId, Long commentId, String commentContent) {
        logger.info("Publishing comment event: user {} commented on post {} by user {}", userId, postId, postAuthorId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", userId);
        event.put("interactionType", "COMMENT");
        event.put("targetId", postId);
        event.put("targetType", "POST");
        event.put("targetAuthorId", postAuthorId);
        event.put("commentId", commentId);
        event.put("commentContent", commentContent);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish comment reply event
     */
    public void publishCommentReplyEvent(Long userId, Long commentId, Long commentAuthorId, Long replyId, String replyContent) {
        logger.info("Publishing comment reply event: user {} replied to comment {} by user {}", userId, commentId, commentAuthorId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", userId);
        event.put("interactionType", "COMMENT_REPLY");
        event.put("targetId", commentId);
        event.put("targetType", "COMMENT");
        event.put("targetAuthorId", commentAuthorId);
        event.put("replyId", replyId);
        event.put("replyContent", replyContent);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish follow event
     */
    public void publishFollowEvent(Long followerId, Long followingId) {
        logger.info("Publishing follow event: user {} followed user {}", followerId, followingId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", followerId);
        event.put("interactionType", "FOLLOW");
        event.put("targetId", followingId);
        event.put("targetType", "USER");
        event.put("targetAuthorId", followingId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, followerId.toString());
    }

    /**
     * Publish unfollow event
     */
    public void publishUnfollowEvent(Long followerId, Long followingId) {
        logger.info("Publishing unfollow event: user {} unfollowed user {}", followerId, followingId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", followerId);
        event.put("interactionType", "UNFOLLOW");
        event.put("targetId", followingId);
        event.put("targetType", "USER");
        event.put("targetAuthorId", followingId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, followerId.toString());
    }

    /**
     * Publish mention event
     */
    public void publishMentionEvent(Long userId, Long mentionedUserId, Long postId, String content) {
        logger.info("Publishing mention event: user {} mentioned user {} in post {}", userId, mentionedUserId, postId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", userId);
        event.put("interactionType", "MENTION");
        event.put("targetId", postId);
        event.put("targetType", "POST");
        event.put("targetAuthorId", mentionedUserId);
        event.put("mentionedUserId", mentionedUserId);
        event.put("content", content);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish share event
     */
    public void publishShareEvent(Long userId, Long postId, Long postAuthorId, String shareType) {
        logger.info("Publishing share event: user {} shared post {} by user {}", userId, postId, postAuthorId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", userId);
        event.put("interactionType", "SHARE");
        event.put("targetId", postId);
        event.put("targetType", "POST");
        event.put("targetAuthorId", postAuthorId);
        event.put("shareType", shareType);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish block event
     */
    public void publishBlockEvent(Long blockerId, Long blockedId) {
        logger.info("Publishing block event: user {} blocked user {}", blockerId, blockedId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", blockerId);
        event.put("interactionType", "BLOCK");
        event.put("targetId", blockedId);
        event.put("targetType", "USER");
        event.put("targetAuthorId", blockedId);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, blockerId.toString());
    }

    /**
     * Publish report event
     */
    public void publishReportEvent(Long reporterId, Long targetId, String targetType, String reason) {
        logger.info("Publishing report event: user {} reported {} {} for {}", reporterId, targetType, targetId, reason);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "SOCIAL_INTERACTION");
        event.put("userId", reporterId);
        event.put("interactionType", "REPORT");
        event.put("targetId", targetId);
        event.put("targetType", targetType);
        event.put("reason", reason);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, reporterId.toString());
    }

    private void publishEvent(Map<String, Object> event, String key) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(socialEventsTopic, key, eventJson);
            logger.debug("Social event published successfully: {}", event.get("interactionType"));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize social event: {}", event.get("interactionType"), e);
        } catch (Exception e) {
            logger.error("Failed to publish social event: {}", event.get("interactionType"), e);
        }
    }
}
