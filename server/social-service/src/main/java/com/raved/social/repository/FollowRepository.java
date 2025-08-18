package com.raved.social.repository;

import com.raved.social.model.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Follow MongoDB documents
 */
@Repository
public interface FollowRepository extends MongoRepository<Follow, String> {
    
    Optional<Follow> findByFollowerIdAndFollowingIdAndStatus(String followerId, String followingId, String status);
    
    Page<Follow> findByFollowerIdAndStatusOrderByCreatedAtDesc(String followerId, String status, Pageable pageable);

    Page<Follow> findByFollowingIdAndStatusOrderByCreatedAtDesc(String followingId, String status, Pageable pageable);

    boolean existsByFollowerIdAndFollowingIdAndStatus(String followerId, String followingId, String status);

    long countByFollowerIdAndStatus(String followerId, String status);

    long countByFollowingIdAndStatus(String followingId, String status);

    @Query("{'followerId': ?0, 'status': 'ACTIVE'}")
    List<Follow> findByFollowerIdAndStatusActive(String followerId);

    @Query("{'followingId': ?0, 'status': 'ACTIVE'}")
    List<Follow> findByFollowingIdAndStatusActive(String followingId);

    @Query("{'followerId': ?0, 'followingId': ?1, 'status': 'ACTIVE'}")
    Optional<Follow> findByFollowerIdAndFollowingIdAndStatusActive(String followerId, String followingId);

    @Query("{'$or': [{'followerId': ?0}, {'followingId': ?0}], 'status': 'ACTIVE'}")
    List<Follow> findByUserIdInvolved(String userId);

    @Query("{'followerId': ?0, 'status': 'BLOCKED'}")
    List<Follow> findBlockedByUser(String userId);

    @Query("{'followingId': ?0, 'status': 'BLOCKED'}")
    List<Follow> findBlockedUsers(String userId);

    @Query("{'followerId': ?0, 'isMutual': true, 'status': 'ACTIVE'}")
    List<Follow> findMutualFollows(String userId);

    @Query("{'followerId': ?0, 'status': 'ACTIVE'}")
    List<Follow> findByFollowerIdOrderByCreatedAtDesc(String followerId);
    
    @Query("{'followingId': ?0, 'status': 'ACTIVE'}")
    List<Follow> findByFollowingIdOrderByCreatedAtDesc(String followingId);
    
    @Query("{'followerId': ?0, 'status': 'ACTIVE'}")
    long countByFollowerId(String followerId);
    
    @Query("{'followingId': ?0, 'status': 'ACTIVE'}")
    long countByFollowingId(String followingId);
    
    @Query("{'followerId': ?0, 'status': 'ACTIVE'}")
    List<Follow> findRecentFollowers(String userId, int limit);
    
    @Query("{'followingId': ?0, 'status': 'ACTIVE'}")
    List<Follow> findRecentFollowing(String userId, int limit);
    
    @Query("{'followerId': ?0, 'status': 'ACTIVE'}")
    List<Follow> findSuggestedFollows(String userId, int limit);
    
    void deleteByFollowerIdAndFollowingId(String followerId, String followingId);

    // Methods for backward compatibility with service layer
    boolean existsByFollowerIdAndFollowingId(String followerId, String followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(String followerId, String followingId);

    Page<Follow> findByFollowingIdOrderByCreatedAtDesc(String followingId, Pageable pageable);

    Page<Follow> findByFollowerIdOrderByCreatedAtDesc(String followerId, Pageable pageable);

    @Query("{'followerId': ?0, 'followingId': ?1, 'status': 'ACTIVE'}")
    List<Follow> findMutualFollows(String userId1, String userId2);
}