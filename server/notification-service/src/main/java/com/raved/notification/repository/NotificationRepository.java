package com.raved.notification.repository;

import com.raved.notification.model.Notification;
import com.raved.notification.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NotificationRepository for TheRavedApp
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find notifications by recipient user ID
     */
    Page<Notification> findByRecipientUserIdOrderByCreatedAtDesc(Long recipientUserId, Pageable pageable);

    /**
     * Find unread notifications by recipient user ID
     */
    Page<Notification> findByRecipientUserIdAndReadAtIsNullOrderByCreatedAtDesc(Long recipientUserId, Pageable pageable);

    /**
     * Find unread notifications (using boolean field if exists)
     */
    List<Notification> findByRecipientUserIdAndReadAtIsNull(Long recipientUserId);

    /**
     * Find notifications by type
     */
    Page<Notification> findByRecipientUserIdAndNotificationTypeOrderByCreatedAtDesc(
            Long recipientUserId, NotificationType notificationType, Pageable pageable);

    /**
     * Find notifications by delivery status
     */
    Page<Notification> findByDeliveryStatusOrderByCreatedAtDesc(Notification.DeliveryStatus deliveryStatus, Pageable pageable);

    /**
     * Find scheduled notifications that are due
     */
    @Query("SELECT n FROM Notification n WHERE n.deliveryStatus = 'PENDING' " +
           "AND n.scheduledAt IS NOT NULL AND n.scheduledAt <= :now")
    List<Notification> findScheduledNotificationsDue(@Param("now") LocalDateTime now);

    /**
     * Count notifications by recipient user ID
     */
    long countByRecipientUserId(Long recipientUserId);

    /**
     * Count unread notifications by recipient user ID
     */
    long countByRecipientUserIdAndReadAtIsNull(Long recipientUserId);

    /**
     * Count notifications by delivery status and date range
     */
    long countByDeliveryStatusAndCreatedAtBetween(Notification.DeliveryStatus deliveryStatus,
                                                 LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count notifications by date range
     */
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get notification statistics by type for a user
     */
    @Query("SELECT n.notificationType as type, COUNT(n) as count " +
           "FROM Notification n WHERE n.recipientUserId = :userId " +
           "GROUP BY n.notificationType")
    List<Object[]> countNotificationsByTypeForUser(@Param("userId") Long userId);

    /**
     * Get delivery statistics by channel
     */
    @Query("SELECT n.notificationType as channel, COUNT(n) as count " +
           "FROM Notification n WHERE n.createdAt BETWEEN :startDate AND :endDate " +
           "AND n.deliveryStatus = 'DELIVERED' " +
           "GROUP BY n.notificationType")
    List<Object[]> getDeliveryStatsByChannelAndDateRange(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Find failed notifications for retry
     */
    @Query("SELECT n FROM Notification n WHERE n.deliveryStatus = 'FAILED' " +
           "AND n.retryCount < n.maxRetries " +
           "AND n.createdAt >= :cutoffTime")
    List<Notification> findFailedNotificationsForRetry(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Find notifications by priority
     */
    List<Notification> findByPriorityAndDeliveryStatusOrderByCreatedAtAsc(
            Notification.Priority priority, Notification.DeliveryStatus deliveryStatus);

    /**
     * Delete old notifications
     */
    void deleteByCreatedAtBefore(LocalDateTime cutoffDate);

    /**
     * Find notifications by metadata (JSON search would be database-specific)
     */
    @Query("SELECT n FROM Notification n WHERE n.metadata LIKE %:searchTerm%")
    List<Notification> findByMetadataContaining(@Param("searchTerm") String searchTerm);
}
