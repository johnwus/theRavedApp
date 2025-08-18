package com.raved.user.controller.v1;

import com.raved.user.dto.request.settings.PrivacySettingsUpdateRequest;
import com.raved.user.dto.request.settings.PreferencesUpdateRequest;
import com.raved.user.model.UserSettings;
import com.raved.user.service.UserSettingsService;
import com.raved.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users/settings")
@CrossOrigin(origins = "*")
public class SettingsV1Controller {

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get current user's settings")
    @GetMapping
    public ResponseEntity<UserSettings> getSettings(Principal principal) {
        Long userId = userService.findUserByUsernameOrEmail(principal.getName())
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return ResponseEntity.ok(userSettingsService.getOrCreate(userId));
    }

    @Operation(summary = "Update privacy settings")
    @PutMapping("/privacy")
    public ResponseEntity<UserSettings> updatePrivacy(@RequestBody PrivacySettingsUpdateRequest payload,
            Principal principal) {
        Long userId = userService.findUserByUsernameOrEmail(principal.getName())
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return ResponseEntity.ok(userSettingsService.updatePrivacy(userId, payload));
    }

    @Operation(summary = "Update user preferences")
    @PutMapping("/preferences")
    public ResponseEntity<UserSettings> updatePreferences(@RequestBody PreferencesUpdateRequest payload,
            Principal principal) {
        Long userId = userService.findUserByUsernameOrEmail(principal.getName())
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return ResponseEntity.ok(userSettingsService.updatePreferences(userId, payload));
    }
}
