package com.raved.social.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a comment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {
    private String content;
    
    // Manual getter in case Lombok is not working
    public String getContent() {
        return content;
    }
}