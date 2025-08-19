package com.raved.notification.service;

import com.raved.notification.dto.request.UpdateNotificationPreferencesRequest;
import com.raved.notification.dto.response.NotificationPreferencesResponse;

import java.util.Map;

public interface NotificationPreferencesService {
    NotificationPreferencesResponse getUserPreferences(String userId);
    NotificationPreferencesResponse updateUserPreferences(String userId, UpdateNotificationPreferencesRequest request);
    void registerDeviceToken(String userId, String token, String platform, String deviceId);
    void unregisterDeviceToken(String userId, String token);
    boolean testNotificationDelivery(String userId, String channel);
    Map<String, Object> getUserNotificationStatistics(String userId);
}

