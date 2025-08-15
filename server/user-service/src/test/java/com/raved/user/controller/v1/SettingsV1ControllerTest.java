package com.raved.user.controller.v1;

import com.raved.user.dto.request.settings.PrivacySettingsUpdateRequest;
import com.raved.user.dto.request.settings.PreferencesUpdateRequest;
import com.raved.user.model.User;
import com.raved.user.model.UserSettings;
import com.raved.user.service.UserService;
import com.raved.user.service.UserSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingsV1ControllerTest {

    @Mock private UserSettingsService userSettingsService;
    @Mock private UserService userService;
    @InjectMocks private SettingsV1Controller controller;

    private Principal principal;
    private User user;

    @BeforeEach
    void setUp() {
        principal = () -> "testuser";
        user = new User();
        user.setId(99L);
        user.setUsername("testuser");
    }

    @Test
    void getSettings_returnsSettingsForPrincipal() {
        when(userService.findUserByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));
        UserSettings settings = new UserSettings();
        settings.setUserId(99L);
        when(userSettingsService.getOrCreate(99L)).thenReturn(settings);

        ResponseEntity<UserSettings> resp = controller.getSettings(principal);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getUserId()).isEqualTo(99L);
    }

    @Test
    void updatePrivacy_updatesSettings() {
        when(userService.findUserByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));
        UserSettings settings = new UserSettings();
        settings.setUserId(99L);
        when(userSettingsService.updatePrivacy(any(Long.class), any(PrivacySettingsUpdateRequest.class))).thenReturn(settings);

        ResponseEntity<UserSettings> resp = controller.updatePrivacy(new PrivacySettingsUpdateRequest(), principal);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getUserId()).isEqualTo(99L);
    }

    @Test
    void updatePreferences_updatesSettings() {
        when(userService.findUserByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));
        UserSettings settings = new UserSettings();
        settings.setUserId(99L);
        when(userSettingsService.updatePreferences(any(Long.class), any(PreferencesUpdateRequest.class))).thenReturn(settings);

        ResponseEntity<UserSettings> resp = controller.updatePreferences(new PreferencesUpdateRequest(), principal);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getUserId()).isEqualTo(99L);
    }
}

