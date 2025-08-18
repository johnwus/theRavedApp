package com.raved.notification.controller;

import com.raved.notification.dto.request.UpdateNotificationPreferencesRequest;
import com.raved.notification.dto.response.NotificationPreferencesResponse;
import com.raved.notification.service.NotificationPreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Notification Preferences Management
 * Handles user notification preferences and settings
 */
@RestController
@RequestMapping("/api/v1/notifications/preferences")
@Tag(name = "Notification Preferences", description = "APIs for managing user notification preferences")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationPreferencesController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationPreferencesController.class);

    @Autowired
    private NotificationPreferencesService preferencesService;

    @Operation(summary = "Get user's notification preferences")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getNotificationPreferences(Authentication authentication) {
        
        logger.debug("Getting notification preferences for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            NotificationPreferencesResponse preferences = preferencesService.getUserPreferences(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "preferences", preferences
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting notification preferences for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get notification preferences: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(summary = "Update user's notification preferences")
    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updateNotificationPreferences(
            @Valid @RequestBody UpdateNotificationPreferencesRequest request,
            Authentication authentication) {
        
        logger.info("Updating notification preferences for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            NotificationPreferencesResponse preferences = preferencesService.updateUserPreferences(userId, request);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "preferences", preferences,
                "message", "Notification preferences updated successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating notification preferences for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to update notification preferences: " + e.getMessage()
            );
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @Operation(summary = "Register device token for push notifications")
    @PostMapping("/device-token")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> registerDeviceToken(
            @Parameter(description = "Device token") @RequestParam String token,
            @Parameter(description = "Platform") @RequestParam String platform,
            @Parameter(description = "Device ID") @RequestParam(required = false) String deviceId,
            Authentication authentication) {
        
        logger.info("Registering device token for user: {} on platform: {}", authentication.getName(), platform);

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            preferencesService.registerDeviceToken(userId, token, platform, deviceId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Device token registered successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error registering device token for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to register device token: " + e.getMessage()
            );
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @Operation(summary = "Unregister device token")
    @DeleteMapping("/device-token")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> unregisterDeviceToken(
            @Parameter(description = "Device token") @RequestParam String token,
            Authentication authentication) {
        
        logger.info("Unregistering device token for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            preferencesService.unregisterDeviceToken(userId, token);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Device token unregistered successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error unregistering device token for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to unregister device token: " + e.getMessage()
            );
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @Operation(summary = "Test notification delivery")
    @PostMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> testNotificationDelivery(
            @Parameter(description = "Channel to test") @RequestParam String channel,
            Authentication authentication) {
        
        logger.info("Testing notification delivery for user: {} on channel: {}", authentication.getName(), channel);

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            boolean success = preferencesService.testNotificationDelivery(userId, channel);
            
            Map<String, Object> response = Map.of(
                "success", success,
                "message", success ? "Test notification sent successfully" : "Test notification failed"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error testing notification delivery for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to test notification delivery: " + e.getMessage()
            );
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @Operation(summary = "Get notification delivery statistics for user")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getUserNotificationStatistics(Authentication authentication) {
        
        logger.debug("Getting notification statistics for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            Map<String, Object> statistics = preferencesService.getUserNotificationStatistics(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "statistics", statistics
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting notification statistics for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get notification statistics: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
