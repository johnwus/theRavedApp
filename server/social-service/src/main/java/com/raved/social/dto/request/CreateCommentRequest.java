package com.raved.social.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a comment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    private Long postId;
    private Long userId;
    private Long parentCommentId;
    private String content;
    
    // Manual getters in case Lombok is not working
    public Long getPostId() {
        return postId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getParentCommentId() {
        return parentCommentId;
    }
    
    public String getContent() {
        return content;
    }
}