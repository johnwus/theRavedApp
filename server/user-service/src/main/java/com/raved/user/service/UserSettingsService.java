package com.raved.user.service;

import com.raved.user.model.UserSettings;
import com.raved.user.dto.request.settings.PrivacySettingsUpdateRequest;
import com.raved.user.dto.request.settings.PreferencesUpdateRequest;

public interface UserSettingsService {
    UserSettings getOrCreate(Long userId);
    UserSettings updatePrivacy(Long userId, PrivacySettingsUpdateRequest request);
    UserSettings updatePreferences(Long userId, PreferencesUpdateRequest request);
}

