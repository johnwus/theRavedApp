package com.raved.social.mapper;

import com.raved.social.dto.response.FollowResponse;
import com.raved.social.model.Follow;
import org.springframework.stereotype.Component;

/**
 * Mapper for Follow entity and DTOs
 */
@Component
public class FollowMapper {

    public FollowResponse toFollowResponse(Follow follow) {
        if (follow == null) {
            return null;
        }

        FollowResponse response = new FollowResponse();
        response.setId(follow.getId());
        response.setFollowerId(follow.getFollowerId());
        response.setFollowingId(follow.getFollowingId());
        response.setIsBlocked(follow.getIsBlocked());
        response.setCreatedAt(follow.getCreatedAt());

        return response;
    }
}
