package com.raved.realtime.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.realtime.service.MessageService;
import com.raved.realtime.service.PresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * RabbitMQ message listeners for real-time communication
 */
@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PresenceService presenceService;

    /**
     * Handle incoming chat messages
     */
    @RabbitListener(queues = "${rabbitmq.queues.chat-messages:chat.messages}")
    public void handleChatMessage(
            @Payload String message,
            @Header(value = "amqp_receivedRoutingKey", required = false) String routingKey) {
        
        logger.info("Received chat message with routing key: {}", routingKey);
        
        try {
            Map<String, Object> messageData = objectMapper.readValue(message, Map.class);
            
            Long senderId = Long.valueOf(messageData.get("senderId").toString());
            Long roomId = Long.valueOf(messageData.get("roomId").toString());
            String content = (String) messageData.get("content");
            String messageType = (String) messageData.getOrDefault("messageType", "TEXT");
            
            logger.info("Processing chat message from user: {} to room: {}", senderId, roomId);
            
            // Process the message through the message service
            messageService.processIncomingMessage(senderId, roomId, content, messageType);
            
            logger.debug("Successfully processed chat message from user: {}", senderId);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse chat message: {}", message, e);
        } catch (Exception e) {
            logger.error("Failed to process chat message: {}", message, e);
        }
    }

    /**
     * Handle presence updates
     */
    @RabbitListener(queues = "${rabbitmq.queues.presence-updates:presence.updates}")
    public void handlePresenceUpdate(
            @Payload String message,
            @Header(value = "amqp_receivedRoutingKey", required = false) String routingKey) {
        
        logger.info("Received presence update with routing key: {}", routingKey);
        
        try {
            Map<String, Object> presenceData = objectMapper.readValue(message, Map.class);
            
            Long userId = Long.valueOf(presenceData.get("userId").toString());
            String status = (String) presenceData.get("status");
            String activity = (String) presenceData.getOrDefault("activity", "");
            
            logger.info("Processing presence update for user: {} with status: {}", userId, status);
            
            // Update user presence through the presence service
            presenceService.updateUserPresence(userId, status, activity);
            
            logger.debug("Successfully processed presence update for user: {}", userId);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse presence update: {}", message, e);
        } catch (Exception e) {
            logger.error("Failed to process presence update: {}", message, e);
        }
    }

    /**
     * Handle notification delivery requests
     */
    @RabbitListener(queues = "${rabbitmq.queues.notifications:notifications}")
    public void handleNotificationDelivery(
            @Payload String message,
            @Header(value = "amqp_receivedRoutingKey", required = false) String routingKey) {
        
        logger.info("Received notification delivery request with routing key: {}", routingKey);
        
        try {
            Map<String, Object> notificationData = objectMapper.readValue(message, Map.class);
            
            Long userId = Long.valueOf(notificationData.get("userId").toString());
            String notificationType = (String) notificationData.get("type");
            String title = (String) notificationData.get("title");
            String body = (String) notificationData.get("body");
            
            logger.info("Processing notification delivery for user: {} of type: {}", userId, notificationType);
            
            // Deliver real-time notification through WebSocket
            deliverRealtimeNotification(userId, notificationType, title, body);
            
            logger.debug("Successfully delivered notification to user: {}", userId);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse notification delivery request: {}", message, e);
        } catch (Exception e) {
            logger.error("Failed to process notification delivery: {}", message, e);
        }
    }

    /**
     * Handle room events (user joined, left, etc.)
     */
    @RabbitListener(queues = "${rabbitmq.queues.room-events:room.events}")
    public void handleRoomEvent(
            @Payload String message,
            @Header(value = "amqp_receivedRoutingKey", required = false) String routingKey) {
        
        logger.info("Received room event with routing key: {}", routingKey);
        
        try {
            Map<String, Object> roomEventData = objectMapper.readValue(message, Map.class);
            
            Long userId = Long.valueOf(roomEventData.get("userId").toString());
            Long roomId = Long.valueOf(roomEventData.get("roomId").toString());
            String eventType = (String) roomEventData.get("eventType");
            
            logger.info("Processing room event: {} for user: {} in room: {}", eventType, userId, roomId);
            
            // Process room event
            processRoomEvent(userId, roomId, eventType);
            
            logger.debug("Successfully processed room event: {} for user: {}", eventType, userId);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse room event: {}", message, e);
        } catch (Exception e) {
            logger.error("Failed to process room event: {}", message, e);
        }
    }

    private void deliverRealtimeNotification(Long userId, String type, String title, String body) {
        // Implementation for delivering real-time notifications via WebSocket
        logger.debug("Delivering real-time notification to user: {} - {}: {}", userId, title, body);
        // This would typically involve sending the notification through WebSocket to connected clients
    }

    private void processRoomEvent(Long userId, Long roomId, String eventType) {
        // Implementation for processing room events
        logger.debug("Processing room event: {} for user: {} in room: {}", eventType, userId, roomId);
        // This could involve updating room membership, broadcasting events to room participants, etc.
    }
}
