package com.raved.social.dto.request;

/**
 * Request DTO for creating/updating comments
 */
public class CommentRequest {

    private Long postId; // Required
    
    private String content; // Required, max 2000 characters

    private Long parentCommentId; // For threaded comments

    // Constructors
    public CommentRequest() {
    }

    public CommentRequest(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public CommentRequest(Long postId, String content, Long parentCommentId) {
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
