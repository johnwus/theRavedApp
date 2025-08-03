package com.raved.social.mapper;

import com.raved.social.dto.response.LikeResponse;
import com.raved.social.model.Like;
import org.springframework.stereotype.Component;

/**
 * Mapper for Like entity and DTOs
 */
@Component
public class LikeMapper {

    public LikeResponse toLikeResponse(Like like) {
        if (like == null) {
            return null;
        }

        LikeResponse response = new LikeResponse();
        response.setId(like.getId());
        response.setUserId(like.getUserId());
        response.setPostId(like.getPostId());
        response.setCreatedAt(like.getCreatedAt());

        return response;
    }
}
