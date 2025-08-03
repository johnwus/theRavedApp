package com.raved.notification.dto.response;

import com.raved.notification.model.NotificationType;

import java.util.Map;

/**
 * Statistics for notifications
 */
public class NotificationStats {

    private long totalNotifications;
    private long unreadNotifications;
    private long readNotifications;
    private Map<NotificationType, Long> notificationsByType;
    private double readRate;

    // Constructors
    public NotificationStats() {}

    public NotificationStats(long totalNotifications, long unreadNotifications, 
                           long readNotifications, Map<NotificationType, Long> notificationsByType) {
        this.totalNotifications = totalNotifications;
        this.unreadNotifications = unreadNotifications;
        this.readNotifications = readNotifications;
        this.notificationsByType = notificationsByType;
        this.readRate = totalNotifications > 0 ? (double) readNotifications / totalNotifications * 100 : 0;
    }

    // Getters and Setters
    public long getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(long totalNotifications) {
        this.totalNotifications = totalNotifications;
    }

    public long getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(long unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }

    public long getReadNotifications() {
        return readNotifications;
    }

    public void setReadNotifications(long readNotifications) {
        this.readNotifications = readNotifications;
    }

    public Map<NotificationType, Long> getNotificationsByType() {
        return notificationsByType;
    }

    public void setNotificationsByType(Map<NotificationType, Long> notificationsByType) {
        this.notificationsByType = notificationsByType;
    }

    public double getReadRate() {
        return readRate;
    }

    public void setReadRate(double readRate) {
        this.readRate = readRate;
    }
}
