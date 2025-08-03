package com.raved.content.service;

import com.raved.content.dto.request.CreatePostRequest;
import com.raved.content.dto.request.UpdatePostRequest;
import com.raved.content.dto.response.PostResponse;
import com.raved.content.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * PostService for TheRavedApp
 */
public interface PostService {

    /**
     * Create a new post
     */
    PostResponse createPost(CreatePostRequest request);

    /**
     * Get post by ID
     */
    Optional<PostResponse> getPostById(Long id);

    /**
     * Update post
     */
    PostResponse updatePost(Long id, UpdatePostRequest request);

    /**
     * Delete post
     */
    void deletePost(Long id);

    /**
     * Get posts by user ID
     */
    Page<PostResponse> getPostsByUserId(Long userId, Pageable pageable);

    /**
     * Get posts by faculty ID
     */
    Page<PostResponse> getPostsByFacultyId(Long facultyId, Pageable pageable);

    /**
     * Get public posts
     */
    Page<PostResponse> getPublicPosts(Pageable pageable);

    /**
     * Get trending posts
     */
    List<PostResponse> getTrendingPosts(int limit);

    /**
     * Get featured posts
     */
    List<PostResponse> getFeaturedPosts(int limit);

    /**
     * Search posts
     */
    Page<PostResponse> searchPosts(String query, Pageable pageable);

    /**
     * Increment view count
     */
    void incrementViewCount(Long postId);

    /**
     * Update engagement metrics
     */
    void updateEngagementMetrics(Long postId, int likesCount, int commentsCount, int sharesCount);

    /**
     * Flag post for moderation
     */
    void flagPost(Long postId, String reason);

    /**
     * Feature post
     */
    void featurePost(Long postId, int durationHours);
}
