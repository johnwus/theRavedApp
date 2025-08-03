package com.raved.notification.service.impl;

import com.google.firebase.messaging.*;
import com.raved.notification.model.DeviceToken;
import com.raved.notification.model.Notification;
import com.raved.notification.repository.DeviceTokenRepository;
import com.raved.notification.service.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of PushNotificationService using Firebase Cloud Messaging
 */
@Service
@Transactional
public class PushNotificationServiceImpl implements PushNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Override
    public boolean sendPushNotification(String deviceToken, String title, String body, Map<String, String> data) {
        logger.info("Sending push notification to device token: {}", deviceToken.substring(0, 10) + "...");
        
        try {
            Message.Builder messageBuilder = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build());
            
            if (data != null && !data.isEmpty()) {
                messageBuilder.putAllData(data);
            }
            
            Message message = messageBuilder.build();
            String response = FirebaseMessaging.getInstance().send(message);
            
            logger.info("Push notification sent successfully. Response: {}", response);
            return true;
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to send push notification to device token: {}", deviceToken, e);
            
            // Handle invalid tokens
            if (e.getErrorCode() == "UNREGISTERED" || e.getErrorCode() == "INVALID_ARGUMENT") {
                logger.info("Removing invalid device token: {}", deviceToken);
                deviceTokenRepository.deleteByToken(deviceToken);
            }
            
            return false;
        }
    }

    @Override
    public void sendPushNotificationToMultipleDevices(List<String> deviceTokens, String title, String body, Map<String, String> data) {
        logger.info("Sending push notification to {} devices", deviceTokens.size());
        
        if (deviceTokens.isEmpty()) {
            logger.warn("No device tokens provided for push notification");
            return;
        }
        
        try {
            MulticastMessage.Builder messageBuilder = MulticastMessage.builder()
                    .addAllTokens(deviceTokens)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build());
            
            if (data != null && !data.isEmpty()) {
                messageBuilder.putAllData(data);
            }
            
            MulticastMessage message = messageBuilder.build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            
            logger.info("Push notification sent to {} devices. Success: {}, Failure: {}", 
                    deviceTokens.size(), response.getSuccessCount(), response.getFailureCount());
            
            // Handle failed tokens
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();
                
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(deviceTokens.get(i));
                        logger.warn("Failed to send to token: {}, Error: {}", 
                                deviceTokens.get(i), responses.get(i).getException().getMessage());
                    }
                }
                
                // Remove invalid tokens
                cleanupFailedTokens(failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to send multicast push notification", e);
        }
    }

    @Override
    public void sendPushNotificationToUser(Long userId, String title, String body, Map<String, String> data) {
        logger.info("Sending push notification to user: {}", userId);
        
        List<DeviceToken> deviceTokens = deviceTokenRepository.findByUserIdAndIsActiveTrue(userId);
        if (deviceTokens.isEmpty()) {
            logger.warn("No active device tokens found for user: {}", userId);
            return;
        }
        
        List<String> tokens = deviceTokens.stream()
                .map(DeviceToken::getToken)
                .collect(Collectors.toList());
        
        sendPushNotificationToMultipleDevices(tokens, title, body, data);
    }

    @Override
    public boolean sendNotificationPush(Notification notification, List<String> deviceTokens) {
        logger.info("Sending notification push for notification: {} to {} devices", 
                notification.getId(), deviceTokens.size());
        
        if (deviceTokens.isEmpty()) {
            logger.warn("No device tokens provided for notification: {}", notification.getId());
            return false;
        }
        
        Map<String, String> data = new HashMap<>();
        data.put("notificationId", notification.getId().toString());
        data.put("type", notification.getNotificationType().name());
        data.put("userId", notification.getRecipientUserId().toString());
        
        if (deviceTokens.size() == 1) {
            return sendPushNotification(deviceTokens.get(0), notification.getSubject(), 
                    notification.getContent(), data);
        } else {
            sendPushNotificationToMultipleDevices(deviceTokens, notification.getSubject(), 
                    notification.getContent(), data);
            return true;
        }
    }

    @Override
    public void registerDeviceToken(Long userId, String deviceToken, String deviceType, String appVersion) {
        logger.info("Registering device token for user: {}", userId);
        
        // Check if token already exists
        DeviceToken existingToken = deviceTokenRepository.findByToken(deviceToken);
        if (existingToken != null) {
            // Update existing token
            existingToken.setUserId(userId);
            existingToken.setDeviceType(deviceType);
            existingToken.setAppVersion(appVersion);
            existingToken.setIsActive(true);
            existingToken.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(existingToken);
            logger.info("Updated existing device token for user: {}", userId);
        } else {
            // Create new token
            DeviceToken newToken = new DeviceToken();
            newToken.setUserId(userId);
            newToken.setToken(deviceToken);
            newToken.setDeviceType(deviceType);
            newToken.setAppVersion(appVersion);
            newToken.setIsActive(true);
            newToken.setCreatedAt(LocalDateTime.now());
            newToken.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(newToken);
            logger.info("Registered new device token for user: {}", userId);
        }
    }

    @Override
    public void unregisterDeviceToken(String deviceToken) {
        logger.info("Unregistering device token: {}", deviceToken.substring(0, 10) + "...");
        
        DeviceToken token = deviceTokenRepository.findByToken(deviceToken);
        if (token != null) {
            token.setIsActive(false);
            token.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(token);
            logger.info("Device token unregistered successfully");
        } else {
            logger.warn("Device token not found for unregistration");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceToken> getUserDeviceTokens(Long userId) {
        logger.debug("Getting device tokens for user: {}", userId);
        return deviceTokenRepository.findByUserIdAndIsActiveTrue(userId);
    }

    @Override
    public boolean sendTopicNotification(String topic, String title, String body, Map<String, String> data) {
        logger.info("Sending topic notification to topic: {}", topic);
        
        try {
            Message.Builder messageBuilder = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build());
            
            if (data != null && !data.isEmpty()) {
                messageBuilder.putAllData(data);
            }
            
            Message message = messageBuilder.build();
            String response = FirebaseMessaging.getInstance().send(message);
            
            logger.info("Topic notification sent successfully. Response: {}", response);
            return true;
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to send topic notification to topic: {}", topic, e);
            return false;
        }
    }

    @Override
    public void subscribeToTopic(String deviceToken, String topic) {
        logger.info("Subscribing device to topic: {}", topic);
        
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(List.of(deviceToken), topic);
            
            logger.info("Subscribed to topic: {}. Success: {}, Failure: {}", 
                    topic, response.getSuccessCount(), response.getFailureCount());
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to subscribe to topic: {}", topic, e);
        }
    }

    @Override
    public void unsubscribeFromTopic(String deviceToken, String topic) {
        logger.info("Unsubscribing device from topic: {}", topic);
        
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(List.of(deviceToken), topic);
            
            logger.info("Unsubscribed from topic: {}. Success: {}, Failure: {}", 
                    topic, response.getSuccessCount(), response.getFailureCount());
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to unsubscribe from topic: {}", topic, e);
        }
    }

    @Override
    public boolean sendSilentPushNotification(String deviceToken, Map<String, String> data) {
        logger.info("Sending silent push notification to device token: {}", deviceToken.substring(0, 10) + "...");
        
        try {
            Message message = Message.builder()
                    .setToken(deviceToken)
                    .putAllData(data)
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setContentAvailable(true)
                                    .build())
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .build();
            
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Silent push notification sent successfully. Response: {}", response);
            return true;
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to send silent push notification", e);
            return false;
        }
    }

    @Override
    public boolean sendScheduledPushNotification(String deviceToken, String title, String body, 
                                               Map<String, String> data, long delayInSeconds) {
        logger.info("Scheduling push notification with delay: {} seconds", delayInSeconds);
        
        // For now, we'll use a simple approach. In production, you might want to use a job scheduler
        new Thread(() -> {
            try {
                Thread.sleep(delayInSeconds * 1000);
                sendPushNotification(deviceToken, title, body, data);
            } catch (InterruptedException e) {
                logger.error("Scheduled push notification interrupted", e);
                Thread.currentThread().interrupt();
            }
        }).start();
        
        return true;
    }

    @Override
    public boolean isValidDeviceToken(String deviceToken) {
        // Basic validation - in production, you might want to test with Firebase
        return deviceToken != null && deviceToken.length() > 50 && !deviceToken.contains(" ");
    }

    @Override
    public void cleanupInvalidTokens() {
        logger.info("Cleaning up invalid device tokens");
        
        // Remove tokens older than 90 days that haven't been updated
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(90);
        List<DeviceToken> oldTokens = deviceTokenRepository.findByUpdatedAtBeforeAndIsActiveTrue(cutoffDate);
        
        oldTokens.forEach(token -> {
            token.setIsActive(false);
            token.setUpdatedAt(LocalDateTime.now());
        });
        
        deviceTokenRepository.saveAll(oldTokens);
        logger.info("Cleaned up {} old device tokens", oldTokens.size());
    }

    private void cleanupFailedTokens(List<String> failedTokens) {
        logger.info("Cleaning up {} failed device tokens", failedTokens.size());
        
        failedTokens.forEach(token -> {
            DeviceToken deviceToken = deviceTokenRepository.findByToken(token);
            if (deviceToken != null) {
                deviceToken.setIsActive(false);
                deviceToken.setUpdatedAt(LocalDateTime.now());
                deviceTokenRepository.save(deviceToken);
            }
        });
    }
}
