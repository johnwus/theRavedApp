package com.raved.social.service.impl;

import com.raved.social.dto.request.CreateActivityRequest;
import com.raved.social.dto.response.ActivityResponse;
import com.raved.social.event.SocialEventPublisher;
import com.raved.social.mapper.ActivityMapper;
import com.raved.social.model.Activity;
import com.raved.social.model.ActivityType;
import com.raved.social.repository.ActivityRepository;
import com.raved.social.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ActivityService
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private SocialEventPublisher eventPublisher;

    @Override
    public ActivityResponse createActivity(CreateActivityRequest request) {
        logger.info("Creating activity for user: {} of type: {}", request.getUserId(), request.getActivityType());
        
        Activity activity = activityMapper.toActivity(request);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish activity event
        publishActivityEvent(savedActivity);
        
        logger.info("Activity created: {}", savedActivity.getId());
        return activityMapper.toActivityResponse(savedActivity);
    }

    @Override
    public void recordLikeActivity(Long userId, Long postId, Long postAuthorId) {
        logger.info("Recording like activity: user {} liked post {} by user {}", userId, postId, postAuthorId);
        
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setActivityType(ActivityType.LIKE);
        activity.setTargetId(postId);
        activity.setTargetType("POST");
        activity.setTargetUserId(postAuthorId);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish like event
        eventPublisher.publishLikeEvent(userId, postId, postAuthorId);
        
        logger.info("Like activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordCommentActivity(Long userId, Long postId, Long postAuthorId, Long commentId) {
        logger.info("Recording comment activity: user {} commented on post {} by user {}", userId, postId, postAuthorId);
        
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setActivityType(ActivityType.COMMENT);
        activity.setTargetId(postId);
        activity.setTargetType("POST");
        activity.setTargetUserId(postAuthorId);
        activity.setReferenceId(commentId);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish comment event
        eventPublisher.publishCommentEvent(userId, postId, postAuthorId, commentId, "");
        
        logger.info("Comment activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordFollowActivity(Long followerId, Long followingId) {
        logger.info("Recording follow activity: user {} followed user {}", followerId, followingId);
        
        Activity activity = new Activity();
        activity.setUserId(followerId);
        activity.setActivityType(ActivityType.FOLLOW);
        activity.setTargetId(followingId);
        activity.setTargetType("USER");
        activity.setTargetUserId(followingId);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish follow event
        eventPublisher.publishFollowEvent(followerId, followingId);
        
        logger.info("Follow activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordShareActivity(Long userId, Long postId, Long postAuthorId, String shareType) {
        logger.info("Recording share activity: user {} shared post {} by user {}", userId, postId, postAuthorId);
        
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setActivityType(ActivityType.SHARE);
        activity.setTargetId(postId);
        activity.setTargetType("POST");
        activity.setTargetUserId(postAuthorId);
        activity.setMetadata("{\"shareType\":\"" + shareType + "\"}");
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish share event
        eventPublisher.publishShareEvent(userId, postId, postAuthorId, shareType);
        
        logger.info("Share activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordPostActivity(Long userId, Long postId) {
        logger.info("Recording post activity: user {} created post {}", userId, postId);
        
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setActivityType(ActivityType.POST);
        activity.setTargetId(postId);
        activity.setTargetType("POST");
        activity.setTargetUserId(userId);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        
        Activity savedActivity = activityRepository.save(activity);
        
        logger.info("Post activity recorded: {}", savedActivity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getUserActivities(Long userId, Pageable pageable) {
        logger.debug("Getting activities for user: {}", userId);
        
        Page<Activity> activities = activityRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return activities.map(activityMapper::toActivityResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivitiesByType(Long userId, ActivityType activityType, Pageable pageable) {
        logger.debug("Getting activities for user: {} of type: {}", userId, activityType);
        
        Page<Activity> activities = activityRepository.findByUserIdAndActivityTypeOrderByCreatedAtDesc(
                userId, activityType, pageable);
        return activities.map(activityMapper::toActivityResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivitiesForTarget(Long targetId, String targetType, Pageable pageable) {
        logger.debug("Getting activities for target: {} of type: {}", targetId, targetType);
        
        Page<Activity> activities = activityRepository.findByTargetIdAndTargetTypeOrderByCreatedAtDesc(
                targetId, targetType, pageable);
        return activities.map(activityMapper::toActivityResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getRecentActivities(Long userId, int limit) {
        logger.debug("Getting recent activities for user: {} with limit: {}", userId, limit);
        
        List<Activity> activities = activityRepository.findTopByUserIdOrderByCreatedAtDesc(userId, limit);
        return activities.stream()
                .map(activityMapper::toActivityResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getActivityCount(Long userId, ActivityType activityType) {
        logger.debug("Getting activity count for user: {} of type: {}", userId, activityType);
        
        return activityRepository.countByUserIdAndActivityType(userId, activityType);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalActivityCount(Long userId) {
        logger.debug("Getting total activity count for user: {}", userId);
        
        return activityRepository.countByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getActivitiesInDateRange(Long userId, LocalDateTime startDate, 
                                                          LocalDateTime endDate) {
        logger.debug("Getting activities for user: {} between {} and {}", userId, startDate, endDate);
        
        List<Activity> activities = activityRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                userId, startDate, endDate);
        
        return activities.stream()
                .map(activityMapper::toActivityResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteActivity(Long activityId) {
        logger.info("Deleting activity: {}", activityId);
        
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            activityRepository.delete(activityOpt.get());
            logger.info("Activity deleted: {}", activityId);
        } else {
            logger.warn("Activity not found for deletion: {}", activityId);
        }
    }

    @Override
    public void deleteUserActivities(Long userId) {
        logger.info("Deleting all activities for user: {}", userId);
        
        long deletedCount = activityRepository.deleteByUserId(userId);
        logger.info("Deleted {} activities for user: {}", deletedCount, userId);
    }

    @Override
    public void cleanupOldActivities(LocalDateTime cutoffDate) {
        logger.info("Cleaning up activities older than: {}", cutoffDate);
        
        long deletedCount = activityRepository.deleteByCreatedAtBefore(cutoffDate);
        logger.info("Cleaned up {} old activities", deletedCount);
    }

    private void publishActivityEvent(Activity activity) {
        try {
            switch (activity.getActivityType()) {
                case LIKE:
                    eventPublisher.publishLikeEvent(activity.getUserId(), activity.getTargetId(), 
                            activity.getTargetUserId());
                    break;
                case COMMENT:
                    eventPublisher.publishCommentEvent(activity.getUserId(), activity.getTargetId(), 
                            activity.getTargetUserId(), activity.getReferenceId(), "");
                    break;
                case FOLLOW:
                    eventPublisher.publishFollowEvent(activity.getUserId(), activity.getTargetUserId());
                    break;
                case SHARE:
                    eventPublisher.publishShareEvent(activity.getUserId(), activity.getTargetId(), 
                            activity.getTargetUserId(), "");
                    break;
                default:
                    logger.debug("No event publishing for activity type: {}", activity.getActivityType());
            }
        } catch (Exception e) {
            logger.error("Error publishing activity event", e);
        }
    }
}
