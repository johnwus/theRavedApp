package com.raved.notification.repository;

import com.raved.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * NotificationRepository for TheRavedApp MongoDB Converted from JPA to MongoDB
 * repository
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Find notifications by user ID
     */
    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * Find unread notifications by user ID
     */
    Page<Notification> findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * Find unread notifications (using boolean field if exists)
     */
    List<Notification> findByUserIdAndReadAtIsNull(String userId);

    /**
     * Find notifications by type
     */
    Page<Notification> findByUserIdAndNotificationTypeOrderByCreatedAtDesc(
            String userId, String notificationType, Pageable pageable);

    /**
     * Find notifications by delivery status (MongoDB enum support)
     */
    Page<Notification> findByDeliveryStatusOrderByCreatedAtDesc(Notification.DeliveryStatus deliveryStatus, Pageable pageable);

    /**
     * Find scheduled notifications that are due (MongoDB query)
     */
    @Query("{'deliveryStatus': ?#{T(com.raved.notification.model.Notification.DeliveryStatus).PENDING}, 'scheduledAt': {'$lte': ?0, '$ne': null}}")
    List<Notification> findScheduledNotificationsDue(LocalDateTime now);

    /**
     * Find notifications by delivery status and scheduled time
     */
    List<Notification> findByDeliveryStatusAndScheduledAtBefore(Notification.DeliveryStatus deliveryStatus, LocalDateTime scheduledAt);

    /**
     * Count notifications by user ID
     */
    long countByUserId(String userId);

    /**
     * Count unread notifications by user ID
     */
    long countByUserIdAndReadAtIsNull(String userId);

    /**
     * Count sent notifications by user ID
     */
    long countByUserIdAndIsSentTrue(String userId);

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
     * Count sent notifications by date range
     */
    long countByCreatedAtBetweenAndIsSentTrue(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count read notifications by date range
     */
    long countByCreatedAtBetweenAndReadAtIsNotNull(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get notification statistics by type for a user (MongoDB aggregation)
     */
    @Query(value = "{'userId': ?0}", fields = "{'notificationType': 1}")
    List<Notification> findNotificationTypesForUser(String userId);

    /**
     * Find failed notifications for retry (MongoDB query)
     */
    @Query("{'deliveryStatus': ?#{T(com.raved.notification.model.Notification.DeliveryStatus).FAILED}, 'createdAt': {'$gte': ?0}}")
    List<Notification> findFailedNotificationsForRetry(LocalDateTime cutoffTime);

    /**
     * Delete old notifications
     */
    void deleteByCreatedAtBefore(LocalDateTime cutoffDate);

    /**
     * Find notifications by metadata (MongoDB text search)
     */
    @Query("{'data': {'$regex': ?0, '$options': 'i'}}")
    List<Notification> findByDataContaining(String searchTerm);

    /**
     * Additional MongoDB-specific methods for better performance
     */
    /**
     * Find notifications by user ID and read status with limit
     */
    @Query("{'userId': ?0, 'isRead': ?1}")
    List<Notification> findByUserIdAndIsRead(String userId, Boolean isRead, Pageable pageable);

    /**
     * Find notifications by multiple delivery statuses
     */
    @Query("{'deliveryStatus': {'$in': ?0}}")
    List<Notification> findByDeliveryStatusIn(List<Notification.DeliveryStatus> statuses);
}
