package com.raved.social.mapper;

import com.raved.social.dto.request.CreateActivityRequest;
import com.raved.social.dto.response.ActivityResponse;
import com.raved.social.model.Activity;
import org.springframework.stereotype.Component;

/**
 * Mapper for Activity entities and DTOs
 */
@Component
public class ActivityMapper {

    /**
     * Convert Activity entity to ActivityResponse DTO
     */
    public ActivityResponse toActivityResponse(Activity activity) {
        if (activity == null) {
            return null;
        }

        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setActivityType(activity.getActivityType());
        response.setTargetUserId(activity.getTargetUserId());
        response.setPostId(activity.getPostId());
        response.setCommentId(activity.getCommentId());
        response.setCreatedAt(activity.getCreatedAt());

        return response;
    }

    /**
     * Convert CreateActivityRequest to Activity entity
     */
    public Activity toActivity(CreateActivityRequest request) {
        if (request == null) {
            return null;
        }

        Activity activity = new Activity();
        activity.setUserId(request.getUserId());
        activity.setActivityType(request.getActivityType());
        activity.setTargetUserId(request.getTargetUserId());
        activity.setPostId(request.getPostId());
        activity.setCommentId(request.getCommentId());

        return activity;
    }
}