package com.raved.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Document(collection = "user_analytics")
@CompoundIndex(name = "idx_user_period_date", def = "{'userId':1,'period':1,'date':1}")
public class UserAnalytics {

    @Id
    private String id;

    @Indexed
    private String userId;

    // 'daily','weekly','monthly'
    @Indexed
    private String period;

    // Period start date
    @Indexed
    private LocalDate date;

    // Aggregated metrics
    private Map<String, Long> metrics;

    // Engagement
    private Map<String, Object> engagement;

    // Audience breakdowns
    private Map<String, Object> audience;

    // Sources
    private Map<String, Long> sources;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Map<String, Long> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Long> metrics) { this.metrics = metrics; }
    public Map<String, Object> getEngagement() { return engagement; }
    public void setEngagement(Map<String, Object> engagement) { this.engagement = engagement; }
    public Map<String, Object> getAudience() { return audience; }
    public void setAudience(Map<String, Object> audience) { this.audience = audience; }
    public Map<String, Long> getSources() { return sources; }
    public void setSources(Map<String, Long> sources) { this.sources = sources; }
}

