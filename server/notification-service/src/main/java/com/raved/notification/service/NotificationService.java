package com.raved.notification.service;

import com.raved.notification.dto.request.CreateNotificationRequest;
import com.raved.notification.dto.request.SendBulkNotificationRequest;
import com.raved.notification.dto.response.NotificationResponse;
import com.raved.notification.model.Notification;
import com.raved.notification.model.NotificationType;
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
    Optional<NotificationResponse> getNotificationById(Long id);

    /**
     * Get notifications for a user
     */
    Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable);

    /**
     * Get unread notifications for a user
     */
    Page<NotificationResponse> getUnreadNotifications(Long userId, Pageable pageable);

    /**
     * Mark notification as read
     */
    NotificationResponse markAsRead(Long notificationId);

    /**
     * Mark all notifications as read for a user
     */
    void markAllAsRead(Long userId);

    /**
     * Delete notification
     */
    void deleteNotification(Long notificationId);

    /**
     * Get notification statistics for a user
     */
    NotificationStats getNotificationStats(Long userId);

    /**
     * Schedule notification for later delivery
     */
    NotificationResponse scheduleNotification(CreateNotificationRequest request, LocalDateTime scheduledAt);

    /**
     * Cancel scheduled notification
     */
    void cancelScheduledNotification(Long notificationId);

    /**
     * Process scheduled notifications
     */
    void processScheduledNotifications();

    /**
     * Send notification by type with template
     */
    NotificationResponse sendNotificationByType(Long userId, NotificationType type,
                                               Map<String, Object> templateData);

    /**
     * Get notifications by type
     */
    Page<NotificationResponse> getNotificationsByType(Long userId, NotificationType type, Pageable pageable);

    /**
     * Resend failed notification
     */
    NotificationResponse resendNotification(Long notificationId);

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
        private long readNotifications;
        private Map<NotificationType, Long> notificationsByType;

        // Constructors, getters, and setters
        public NotificationStats() {}

        public NotificationStats(long totalNotifications, long unreadNotifications,
                               long readNotifications, Map<NotificationType, Long> notificationsByType) {
            this.totalNotifications = totalNotifications;
            this.unreadNotifications = unreadNotifications;
            this.readNotifications = readNotifications;
            this.notificationsByType = notificationsByType;
        }

        // Getters and setters
        public long getTotalNotifications() { return totalNotifications; }
        public void setTotalNotifications(long totalNotifications) { this.totalNotifications = totalNotifications; }

        public long getUnreadNotifications() { return unreadNotifications; }
        public void setUnreadNotifications(long unreadNotifications) { this.unreadNotifications = unreadNotifications; }

        public long getReadNotifications() { return readNotifications; }
        public void setReadNotifications(long readNotifications) { this.readNotifications = readNotifications; }

        public Map<NotificationType, Long> getNotificationsByType() { return notificationsByType; }
        public void setNotificationsByType(Map<NotificationType, Long> notificationsByType) { this.notificationsByType = notificationsByType; }
    }

    class DeliveryStats {
        private long totalSent;
        private long totalDelivered;
        private long totalFailed;
        private Map<String, Long> deliveryByChannel;

        // Constructors, getters, and setters
        public DeliveryStats() {}

        public DeliveryStats(long totalSent, long totalDelivered, long totalFailed, Map<String, Long> deliveryByChannel) {
            this.totalSent = totalSent;
            this.totalDelivered = totalDelivered;
            this.totalFailed = totalFailed;
            this.deliveryByChannel = deliveryByChannel;
        }

        // Getters and setters
        public long getTotalSent() { return totalSent; }
        public void setTotalSent(long totalSent) { this.totalSent = totalSent; }

        public long getTotalDelivered() { return totalDelivered; }
        public void setTotalDelivered(long totalDelivered) { this.totalDelivered = totalDelivered; }

        public long getTotalFailed() { return totalFailed; }
        public void setTotalFailed(long totalFailed) { this.totalFailed = totalFailed; }

        public Map<String, Long> getDeliveryByChannel() { return deliveryByChannel; }
        public void setDeliveryByChannel(Map<String, Long> deliveryByChannel) { this.deliveryByChannel = deliveryByChannel; }
    }
}
