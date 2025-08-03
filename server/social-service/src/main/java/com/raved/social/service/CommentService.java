package com.raved.social.service;

import com.raved.social.dto.request.CreateCommentRequest;
import com.raved.social.dto.request.UpdateCommentRequest;
import com.raved.social.dto.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * CommentService for TheRavedApp
 */
public interface CommentService {

    /**
     * Create a new comment
     */
    CommentResponse createComment(CreateCommentRequest request);

    /**
     * Get comment by ID
     */
    Optional<CommentResponse> getCommentById(Long id);

    /**
     * Update comment
     */
    CommentResponse updateComment(Long id, UpdateCommentRequest request);

    /**
     * Delete comment
     */
    void deleteComment(Long id);

    /**
     * Get comments for a post
     */
    Page<CommentResponse> getCommentsByPost(Long postId, Pageable pageable);

    /**
     * Get comments by user
     */
    Page<CommentResponse> getCommentsByUser(Long userId, Pageable pageable);

    /**
     * Get replies to a comment
     */
    List<CommentResponse> getCommentReplies(Long parentCommentId);

    /**
     * Like a comment
     */
    void likeComment(Long commentId, Long userId);

    /**
     * Unlike a comment
     */
    void unlikeComment(Long commentId, Long userId);

    /**
     * Get comment like count
     */
    long getCommentLikeCount(Long commentId);

    /**
     * Check if user liked comment
     */
    boolean hasUserLikedComment(Long commentId, Long userId);

    /**
     * Report comment
     */
    void reportComment(Long commentId, Long reporterId, String reason);

    /**
     * Get recent comments for user's posts
     */
    List<CommentResponse> getRecentCommentsForUser(Long userId, int limit);
}
