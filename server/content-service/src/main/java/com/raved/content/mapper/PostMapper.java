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
        response.setContentType(post.getContentType() != null ? post.getContentType().name() : null);
        response.setVisibility(post.getVisibility() != null ? post.getVisibility().name() : null);
        response.setModerationStatus(post.getModerationStatus() != null ? post.getModerationStatus().name() : null);
        response.setIsEdited(post.getIsEdited());
        response.setIsPinned(post.getIsPinned());
        response.setIsFeatured(post.getIsFeatured());
        response.setAllowComments(post.getAllowComments());
        response.setAllowSharing(post.getAllowSharing());
        
        // Engagement metrics
        response.setLikesCount(post.getLikesCount());
        response.setCommentsCount(post.getCommentsCount());
        response.setSharesCount(post.getSharesCount());
        response.setViewsCount(post.getViewsCount());
        
        // Timestamps
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setEditedAt(post.getEditedAt());

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
            post.setContentType(Post.ContentType.valueOf(request.getContentType()));
        } else {
            post.setContentType(Post.ContentType.TEXT);
        }
        
        if (request.getVisibility() != null) {
            post.setVisibility(Post.Visibility.valueOf(request.getVisibility()));
        } else {
            post.setVisibility(Post.Visibility.PUBLIC);
        }
        
        post.setFacultyId(request.getFacultyId());
        post.setAllowComments(request.getAllowComments() != null ? request.getAllowComments() : true);
        post.setAllowSharing(request.getAllowSharing() != null ? request.getAllowSharing() : true);
        
        // Initialize counters
        post.setLikesCount(0);
        post.setCommentsCount(0);
        post.setSharesCount(0);
        post.setViewsCount(0);
        
        // Initialize flags
        post.setIsEdited(false);
        post.setIsPinned(false);
        post.setIsFeatured(false);
        post.setIsDeleted(false);
        post.setIsFlagged(false);
        
        // Set moderation status
        post.setModerationStatus(Post.ModerationStatus.APPROVED);
        
        return post;
    }
}
