package com.raved.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ContentMetrics Document for TheRavedApp MongoDB
 *
 * Comprehensive content analytics and metrics tracking including social
 * content, ecommerce products, subscription plans, and performance indicators.
 */
@Document(collection = "content_metrics")
@CompoundIndex(name = "idx_content_type_date", def = "{'content_type': 1, 'metrics_date': 1}")
@CompoundIndex(name = "idx_content_engagement", def = "{'content_type': 1, 'engagement_rate': -1}")
@CompoundIndex(name = "idx_content_viral", def = "{'content_type': 1, 'viral_score': -1}")
@CompoundIndex(name = "idx_content_ecommerce", def = "{'content_type': 1, 'ecommerce_metrics.conversionRate': -1}")
@CompoundIndex(name = "idx_content_subscription", def = "{'content_type': 1, 'subscription_metrics.subscriptionRate': -1}")
public class ContentMetrics {

    @Id
    private String id;

    @Indexed
    @Field("content_id")
    private String contentId; // post_id, product_id, subscription_plan_id, etc.

    @Indexed
    @Field("content_type")
    private String contentType; // POST, PRODUCT, SUBSCRIPTION_PLAN, ARTICLE, VIDEO, COMMENT

    @Field("content_category")
    private String contentCategory; // e.g., "fashion", "technology", "premium", "basic"

    @Field("content_subcategory")
    private String contentSubcategory;

    @Field("content_owner_id")
    private String contentOwnerId; // user_id who created the content

    @Field("content_owner_type")
    private String contentOwnerType; // USER, BUSINESS, INFLUENCER

    // Basic Engagement Metrics
    @Indexed
    @Field("views_count")
    private Integer viewsCount;

    @Field("unique_views_count")
    private Integer uniqueViewsCount;

    @Field("likes_count")
    private Integer likesCount;

    @Field("dislikes_count")
    private Integer dislikesCount;

    @Field("comments_count")
    private Integer commentsCount;

    @Field("shares_count")
    private Integer sharesCount;

    @Field("saves_count")
    private Integer savesCount;

    @Field("bookmarks_count")
    private Integer bookmarksCount;

    // Advanced Engagement Metrics
    @Indexed
    @Field("engagement_rate")
    private BigDecimal engagementRate; // (likes + comments + shares) / views

    @Indexed
    @Field("viral_score")
    private BigDecimal viralScore; // weighted score based on shares and reach

    @Field("reach")
    private Integer reach; // unique users who saw the content

    @Field("impressions")
    private Integer impressions; // total number of times content was displayed

    @Field("click_through_rate")
    private BigDecimal clickThroughRate;

    @Field("bounce_rate")
    private BigDecimal bounceRate;

    // Content Performance Metrics
    @Field("content_quality_score")
    private BigDecimal contentQualityScore;

    @Field("content_relevance_score")
    private BigDecimal contentRelevanceScore;

    @Field("content_freshness_score")
    private BigDecimal contentFreshnessScore;

    @Field("content_completeness_score")
    private BigDecimal contentCompletenessScore;

    // E-commerce Specific Metrics (for products)
    @Field("ecommerce_metrics")
    private EcommerceMetrics ecommerceMetrics;

    // Subscription Specific Metrics (for subscription plans)
    @Field("subscription_metrics")
    private SubscriptionMetrics subscriptionMetrics;

    // Social Content Metrics (for posts, comments)
    @Field("social_metrics")
    private SocialMetrics socialMetrics;

    // Content Analytics
    @Field("content_analytics")
    private ContentAnalytics contentAnalytics;

    // Time-based Metrics
    @Indexed
    @Field("peak_engagement_time")
    private LocalDateTime peakEngagementTime;

    @Indexed
    @Field("metrics_date")
    private String metricsDate; // YYYY-MM-DD format

    @Field("metrics_hour")
    private Integer metricsHour; // 0-23 for hourly analytics

    @Field("metrics_weekday")
    private Integer metricsWeekday; // 1-7 for weekly patterns

    @Field("metrics_month")
    private Integer metricsMonth; // 1-12 for monthly trends

    // Timeline
    @Field("last_calculated_at")
    private LocalDateTime lastCalculatedAt;

    @Field("created_at")
    private LocalDateTime createdAt;

