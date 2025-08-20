package com.raved.notification.service.impl;

import com.raved.notification.dto.request.UpdateNotificationPreferencesRequest;
import com.raved.notification.dto.response.NotificationPreferencesResponse;
import com.raved.notification.model.DeviceToken;
import com.raved.notification.repository.DeviceTokenRepository;
import com.raved.notification.service.NotificationPreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationPreferencesServiceImpl implements NotificationPreferencesService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationPreferencesServiceImpl.class);

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Override
    public NotificationPreferencesResponse getUserPreferences(String userId) {
        NotificationPreferencesResponse resp = new NotificationPreferencesResponse();
        resp.setPushEnabled(true);
        resp.setEmailEnabled(true);
        resp.setSmsEnabled(false);
        resp.setQuietHours(false);
        resp.setTimezone("UTC");
        return resp;
    }

    @Override
    public NotificationPreferencesResponse updateUserPreferences(String userId, UpdateNotificationPreferencesRequest request) {
        logger.info("Updating notification preferences for user {}", userId);
        NotificationPreferencesResponse resp = new NotificationPreferencesResponse();
        resp.setPushEnabled(request.getChannels().isPushEnabled());
        resp.setEmailEnabled(request.getChannels().isEmailEnabled());
        resp.setSmsEnabled(request.getChannels().isSmsEnabled());
        resp.setQuietHours(request.getQuietHours().isEnabled());
        resp.setQuietStart(request.getQuietHours().getStartTime());
        resp.setQuietEnd(request.getQuietHours().getEndTime());
        resp.setTimezone(request.getQuietHours().getTimezone());
        return resp;
    }

    @Override
    public void registerDeviceToken(String userId, String token, String platform, String deviceId) {
        DeviceToken dt = new DeviceToken();
        dt.setUserId(userId);
        dt.setToken(token);
        try {
            dt.setPlatform(DeviceToken.Platform.valueOf(platform.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            dt.setPlatform(DeviceToken.Platform.WEB);
        }
        deviceTokenRepository.save(dt);
    }

    @Override
    public void unregisterDeviceToken(String userId, String token) {
        deviceTokenRepository.deleteByToken(token);
    }

    @Override
    public boolean testNotificationDelivery(String userId, String channel) {
        return true;
    }

    @Override
    public Map<String, Object> getUserNotificationStatistics(String userId) {
        return new HashMap<>();
    }
}

