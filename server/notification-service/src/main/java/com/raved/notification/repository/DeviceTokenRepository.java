package com.raved.notification.repository;

import com.raved.notification.model.DeviceToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DeviceTokenRepository for TheRavedApp
 */
@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    /**
     * Find device token by token string
     */
    Optional<DeviceToken> findByToken(String token);

    /**
     * Find active device tokens by user ID
     */
    List<DeviceToken> findByUserIdAndIsActiveTrue(Long userId);

    /**
     * Find all device tokens by user ID
     */
    List<DeviceToken> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find device tokens by platform
     */
    List<DeviceToken> findByPlatformAndIsActiveTrue(DeviceToken.Platform platform);

    /**
     * Find device tokens by user ID and platform
     */
    List<DeviceToken> findByUserIdAndPlatformAndIsActiveTrue(Long userId, DeviceToken.Platform platform);

    /**
     * Check if token exists for user
     */
    boolean existsByUserIdAndToken(Long userId, String token);

    /**
     * Delete device token by token string
     */
    void deleteByToken(String token);

    /**
     * Delete device tokens by user ID
     */
    void deleteByUserId(Long userId);

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
    long countByUserIdAndIsActiveTrue(Long userId);

    /**
     * Count device tokens by platform
     */
    long countByPlatformAndIsActiveTrue(DeviceToken.Platform platform);

    /**
     * Find device tokens by app version
     */
    List<DeviceToken> findByAppVersionAndIsActiveTrue(String appVersion);

    /**
     * Find device tokens for bulk operations
     */
    @Query("SELECT dt FROM DeviceToken dt WHERE dt.userId IN :userIds AND dt.isActive = true")
    List<DeviceToken> findByUserIdsAndIsActiveTrue(@Param("userIds") List<Long> userIds);

    /**
     * Find device tokens by platform and app version
     */
    List<DeviceToken> findByPlatformAndAppVersionAndIsActiveTrue(DeviceToken.Platform platform, String appVersion);

    /**
     * Get device token statistics
     */
    @Query("SELECT dt.platform as platform, COUNT(dt) as count " +
           "FROM DeviceToken dt WHERE dt.isActive = true " +
           "GROUP BY dt.platform")
    List<Object[]> getDeviceTokenStatsByPlatform();

    /**
     * Find device tokens that need cleanup
     */
    @Query("SELECT dt FROM DeviceToken dt WHERE " +
           "(dt.isActive = false AND dt.updatedAt < :inactiveThreshold) OR " +
           "(dt.lastUsedAt IS NOT NULL AND dt.lastUsedAt < :unusedThreshold)")
    List<DeviceToken> findTokensForCleanup(@Param("inactiveThreshold") LocalDateTime inactiveThreshold,
                                          @Param("unusedThreshold") LocalDateTime unusedThreshold);

    /**
     * Update last used timestamp
     */
    @Query("UPDATE DeviceToken dt SET dt.lastUsedAt = :lastUsedAt, dt.updatedAt = :updatedAt " +
           "WHERE dt.token = :token")
    void updateLastUsedAt(@Param("token") String token, 
                         @Param("lastUsedAt") LocalDateTime lastUsedAt,
                         @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * Deactivate device tokens by user ID
     */
    @Query("UPDATE DeviceToken dt SET dt.isActive = false, dt.updatedAt = :updatedAt " +
           "WHERE dt.userId = :userId")
    void deactivateTokensByUserId(@Param("userId") Long userId, @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * Find paginated device tokens by user
     */
    Page<DeviceToken> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find device tokens by creation date range
     */
    List<DeviceToken> findByCreatedAtBetweenAndIsActiveTrue(LocalDateTime startDate, LocalDateTime endDate);
}
