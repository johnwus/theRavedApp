package com.raved.social.repository;

import com.raved.social.model.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * FollowRepository for TheRavedApp
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * Check if follow relationship exists
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * Find follow relationship
     */
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * Find followers of a user
     */
    Page<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId, Pageable pageable);

    /**
     * Find users that a user is following
     */
    Page<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId, Pageable pageable);

    /**
     * Count followers
     */
    long countByFollowingId(Long followingId);

    /**
     * Count following
     */
    long countByFollowerId(Long followerId);

    /**
     * Delete follow relationship
     */
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * Find mutual followers
     */
    @Query("SELECT f1 FROM Follow f1 WHERE f1.followingId IN " +
           "(SELECT f2.followingId FROM Follow f2 WHERE f2.followerId = :userId1) " +
           "AND f1.followerId IN " +
           "(SELECT f3.followingId FROM Follow f3 WHERE f3.followerId = :userId2)")
    List<Follow> findMutualFollowers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * Find suggested follows (users followed by people you follow)
     */
    @Query("SELECT f FROM Follow f WHERE f.followingId IN " +
           "(SELECT f2.followingId FROM Follow f2 WHERE f2.followerId IN " +
           "(SELECT f3.followingId FROM Follow f3 WHERE f3.followerId = :userId)) " +
           "AND f.followerId != :userId " +
           "AND NOT EXISTS (SELECT 1 FROM Follow f4 WHERE f4.followerId = :userId AND f4.followingId = f.followingId) " +
           "GROUP BY f.followingId ORDER BY COUNT(f.followingId) DESC")
    List<Follow> findSuggestedFollows(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Find recent followers
     */
    @Query("SELECT f FROM Follow f WHERE f.followingId = :userId " +
           "ORDER BY f.createdAt DESC")
    List<Follow> findRecentFollowers(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Block-related methods
     */
    Optional<Follow> findByFollowerIdAndFollowingIdAndIsBlockedTrue(Long followerId, Long followingId);

    boolean existsByFollowerIdAndFollowingIdAndIsBlockedTrue(Long followerId, Long followingId);

    Page<Follow> findByFollowerIdAndIsBlockedTrueOrderByCreatedAtDesc(Long followerId, Pageable pageable);
}
