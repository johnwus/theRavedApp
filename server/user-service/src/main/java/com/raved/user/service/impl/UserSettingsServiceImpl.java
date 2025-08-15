package com.raved.user.service.impl;

import com.raved.user.dto.request.settings.PrivacySettingsUpdateRequest;
import com.raved.user.dto.request.settings.PreferencesUpdateRequest;
import com.raved.user.model.UserSettings;
import com.raved.user.repository.UserSettingsRepository;
import com.raved.user.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    @Autowired
    private UserSettingsRepository repository;

    @Override
    public UserSettings getOrCreate(Long userId) {
        return repository.findById(userId).orElseGet(() -> {
            UserSettings us = new UserSettings();
            us.setUserId(userId);
            return repository.save(us);
        });
    }

    @Override
    public UserSettings updatePrivacy(Long userId, PrivacySettingsUpdateRequest req) {
        UserSettings us = getOrCreate(userId);
        if (req.getShowActivity() != null) us.setShowActivity(req.getShowActivity());
        if (req.getReadReceipts() != null) us.setReadReceipts(req.getReadReceipts());
        if (req.getAllowDownloads() != null) us.setAllowDownloads(req.getAllowDownloads());
        if (req.getAllowStorySharing() != null) us.setAllowStorySharing(req.getAllowStorySharing());
        us.setUpdatedAt(Instant.now());
        return repository.save(us);
    }

    @Override
    public UserSettings updatePreferences(Long userId, PreferencesUpdateRequest req) {
        UserSettings us = getOrCreate(userId);
        if (req.getAnalyticsEnabled() != null) us.setAnalyticsEnabled(req.getAnalyticsEnabled());
        if (req.getPersonalizedAds() != null) us.setPersonalizedAds(req.getPersonalizedAds());
        if (req.getLanguage() != null) us.setLanguage(req.getLanguage());
        if (req.getDateFormat() != null) us.setDateFormat(req.getDateFormat());
        if (req.getCurrency() != null) us.setCurrency(req.getCurrency());
        if (req.getTheme() != null) us.setTheme(req.getTheme());
        if (req.getNotificationPreferencesJson() != null) us.setNotificationPreferences(req.getNotificationPreferencesJson());
        us.setUpdatedAt(Instant.now());
        return repository.save(us);
    }
}

