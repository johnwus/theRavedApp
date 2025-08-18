package com.raved.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Document(collection = "content_analytics")
@CompoundIndex(name = "idx_content_period_date", def = "{'contentId':1,'period':1,'date':1}")
public class ContentAnalytics {

    @Id
    private String id;

    @Indexed
    private String contentId;

    private String contentType; // post, story, product

    private String authorId;

    @Indexed
    private String period; // daily, weekly, monthly

    @Indexed
    private LocalDate date; // period start

    private Map<String, Long> metrics; // views, likes, comments, shares, bookmarks, saves

    private Map<String, Object> demographics; // top faculties, age groups, genders

    private Map<String, Object> sources; // feed, profile, search, direct

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContentId() { return contentId; }
    public void setContentId(String contentId) { this.contentId = contentId; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Map<String, Long> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Long> metrics) { this.metrics = metrics; }
    public Map<String, Object> getDemographics() { return demographics; }
    public void setDemographics(Map<String, Object> demographics) { this.demographics = demographics; }
    public Map<String, Object> getSources() { return sources; }
    public void setSources(Map<String, Object> sources) { this.sources = sources; }
}

