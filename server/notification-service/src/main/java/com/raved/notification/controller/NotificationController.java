package com.raved.notification.controller;

import com.raved.notification.dto.request.CreateNotificationRequest;
import com.raved.notification.dto.request.SendBulkNotificationRequest;
import com.raved.notification.dto.request.UpdateNotificationPreferencesRequest;
import com.raved.notification.dto.response.NotificationResponse;
import com.raved.notification.dto.response.NotificationPreferencesResponse;
import com.raved.notification.service.NotificationService;
import com.raved.notification.service.EmailService;
import com.raved.notification.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Notification Management
 * Handles notification delivery, preferences, and user notification operations
 */
@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "APIs for managing notifications and delivery")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Operation(summary = "Send notification to user")
    @PostMapping("/send")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendNotification(
            @Valid @RequestBody CreateNotificationRequest request,
            Authentication authentication) {

        logger.info("Sending notification to user: {} by: {}", request.getUserId(), authentication.getName());

        try {
            NotificationResponse notification = notificationService.createAndSendNotification(request);

            Map<String, Object> response = Map.of(
                "success", true,
                "notification", notification,
                "message", "Notification sent successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error sending notification: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to send notification: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get user's notifications")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getUserNotifications(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int pageSize,
            @Parameter(description = "Only unread") @RequestParam(defaultValue = "false") boolean unreadOnly,
            Authentication authentication) {

        logger.debug("Getting notifications for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

            Page<NotificationResponse> notifications;
            if (unreadOnly) {
                notifications = notificationService.getUnreadNotifications(userId, pageable);
            } else {
                notifications = notificationService.getUserNotifications(userId, pageable);
            }

            // Get notification statistics
            NotificationService.NotificationStats stats = notificationService.getNotificationStats(userId);

            Map<String, Object> response = Map.of(
                "success", true,
                "notifications", notifications.getContent(),
                "unreadCount", stats.getUnreadNotifications(),
                "pagination", Map.of(
                    "page", notifications.getNumber(),
                    "size", notifications.getSize(),
                    "totalElements", notifications.getTotalElements(),
                    "totalPages", notifications.getTotalPages(),
                    "hasNext", notifications.hasNext()
                )
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting notifications for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get notifications: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get specific notification")
    @GetMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getNotification(
            @PathVariable String notificationId,
            Authentication authentication) {

        logger.debug("Getting notification by ID: {} for user: {}", notificationId, authentication.getName());

        Optional<NotificationResponse> notificationOpt = notificationService.getNotificationById(notificationId);

        if (notificationOpt.isPresent()) {
            // TODO: Add authorization check - user should only see their own notifications

            Map<String, Object> response = Map.of(
                "success", true,
                "notification", notificationOpt.get()
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Notification not found"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Mark notification as read")
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> markNotificationAsRead(
            @PathVariable String notificationId,
            Authentication authentication) {

        logger.info("Marking notification {} as read by user: {}", notificationId, authentication.getName());

        try {
            // TODO: Add authorization check - user should only mark their own notifications as read

            NotificationResponse notification = notificationService.markAsRead(notificationId);

            Map<String, Object> response = Map.of(
                "success", true,
                "notification", notification,
                "message", "Notification marked as read"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error marking notification {} as read: {}", notificationId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to mark notification as read: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Mark all notifications as read")
    @PutMapping("/read-all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> markAllNotificationsAsRead(Authentication authentication) {

        logger.info("Marking all notifications as read for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            String userId = "user_123"; // Placeholder - should extract from JWT token

            notificationService.markAllAsRead(userId);

            Map<String, Object> response = Map.of(
                "success", true,
                "message", "All notifications marked as read"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error marking all notifications as read for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to mark all notifications as read: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Send bulk notifications (admin only)")
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendBulkNotifications(
            @Valid @RequestBody SendBulkNotificationRequest request,
            Authentication authentication) {

        logger.info("Sending bulk notifications by admin: {}", authentication.getName());

        try {
            List<NotificationResponse> notifications = notificationService.sendBulkNotifications(request);

            Map<String, Object> response = Map.of(
                "success", true,
                "notifications", notifications,
                "count", notifications.size(),
                "message", "Bulk notifications sent successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error sending bulk notifications: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to send bulk notifications: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Send email verification")
    @PostMapping("/email/verification")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendEmailVerification(
            @Parameter(description = "Email address") @RequestParam String email,
            @Parameter(description = "Verification code") @RequestParam String code,
            @Parameter(description = "User name") @RequestParam String userName) {

        logger.info("Sending email verification to: {}", email);

        try {
            boolean sent = emailService.sendVerificationEmail(email, code, userName);

            if (sent) {
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Verification email sent successfully"
                );
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Failed to send verification email"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Error sending verification email to {}: {}", email, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to send verification email: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Send welcome email")
    @PostMapping("/email/welcome")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendWelcomeEmail(
            @Parameter(description = "Email address") @RequestParam String email,
            @Parameter(description = "User name") @RequestParam String userName) {

        logger.info("Sending welcome email to: {}", email);

        try {
            boolean sent = emailService.sendWelcomeEmail(email, userName);

            if (sent) {
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Welcome email sent successfully"
                );
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Failed to send welcome email"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Error sending welcome email to {}: {}", email, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to send welcome email: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Send SMS verification")
    @PostMapping("/sms/verification")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendSmsVerification(
            @Parameter(description = "Phone number") @RequestParam String phone,
            @Parameter(description = "Verification code") @RequestParam String code,
            @Parameter(description = "User name") @RequestParam String userName) {

        logger.info("Sending SMS verification to: {}", phone);

        try {
            boolean sent = smsService.sendVerificationSms(phone, code, userName);

            if (sent) {
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Verification SMS sent successfully"
                );
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Failed to send verification SMS"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Error sending verification SMS to {}: {}", phone, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to send verification SMS: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get notification statistics (admin only)")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getNotificationStatistics() {

        logger.debug("Getting notification statistics");

        try {
            // TODO: Implement comprehensive notification statistics
            Map<String, Object> statistics = Map.of(
                "totalNotifications", "Statistics would be implemented here",
                "deliveryRates", "Delivery rate statistics would be here",
                "channelBreakdown", "Channel breakdown would be here"
            );

            Map<String, Object> response = Map.of(
                "success", true,
                "statistics", statistics
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting notification statistics: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get notification statistics: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
