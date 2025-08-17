package com.raved.social.repository;

import com.raved.social.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Activity MongoDB documents
 */
@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
    
    Page<Activity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    Page<Activity> findByUserIdAndActivityTypeOrderByCreatedAtDesc(String userId, String activityType, Pageable pageable);
    
    Page<Activity> findByTargetIdAndTargetTypeOrderByCreatedAtDesc(String targetId, String targetType, Pageable pageable);
    
    @Query("{'userId': ?0}")
    List<Activity> findTopByUserIdOrderByCreatedAtDesc(String userId, int limit);
    
    long countByUserIdAndActivityType(String userId, String activityType);
    
    long countByUserId(String userId);
    
    List<Activity> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    long deleteByUserId(String userId);
    
    long deleteByCreatedAtBefore(LocalDateTime cutoffDate);

    // Enhanced MongoDB-specific methods
    @Query("{'userId': ?0, 'isRead': false}")
    List<Activity> findUnreadByUserId(String userId);

    @Query("{'userId': ?0, 'isRead': false}")
    long countUnreadByUserId(String userId);

    @Query("{'userId': ?0, 'visibility': ?1}")
    List<Activity> findByUserIdAndVisibility(String userId, String visibility);

    @Query("{'userId': ?0, 'activityCategory': ?1}")
    List<Activity> findByUserIdAndActivityCategory(String userId, String activityCategory);

    @Query("{'userId': ?0, 'priority': {$gte: ?1}}")
    List<Activity> findByUserIdAndPriorityGreaterThanEqual(String userId, Integer priority);

    @Query("{'userId': ?0, 'isAnonymous': ?1}")
    List<Activity> findByUserIdAndIsAnonymous(String userId, Boolean isAnonymous);

    @Query("{'targetId': ?0, 'targetType': ?1, 'visibility': 'PUBLIC'}")
    List<Activity> findPublicByTarget(String targetId, String targetType);

    @Query("{'activityType': ?0, 'visibility': 'PUBLIC'}")
    List<Activity> findPublicByActivityType(String activityType);

    @Query("{'activityCategory': ?0, 'visibility': 'PUBLIC'}")
    List<Activity> findPublicByActivityCategory(String activityCategory);

    @Query("{'createdAt': {$gte: ?0}, 'visibility': 'PUBLIC'}")
    List<Activity> findRecentPublicActivities(LocalDateTime since);

    @Query("{'userId': ?0, 'source': ?1}")
    List<Activity> findByUserIdAndSource(String userId, String source);

    @Query("{'location': {$regex: ?0, $options: 'i'}}")
    List<Activity> findByLocationContaining(String location);

    @Query("{'deviceInfo': {$regex: ?0, $options: 'i'}}")
    List<Activity> findByDeviceInfoContaining(String deviceInfo);

    @Query("{'userAgent': {$regex: ?0, $options: 'i'}}")
    List<Activity> findByUserAgentContaining(String userAgent);
}