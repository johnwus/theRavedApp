package com.raved.user.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "show_activity")
    private Boolean showActivity = true;

    @Column(name = "read_receipts")
    private Boolean readReceipts = true;

    @Column(name = "allow_downloads")
    private Boolean allowDownloads = false;

    @Column(name = "allow_story_sharing")
    private Boolean allowStorySharing = true;

    @Column(name = "analytics_enabled")
    private Boolean analyticsEnabled = true;

    @Column(name = "personalized_ads")
    private Boolean personalizedAds = false;

    @Column(name = "language", length = 10)
    private String language = "en";

    @Column(name = "date_format", length = 20)
    private String dateFormat = "DD/MM/YYYY";

    @Column(name = "currency", length = 10)
    private String currency = "GHS";

    @Column(name = "theme", length = 50)
    private String theme = "default";

    @Column(name = "notification_preferences", columnDefinition = "jsonb")
    private String notificationPreferences = "{}";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Boolean getShowActivity() { return showActivity; }
    public void setShowActivity(Boolean showActivity) { this.showActivity = showActivity; }

    public Boolean getReadReceipts() { return readReceipts; }
    public void setReadReceipts(Boolean readReceipts) { this.readReceipts = readReceipts; }

    public Boolean getAllowDownloads() { return allowDownloads; }
    public void setAllowDownloads(Boolean allowDownloads) { this.allowDownloads = allowDownloads; }

    public Boolean getAllowStorySharing() { return allowStorySharing; }
    public void setAllowStorySharing(Boolean allowStorySharing) { this.allowStorySharing = allowStorySharing; }

    public Boolean getAnalyticsEnabled() { return analyticsEnabled; }
    public void setAnalyticsEnabled(Boolean analyticsEnabled) { this.analyticsEnabled = analyticsEnabled; }

    public Boolean getPersonalizedAds() { return personalizedAds; }
    public void setPersonalizedAds(Boolean personalizedAds) { this.personalizedAds = personalizedAds; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getNotificationPreferences() { return notificationPreferences; }
    public void setNotificationPreferences(String notificationPreferences) { this.notificationPreferences = notificationPreferences; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void preUpdate() { this.updatedAt = Instant.now(); }
}

