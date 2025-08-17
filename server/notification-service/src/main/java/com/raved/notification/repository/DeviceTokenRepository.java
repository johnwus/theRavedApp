package com.raved.notification.repository;

import com.raved.notification.model.DeviceToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DeviceTokenRepository for TheRavedApp MongoDB Converted from JPA to MongoDB
 * repository
 */
@Repository
public interface DeviceTokenRepository extends MongoRepository<DeviceToken, String> {

    /**
     * Find device token by token string
     */
    Optional<DeviceToken> findByToken(String token);

    /**
     * Find active device tokens by user ID
     */
    List<DeviceToken> findByUserIdAndIsActiveTrue(String userId);

    /**
     * Find all device tokens by user ID
     */
    List<DeviceToken> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Find device tokens by platform
     */
    List<DeviceToken> findByPlatformAndIsActiveTrue(DeviceToken.Platform platform);

    /**
     * Find device tokens by user ID and platform
     */
    List<DeviceToken> findByUserIdAndPlatformAndIsActiveTrue(String userId, DeviceToken.Platform platform);

    /**
     * Check if token exists for user
     */
    boolean existsByUserIdAndToken(String userId, String token);

    /**
     * Delete device token by token string
     */
    void deleteByToken(String token);

    /**
     * Delete device tokens by user ID
     */
    void deleteByUserId(String userId);

    /**
     * Find inactive device tokens
     */
    List<DeviceToken> findByIsActiveFalse();

    /**
     * Find device tokens that haven't been used recently
     */
    List<DeviceToken> findByLastUsedAtBeforeAndIsActiveTrue(LocalDateTime cutoffDate);

    /**
     * Find device tokens updated before a certain date
     */
    List<DeviceToken> findByUpdatedAtBeforeAndIsActiveTrue(LocalDateTime cutoffDate);

    /**
     * Count active device tokens by user
     */
    long countByUserIdAndIsActiveTrue(String userId);

    /**
     * Count device tokens by platform
     */
    long countByPlatformAndIsActiveTrue(DeviceToken.Platform platform);

    /**
     * Find device tokens for bulk operations (MongoDB query)
     */
    @Query("{'userId': {'$in': ?0}, 'isActive': true}")
    List<DeviceToken> findByUserIdsAndIsActiveTrue(List<String> userIds);

    /**
     * Get device token statistics (MongoDB aggregation - simplified)
     */
    @Query(value = "{'isActive': true}", fields = "{'platform': 1}")
    List<DeviceToken> getActiveDeviceTokensForStats();

    /**
     * Find device tokens that need cleanup (MongoDB query)
     */
    @Query("{'$or': [{'isActive': false, 'updatedAt': {'$lt': ?0}}, {'lastUsedAt': {'$lt': ?1, '$ne': null}}]}")
    List<DeviceToken> findTokensForCleanup(LocalDateTime inactiveThreshold, LocalDateTime unusedThreshold);

    /**
     * Find paginated device tokens by user
     */
    Page<DeviceToken> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * Find device tokens by creation date range
     */
    List<DeviceToken> findByCreatedAtBetweenAndIsActiveTrue(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Additional MongoDB-specific methods for better performance
     */

    /**
     * Find device tokens by multiple platforms
     */
    @Query("{'platform': {'$in': ?0}, 'isActive': true}")
    List<DeviceToken> findByPlatformsAndIsActiveTrue(List<DeviceToken.Platform> platforms);

    /**
     * Count device tokens by user and platform
     */
    long countByUserIdAndPlatformAndIsActiveTrue(String userId, DeviceToken.Platform platform);
}
