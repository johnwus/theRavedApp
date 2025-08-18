package com.raved.analytics.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Response DTO for user metrics with comprehensive analytics
 * 
 * Enhanced response DTO supporting e-commerce, subscription, social,
 * content, and performance analytics for MongoDB-driven analytics service.
 */
public class UserMetricsResponse {

    private String id;
    private String userId;

    // Basic social metrics
    private Long totalPosts;
    private Long totalLikes;
    private Long totalComments;
    private Long totalShares;
    private Long totalViews;
    private Long totalFollowers;
    private Long totalFollowing;
    private Double engagementRate;

    // Enhanced social metrics
    private SocialMetrics socialMetrics;

    // E-commerce metrics
    private EcommerceMetrics ecommerceMetrics;

    // Subscription metrics
    private SubscriptionMetrics subscriptionMetrics;

    // Content metrics
    private ContentMetrics contentMetrics;

    // Activity metrics
    private ActivityMetrics activityMetrics;

    // Performance metrics
    private PerformanceMetrics performanceMetrics;

    // Behavioral patterns
    private BehavioralPatterns behavioralPatterns;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Constructors
    public UserMetricsResponse() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(Long totalPosts) {
        this.totalPosts = totalPosts;
    }

    public Long getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Long totalLikes) {
        this.totalLikes = totalLikes;
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

    public Long getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Long totalViews) {
        this.totalViews = totalViews;
    }

    public Long getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(Long totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public Long getTotalFollowing() {
        return totalFollowing;
    }

    public void setTotalFollowing(Long totalFollowing) {
        this.totalFollowing = totalFollowing;
    }

    public Double getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(Double engagementRate) {
        this.engagementRate = engagementRate;
    }

    public SocialMetrics getSocialMetrics() {
        return socialMetrics;
    }

    public void setSocialMetrics(SocialMetrics socialMetrics) {
        this.socialMetrics = socialMetrics;
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

    public ContentMetrics getContentMetrics() {
        return contentMetrics;
    }

    public void setContentMetrics(ContentMetrics contentMetrics) {
        this.contentMetrics = contentMetrics;
    }

    public ActivityMetrics getActivityMetrics() {
        return activityMetrics;
    }

    public void setActivityMetrics(ActivityMetrics activityMetrics) {
        this.activityMetrics = activityMetrics;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public BehavioralPatterns getBehavioralPatterns() {
        return behavioralPatterns;
    }

    public void setBehavioralPatterns(BehavioralPatterns behavioralPatterns) {
        this.behavioralPatterns = behavioralPatterns;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
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

    // Nested classes for comprehensive metrics
    public static class SocialMetrics {
        private Long totalInteractions;
        private Long totalReactions;
        private Long totalMentions;
        private Long totalHashtags;
        private Double viralCoefficient;
        private Long reach;
        private Long impressions;
        private Map<String, Long> postTypeDistribution;
        private Map<String, Long> interactionTypeDistribution;
        private List<String> topHashtags;
        private List<String> topMentions;

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

        public Long getTotalMentions() {
            return totalMentions;
        }

        public void setTotalMentions(Long totalMentions) {
            this.totalMentions = totalMentions;
        }

        public Long getTotalHashtags() {
            return totalHashtags;
        }

        public void setTotalHashtags(Long totalHashtags) {
            this.totalHashtags = totalHashtags;
        }

        public Double getViralCoefficient() {
            return viralCoefficient;
        }

        public void setViralCoefficient(Double viralCoefficient) {
            this.viralCoefficient = viralCoefficient;
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

        public Map<String, Long> getPostTypeDistribution() {
            return postTypeDistribution;
        }

        public void setPostTypeDistribution(Map<String, Long> postTypeDistribution) {
            this.postTypeDistribution = postTypeDistribution;
        }

        public Map<String, Long> getInteractionTypeDistribution() {
            return interactionTypeDistribution;
        }

        public void setInteractionTypeDistribution(Map<String, Long> interactionTypeDistribution) {
            this.interactionTypeDistribution = interactionTypeDistribution;
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
    }

    public static class EcommerceMetrics {
        private Long totalOrders;
        private Long totalProducts;
        private BigDecimal totalSpent;
        private BigDecimal averageOrderValue;
        private Long totalWishlistItems;
        private Long totalCartItems;
        private Double conversionRate;
        private Long totalReviews;
        private Double averageRating;
        private Map<String, Long> categoryPreferences;
        private Map<String, Long> brandPreferences;
        private List<String> favoriteCategories;
        private List<String> favoriteBrands;

        // Getters and setters
        public Long getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(Long totalOrders) {
            this.totalOrders = totalOrders;
        }

        public Long getTotalProducts() {
            return totalProducts;
        }

        public void setTotalProducts(Long totalProducts) {
            this.totalProducts = totalProducts;
        }

        public BigDecimal getTotalSpent() {
            return totalSpent;
        }

        public void setTotalSpent(BigDecimal totalSpent) {
            this.totalSpent = totalSpent;
        }

        public BigDecimal getAverageOrderValue() {
            return averageOrderValue;
        }

        public void setAverageOrderValue(BigDecimal averageOrderValue) {
            this.averageOrderValue = averageOrderValue;
        }

        public Long getTotalWishlistItems() {
            return totalWishlistItems;
        }

        public void setTotalWishlistItems(Long totalWishlistItems) {
            this.totalWishlistItems = totalWishlistItems;
        }

        public Long getTotalCartItems() {
            return totalCartItems;
        }

        public void setTotalCartItems(Long totalCartItems) {
            this.totalCartItems = totalCartItems;
        }

        public Double getConversionRate() {
            return conversionRate;
        }

        public void setConversionRate(Double conversionRate) {
            this.conversionRate = conversionRate;
        }

        public Long getTotalReviews() {
            return totalReviews;
        }

        public void setTotalReviews(Long totalReviews) {
            this.totalReviews = totalReviews;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }

        public Map<String, Long> getCategoryPreferences() {
            return categoryPreferences;
        }

        public void setCategoryPreferences(Map<String, Long> categoryPreferences) {
            this.categoryPreferences = categoryPreferences;
        }

        public Map<String, Long> getBrandPreferences() {
            return brandPreferences;
        }

        public void setBrandPreferences(Map<String, Long> brandPreferences) {
            this.brandPreferences = brandPreferences;
        }

        public List<String> getFavoriteCategories() {
            return favoriteCategories;
        }

        public void setFavoriteCategories(List<String> favoriteCategories) {
            this.favoriteCategories = favoriteCategories;
        }

        public List<String> getFavoriteBrands() {
            return favoriteBrands;
        }

        public void setFavoriteBrands(List<String> favoriteBrands) {
            this.favoriteBrands = favoriteBrands;
        }
    }

    public static class SubscriptionMetrics {
        private Long totalSubscriptions;
        private Long activeSubscriptions;
        private Long cancelledSubscriptions;
        private BigDecimal totalSpentOnSubscriptions;
        private Double retentionRate;
        private Long averageSubscriptionDuration;
        private Map<String, Long> planTypeDistribution;
        private Map<String, Long> subscriptionStatusDistribution;
        private List<String> preferredPlans;
        private Double upgradeRate;
        private Double downgradeRate;

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

        public Long getCancelledSubscriptions() {
            return cancelledSubscriptions;
        }

        public void setCancelledSubscriptions(Long cancelledSubscriptions) {
            this.cancelledSubscriptions = cancelledSubscriptions;
        }

        public BigDecimal getTotalSpentOnSubscriptions() {
            return totalSpentOnSubscriptions;
        }

        public void setTotalSpentOnSubscriptions(BigDecimal totalSpentOnSubscriptions) {
            this.totalSpentOnSubscriptions = totalSpentOnSubscriptions;
        }

        public Double getRetentionRate() {
            return retentionRate;
        }

        public void setRetentionRate(Double retentionRate) {
            this.retentionRate = retentionRate;
        }

        public Long getAverageSubscriptionDuration() {
            return averageSubscriptionDuration;
        }

        public void setAverageSubscriptionDuration(Long averageSubscriptionDuration) {
            this.averageSubscriptionDuration = averageSubscriptionDuration;
        }

        public Map<String, Long> getPlanTypeDistribution() {
            return planTypeDistribution;
        }

        public void setPlanTypeDistribution(Map<String, Long> planTypeDistribution) {
            this.planTypeDistribution = planTypeDistribution;
        }

        public Map<String, Long> getSubscriptionStatusDistribution() {
            return subscriptionStatusDistribution;
        }

        public void setSubscriptionStatusDistribution(Map<String, Long> subscriptionStatusDistribution) {
            this.subscriptionStatusDistribution = subscriptionStatusDistribution;
        }

        public List<String> getPreferredPlans() {
            return preferredPlans;
        }

        public void setPreferredPlans(List<String> preferredPlans) {
            this.preferredPlans = preferredPlans;
        }

        public Double getUpgradeRate() {
            return upgradeRate;
        }

        public void setUpgradeRate(Double upgradeRate) {
            this.upgradeRate = upgradeRate;
        }

        public Double getDowngradeRate() {
            return downgradeRate;
        }

        public void setDowngradeRate(Double downgradeRate) {
            this.downgradeRate = downgradeRate;
        }
    }

    public static class ContentMetrics {
        private Long totalContent;
        private Long totalViews;
        private Long totalDownloads;
        private Double averageRating;
        private Long totalReviews;
        private Map<String, Long> contentTypeDistribution;
        private Map<String, Long> contentCategoryDistribution;
        private List<String> topContentTypes;
        private List<String> topCategories;
        private Double contentEngagementRate;

        // Getters and setters
        public Long getTotalContent() {
            return totalContent;
        }

        public void setTotalContent(Long totalContent) {
            this.totalContent = totalContent;
        }

        public Long getTotalViews() {
            return totalViews;
        }

        public void setTotalViews(Long totalViews) {
            this.totalViews = totalViews;
        }

        public Long getTotalDownloads() {
            return totalDownloads;
        }

        public void setTotalDownloads(Long totalDownloads) {
            this.totalDownloads = totalDownloads;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }

        public Long getTotalReviews() {
            return totalReviews;
        }

        public void setTotalReviews(Long totalReviews) {
            this.totalReviews = totalReviews;
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

        public List<String> getTopContentTypes() {
            return topContentTypes;
        }

        public void setTopContentTypes(List<String> topContentTypes) {
            this.topContentTypes = topContentTypes;
        }

        public List<String> getTopCategories() {
            return topCategories;
        }

        public void setTopCategories(List<String> topCategories) {
            this.topCategories = topCategories;
        }

        public Double getContentEngagementRate() {
            return contentEngagementRate;
        }

        public void setContentEngagementRate(Double contentEngagementRate) {
            this.contentEngagementRate = contentEngagementRate;
        }
    }

    public static class ActivityMetrics {
        private Long totalSessions;
        private Long totalPageViews;
        private Long totalClicks;
        private Double averageSessionDuration;
        private Long totalActions;
        private Map<String, Long> actionTypeDistribution;
        private Map<String, Long> pageTypeDistribution;
        private List<String> mostVisitedPages;
        private List<String> mostPerformedActions;

        // Getters and setters
        public Long getTotalSessions() {
            return totalSessions;
        }

        public void setTotalSessions(Long totalSessions) {
            this.totalSessions = totalSessions;
        }

        public Long getTotalPageViews() {
            return totalPageViews;
        }

        public void setTotalPageViews(Long totalPageViews) {
            this.totalPageViews = totalPageViews;
        }

        public Long getTotalClicks() {
            return totalClicks;
        }

        public void setTotalClicks(Long totalClicks) {
            this.totalClicks = totalClicks;
        }

        public Double getAverageSessionDuration() {
            return averageSessionDuration;
        }

        public void setAverageSessionDuration(Double averageSessionDuration) {
            this.averageSessionDuration = averageSessionDuration;
        }

        public Long getTotalActions() {
            return totalActions;
        }

        public void setTotalActions(Long totalActions) {
            this.totalActions = totalActions;
        }

        public Map<String, Long> getActionTypeDistribution() {
            return actionTypeDistribution;
        }

        public void setActionTypeDistribution(Map<String, Long> actionTypeDistribution) {
            this.actionTypeDistribution = actionTypeDistribution;
        }

        public Map<String, Long> getPageTypeDistribution() {
            return pageTypeDistribution;
        }

        public void setPageTypeDistribution(Map<String, Long> pageTypeDistribution) {
            this.pageTypeDistribution = pageTypeDistribution;
        }

        public List<String> getMostVisitedPages() {
            return mostVisitedPages;
        }

        public void setMostVisitedPages(List<String> mostVisitedPages) {
            this.mostVisitedPages = mostVisitedPages;
        }

        public List<String> getMostPerformedActions() {
            return mostPerformedActions;
        }

        public void setMostPerformedActions(List<String> mostPerformedActions) {
            this.mostPerformedActions = mostPerformedActions;
        }
    }

    public static class PerformanceMetrics {
        private Long averageResponseTime;
        private Long averageLoadTime;
        private Long averageRenderTime;
        private Integer averageMemoryUsage;
        private Integer averageCpuUsage;
        private Long totalErrors;
        private Double errorRate;
        private Map<String, Long> errorTypeDistribution;

        // Getters and setters
        public Long getAverageResponseTime() {
            return averageResponseTime;
        }

        public void setAverageResponseTime(Long averageResponseTime) {
            this.averageResponseTime = averageResponseTime;
        }

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

    public static class BehavioralPatterns {
        private String userSegment;
        private String userTier;
        private Boolean isPremium;
        private String referralSource;
        private String campaignId;
        private Map<String, Double> behaviorScores;
        private List<String> interests;
        private List<String> preferences;
        private Map<String, Long> timePatterns;
        private Map<String, Long> devicePatterns;

        // Getters and setters
        public String getUserSegment() {
            return userSegment;
        }

        public void setUserSegment(String userSegment) {
            this.userSegment = userSegment;
        }

        public String getUserTier() {
            return userTier;
        }

        public void setUserTier(String userTier) {
            this.userTier = userTier;
        }

        public Boolean getIsPremium() {
            return isPremium;
        }

        public void setIsPremium(Boolean isPremium) {
            this.isPremium = isPremium;
        }

        public String getReferralSource() {
            return referralSource;
        }

        public void setReferralSource(String referralSource) {
            this.referralSource = referralSource;
        }

        public String getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(String campaignId) {
            this.campaignId = campaignId;
        }

        public Map<String, Double> getBehaviorScores() {
            return behaviorScores;
        }

        public void setBehaviorScores(Map<String, Double> behaviorScores) {
            this.behaviorScores = behaviorScores;
        }

        public List<String> getInterests() {
            return interests;
        }

        public void setInterests(List<String> interests) {
            this.interests = interests;
        }

        public List<String> getPreferences() {
            return preferences;
        }

        public void setPreferences(List<String> preferences) {
            this.preferences = preferences;
        }

        public Map<String, Long> getTimePatterns() {
            return timePatterns;
        }

        public void setTimePatterns(Map<String, Long> timePatterns) {
            this.timePatterns = timePatterns;
        }

        public Map<String, Long> getDevicePatterns() {
            return devicePatterns;
        }

        public void setDevicePatterns(Map<String, Long> devicePatterns) {
            this.devicePatterns = devicePatterns;
        }
    }
}
