package com.raved.social.service.impl;

import com.raved.social.dto.request.CreateActivityRequest;
import com.raved.social.dto.response.ActivityResponse;
import com.raved.social.event.SocialEventPublisher;
import com.raved.social.mapper.ActivityMapper;
import com.raved.social.model.Activity;
import com.raved.social.model.ActivityType;
import com.raved.social.repository.ActivityRepository;
import com.raved.social.service.ActivityService;
import com.raved.social.util.MongoIdConverter;
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
        // createdAt is automatically set in the constructor and @PrePersist
        
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
        activity.setUserId(MongoIdConverter.toStringId(userId));
        activity.setActivityType(ActivityType.LIKE.name());
        activity.setPostId(MongoIdConverter.toStringId(postId));
        activity.setTargetUserId(MongoIdConverter.toStringId(postAuthorId));
        // createdAt is automatically set in the constructor and @PrePersist
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish like event
        eventPublisher.publishLikeEvent(userId, postId, postAuthorId);
        
        logger.info("Like activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordCommentActivity(Long userId, Long postId, Long postAuthorId, Long commentId) {
        logger.info("Recording comment activity: user {} commented on post {} by user {}", userId, postId, postAuthorId);
        
        Activity activity = new Activity();
        activity.setUserId(MongoIdConverter.toStringId(userId));
        activity.setActivityType(ActivityType.COMMENT.name());
        activity.setPostId(MongoIdConverter.toStringId(postId));
        activity.setCommentId(MongoIdConverter.toStringId(commentId));
        activity.setTargetUserId(MongoIdConverter.toStringId(postAuthorId));
        // createdAt is automatically set in the constructor and @PrePersist
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish comment event
        eventPublisher.publishCommentCreatedEvent(userId, postId, commentId, postAuthorId);
        
        logger.info("Comment activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordFollowActivity(Long followerId, Long followingId) {
        logger.info("Recording follow activity: user {} followed user {}", followerId, followingId);
        
        Activity activity = new Activity();
        activity.setUserId(MongoIdConverter.toStringId(followerId));
        activity.setActivityType(ActivityType.FOLLOW.name());
        activity.setTargetUserId(MongoIdConverter.toStringId(followingId));
        // createdAt is automatically set in the constructor and @PrePersist
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish follow event
        eventPublisher.publishFollowCreatedEvent(followerId, followingId);
        
        logger.info("Follow activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordShareActivity(Long userId, Long postId, Long postAuthorId, String shareType) {
        logger.info("Recording share activity: user {} shared post {} by user {}", userId, postId, postAuthorId);
        
        Activity activity = new Activity();
        activity.setUserId(MongoIdConverter.toStringId(userId));
        activity.setActivityType(ActivityType.SHARE.name());
        activity.setPostId(MongoIdConverter.toStringId(postId));
        activity.setTargetUserId(MongoIdConverter.toStringId(postAuthorId));
        // Note: metadata field doesn't exist in the current Activity model
        // createdAt is automatically set in the constructor and @PrePersist
        
        Activity savedActivity = activityRepository.save(activity);
        
        // Publish share event - using the available method
        eventPublisher.publishActivityCreatedEvent(userId, ActivityType.SHARE.name(), postId, "POST");
        
        logger.info("Share activity recorded: {}", savedActivity.getId());
    }

    @Override
    public void recordPostActivity(Long userId, Long postId) {
        logger.info("Recording post activity: user {} created post {}", userId, postId);
        
        Activity activity = new Activity();
        activity.setUserId(MongoIdConverter.toStringId(userId));
        activity.setActivityType(ActivityType.POST.name());
        activity.setPostId(MongoIdConverter.toStringId(postId));
        activity.setTargetUserId(MongoIdConverter.toStringId(userId));
        // createdAt is automatically set in the constructor and @PrePersist
        
        Activity savedActivity = activityRepository.save(activity);
        
        logger.info("Post activity recorded: {}", savedActivity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getUserActivities(Long userId, Pageable pageable) {
        logger.debug("Getting activities for user: {}", userId);
        
        Page<Activity> activities = activityRepository.findByUserIdOrderByCreatedAtDesc(MongoIdConverter.toStringId(userId), pageable);
        return activities.map(activityMapper::toActivityResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivitiesByType(Long userId, ActivityType activityType, Pageable pageable) {
        logger.debug("Getting activities for user: {} of type: {}", userId, activityType);
        
        Page<Activity> activities = activityRepository.findByUserIdAndActivityTypeOrderByCreatedAtDesc(
                MongoIdConverter.toStringId(userId), activityType.name(), pageable);
        return activities.map(activityMapper::toActivityResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivitiesForTarget(Long targetId, String targetType, Pageable pageable) {
        logger.debug("Getting activities for target: {} of type: {}", targetId, targetType);

        // Use the existing repository method that takes targetId and targetType
        Page<Activity> activities = activityRepository.findByTargetIdAndTargetTypeOrderByCreatedAtDesc(
                MongoIdConverter.toStringId(targetId), targetType, pageable);

        return activities.map(activityMapper::toActivityResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getRecentActivities(Long userId, int limit) {
        logger.debug("Getting recent activities for user: {} with limit: {}", userId, limit);

        List<Activity> activities = activityRepository.findTopByUserIdOrderByCreatedAtDesc(MongoIdConverter.toStringId(userId), limit);
        return activities.stream()
                .map(activityMapper::toActivityResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getActivityCount(Long userId, ActivityType activityType) {
        logger.debug("Getting activity count for user: {} of type: {}", userId, activityType);

        return activityRepository.countByUserIdAndActivityType(MongoIdConverter.toStringId(userId), activityType.name());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalActivityCount(Long userId) {
        logger.debug("Getting total activity count for user: {}", userId);

        return activityRepository.countByUserId(MongoIdConverter.toStringId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getActivitiesInDateRange(Long userId, LocalDateTime startDate, 
                                                          LocalDateTime endDate) {
        logger.debug("Getting activities for user: {} between {} and {}", userId, startDate, endDate);
        
        List<Activity> activities = activityRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                MongoIdConverter.toStringId(userId), startDate, endDate);

        return activities.stream()
                .map(activityMapper::toActivityResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteActivity(Long activityId) {
        logger.info("Deleting activity: {}", activityId);

        Optional<Activity> activityOpt = activityRepository.findById(MongoIdConverter.toStringId(activityId));
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

        long deletedCount = activityRepository.deleteByUserId(MongoIdConverter.toStringId(userId));
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
            String activityType = activity.getActivityType();
            if (ActivityType.LIKE.name().equals(activityType)) {
                eventPublisher.publishLikeEvent(MongoIdConverter.toLongId(activity.getUserId()), MongoIdConverter.toLongId(activity.getPostId()),
                        MongoIdConverter.toLongId(activity.getTargetUserId()));
            } else if (ActivityType.COMMENT.name().equals(activityType)) {
                eventPublisher.publishCommentCreatedEvent(MongoIdConverter.toLongId(activity.getUserId()), MongoIdConverter.toLongId(activity.getPostId()),
                        MongoIdConverter.toLongId(activity.getCommentId()), MongoIdConverter.toLongId(activity.getTargetUserId()));
            } else if (ActivityType.FOLLOW.name().equals(activityType)) {
                eventPublisher.publishFollowCreatedEvent(MongoIdConverter.toLongId(activity.getUserId()), MongoIdConverter.toLongId(activity.getTargetUserId()));
            } else if (ActivityType.SHARE.name().equals(activityType)) {
                eventPublisher.publishActivityCreatedEvent(MongoIdConverter.toLongId(activity.getUserId()), activityType,
                        MongoIdConverter.toLongId(activity.getPostId()), "POST");
            } else {
                logger.debug("No event publishing for activity type: {}", activityType);
            }
        } catch (Exception e) {
            logger.error("Error publishing activity event", e);
        }
    }
}
