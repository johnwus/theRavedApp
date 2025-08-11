package com.raved.notification.repository;

import com.raved.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * NotificationRepository for TheRavedApp
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find notifications by user ID
     */
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find unread notifications by user ID
     */
    Page<Notification> findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find unread notifications (using boolean field if exists)
     */
    List<Notification> findByUserIdAndReadAtIsNull(Long userId);

    /**
     * Find notifications by type
     */
    Page<Notification> findByUserIdAndNotificationTypeOrderByCreatedAtDesc(
            Long userId, String notificationType, Pageable pageable);

    /**
     * Find notifications by delivery status
     */
    Page<Notification> findByDeliveryStatusOrderByCreatedAtDesc(String deliveryStatus, Pageable pageable);

    /**
     * Find scheduled notifications that are due
     */
    @Query("SELECT n FROM Notification n WHERE n.deliveryStatus = 'SCHEDULED' " +
           "AND n.scheduledAt IS NOT NULL AND n.scheduledAt <= :now")
    List<Notification> findScheduledNotificationsDue(@Param("now") LocalDateTime now);

    /**
     * Find notifications by delivery status and scheduled time
     */
    List<Notification> findByDeliveryStatusAndScheduledAtBefore(String deliveryStatus, LocalDateTime scheduledAt);

    /**
     * Count notifications by user ID
     */
    long countByUserId(Long userId);

    /**
     * Count unread notifications by user ID
     */
    long countByUserIdAndReadAtIsNull(Long userId);

    /**
     * Count sent notifications by user ID
     */
    long countByUserIdAndIsSentTrue(Long userId);

    /**
     * Count notifications by delivery status and date range
     */
    long countByDeliveryStatusAndCreatedAtBetween(String deliveryStatus,
                                                 LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count notifications by date range
     */
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count sent notifications by date range
     */
    long countByCreatedAtBetweenAndIsSentTrue(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count read notifications by date range
     */
    long countByCreatedAtBetweenAndReadAtIsNotNull(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get notification statistics by type for a user
     */
    @Query("SELECT n.notificationType as type, COUNT(n) as count " +
           "FROM Notification n WHERE n.userId = :userId " +
           "GROUP BY n.notificationType")
    List<Object[]> countNotificationsByTypeForUser(@Param("userId") Long userId);

    /**
     * Find failed notifications for retry
     */
    @Query("SELECT n FROM Notification n WHERE n.deliveryStatus = 'FAILED' " +
           "AND n.createdAt >= :cutoffTime")
    List<Notification> findFailedNotificationsForRetry(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Delete old notifications
     */
    void deleteByCreatedAtBefore(LocalDateTime cutoffDate);

    /**
     * Find notifications by metadata (JSON search would be database-specific)
     */
    @Query("SELECT n FROM Notification n WHERE n.data LIKE %:searchTerm%")
    List<Notification> findByDataContaining(@Param("searchTerm") String searchTerm);
}
