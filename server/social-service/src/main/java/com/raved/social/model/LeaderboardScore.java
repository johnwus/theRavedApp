package com.raved.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a leaderboard score for a user in a season
 */
@Entity
@Table(name = "leaderboard_scores", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"season_id", "user_id"}),
       indexes = {
           @Index(name = "idx_leaderboard_season", columnList = "season_id, score DESC")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardScore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "season_id", nullable = false)
    private Long seasonId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "score", nullable = false)
    private Integer score = 0;
    
    @Column(name = "rank")
    private Integer rank;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.updatedAt = LocalDateTime.now();
    }
}
