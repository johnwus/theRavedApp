package com.raved.social.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * LeaderboardScore Document for TheRavedApp MongoDB
 *
 * Represents a leaderboard score for a user in a season. Converted from JPA
 * entity to MongoDB document.
 */
@Document(collection = "leaderboard_scores")
@CompoundIndexes({
    @CompoundIndex(name = "season_user_idx", def = "{'seasonId': 1, 'userId': 1}", unique = true),
    @CompoundIndex(name = "season_score_idx", def = "{'seasonId': 1, 'score': -1}"),
    @CompoundIndex(name = "season_rank_idx", def = "{'seasonId': 1, 'rank': 1}")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardScore {

    @Id
    private String id;

    @Indexed
    @NotBlank(message = "Season ID is required")
    private String seasonId;

    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Score is required")
    private Integer score = 0;

    private Integer rank;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    /**
     * Sets timestamps before saving
     */
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }
}
