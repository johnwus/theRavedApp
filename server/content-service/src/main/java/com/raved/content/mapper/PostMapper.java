package com.raved.content.mapper;

import com.raved.content.dto.request.CreatePostRequest;
import com.raved.content.dto.request.UpdatePostRequest;
import com.raved.content.dto.response.PostResponse;
import com.raved.content.model.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Mapper for Post MongoDB document and DTOs
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
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setContentType(post.getPostType());
        response.setVisibility(post.getVisibility());
        response.setPublishStatus(post.getPublishStatus());
        response.setModerationStatus(post.getModerationStatus());

        // MongoDB-specific fields
        response.setCategory(post.getCategory());
        response.setLanguage(post.getLanguage());
        response.setAccessLevel(post.getAccessLevel());
        response.setContentFlags(post.getContentFlags());
        response.setSentiment(post.getSentiment());
        response.setEngagementScore(post.getEngagementScore());
        response.setTrendingScore(post.getTrendingScore());
        response.setViralityScore(post.getViralityScore());

        // Flags and status
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
        response.setSavesCount(post.getSavesCount());

        // Timestamps
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setEditedAt(post.getEditedAt());
        response.setPublishedAt(post.getPublishedAt());
        response.setExpiresAt(post.getExpiresAt());
        response.setFeaturedAt(post.getFeaturedAt());
        response.setPinnedAt(post.getPinnedAt());

        // SEO and metadata
        response.setSeoTitle(post.getSeoTitle());
        response.setSeoDescription(post.getSeoDescription());
        response.setSeoKeywords(post.getSeoKeywords());
        response.setMetadata(post.getMetadata());
        response.setAnalytics(post.getAnalytics());
        response.setCustomFields(post.getCustomFields());

        // Moderation
        response.setModeratorId(post.getModeratorId());
        response.setModerationReason(post.getModerationReason());
        response.setModeratedAt(post.getModeratedAt());

        // Media and tags
        response.setTags(post.getTags());
        response.setMediaFileIds(post.getMediaFileIds());

        // TODO: Add media files, mentions mapping
        // This would require additional service calls or joins

        return response;
    }

    public Post toPost(CreatePostRequest request) {
        if (request == null) {
            return null;
        }

        Post post = new Post();
        post.setUserId(request.getUserId());
        post.setTitle(request.getTitle());
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

        // MongoDB-specific fields
        post.setTags(request.getTags());
        post.setCategory(request.getCategory());
        post.setLanguage(request.getLanguage());
        post.setAccessLevel(request.getAccessLevel());
        post.setMediaFileIds(request.getMediaFileIds());
        post.setSeoTitle(request.getSeoTitle());
        post.setSeoDescription(request.getSeoDescription());
        post.setSeoKeywords(request.getSeoKeywords());
        post.setCustomFields(request.getCustomFields());

        // Handle scheduled publishing
        if (request.getScheduledAt() != null) {
            try {
                LocalDateTime scheduledAt = LocalDateTime.parse(request.getScheduledAt(),
                        DateTimeFormatter.ISO_DATE_TIME);
                post.setScheduledAt(scheduledAt);
                post.setPublishStatus("SCHEDULED");
            } catch (Exception e) {
                // If parsing fails, default to draft
                post.setPublishStatus("DRAFT");
            }
        } else {
            post.setPublishStatus("DRAFT");
        }

        if (request.getExpiresAt() != null) {
            try {
                LocalDateTime expiresAt = LocalDateTime.parse(request.getExpiresAt(), DateTimeFormatter.ISO_DATE_TIME);
                post.setExpiresAt(expiresAt);
            } catch (Exception e) {
                // If parsing fails, ignore expiration
            }
        }

        // Initialize counters
        post.setLikesCount(0);
        post.setCommentsCount(0);
        post.setSharesCount(0);
        post.setViewsCount(0);
        post.setSavesCount(0);

        // Initialize scores
        post.setEngagementScore(0.0);
        post.setTrendingScore(0.0);
        post.setViralityScore(0.0);

        // Initialize flags
        post.setIsFeatured(false);
        post.setIsDeleted(false);
        post.setIsFlagged(false);
        post.setIsEdited(false);
        post.setIsPinned(false);

        // Set moderation status
        post.setModerationStatus("APPROVED");

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        return post;
    }

    public Post updatePostFromRequest(Post existingPost, UpdatePostRequest request) {
        if (request == null || existingPost == null) {
            return existingPost;
        }

        // Update basic fields if provided
        if (request.getTitle() != null) {
            existingPost.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            existingPost.setContent(request.getContent());
        }
        if (request.getVisibility() != null) {
            existingPost.setVisibility(request.getVisibility());
        }
        if (request.getPublishStatus() != null) {
            existingPost.setPublishStatus(request.getPublishStatus());
        }
        if (request.getAllowComments() != null) {
            existingPost.setAllowComments(request.getAllowComments());
        }
        if (request.getAllowSharing() != null) {
            existingPost.setAllowSharing(request.getAllowSharing());
        }

        // Update MongoDB-specific fields
        if (request.getTags() != null) {
            existingPost.setTags(request.getTags());
        }
        if (request.getCategory() != null) {
            existingPost.setCategory(request.getCategory());
        }
        if (request.getLanguage() != null) {
            existingPost.setLanguage(request.getLanguage());
        }
        if (request.getAccessLevel() != null) {
            existingPost.setAccessLevel(request.getAccessLevel());
        }
        if (request.getMediaFileIds() != null) {
            existingPost.setMediaFileIds(request.getMediaFileIds());
        }
        if (request.getSeoTitle() != null) {
            existingPost.setSeoTitle(request.getSeoTitle());
        }
        if (request.getSeoDescription() != null) {
            existingPost.setSeoDescription(request.getSeoDescription());
        }
        if (request.getSeoKeywords() != null) {
            existingPost.setSeoKeywords(request.getSeoKeywords());
        }
        if (request.getCustomFields() != null) {
            existingPost.setCustomFields(request.getCustomFields());
        }

        // Handle scheduled publishing
        if (request.getScheduledAt() != null) {
            try {
                LocalDateTime scheduledAt = LocalDateTime.parse(request.getScheduledAt(),
                        DateTimeFormatter.ISO_DATE_TIME);
                existingPost.setScheduledAt(scheduledAt);
                if (request.getPublishStatus() == null) {
                    existingPost.setPublishStatus("SCHEDULED");
                }
            } catch (Exception e) {
                // If parsing fails, ignore
            }
        }

        if (request.getExpiresAt() != null) {
            try {
                LocalDateTime expiresAt = LocalDateTime.parse(request.getExpiresAt(), DateTimeFormatter.ISO_DATE_TIME);
                existingPost.setExpiresAt(expiresAt);
            } catch (Exception e) {
                // If parsing fails, ignore
            }
        }

        // Mark as edited
        existingPost.setIsEdited(true);
        existingPost.setEditedAt(LocalDateTime.now());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return existingPost;
    }

    public List<PostResponse> toPostResponseList(List<Post> posts) {
        if (posts == null) {
            return List.of();
        }
        return posts.stream()
                .map(this::toPostResponse)
                .toList();
    }
}
