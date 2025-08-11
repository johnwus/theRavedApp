package com.raved.social.service;

import com.raved.social.dto.request.CreateActivityRequest;
import com.raved.social.dto.response.ActivityResponse;
import com.raved.social.model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing user activities
 */
public interface ActivityService {
    
    /**
     * Create a new activity
     */
    ActivityResponse createActivity(CreateActivityRequest request);
    
    /**
     * Record a like activity
     */
    void recordLikeActivity(Long userId, Long postId, Long postAuthorId);
    
    /**
     * Record a comment activity
     */
    void recordCommentActivity(Long userId, Long postId, Long postAuthorId, Long commentId);
    
    /**
     * Record a follow activity
     */
    void recordFollowActivity(Long followerId, Long followingId);
    
    /**
     * Record a share activity
     */
    void recordShareActivity(Long userId, Long postId, Long postAuthorId, String shareType);
    
    /**
     * Record a post activity
     */
    void recordPostActivity(Long userId, Long postId);
    
    /**
     * Get activities for a user
     */
    Page<ActivityResponse> getUserActivities(Long userId, Pageable pageable);
    
    /**
     * Get activities by type for a user
     */
    Page<ActivityResponse> getActivitiesByType(Long userId, ActivityType activityType, Pageable pageable);
    
    /**
     * Get activities for a target
     */
    Page<ActivityResponse> getActivitiesForTarget(Long targetId, String targetType, Pageable pageable);
    
    /**
     * Get recent activities for a user
     */
    List<ActivityResponse> getRecentActivities(Long userId, int limit);
    
    /**
     * Get activity count by type for a user
     */
    long getActivityCount(Long userId, ActivityType activityType);
    
    /**
     * Get total activity count for a user
     */
    long getTotalActivityCount(Long userId);
    
    /**
     * Get activities in a date range for a user
     */
    List<ActivityResponse> getActivitiesInDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Delete an activity
     */
    void deleteActivity(Long activityId);
    
    /**
     * Delete all activities for a user
     */
    void deleteUserActivities(Long userId);
    
    /**
     * Clean up old activities
     */
    void cleanupOldActivities(LocalDateTime cutoffDate);
}