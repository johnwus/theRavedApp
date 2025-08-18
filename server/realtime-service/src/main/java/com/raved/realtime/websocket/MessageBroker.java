package com.raved.realtime.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Message broker for WebSocket communication
 */
@Component
public class MessageBroker {

    private static final Logger logger = LoggerFactory.getLogger(MessageBroker.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Broadcast a message to all users in a chat room
     */
    public void broadcastToRoom(String roomId, String eventType, Object payload) {
        logger.debug("Broadcasting {} event to room: {}", eventType, roomId);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, 
                createMessage(eventType, payload));
    }

    /**
     * Send a message to a specific user
     */
    public void sendToUser(Long userId, String eventType, Object payload) {
        logger.debug("Sending {} event to user: {}", eventType, userId);
        messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/private", 
                createMessage(eventType, payload));
    }

    /**
     * Broadcast user presence update
     */
    public void broadcastPresenceUpdate(Map<String, Object> presenceData) {
        logger.debug("Broadcasting presence update for user: {}", presenceData.get("userId"));
        messagingTemplate.convertAndSend("/topic/presence", 
                createMessage("PRESENCE_UPDATE", presenceData));
    }

    /**
     * Broadcast user online status
     */
    public void broadcastUserOnline(Long userId) {
        logger.debug("Broadcasting online status for user: {}", userId);
        messagingTemplate.convertAndSend("/topic/presence", 
                createMessage("USER_ONLINE", userId));
    }

    /**
     * Broadcast user offline status
     */
    public void broadcastUserOffline(Long userId) {
        logger.debug("Broadcasting offline status for user: {}", userId);
        messagingTemplate.convertAndSend("/topic/presence", 
                createMessage("USER_OFFLINE", userId));
    }

    /**
     * Create a message with event type and payload
     */
    private Map<String, Object> createMessage(String eventType, Object payload) {
        return Map.of(
            "eventType", eventType,
            "payload", payload,
            "timestamp", System.currentTimeMillis()
        );
    }
}