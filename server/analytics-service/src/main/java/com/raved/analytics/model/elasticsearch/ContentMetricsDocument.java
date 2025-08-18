package com.raved.analytics.model.elasticsearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Elasticsearch document model for content metrics
 * This model is optimized for analytics aggregations and search
 */
@Document(indexName = "content-metrics")
public class ContentMetricsDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String contentId;

    @Field(type = FieldType.Keyword)
    private String contentType;

    @Field(type = FieldType.Keyword)
    private String contentCategory;

    @Field(type = FieldType.Keyword)
    private String contentOwnerId;

    @Field(type = FieldType.Long)
    private Long viewsCount;

    @Field(type = FieldType.Long)
    private Long likesCount;

    @Field(type = FieldType.Long)
    private Long commentsCount;

    @Field(type = FieldType.Long)
    private Long sharesCount;

    @Field(type = FieldType.Double)
    private BigDecimal engagementRate;

    @Field(type = FieldType.Long)
    private Long reach;

    @Field(type = FieldType.Long)
    private Long impressions;

    @Field(type = FieldType.Date)
    private LocalDateTime metricsDate;

    @Field(type = FieldType.Date)
    private LocalDateTime lastCalculatedAt;

    @Field(type = FieldType.Object)
    private Map<String, Object> additionalMetrics;

    // Constructors
    public ContentMetricsDocument() {
    }

    public ContentMetricsDocument(String id, String contentId, String contentType, String contentOwnerId) {
        this.id = id;
        this.contentId = contentId;
        this.contentType = contentType;
        this.contentOwnerId = contentOwnerId;
        this.metricsDate = LocalDateTime.now();
        this.lastCalculatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(String contentCategory) {
        this.contentCategory = contentCategory;
    }

    public String getContentOwnerId() {
        return contentOwnerId;
    }

    public void setContentOwnerId(String contentOwnerId) {
        this.contentOwnerId = contentOwnerId;
    }

    public Long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Long getSharesCount() {
        return sharesCount;
    }

    public void setSharesCount(Long sharesCount) {
        this.sharesCount = sharesCount;
    }

    public BigDecimal getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(BigDecimal engagementRate) {
        this.engagementRate = engagementRate;
    }

    public Long getReach() {
        return reach;
    }

    public void setReach(Long reach) {
        this.reach = reach;
    }

    public Long getImpressions() {
        return impressions;
    }

    public void setImpressions(Long impressions) {
        this.impressions = impressions;
    }

    public LocalDateTime getMetricsDate() {
        return metricsDate;
    }

    public void setMetricsDate(LocalDateTime metricsDate) {
        this.metricsDate = metricsDate;
    }

    public LocalDateTime getLastCalculatedAt() {
        return lastCalculatedAt;
    }

    public void setLastCalculatedAt(LocalDateTime lastCalculatedAt) {
        this.lastCalculatedAt = lastCalculatedAt;
    }

    public Map<String, Object> getAdditionalMetrics() {
        return additionalMetrics;
    }

    public void setAdditionalMetrics(Map<String, Object> additionalMetrics) {
        this.additionalMetrics = additionalMetrics;
    }

    @Override
    public String toString() {
        return "ContentMetricsDocument{" +
                "id='" + id + '\'' +
                ", contentId='" + contentId + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentOwnerId='" + contentOwnerId + '\'' +
                ", viewsCount=" + viewsCount +
                ", likesCount=" + likesCount +
                ", engagementRate=" + engagementRate +
                ", metricsDate=" + metricsDate +
                '}';
    }
}
