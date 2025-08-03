package com.raved.notification.service;

import com.raved.notification.model.DeviceToken;
import com.raved.notification.model.Notification;

import java.util.List;
import java.util.Map;

/**
 * PushNotificationService for TheRavedApp
 */
public interface PushNotificationService {

    /**
     * Send push notification to a single device
     */
    boolean sendPushNotification(String deviceToken, String title, String body, Map<String, String> data);

    /**
     * Send push notification to multiple devices
     */
    void sendPushNotificationToMultipleDevices(List<String> deviceTokens, String title, String body, Map<String, String> data);

    /**
     * Send push notification to a user (all their devices)
     */
    void sendPushNotificationToUser(Long userId, String title, String body, Map<String, String> data);

    /**
     * Send notification via push
     */
    boolean sendNotificationPush(Notification notification, List<String> deviceTokens);

    /**
     * Register device token for a user
     */
    void registerDeviceToken(Long userId, String deviceToken, String deviceType, String appVersion);

    /**
     * Unregister device token
     */
    void unregisterDeviceToken(String deviceToken);

    /**
     * Get device tokens for a user
     */
    List<DeviceToken> getUserDeviceTokens(Long userId);

    /**
     * Send topic-based notification
     */
    boolean sendTopicNotification(String topic, String title, String body, Map<String, String> data);

    /**
     * Subscribe user to topic
     */
    void subscribeToTopic(String deviceToken, String topic);

    /**
     * Unsubscribe user from topic
     */
    void unsubscribeFromTopic(String deviceToken, String topic);

    /**
     * Send silent push notification (data only)
     */
    boolean sendSilentPushNotification(String deviceToken, Map<String, String> data);

    /**
     * Send scheduled push notification
     */
    boolean sendScheduledPushNotification(String deviceToken, String title, String body,
                                        Map<String, String> data, long delayInSeconds);

    /**
     * Validate device token
     */
    boolean isValidDeviceToken(String deviceToken);

    /**
     * Clean up invalid device tokens
     */
    void cleanupInvalidTokens();
}
