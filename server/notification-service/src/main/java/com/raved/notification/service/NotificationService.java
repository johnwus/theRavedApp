package com.raved.notification.service;

import com.raved.notification.dto.request.CreateNotificationRequest;
import com.raved.notification.dto.request.SendBulkNotificationRequest;
import com.raved.notification.dto.response.NotificationResponse;
import com.raved.notification.dto.response.DeliveryStats;
import com.raved.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NotificationService for TheRavedApp
 */
public interface NotificationService {

    /**
     * Create and send a notification
     */
    NotificationResponse createAndSendNotification(CreateNotificationRequest request);

    /**
     * Send bulk notifications
     */
    List<NotificationResponse> sendBulkNotifications(SendBulkNotificationRequest request);

    /**
     * Get notification by ID
     */
    Optional<NotificationResponse> getNotificationById(String id);

    /**
     * Get notifications for a user
     */
    Page<NotificationResponse> getUserNotifications(String userId, Pageable pageable);

    /**
     * Get unread notifications for a user
     */
    Page<NotificationResponse> getUnreadNotifications(String userId, Pageable pageable);

    /**
     * Mark notification as read
     */
    NotificationResponse markAsRead(String notificationId);

    /**
     * Mark all notifications as read for a user
     */
    void markAllAsRead(String userId);

    /**
     * Delete notification
     */
    void deleteNotification(String notificationId);

    /**
     * Get notification statistics for a user
     */
    NotificationStats getNotificationStats(String userId);

    /**
     * Schedule notification for later delivery
     */
    NotificationResponse scheduleNotification(CreateNotificationRequest request, LocalDateTime scheduledAt);

    /**
     * Cancel scheduled notification
     */
    void cancelScheduledNotification(String notificationId);

    /**
     * Process scheduled notifications
     */
    void processScheduledNotifications();

    /**
     * Send notification by type with template
     */
    NotificationResponse sendNotificationByType(String userId, String notificationType,
                                               Map<String, Object> templateData);

    /**
     * Get notifications by type
     */
    Page<NotificationResponse> getNotificationsByType(String userId, String notificationType, Pageable pageable);

    /**
     * Resend failed notification
     */
    NotificationResponse resendNotification(String notificationId);

    /**
     * Get delivery statistics
     */
    DeliveryStats getDeliveryStats(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Inner classes for statistics
     */
    class NotificationStats {
        private long totalNotifications;
        private long unreadNotifications;
        private long sentNotifications;

        // Constructors, getters, and setters
        public NotificationStats() {}

        public NotificationStats(long totalNotifications, long unreadNotifications, long sentNotifications) {
            this.totalNotifications = totalNotifications;
            this.unreadNotifications = unreadNotifications;
            this.sentNotifications = sentNotifications;
        }

        // Getters and setters
        public long getTotalNotifications() { return totalNotifications; }
        public void setTotalNotifications(long totalNotifications) { this.totalNotifications = totalNotifications; }

        public long getUnreadNotifications() { return unreadNotifications; }
        public void setUnreadNotifications(long unreadNotifications) { this.unreadNotifications = unreadNotifications; }

        public long getSentNotifications() { return sentNotifications; }
        public void setSentNotifications(long sentNotifications) { this.sentNotifications = sentNotifications; }
    }
}
