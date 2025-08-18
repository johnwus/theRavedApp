package com.raved.social.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
* LeaderboardSeason Document for TheRavedApp MongoDB
 *
 * Represents a leaderboard season for competitive rankings. Converted from JPA
 * entity to MongoDB document.
 */
@Document(collection = "leaderboard_seasons")
@CompoundIndexes({
    @CompoundIndex(name = "status_dates_idx", def = "{'status': 1, 'startDate': 1, 'endDate': 1}"),
    @CompoundIndex(name = "name_status_idx", def = "{'name': 1, 'status': 1}"),
    @CompoundIndex(name = "dates_status_idx", def = "{'startDate': 1, 'endDate': 1, 'status': 1}")
})
public class LeaderboardSeason {
    
    @Id
    private String id;
    
    @Indexed
    @NotBlank(message = "Season name is required")
    private String name;
    
    private String description;

    @Indexed
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @Indexed
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
    @Indexed
    private String status = "ACTIVE";
    
    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;
    
    // Additional MongoDB-specific fields
    private LocalDateTime updatedAt;

    private String seasonType = "REGULAR"; // REGULAR, SPECIAL, TOURNAMENT

    private String category; // GAMING, ACADEMIC, SOCIAL, etc.

    private Map<String, Object> rules; // Season-specific rules

    private Map<String, Object> rewards; // Season rewards configuration

    private String timezone = "UTC"; // Season timezone

    private Boolean isPublic = true; // Public visibility

    private String createdBy; // Admin who created the season

    private String moderatedBy; // Admin who moderates the season

    private LocalDateTime registrationStartDate; // When registration opens

    private LocalDateTime registrationEndDate; // When registration closes

    private Integer maxParticipants; // Maximum number of participants

    private String eligibilityCriteria; // Who can participate

    private String seasonTheme; // Visual theme for the season

    private String seasonLogo; // Logo URL for the season

    private String seasonColor; // Primary color for the season

    private Boolean isRegistrationRequired = false; // Whether registration is needed

    private Boolean isAutoEnrollment = true; // Auto-enroll users

    private String enrollmentCriteria; // Criteria for auto-enrollment

    private Map<String, Object> metadata; // Additional custom fields

    // Default constructor
    public LeaderboardSeason() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with core fields
    public LeaderboardSeason(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this();
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSeasonType() {
        return seasonType;
    }

    public void setSeasonType(String seasonType) {
        this.seasonType = seasonType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> getRules() {
        return rules;
    }

    public void setRules(Map<String, Object> rules) {
        this.rules = rules;
    }

    public Map<String, Object> getRewards() {
        return rewards;
    }

    public void setRewards(Map<String, Object> rewards) {
        this.rewards = rewards;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModeratedBy() {
        return moderatedBy;
    }

    public void setModeratedBy(String moderatedBy) {
        this.moderatedBy = moderatedBy;
    }

    public LocalDateTime getRegistrationStartDate() {
        return registrationStartDate;
    }

    public void setRegistrationStartDate(LocalDateTime registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
    }

    public LocalDateTime getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(LocalDateTime registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    public void setEligibilityCriteria(String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }

    public String getSeasonTheme() {
        return seasonTheme;
    }

    public void setSeasonTheme(String seasonTheme) {
        this.seasonTheme = seasonTheme;
    }

    public String getSeasonLogo() {
        return seasonLogo;
    }

    public void setSeasonLogo(String seasonLogo) {
        this.seasonLogo = seasonLogo;
    }

    public String getSeasonColor() {
        return seasonColor;
    }

    public void setSeasonColor(String seasonColor) {
        this.seasonColor = seasonColor;
    }

    public Boolean getIsRegistrationRequired() {
        return isRegistrationRequired;
    }

    public void setIsRegistrationRequired(Boolean isRegistrationRequired) {
        this.isRegistrationRequired = isRegistrationRequired;
    }

    public Boolean getIsAutoEnrollment() {
        return isAutoEnrollment;
    }

    public void setIsAutoEnrollment(Boolean isAutoEnrollment) {
        this.isAutoEnrollment = isAutoEnrollment;
    }

    public String getEnrollmentCriteria() {
        return enrollmentCriteria;
    }

    public void setEnrollmentCriteria(String enrollmentCriteria) {
        this.enrollmentCriteria = enrollmentCriteria;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    // Business logic methods
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isUpcoming() {
        return "UPCOMING".equals(this.status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }

    public boolean isRegistrationOpen() {
        if (this.registrationStartDate == null || this.registrationEndDate == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.registrationStartDate) && now.isBefore(this.registrationEndDate);
    }

    public boolean isSeasonRunning() {
        if (this.startDate == null || this.endDate == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.startDate) && now.isBefore(this.endDate);
    }

    public void activate() {
        this.status = "ACTIVE";
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = "COMPLETED";
        this.updatedAt = LocalDateTime.now();
    }

    public void pause() {
        this.status = "PAUSED";
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "LeaderboardSeason{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", status='" + status + '\''
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + ", isActive=" + isActive()
                + '}';
    }
}
