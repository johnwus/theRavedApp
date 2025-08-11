package com.raved.content.mapper;

import com.raved.content.dto.request.CreatePostRequest;
import com.raved.content.dto.response.PostResponse;
import com.raved.content.model.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper for Post entity and DTOs
 */
@Component
public class PostMapper {

    public PostResponse toPostResponse(Post post) {
        if (post == null) {
            return null;
        }

        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setAuthorId(post.getUserId());
        response.setContent(post.getContent());
        response.setContentType(post.getPostType()); // Use postType instead of contentType
        response.setVisibility(post.getVisibility());
        response.setModerationStatus(post.getModerationStatus());
        
        // Set default values for fields not in Post model
        response.setIsEdited(false); // Not supported in current model
        response.setIsPinned(false); // Not supported in current model
        response.setIsFeatured(post.getIsFeatured());
        response.setAllowComments(true); // Default to true, not supported in current model
        response.setAllowSharing(true); // Default to true, not supported in current model
        
        // Engagement metrics
        response.setLikesCount(post.getLikesCount());
        response.setCommentsCount(post.getCommentsCount());
        response.setSharesCount(post.getSharesCount());
        response.setViewsCount(post.getViewsCount());
        
        // Timestamps
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setEditedAt(null); // Not supported in current model

        // TODO: Add media files, tags, and mentions mapping
        // This would require additional service calls or joins
        
        return response;
    }

    public Post toPost(CreatePostRequest request) {
        if (request == null) {
            return null;
        }

        Post post = new Post();
        post.setUserId(request.getUserId());
        post.setContent(request.getContent());
        
        if (request.getContentType() != null) {
            post.setPostType(request.getContentType());
        } else {
            post.setPostType("OUTFIT");
        }
        
        if (request.getVisibility() != null) {
            post.setVisibility(request.getVisibility());
        } else {
            post.setVisibility("PUBLIC");
        }
        
        post.setFacultyId(request.getFacultyId());
        
        // Initialize counters
        post.setLikesCount(0);
        post.setCommentsCount(0);
        post.setSharesCount(0);
        post.setViewsCount(0);
        post.setSavesCount(0);
        
        // Initialize flags
        post.setIsFeatured(false);
        post.setIsDeleted(false);
        post.setIsFlagged(false);
        
        // Set moderation status
        post.setModerationStatus("APPROVED");
        
        return post;
    }
}
