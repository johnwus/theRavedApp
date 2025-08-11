package com.raved.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing a saved post (bookmark) by a user
 */
@Entity
@Table(name = "saved_posts", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedPost {

    @EmbeddedId
    private SavedPostId id;

    @Column(name = "saved_at", nullable = false, updatable = false)
    private LocalDateTime savedAt;

    @PrePersist
    public void prePersist() {
        this.savedAt = LocalDateTime.now();
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SavedPostId implements java.io.Serializable {
        
        @Column(name = "user_id", nullable = false)
        private Long userId;
        
        @Column(name = "post_id", nullable = false)
        private Long postId;
    }
}
