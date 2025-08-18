package com.raved.social.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a comment
 * Updated for MongoDB compatibility with String IDs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "Post ID is required")
    private String postId;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String parentCommentId;

    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Comment content must not exceed 2000 characters")
    private String content;
    
    // Manual getters in case Lombok is not working
    public String getPostId() {
        return postId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getParentCommentId() {
        return parentCommentId;
    }
    
    public String getContent() {
        return content;
    }
}