    // Constructors
    public ContentMetrics() {
        this.createdAt = LocalDateTime.now();
        this.lastCalculatedAt = LocalDateTime.now();
        this.metricsDate = LocalDateTime.now().toLocalDate().toString();
        this.metricsHour = LocalDateTime.now().getHour();
        this.metricsWeekday = LocalDateTime.now().getDayOfWeek().getValue();
        this.metricsMonth = LocalDateTime.now().getMonthValue();
        this.viewsCount = 0;
        this.likesCount = 0;
        this.commentsCount = 0;
        this.sharesCount = 0;
        this.engagementRate = BigDecimal.ZERO;
        this.viralScore = BigDecimal.ZERO;
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

    public String getContentSubcategory() {
        return contentSubcategory;
    }

    public void setContentSubcategory(String contentSubcategory) {
        this.contentSubcategory = contentSubcategory;
    }

    public String getContentOwnerId() {
        return contentOwnerId;
    }

    public void setContentOwnerId(String contentOwnerId) {
        this.contentOwnerId = contentOwnerId;
    }

    public String getContentOwnerType() {
        return contentOwnerType;
    }

    public void setContentOwnerType(String contentOwnerType) {
        this.contentOwnerType = contentOwnerType;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getUniqueViewsCount() {
        return uniqueViewsCount;
    }

    public void setUniqueViewsCount(Integer uniqueViewsCount) {
        this.uniqueViewsCount = uniqueViewsCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(Integer dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getSharesCount() {
        return sharesCount;
    }

    public void setSharesCount(Integer sharesCount) {
        this.sharesCount = sharesCount;
    }

    public Integer getSavesCount() {
        return savesCount;
    }

    public void setSavesCount(Integer savesCount) {
        this.savesCount = savesCount;
    }

    public Integer getBookmarksCount() {
        return bookmarksCount;
    }

    public void setBookmarksCount(Integer bookmarksCount) {
        this.bookmarksCount = bookmarksCount;
    }

    public BigDecimal getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(BigDecimal engagementRate) {
        this.engagementRate = engagementRate;
    }

    public BigDecimal getViralScore() {
        return viralScore;
    }

    public void setViralScore(BigDecimal viralScore) {
        this.viralScore = viralScore;
    }

    public Integer getReach() {
        return reach;
    }

    public void setReach(Integer reach) {
        this.reach = reach;
    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public BigDecimal getClickThroughRate() {
        return clickThroughRate;
    }

    public void setClickThroughRate(BigDecimal clickThroughRate) {
        this.clickThroughRate = clickThroughRate;
    }

    public BigDecimal getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(BigDecimal bounceRate) {
        this.bounceRate = bounceRate;
    }

    public BigDecimal getContentQualityScore() {
        return contentQualityScore;
    }

    public void setContentQualityScore(BigDecimal contentQualityScore) {
        this.contentQualityScore = contentQualityScore;
    }

    public BigDecimal getContentRelevanceScore() {
        return contentRelevanceScore;
    }

    public void setContentRelevanceScore(BigDecimal contentRelevanceScore) {
        this.contentRelevanceScore = contentRelevanceScore;
    }

    public BigDecimal getContentFreshnessScore() {
        return contentFreshnessScore;
    }

    public void setContentFreshnessScore(BigDecimal contentFreshnessScore) {
        this.contentFreshnessScore = contentFreshnessScore;
    }

    public BigDecimal getContentCompletenessScore() {
        return contentCompletenessScore;
    }

    public void setContentCompletenessScore(BigDecimal contentCompletenessScore) {
        this.contentCompletenessScore = contentCompletenessScore;
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

    public LocalDateTime getPeakEngagementTime() {
        return peakEngagementTime;
    }

    public void setPeakEngagementTime(LocalDateTime peakEngagementTime) {
        this.peakEngagementTime = peakEngagementTime;
    }

    public String getMetricsDate() {
        return metricsDate;
    }

    public void setMetricsDate(String metricsDate) {
        this.metricsDate = metricsDate;
    }

    public Integer getMetricsHour() {
        return metricsHour;
    }

    public void setMetricsHour(Integer metricsHour) {
        this.metricsHour = metricsHour;
    }

    public Integer getMetricsWeekday() {
        return metricsWeekday;
    }

    public void setMetricsWeekday(Integer metricsWeekday) {
        this.metricsWeekday = metricsWeekday;
    }

    public Integer getMetricsMonth() {
        return metricsMonth;
    }

    public void setMetricsMonth(Integer metricsMonth) {
        this.metricsMonth = metricsMonth;
    }

    public LocalDateTime getLastCalculatedAt() {
        return lastCalculatedAt;
    }

    public void setLastCalculatedAt(LocalDateTime lastCalculatedAt) {
        this.lastCalculatedAt = lastCalculatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Inner Classes for Enhanced Content Analytics
    public static class EcommerceMetrics {

        private BigDecimal conversionRate;
        private BigDecimal revenueGenerated;
        private Integer addToCartCount;
        private Integer purchaseCount;
        private BigDecimal averageOrderValue;
        private Integer wishlistAdds;
        private BigDecimal costPerClick;
        private BigDecimal returnOnAdSpend;
        private Map<String, Integer> conversionsBySource;
        private Map<String, BigDecimal> revenueBySource;
        private List<String> topReferringPages;
        private Map<String, Integer> performanceByDevice;

        // Getters and setters
        public BigDecimal getConversionRate() {
            return conversionRate;
        }

        public void setConversionRate(BigDecimal conversionRate) {
            this.conversionRate = conversionRate;
        }

        public BigDecimal getRevenueGenerated() {
            return revenueGenerated;
        }

        public void setRevenueGenerated(BigDecimal revenueGenerated) {
            this.revenueGenerated = revenueGenerated;
        }

        public Integer getAddToCartCount() {
            return addToCartCount;
        }

        public void setAddToCartCount(Integer addToCartCount) {
            this.addToCartCount = addToCartCount;
        }

        public Integer getPurchaseCount() {
            return purchaseCount;
        }

        public void setPurchaseCount(Integer purchaseCount) {
            this.purchaseCount = purchaseCount;
        }

        public BigDecimal getAverageOrderValue() {
            return averageOrderValue;
        }

        public void setAverageOrderValue(BigDecimal averageOrderValue) {
            this.averageOrderValue = averageOrderValue;
        }

        public Integer getWishlistAdds() {
            return wishlistAdds;
        }

        public void setWishlistAdds(Integer wishlistAdds) {
            this.wishlistAdds = wishlistAdds;
        }

        public BigDecimal getCostPerClick() {
            return costPerClick;
        }

        public void setCostPerClick(BigDecimal costPerClick) {
            this.costPerClick = costPerClick;
        }

        public BigDecimal getReturnOnAdSpend() {
            return returnOnAdSpend;
        }

        public void setReturnOnAdSpend(BigDecimal returnOnAdSpend) {
            this.returnOnAdSpend = returnOnAdSpend;
        }

        public Map<String, Integer> getConversionsBySource() {
            return conversionsBySource;
        }

        public void setConversionsBySource(Map<String, Integer> conversionsBySource) {
            this.conversionsBySource = conversionsBySource;
        }

        public Map<String, BigDecimal> getRevenueBySource() {
            return revenueBySource;
        }

        public void setRevenueBySource(Map<String, BigDecimal> revenueBySource) {
            this.revenueBySource = revenueBySource;
        }

        public List<String> getTopReferringPages() {
            return topReferringPages;
        }

        public void setTopReferringPages(List<String> topReferringPages) {
            this.topReferringPages = topReferringPages;
        }

        public Map<String, Integer> getPerformanceByDevice() {
            return performanceByDevice;
        }

        public void setPerformanceByDevice(Map<String, Integer> performanceByDevice) {
            this.performanceByDevice = performanceByDevice;
        }
    }

    public static class SubscriptionMetrics {

        private BigDecimal subscriptionRate;
        private Integer subscriptionConversions;
        private BigDecimal averageSubscriptionValue;
        private Integer trialSignups;
        private Integer trialConversions;
        private BigDecimal trialToPaidConversionRate;
        private Integer planUpgrades;
        private Integer planDowngrades;
        private BigDecimal customerLifetimeValue;
        private Map<String, Integer> subscriptionsByPlan;
        private Map<String, BigDecimal> revenueByPlan;
        private List<String> topConversionSources;

        // Getters and setters
        public BigDecimal getSubscriptionRate() {
            return subscriptionRate;
        }

        public void setSubscriptionRate(BigDecimal subscriptionRate) {
            this.subscriptionRate = subscriptionRate;
        }

        public Integer getSubscriptionConversions() {
            return subscriptionConversions;
        }

        public void setSubscriptionConversions(Integer subscriptionConversions) {
            this.subscriptionConversions = subscriptionConversions;
        }

        public BigDecimal getAverageSubscriptionValue() {
            return averageSubscriptionValue;
        }

        public void setAverageSubscriptionValue(BigDecimal averageSubscriptionValue) {
            this.averageSubscriptionValue = averageSubscriptionValue;
        }

        public Integer getTrialSignups() {
            return trialSignups;
        }

        public void setTrialSignups(Integer trialSignups) {
            this.trialSignups = trialSignups;
        }

        public Integer getTrialConversions() {
            return trialConversions;
        }

        public void setTrialConversions(Integer trialConversions) {
            this.trialConversions = trialConversions;
        }

        public BigDecimal getTrialToPaidConversionRate() {
            return trialToPaidConversionRate;
        }

        public void setTrialToPaidConversionRate(BigDecimal trialToPaidConversionRate) {
            this.trialToPaidConversionRate = trialToPaidConversionRate;
        }

        public Integer getPlanUpgrades() {
            return planUpgrades;
        }

        public void setPlanUpgrades(Integer planUpgrades) {
            this.planUpgrades = planUpgrades;
        }

        public Integer getPlanDowngrades() {
            return planDowngrades;
        }

        public void setPlanDowngrades(Integer planDowngrades) {
            this.planDowngrades = planDowngrades;
        }

        public BigDecimal getCustomerLifetimeValue() {
            return customerLifetimeValue;
        }

        public void setCustomerLifetimeValue(BigDecimal customerLifetimeValue) {
            this.customerLifetimeValue = customerLifetimeValue;
        }

        public Map<String, Integer> getSubscriptionsByPlan() {
            return subscriptionsByPlan;
        }

        public void setSubscriptionsByPlan(Map<String, Integer> subscriptionsByPlan) {
            this.subscriptionsByPlan = subscriptionsByPlan;
        }

        public Map<String, BigDecimal> getRevenueByPlan() {
            return revenueByPlan;
        }

        public void setRevenueByPlan(Map<String, BigDecimal> revenueByPlan) {
            this.revenueByPlan = revenueByPlan;
        }

        public List<String> getTopConversionSources() {
            return topConversionSources;
        }

        public void setTopConversionSources(List<String> topConversionSources) {
            this.topConversionSources = topConversionSources;
        }
    }

    public static class SocialMetrics {

        private Integer postReach;
        private Integer postImpressions;
        private BigDecimal postEngagementRate;
        private Integer hashtagClicks;
        private Integer profileVisits;
        private Integer linkClicks;
        private Map<String, Integer> engagementByPlatform;
        private List<String> topHashtags;
        private List<String> topMentions;
        private Map<String, Integer> performanceByAudience;

        // Getters and setters
        public Integer getPostReach() {
            return postReach;
        }

        public void setPostReach(Integer postReach) {
            this.postReach = postReach;
        }

        public Integer getPostImpressions() {
            return postImpressions;
        }

        public void setPostImpressions(Integer postImpressions) {
            this.postImpressions = postImpressions;
        }

        public BigDecimal getPostEngagementRate() {
            return postEngagementRate;
        }

        public void setPostEngagementRate(BigDecimal postEngagementRate) {
            this.postEngagementRate = postEngagementRate;
        }

        public Integer getHashtagClicks() {
            return hashtagClicks;
        }

        public void setHashtagClicks(Integer hashtagClicks) {
            this.hashtagClicks = hashtagClicks;
        }

        public Integer getProfileVisits() {
            return profileVisits;
        }

        public void setProfileVisits(Integer profileVisits) {
            this.profileVisits = profileVisits;
        }

        public Integer getLinkClicks() {
            return linkClicks;
        }

        public void setLinkClicks(Integer linkClicks) {
            this.linkClicks = linkClicks;
        }

        public Map<String, Integer> getEngagementByPlatform() {
            return engagementByPlatform;
        }

        public void setEngagementByPlatform(Map<String, Integer> engagementByPlatform) {
            this.engagementByPlatform = engagementByPlatform;
        }

        public List<String> getTopHashtags() {
            return topHashtags;
        }

        public void setTopHashtags(List<String> topHashtags) {
            this.topHashtags = topHashtags;
        }

        public List<String> getTopMentions() {
            return topMentions;
        }

        public void setTopMentions(List<String> topMentions) {
            this.topMentions = topMentions;
        }

        public Map<String, Integer> getPerformanceByAudience() {
            return performanceByAudience;
        }

        public void setPerformanceByAudience(Map<String, Integer> performanceByAudience) {
            this.performanceByAudience = performanceByAudience;
        }
    }

    public static class ContentAnalytics {

        private String contentLanguage;
        private String contentFormat;
        private Integer contentLength;
        private String contentQuality;
        private String contentSource;
        private Boolean isOriginal;
        private String contentStatus;
        private List<String> contentTags;
        private Map<String, Integer> performanceByTag;
        private Map<String, BigDecimal> engagementByCategory;
        private List<String> relatedContent;
        private Map<String, Integer> performanceByTimeSlot;

        // Getters and setters
        public String getContentLanguage() {
            return contentLanguage;
        }

        public void setContentLanguage(String contentLanguage) {
            this.contentLanguage = contentLanguage;
        }

        public String getContentFormat() {
            return contentFormat;
        }

        public void setContentFormat(String contentFormat) {
            this.contentFormat = contentFormat;
        }

        public Integer getContentLength() {
            return contentLength;
        }

        public void setContentLength(Integer contentLength) {
            this.contentLength = contentLength;
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

        public List<String> getContentTags() {
            return contentTags;
        }

        public void setContentTags(List<String> contentTags) {
            this.contentTags = contentTags;
        }

        public Map<String, Integer> getPerformanceByTag() {
            return performanceByTag;
        }

        public void setPerformanceByTag(Map<String, Integer> performanceByTag) {
            this.performanceByTag = performanceByTag;
        }

        public Map<String, BigDecimal> getEngagementByCategory() {
            return engagementByCategory;
        }

        public void setEngagementByCategory(Map<String, BigDecimal> engagementByCategory) {
            this.engagementByCategory = engagementByCategory;
        }

        public List<String> getRelatedContent() {
            return relatedContent;
        }

        public void setRelatedContent(List<String> relatedContent) {
            this.relatedContent = relatedContent;
        }

        public Map<String, Integer> getPerformanceByTimeSlot() {
            return performanceByTimeSlot;
        }

        public void setPerformanceByTimeSlot(Map<String, Integer> performanceByTimeSlot) {
            this.performanceByTimeSlot = performanceByTimeSlot;
        }
    }

    // Additional getters and setters for service compatibility
    public LocalDateTime getUpdatedAt() {
        return lastCalculatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.lastCalculatedAt = updatedAt;
    }

    // Engagement metrics getters/setters using direct fields
    public Long getViewCount() {
        return viewsCount != null ? Long.valueOf(viewsCount) : 0L;
    }

    public void setViewCount(long viewCount) {
        this.viewsCount = (int) viewCount;
    }

    public Long getLikeCount() {
        return likesCount != null ? Long.valueOf(likesCount) : 0L;
    }

    public void setLikeCount(long likeCount) {
        this.likesCount = (int) likeCount;
    }

    public Long getCommentCount() {
        return commentsCount != null ? Long.valueOf(commentsCount) : 0L;
    }

    public void setCommentCount(long commentCount) {
        this.commentsCount = (int) commentCount;
    }

    public Long getShareCount() {
        return sharesCount != null ? Long.valueOf(sharesCount) : 0L;
    }

    public void setShareCount(long shareCount) {
        this.sharesCount = (int) shareCount;
    }

    public BigDecimal getEngagementScore() {
        return engagementRate != null ? engagementRate : BigDecimal.ZERO;
    }

    public void setEngagementScore(BigDecimal engagementScore) {
        this.engagementRate = engagementScore;
    }

    public void setEngagementScore(double engagementScore) {
        this.engagementRate = BigDecimal.valueOf(engagementScore);
    }

    public Long getReachCount() {
        return reach != null ? Long.valueOf(reach) : 0L;
    }

    public void setReachCount(long reachCount) {
        this.reach = (int) reachCount;
    }

    public Long getImpressionCount() {
        return impressions != null ? Long.valueOf(impressions) : 0L;
    }

    public void setImpressionCount(long impressionCount) {
        this.impressions = (int) impressionCount;
    }


}
