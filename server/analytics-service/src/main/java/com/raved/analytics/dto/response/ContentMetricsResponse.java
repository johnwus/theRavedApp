package com.raved.analytics.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Response DTO for content metrics with comprehensive analytics
 * 
 * Enhanced response DTO supporting e-commerce, subscription, social,
 * and performance analytics for MongoDB-driven analytics service.
 */
public class ContentMetricsResponse {

    private String id;
    private String contentId;
    private String contentOwnerId;

    // Basic engagement metrics
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Double engagementScore;
    private Long reachCount;
    private Long impressionCount;

    // Enhanced engagement metrics
    private EngagementMetrics engagementMetrics;

    // Performance metrics
    private PerformanceMetrics performanceMetrics;

    // E-commerce metrics
    private EcommerceMetrics ecommerceMetrics;

    // Subscription metrics
    private SubscriptionMetrics subscriptionMetrics;

    // Social metrics
    private SocialMetrics socialMetrics;

    // Content analytics
    private ContentAnalytics contentAnalytics;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Constructors
    public ContentMetricsResponse() {}

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

    public String getContentOwnerId() {
        return contentOwnerId;
    }

    public void setContentOwnerId(String contentOwnerId) {
        this.contentOwnerId = contentOwnerId;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Double getEngagementScore() {
        return engagementScore;
    }

    public void setEngagementScore(Double engagementScore) {
        this.engagementScore = engagementScore;
    }

    public Long getReachCount() {
        return reachCount;
    }

    public void setReachCount(Long reachCount) {
        this.reachCount = reachCount;
    }

    public Long getImpressionCount() {
        return impressionCount;
    }

    public void setImpressionCount(Long impressionCount) {
        this.impressionCount = impressionCount;
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

    // Getters and setters for nested metrics
    public EngagementMetrics getEngagementMetrics() {
        return engagementMetrics;
    }

    public void setEngagementMetrics(EngagementMetrics engagementMetrics) {
        this.engagementMetrics = engagementMetrics;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public EcommerceMetrics getEcommerceMetrics() {
        return ecommerceMetrics;
    }

    public void setEcommerceMetrics(EcommerceMetrics ecommerceMetrics) {
        this.ecommerceMetrics = ecommerceMetrics;
    }

    public SubscriptionMetrics getSubscriptionMetrics() {
        return subscriptionMetrics;
    }

    public void setSubscriptionMetrics(SubscriptionMetrics subscriptionMetrics) {
        this.subscriptionMetrics = subscriptionMetrics;
    }

    public SocialMetrics getSocialMetrics() {
        return socialMetrics;
    }

    public void setSocialMetrics(SocialMetrics socialMetrics) {
        this.socialMetrics = socialMetrics;
    }

    public ContentAnalytics getContentAnalytics() {
        return contentAnalytics;
    }

    public void setContentAnalytics(ContentAnalytics contentAnalytics) {
        this.contentAnalytics = contentAnalytics;
    }

    // Nested classes for comprehensive metrics
    public static class EngagementMetrics {
        private Long totalInteractions;
        private Long totalReactions;
        private Long totalComments;
        private Long totalShares;
        private Double engagementRate;
        private Long uniqueViewers;
        private Long returningViewers;
        private Map<String, Long> interactionTypeDistribution;
        private List<String> topCommenters;
        private List<String> topSharers;

        // Getters and setters
        public Long getTotalInteractions() {
            return totalInteractions;
        }

        public void setTotalInteractions(Long totalInteractions) {
            this.totalInteractions = totalInteractions;
        }

        public Long getTotalReactions() {
            return totalReactions;
        }

        public void setTotalReactions(Long totalReactions) {
            this.totalReactions = totalReactions;
        }

        public Long getTotalComments() {
            return totalComments;
        }

        public void setTotalComments(Long totalComments) {
            this.totalComments = totalComments;
        }

        public Long getTotalShares() {
            return totalShares;
        }

        public void setTotalShares(Long totalShares) {
            this.totalShares = totalShares;
        }

        public Double getEngagementRate() {
            return engagementRate;
        }

        public void setEngagementRate(Double engagementRate) {
            this.engagementRate = engagementRate;
        }

        public Long getUniqueViewers() {
            return uniqueViewers;
        }

        public void setUniqueViewers(Long uniqueViewers) {
            this.uniqueViewers = uniqueViewers;
        }

        public Long getReturningViewers() {
            return returningViewers;
        }

        public void setReturningViewers(Long returningViewers) {
            this.returningViewers = returningViewers;
        }

        public Map<String, Long> getInteractionTypeDistribution() {
            return interactionTypeDistribution;
        }

        public void setInteractionTypeDistribution(Map<String, Long> interactionTypeDistribution) {
            this.interactionTypeDistribution = interactionTypeDistribution;
        }

        public List<String> getTopCommenters() {
            return topCommenters;
        }

        public void setTopCommenters(List<String> topCommenters) {
            this.topCommenters = topCommenters;
        }

        public List<String> getTopSharers() {
            return topSharers;
        }

        public void setTopSharers(List<String> topSharers) {
            this.topSharers = topSharers;
        }
    }

    public static class PerformanceMetrics {
        private Long averageLoadTime;
        private Long averageRenderTime;
        private Integer averageMemoryUsage;
        private Integer averageCpuUsage;
        private Long totalErrors;
        private Double errorRate;
        private Map<String, Long> errorTypeDistribution;

        // Getters and setters
        public Long getAverageLoadTime() {
            return averageLoadTime;
        }

        public void setAverageLoadTime(Long averageLoadTime) {
            this.averageLoadTime = averageLoadTime;
        }

        public Long getAverageRenderTime() {
            return averageRenderTime;
        }

        public void setAverageRenderTime(Long averageRenderTime) {
            this.averageRenderTime = averageRenderTime;
        }

        public Integer getAverageMemoryUsage() {
            return averageMemoryUsage;
        }

        public void setAverageMemoryUsage(Integer averageMemoryUsage) {
            this.averageMemoryUsage = averageMemoryUsage;
        }

        public Integer getAverageCpuUsage() {
            return averageCpuUsage;
        }

        public void setAverageCpuUsage(Integer averageCpuUsage) {
            this.averageCpuUsage = averageCpuUsage;
        }

        public Long getTotalErrors() {
            return totalErrors;
        }

        public void setTotalErrors(Long totalErrors) {
            this.totalErrors = totalErrors;
        }

        public Double getErrorRate() {
            return errorRate;
        }

        public void setErrorRate(Double errorRate) {
            this.errorRate = errorRate;
        }

        public Map<String, Long> getErrorTypeDistribution() {
            return errorTypeDistribution;
        }

        public void setErrorTypeDistribution(Map<String, Long> errorTypeDistribution) {
            this.errorTypeDistribution = errorTypeDistribution;
        }
    }

    public static class EcommerceMetrics {
        private Long totalPurchases;
        private Long totalRevenue;
        private Long totalWishlistAdds;
        private Long totalCartAdds;
        private Double conversionRate;
        private Map<String, Long> productCategoryDistribution;
        private List<String> topProducts;
        private List<String> topCategories;

        // Getters and setters
        public Long getTotalPurchases() {
            return totalPurchases;
        }

        public void setTotalPurchases(Long totalPurchases) {
            this.totalPurchases = totalPurchases;
        }

        public Long getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(Long totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public Long getTotalWishlistAdds() {
            return totalWishlistAdds;
        }

        public void setTotalWishlistAdds(Long totalWishlistAdds) {
            this.totalWishlistAdds = totalWishlistAdds;
        }

        public Long getTotalCartAdds() {
            return totalCartAdds;
        }

        public void setTotalCartAdds(Long totalCartAdds) {
            this.totalCartAdds = totalCartAdds;
        }

        public Double getConversionRate() {
            return conversionRate;
        }

        public void setConversionRate(Double conversionRate) {
            this.conversionRate = conversionRate;
        }

        public Map<String, Long> getProductCategoryDistribution() {
            return productCategoryDistribution;
        }

        public void setProductCategoryDistribution(Map<String, Long> productCategoryDistribution) {
            this.productCategoryDistribution = productCategoryDistribution;
        }

        public List<String> getTopProducts() {
            return topProducts;
        }

        public void setTopProducts(List<String> topProducts) {
            this.topProducts = topProducts;
        }

        public List<String> getTopCategories() {
            return topCategories;
        }

        public void setTopCategories(List<String> topCategories) {
            this.topCategories = topCategories;
        }
    }

    public static class SubscriptionMetrics {
        private Long totalSubscriptions;
        private Long activeSubscriptions;
        private Double retentionRate;
        private Map<String, Long> planTypeDistribution;
        private List<String> preferredPlans;

        // Getters and setters
        public Long getTotalSubscriptions() {
            return totalSubscriptions;
        }

        public void setTotalSubscriptions(Long totalSubscriptions) {
            this.totalSubscriptions = totalSubscriptions;
        }

        public Long getActiveSubscriptions() {
            return activeSubscriptions;
        }

        public void setActiveSubscriptions(Long activeSubscriptions) {
            this.activeSubscriptions = activeSubscriptions;
        }

        public Double getRetentionRate() {
            return retentionRate;
        }

        public void setRetentionRate(Double retentionRate) {
            this.retentionRate = retentionRate;
        }

        public Map<String, Long> getPlanTypeDistribution() {
            return planTypeDistribution;
        }

        public void setPlanTypeDistribution(Map<String, Long> planTypeDistribution) {
            this.planTypeDistribution = planTypeDistribution;
        }

        public List<String> getPreferredPlans() {
            return preferredPlans;
        }

        public void setPreferredPlans(List<String> preferredPlans) {
            this.preferredPlans = preferredPlans;
        }
    }

    public static class SocialMetrics {
        private Long totalSocialInteractions;
        private Long totalSocialShares;
        private Long totalSocialMentions;
        private Map<String, Long> socialPlatformDistribution;
        private List<String> topSocialPlatforms;

        // Getters and setters
        public Long getTotalSocialInteractions() {
            return totalSocialInteractions;
        }

        public void setTotalSocialInteractions(Long totalSocialInteractions) {
            this.totalSocialInteractions = totalSocialInteractions;
        }

        public Long getTotalSocialShares() {
            return totalSocialShares;
        }

        public void setTotalSocialShares(Long totalSocialShares) {
            this.totalSocialShares = totalSocialShares;
        }

        public Long getTotalSocialMentions() {
            return totalSocialMentions;
        }

        public void setTotalSocialMentions(Long totalSocialMentions) {
            this.totalSocialMentions = totalSocialMentions;
        }

        public Map<String, Long> getSocialPlatformDistribution() {
            return socialPlatformDistribution;
        }

        public void setSocialPlatformDistribution(Map<String, Long> socialPlatformDistribution) {
            this.socialPlatformDistribution = socialPlatformDistribution;
        }

        public List<String> getTopSocialPlatforms() {
            return topSocialPlatforms;
        }

        public void setTopSocialPlatforms(List<String> topSocialPlatforms) {
            this.topSocialPlatforms = topSocialPlatforms;
        }
    }

    public static class ContentAnalytics {
        private String contentType;
        private String contentCategory;
        private List<String> contentTags;
        private String contentLanguage;
        private Integer contentLength;
        private String contentFormat;
        private String contentQuality;
        private String contentSource;
        private Boolean isOriginal;
        private String contentStatus;
        private Map<String, Long> contentTypeDistribution;
        private Map<String, Long> contentCategoryDistribution;
        private List<String> trendingTopics;
        private List<String> relatedContent;

        // Getters and setters
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

        public List<String> getContentTags() {
            return contentTags;
        }

        public void setContentTags(List<String> contentTags) {
            this.contentTags = contentTags;
        }

        public String getContentLanguage() {
            return contentLanguage;
        }

        public void setContentLanguage(String contentLanguage) {
            this.contentLanguage = contentLanguage;
        }

        public Integer getContentLength() {
            return contentLength;
        }

        public void setContentLength(Integer contentLength) {
            this.contentLength = contentLength;
        }

        public String getContentFormat() {
            return contentFormat;
        }

        public void setContentFormat(String contentFormat) {
            this.contentFormat = contentFormat;
        }

        public String getContentQuality() {
            return contentQuality;
        }

        public void setContentQuality(String contentQuality) {
            this.contentQuality = contentQuality;
        }

        public String getContentSource() {
            return contentSource;
        }

        public void setContentSource(String contentSource) {
            this.contentSource = contentSource;
        }

        public Boolean getIsOriginal() {
            return isOriginal;
        }

        public void setIsOriginal(Boolean isOriginal) {
            this.isOriginal = isOriginal;
        }

        public String getContentStatus() {
            return contentStatus;
        }

        public void setContentStatus(String contentStatus) {
            this.contentStatus = contentStatus;
        }

        public Map<String, Long> getContentTypeDistribution() {
            return contentTypeDistribution;
        }

        public void setContentTypeDistribution(Map<String, Long> contentTypeDistribution) {
            this.contentTypeDistribution = contentTypeDistribution;
        }

        public Map<String, Long> getContentCategoryDistribution() {
            return contentCategoryDistribution;
        }

        public void setContentCategoryDistribution(Map<String, Long> contentCategoryDistribution) {
            this.contentCategoryDistribution = contentCategoryDistribution;
        }

        public List<String> getTrendingTopics() {
            return trendingTopics;
        }

        public void setTrendingTopics(List<String> trendingTopics) {
            this.trendingTopics = trendingTopics;
        }

        public List<String> getRelatedContent() {
            return relatedContent;
        }

        public void setRelatedContent(List<String> relatedContent) {
            this.relatedContent = relatedContent;
        }
    }
}
