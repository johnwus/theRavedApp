package com.raved.social.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating/updating comments
 * Updated for MongoDB compatibility
 * with String IDs
 */
public class CommentRequest {

    @NotBlank(message = "Post ID is required")
    private String postId; // Required
    
    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Comment content must not exceed 2000 characters")
    private String content; // Required, max 2000 characters

    private String parentCommentId; // For threaded comments

    // Constructors
    public CommentRequest() {
    }

    public CommentRequest(String postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public CommentRequest(String postId, String content, String parentCommentId) {
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    // Getters and Setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
