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
 * Repository for Follow entities
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    Page<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId, Pageable pageable);
    
    Page<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId, Pageable pageable);
    
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    long countByFollowerId(Long followerId);
    
    long countByFollowingId(Long followingId);
    
    @Query("SELECT f.followingId FROM Follow f WHERE f.followerId = :userId")
    List<Long> findFollowingIds(@Param("userId") Long userId);
    
    @Query("SELECT f.followerId FROM Follow f WHERE f.followingId = :userId")
    List<Long> findFollowerIds(@Param("userId") Long userId);
    
    @Query("SELECT f.followingId FROM Follow f WHERE f.followerId = :userId1 AND f.followingId IN (SELECT f2.followerId FROM Follow f2 WHERE f2.followingId = :userId2)")
    List<Long> findMutualFollows(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = :userId1 AND f.followingId IN (SELECT f2.followerId FROM Follow f2 WHERE f2.followingId = :userId2)")
    long countMutualFollows(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    @Query(value = "SELECT f.* FROM follows f WHERE f.following_id = :userId ORDER BY f.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Follow> findRecentFollowers(@Param("userId") Long userId, @Param("limit") int limit);
    
    @Query(value = "SELECT f.* FROM follows f WHERE f.follower_id = :userId ORDER BY f.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Follow> findRecentFollowing(@Param("userId") Long userId, @Param("limit") int limit);
    
    @Query(value = "SELECT f.* FROM follows f WHERE f.follower_id != :userId AND f.following_id != :userId AND f.following_id NOT IN (SELECT f2.following_id FROM follows f2 WHERE f2.follower_id = :userId) ORDER BY f.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Follow> findSuggestedFollows(@Param("userId") Long userId, @Param("limit") int limit);
    
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}