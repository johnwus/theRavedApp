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
 * UserMetrics Document for TheRavedApp MongoDB
 *
 * Comprehensive user analytics and metrics tracking including social
 * engagement, ecommerce activities, subscription behavior, and performance
 * indicators.
 */
@Document(collection = "user_metrics")
@CompoundIndex(name = "idx_user_engagement", def = "{'user_id': 1, 'engagement_score': -1}")
@CompoundIndex(name = "idx_user_influence", def = "{'user_id': 1, 'influence_score': -1}")
@CompoundIndex(name = "idx_user_segment", def = "{'user_segment': 1, 'engagement_score': -1}")
@CompoundIndex(name = "idx_user_ecommerce", def = "{'user_id': 1, 'ecommerce_metrics.totalSalesAmount': -1}")
@CompoundIndex(name = "idx_user_subscription", def = "{'user_id': 1, 'subscription_metrics.subscriptionValue': -1}")
public class UserMetrics {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("user_id")
    private String userId; // Reference to user service

    // User Profile & Segmentation
    @Indexed
    @Field("user_segment")
    private String userSegment; // "premium", "regular", "new", "churned"

    @Field("user_tier")
    private String userTier; // "bronze", "silver", "gold", "platinum"

    @Field("is_premium")
    private Boolean isPremium;

    @Field("registration_date")
    private LocalDateTime registrationDate;

    @Field("last_active_date")
    private LocalDateTime lastActiveDate;

    // Social Engagement Metrics
    @Field("social_metrics")
    private SocialMetrics socialMetrics;

    // E-commerce Metrics
    @Field("ecommerce_metrics")
    private EcommerceMetrics ecommerceMetrics;

    // Subscription Metrics
    @Field("subscription_metrics")
    private SubscriptionMetrics subscriptionMetrics;

    // Content Creation Metrics
    @Field("content_metrics")
    private ContentMetrics contentMetrics;

    // Activity & Engagement Metrics
    @Field("activity_metrics")
    private ActivityMetrics activityMetrics;

    // Performance & Quality Metrics
    @Field("performance_metrics")
    private PerformanceMetrics performanceMetrics;

    // Behavioral Patterns
    @Field("behavioral_patterns")
    private BehavioralPatterns behavioralPatterns;

    // Calculated Scores
    @Indexed
    @Field("engagement_score")
    private BigDecimal engagementScore;

    @Indexed
    @Field("influence_score")
    private BigDecimal influenceScore;

    @Field("retention_score")
    private BigDecimal retentionScore;

    @Field("monetization_score")
    private BigDecimal monetizationScore;

    // Timeline
    @Field("last_calculated_at")
    private LocalDateTime lastCalculatedAt;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public UserMetrics() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastCalculatedAt = LocalDateTime.now();
        this.engagementScore = BigDecimal.ZERO;
        this.influenceScore = BigDecimal.ZERO;
        this.retentionScore = BigDecimal.ZERO;
        this.monetizationScore = BigDecimal.ZERO;
    }

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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
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

    public BigDecimal getEngagementScore() {
        return engagementScore;
    }

    public void setEngagementScore(BigDecimal engagementScore) {
        this.engagementScore = engagementScore;
    }

    public BigDecimal getInfluenceScore() {
        return influenceScore;
    }

    public void setInfluenceScore(BigDecimal influenceScore) {
        this.influenceScore = influenceScore;
    }

    public BigDecimal getRetentionScore() {
        return retentionScore;
    }

    public void setRetentionScore(BigDecimal retentionScore) {
        this.retentionScore = retentionScore;
    }

    public BigDecimal getMonetizationScore() {
        return monetizationScore;
    }

    public void setMonetizationScore(BigDecimal monetizationScore) {
        this.monetizationScore = monetizationScore;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Inner Classes for Comprehensive Metrics
    public static class SocialMetrics {

        private Integer postsCount;
        private Integer likesGiven;
        private Integer likesReceived;
        private Integer commentsGiven;
        private Integer commentsReceived;
        private Integer sharesGiven;
        private Integer sharesReceived;
        private Integer followersCount;
        private Integer followingCount;
        private Integer connectionsCount;
        private Integer groupsJoined;
        private Integer eventsAttended;
        private BigDecimal socialReach;
        private BigDecimal socialEngagement;
        private List<String> topHashtags;
        private List<String> topMentions;
        private Map<String, Integer> interactionByType;

        // Getters and setters
        public Integer getPostsCount() {
            return postsCount;
        }

        public void setPostsCount(Integer postsCount) {
            this.postsCount = postsCount;
        }

        public Integer getLikesGiven() {
            return likesGiven;
        }

        public void setLikesGiven(Integer likesGiven) {
            this.likesGiven = likesGiven;
        }

        public Integer getLikesReceived() {
            return likesReceived;
        }

        public void setLikesReceived(Integer likesReceived) {
            this.likesReceived = likesReceived;
        }

        public Integer getCommentsGiven() {
            return commentsGiven;
        }

        public void setCommentsGiven(Integer commentsGiven) {
            this.commentsGiven = commentsGiven;
        }

        public Integer getCommentsReceived() {
            return commentsReceived;
        }

        public void setCommentsReceived(Integer commentsReceived) {
            this.commentsReceived = commentsReceived;
        }

        public Integer getSharesGiven() {
            return sharesGiven;
        }

        public void setSharesGiven(Integer sharesGiven) {
            this.sharesGiven = sharesGiven;
        }

        public Integer getSharesReceived() {
            return sharesReceived;
        }

        public void setSharesReceived(Integer sharesReceived) {
            this.sharesReceived = sharesReceived;
        }

        public Integer getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(Integer followersCount) {
            this.followersCount = followersCount;
        }

        public Integer getFollowingCount() {
            return followingCount;
        }

        public void setFollowingCount(Integer followingCount) {
            this.followingCount = followingCount;
        }

        public Integer getConnectionsCount() {
            return connectionsCount;
        }

        public void setConnectionsCount(Integer connectionsCount) {
            this.connectionsCount = connectionsCount;
        }

        public Integer getGroupsJoined() {
            return groupsJoined;
        }

        public void setGroupsJoined(Integer groupsJoined) {
            this.groupsJoined = groupsJoined;
        }

        public Integer getEventsAttended() {
            return eventsAttended;
        }

        public void setEventsAttended(Integer eventsAttended) {
            this.eventsAttended = eventsAttended;
        }

        public BigDecimal getSocialReach() {
            return socialReach;
        }

        public void setSocialReach(BigDecimal socialReach) {
            this.socialReach = socialReach;
        }

        public BigDecimal getSocialEngagement() {
            return socialEngagement;
        }

        public void setSocialEngagement(BigDecimal socialEngagement) {
            this.socialEngagement = socialEngagement;
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

        public Map<String, Integer> getInteractionByType() {
            return interactionByType;
        }

        public void setInteractionByType(Map<String, Integer> interactionByType) {
            this.interactionByType = interactionByType;
        }
    }

    public static class EcommerceMetrics {

        private Integer productsSold;
        private Integer productsBought;
        private BigDecimal totalSalesAmount;
        private BigDecimal totalPurchasesAmount;
        private BigDecimal totalRefundAmount;
        private Integer ordersPlaced;
        private Integer ordersReceived;
        private BigDecimal averageOrderValue;
        private BigDecimal averagePurchaseValue;
        private Integer cartAbandonments;
        private BigDecimal cartAbandonmentRate;
        private Integer wishlistItems;
        private Integer reviewsGiven;
        private Integer reviewsReceived;
        private BigDecimal averageRating;
        private List<String> topCategories;
        private List<String> topBrands;
        private Map<String, Integer> purchasesByCategory;
        private Map<String, BigDecimal> revenueByCategory;
        private LocalDateTime lastPurchaseDate;
        private LocalDateTime lastSaleDate;

        // Getters and setters
        public Integer getProductsSold() {
            return productsSold;
        }

        public void setProductsSold(Integer productsSold) {
            this.productsSold = productsSold;
        }

        public Integer getProductsBought() {
            return productsBought;
        }

        public void setProductsBought(Integer productsBought) {
            this.productsBought = productsBought;
        }

        public BigDecimal getTotalSalesAmount() {
            return totalSalesAmount;
        }

        public void setTotalSalesAmount(BigDecimal totalSalesAmount) {
            this.totalSalesAmount = totalSalesAmount;
        }

        public BigDecimal getTotalPurchasesAmount() {
            return totalPurchasesAmount;
        }

        public void setTotalPurchasesAmount(BigDecimal totalPurchasesAmount) {
            this.totalPurchasesAmount = totalPurchasesAmount;
        }

        public BigDecimal getTotalRefundAmount() {
            return totalRefundAmount;
        }

        public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
            this.totalRefundAmount = totalRefundAmount;
        }

        public Integer getOrdersPlaced() {
            return ordersPlaced;
        }

        public void setOrdersPlaced(Integer ordersPlaced) {
            this.ordersPlaced = ordersPlaced;
        }

        public Integer getOrdersReceived() {
            return ordersReceived;
        }

        public void setOrdersReceived(Integer ordersReceived) {
            this.ordersReceived = ordersReceived;
        }

        public BigDecimal getAverageOrderValue() {
            return averageOrderValue;
        }

        public void setAverageOrderValue(BigDecimal averageOrderValue) {
            this.averageOrderValue = averageOrderValue;
        }

        public BigDecimal getAveragePurchaseValue() {
            return averagePurchaseValue;
        }

        public void setAveragePurchaseValue(BigDecimal averagePurchaseValue) {
            this.averagePurchaseValue = averagePurchaseValue;
        }

        public Integer getCartAbandonments() {
            return cartAbandonments;
        }

        public void setCartAbandonments(Integer cartAbandonments) {
            this.cartAbandonments = cartAbandonments;
        }

        public BigDecimal getCartAbandonmentRate() {
            return cartAbandonmentRate;
        }

        public void setCartAbandonmentRate(BigDecimal cartAbandonmentRate) {
            this.cartAbandonmentRate = cartAbandonmentRate;
        }

        public Integer getWishlistItems() {
            return wishlistItems;
        }

        public void setWishlistItems(Integer wishlistItems) {
            this.wishlistItems = wishlistItems;
        }

        public Integer getReviewsGiven() {
            return reviewsGiven;
        }

        public void setReviewsGiven(Integer reviewsGiven) {
            this.reviewsGiven = reviewsGiven;
        }

        public Integer getReviewsReceived() {
            return reviewsReceived;
        }

        public void setReviewsReceived(Integer reviewsReceived) {
            this.reviewsReceived = reviewsReceived;
        }

        public BigDecimal getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(BigDecimal averageRating) {
            this.averageRating = averageRating;
        }

        public List<String> getTopCategories() {
            return topCategories;
        }

        public void setTopCategories(List<String> topCategories) {
            this.topCategories = topCategories;
        }

        public List<String> getTopBrands() {
            return topBrands;
        }

        public void setTopBrands(List<String> topBrands) {
            this.topBrands = topBrands;
        }

        public Map<String, Integer> getPurchasesByCategory() {
            return purchasesByCategory;
        }

        public void setPurchasesByCategory(Map<String, Integer> purchasesByCategory) {
            this.purchasesByCategory = purchasesByCategory;
        }

        public Map<String, BigDecimal> getRevenueByCategory() {
            return revenueByCategory;
        }

        public void setRevenueByCategory(Map<String, BigDecimal> revenueByCategory) {
            this.revenueByCategory = revenueByCategory;
        }

        public LocalDateTime getLastPurchaseDate() {
            return lastPurchaseDate;
        }

        public void setLastPurchaseDate(LocalDateTime lastPurchaseDate) {
            this.lastPurchaseDate = lastPurchaseDate;
        }

        public LocalDateTime getLastSaleDate() {
            return lastSaleDate;
        }

        public void setLastSaleDate(LocalDateTime lastSaleDate) {
            this.lastSaleDate = lastSaleDate;
        }
    }

    public static class SubscriptionMetrics {

        private Integer activeSubscriptions;
        private Integer totalSubscriptions;
        private BigDecimal subscriptionValue;
        private BigDecimal monthlyRecurringRevenue;
        private BigDecimal annualRecurringRevenue;
        private Integer subscriptionUpgrades;
        private Integer subscriptionDowngrades;
        private Integer subscriptionCancellations;
        private BigDecimal churnRate;
        private BigDecimal retentionRate;
        private LocalDateTime firstSubscriptionDate;
        private LocalDateTime lastSubscriptionDate;
        private String preferredBillingCycle;
        private String preferredPaymentMethod;
        private List<String> activePlanTypes;
        private Map<String, Integer> subscriptionsByPlan;
        private Map<String, BigDecimal> revenueByPlan;

        // Getters and setters
        public Integer getActiveSubscriptions() {
            return activeSubscriptions;
        }

        public void setActiveSubscriptions(Integer activeSubscriptions) {
            this.activeSubscriptions = activeSubscriptions;
        }

        public Integer getTotalSubscriptions() {
            return totalSubscriptions;
        }

        public void setTotalSubscriptions(Integer totalSubscriptions) {
            this.totalSubscriptions = totalSubscriptions;
        }

        public BigDecimal getSubscriptionValue() {
            return subscriptionValue;
        }

        public void setSubscriptionValue(BigDecimal subscriptionValue) {
            this.subscriptionValue = subscriptionValue;
        }

        public BigDecimal getMonthlyRecurringRevenue() {
            return monthlyRecurringRevenue;
        }

        public void setMonthlyRecurringRevenue(BigDecimal monthlyRecurringRevenue) {
            this.monthlyRecurringRevenue = monthlyRecurringRevenue;
        }

        public BigDecimal getAnnualRecurringRevenue() {
            return annualRecurringRevenue;
        }

        public void setAnnualRecurringRevenue(BigDecimal annualRecurringRevenue) {
            this.annualRecurringRevenue = annualRecurringRevenue;
        }

        public Integer getSubscriptionUpgrades() {
            return subscriptionUpgrades;
        }

        public void setSubscriptionUpgrades(Integer subscriptionUpgrades) {
            this.subscriptionUpgrades = subscriptionUpgrades;
        }

        public Integer getSubscriptionDowngrades() {
            return subscriptionDowngrades;
        }

        public void setSubscriptionDowngrades(Integer subscriptionDowngrades) {
            this.subscriptionDowngrades = subscriptionDowngrades;
        }

        public Integer getSubscriptionCancellations() {
            return subscriptionCancellations;
        }

        public void setSubscriptionCancellations(Integer subscriptionCancellations) {
            this.subscriptionCancellations = subscriptionCancellations;
        }

        public BigDecimal getChurnRate() {
            return churnRate;
        }

        public void setChurnRate(BigDecimal churnRate) {
            this.churnRate = churnRate;
        }

        public BigDecimal getRetentionRate() {
            return retentionRate;
        }

        public void setRetentionRate(BigDecimal retentionRate) {
            this.retentionRate = retentionRate;
        }

        public LocalDateTime getFirstSubscriptionDate() {
            return firstSubscriptionDate;
        }

        public void setFirstSubscriptionDate(LocalDateTime firstSubscriptionDate) {
            this.firstSubscriptionDate = firstSubscriptionDate;
        }

        public LocalDateTime getLastSubscriptionDate() {
            return lastSubscriptionDate;
        }

        public void setLastSubscriptionDate(LocalDateTime lastSubscriptionDate) {
            this.lastSubscriptionDate = lastSubscriptionDate;
        }

        public String getPreferredBillingCycle() {
            return preferredBillingCycle;
        }

        public void setPreferredBillingCycle(String preferredBillingCycle) {
            this.preferredBillingCycle = preferredBillingCycle;
        }

        public String getPreferredPaymentMethod() {
            return preferredPaymentMethod;
        }

        public void setPreferredPaymentMethod(String preferredPaymentMethod) {
            this.preferredPaymentMethod = preferredPaymentMethod;
        }

        public List<String> getActivePlanTypes() {
            return activePlanTypes;
        }

        public void setActivePlanTypes(List<String> activePlanTypes) {
            this.activePlanTypes = activePlanTypes;
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
    }

    public static class ContentMetrics {

        private Integer totalContent;
        private Integer publishedContent;
        private Integer draftContent;
        private Integer archivedContent;
        private Map<String, Integer> contentByType;
        private Map<String, Integer> contentByCategory;
        private BigDecimal averageContentRating;
        private Integer totalContentViews;
        private Integer totalContentLikes;
        private Integer totalContentComments;
        private Integer totalContentShares;
        private BigDecimal contentEngagementRate;
        private List<String> topContentTags;
        private Map<String, Integer> contentPerformanceByType;

        // Getters and setters
        public Integer getTotalContent() {
            return totalContent;
        }

        public void setTotalContent(Integer totalContent) {
            this.totalContent = totalContent;
        }

        public Integer getPublishedContent() {
            return publishedContent;
        }

        public void setPublishedContent(Integer publishedContent) {
            this.publishedContent = publishedContent;
        }

        public Integer getDraftContent() {
            return draftContent;
        }

        public void setDraftContent(Integer draftContent) {
            this.draftContent = draftContent;
        }

        public Integer getArchivedContent() {
            return archivedContent;
        }

        public void setArchivedContent(Integer archivedContent) {
            this.archivedContent = archivedContent;
        }

        public Map<String, Integer> getContentByType() {
            return contentByType;
        }

        public void setContentByType(Map<String, Integer> contentByType) {
            this.contentByType = contentByType;
        }

        public Map<String, Integer> getContentByCategory() {
            return contentByCategory;
        }

        public void setContentByCategory(Map<String, Integer> contentByCategory) {
            this.contentByCategory = contentByCategory;
        }

        public BigDecimal getAverageContentRating() {
            return averageContentRating;
        }

        public void setAverageContentRating(BigDecimal averageContentRating) {
            this.averageContentRating = averageContentRating;
        }

        public Integer getTotalContentViews() {
            return totalContentViews;
        }

        public void setTotalContentViews(Integer totalContentViews) {
            this.totalContentViews = totalContentViews;
        }

        public Integer getTotalContentLikes() {
            return totalContentLikes;
        }

        public void setTotalContentLikes(Integer totalContentLikes) {
            this.totalContentLikes = totalContentLikes;
        }

        public Integer getTotalContentComments() {
            return totalContentComments;
        }

        public void setTotalContentComments(Integer totalContentComments) {
            this.totalContentComments = totalContentComments;
        }

        public Integer getTotalContentShares() {
            return totalContentShares;
        }

        public void setTotalContentShares(Integer totalContentShares) {
            this.totalContentShares = totalContentShares;
        }

        public BigDecimal getContentEngagementRate() {
            return contentEngagementRate;
        }

        public void setContentEngagementRate(BigDecimal contentEngagementRate) {
            this.contentEngagementRate = contentEngagementRate;
        }

        public List<String> getTopContentTags() {
            return topContentTags;
        }

        public void setTopContentTags(List<String> topContentTags) {
            this.topContentTags = topContentTags;
        }

        public Map<String, Integer> getContentPerformanceByType() {
            return contentPerformanceByType;
        }

        public void setContentPerformanceByType(Map<String, Integer> contentPerformanceByType) {
            this.contentPerformanceByType = contentPerformanceByType;
        }
    }

    public static class ActivityMetrics {

        private Integer loginStreak;
        private Integer totalSessions;
        private Long totalTimeSpent; // in seconds
        private BigDecimal averageSessionDuration;
        private Integer totalPageViews;
        private Integer totalActions;
        private LocalDateTime lastLoginDate;
        private LocalDateTime lastLogoutDate;
        private Map<String, Integer> actionsByType;
        private Map<String, Integer> pageViewsByPage;
        private List<String> mostVisitedPages;
        private Map<String, Integer> activityByHour;
        private Map<String, Integer> activityByDay;

        // Getters and setters
        public Integer getLoginStreak() {
            return loginStreak;
        }

        public void setLoginStreak(Integer loginStreak) {
            this.loginStreak = loginStreak;
        }

        public Integer getTotalSessions() {
            return totalSessions;
        }

        public void setTotalSessions(Integer totalSessions) {
            this.totalSessions = totalSessions;
        }

        public Long getTotalTimeSpent() {
            return totalTimeSpent;
        }

        public void setTotalTimeSpent(Long totalTimeSpent) {
            this.totalTimeSpent = totalTimeSpent;
        }

        public BigDecimal getAverageSessionDuration() {
            return averageSessionDuration;
        }

        public void setAverageSessionDuration(BigDecimal averageSessionDuration) {
            this.averageSessionDuration = averageSessionDuration;
        }

        public Integer getTotalPageViews() {
            return totalPageViews;
        }

        public void setTotalPageViews(Integer totalPageViews) {
            this.totalPageViews = totalPageViews;
        }

        public Integer getTotalActions() {
            return totalActions;
        }

        public void setTotalActions(Integer totalActions) {
            this.totalActions = totalActions;
        }

        public LocalDateTime getLastLoginDate() {
            return lastLoginDate;
        }

        public void setLastLoginDate(LocalDateTime lastLoginDate) {
            this.lastLoginDate = lastLoginDate;
        }

        public LocalDateTime getLastLogoutDate() {
            return lastLogoutDate;
        }

        public void setLastLogoutDate(LocalDateTime lastLogoutDate) {
            this.lastLogoutDate = lastLogoutDate;
        }

        public Map<String, Integer> getActionsByType() {
            return actionsByType;
        }

        public void setActionsByType(Map<String, Integer> actionsByType) {
            this.actionsByType = actionsByType;
        }

        public Map<String, Integer> getPageViewsByPage() {
            return pageViewsByPage;
        }

        public void setPageViewsByPage(Map<String, Integer> pageViewsByPage) {
            this.pageViewsByPage = pageViewsByPage;
        }

        public List<String> getMostVisitedPages() {
            return mostVisitedPages;
        }

        public void setMostVisitedPages(List<String> mostVisitedPages) {
            this.mostVisitedPages = mostVisitedPages;
        }

        public Map<String, Integer> getActivityByHour() {
            return activityByHour;
        }

        public void setActivityByHour(Map<String, Integer> activityByHour) {
            this.activityByHour = activityByHour;
        }

        public Map<String, Integer> getActivityByDay() {
            return activityByDay;
        }

        public void setActivityByDay(Map<String, Integer> activityByDay) {
            this.activityByDay = activityByDay;
        }
    }

    public static class PerformanceMetrics {

        private BigDecimal responseTime;
        private BigDecimal loadTime;
        private BigDecimal renderTime;
        private Integer memoryUsage;
        private Integer cpuUsage;
        private Integer errorCount;
        private BigDecimal errorRate;
        private Integer cacheHitRate;
        private Integer apiCallSuccessRate;
        private Map<String, BigDecimal> performanceByFeature;

        // Getters and setters
        public BigDecimal getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(BigDecimal responseTime) {
            this.responseTime = responseTime;
        }

        public BigDecimal getLoadTime() {
            return loadTime;
        }

        public void setLoadTime(BigDecimal loadTime) {
            this.loadTime = loadTime;
        }

        public BigDecimal getRenderTime() {
            return renderTime;
        }

        public void setRenderTime(BigDecimal renderTime) {
            this.renderTime = renderTime;
        }

        public Integer getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(Integer memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public Integer getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(Integer cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public Integer getErrorCount() {
            return errorCount;
        }

        public void setErrorCount(Integer errorCount) {
            this.errorCount = errorCount;
        }

        public BigDecimal getErrorRate() {
            return errorRate;
        }

        public void setErrorRate(BigDecimal errorRate) {
            this.errorRate = errorRate;
        }

        public Integer getCacheHitRate() {
            return cacheHitRate;
        }

        public void setCacheHitRate(Integer cacheHitRate) {
            this.cacheHitRate = cacheHitRate;
        }

        public Integer getApiCallSuccessRate() {
            return apiCallSuccessRate;
        }

        public void setApiCallSuccessRate(Integer apiCallSuccessRate) {
            this.apiCallSuccessRate = apiCallSuccessRate;
        }

        public Map<String, BigDecimal> getPerformanceByFeature() {
            return performanceByFeature;
        }

        public void setPerformanceByFeature(Map<String, BigDecimal> performanceByFeature) {
            this.performanceByFeature = performanceByFeature;
        }
    }

    public static class BehavioralPatterns {

        private String preferredContentType;
        private String preferredInteractionTime;
        private String preferredDevice;
        private String preferredPlatform;
        private List<String> interests;
        private List<String> preferences;
        private Map<String, Integer> behaviorFrequency;
        private Map<String, BigDecimal> behaviorScores;
        private String userPersona;
        private String engagementStyle;

        // Getters and setters
        public String getPreferredContentType() {
            return preferredContentType;
        }

        public void setPreferredContentType(String preferredContentType) {
            this.preferredContentType = preferredContentType;
        }

        public String getPreferredInteractionTime() {
            return preferredInteractionTime;
        }

        public void setPreferredInteractionTime(String preferredInteractionTime) {
            this.preferredInteractionTime = preferredInteractionTime;
        }

        public String getPreferredDevice() {
            return preferredDevice;
        }

        public void setPreferredDevice(String preferredDevice) {
            this.preferredDevice = preferredDevice;
        }

        public String getPreferredPlatform() {
            return preferredPlatform;
        }

        public void setPreferredPlatform(String preferredPlatform) {
            this.preferredPlatform = preferredPlatform;
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

        public Map<String, Integer> getBehaviorFrequency() {
            return behaviorFrequency;
        }

        public void setBehaviorFrequency(Map<String, Integer> behaviorFrequency) {
            this.behaviorFrequency = behaviorFrequency;
        }

        public Map<String, BigDecimal> getBehaviorScores() {
            return behaviorScores;
        }

        public void setBehaviorScores(Map<String, BigDecimal> behaviorScores) {
            this.behaviorScores = behaviorScores;
        }

        public String getUserPersona() {
            return userPersona;
        }

        public void setUserPersona(String userPersona) {
            this.userPersona = userPersona;
        }

        public String getEngagementStyle() {
            return engagementStyle;
        }

        public void setEngagementStyle(String engagementStyle) {
            this.engagementStyle = engagementStyle;
        }
    }

    // Additional getters and setters for service compatibility
    public LocalDateTime getLastActiveAt() {
        return lastActiveDate;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveDate = lastActiveAt;
    }

    public Long getTotalPosts() {
        return socialMetrics != null ? Long.valueOf(socialMetrics.getPostsCount() != null ? socialMetrics.getPostsCount() : 0) : 0L;
    }

    public void setTotalPosts(long totalPosts) {
        if (socialMetrics == null) {
            socialMetrics = new SocialMetrics();
        }
        socialMetrics.setPostsCount((int) totalPosts);
    }

    public Long getTotalLikes() {
        return socialMetrics != null ? Long.valueOf(socialMetrics.getLikesReceived() != null ? socialMetrics.getLikesReceived() : 0) : 0L;
    }

    public void setTotalLikes(long totalLikes) {
        if (socialMetrics == null) {
            socialMetrics = new SocialMetrics();
        }
        socialMetrics.setLikesReceived((int) totalLikes);
    }

    public Long getTotalComments() {
        return socialMetrics != null ? Long.valueOf(socialMetrics.getCommentsReceived() != null ? socialMetrics.getCommentsReceived() : 0) : 0L;
    }

    public void setTotalComments(long totalComments) {
        if (socialMetrics == null) {
            socialMetrics = new SocialMetrics();
        }
        socialMetrics.setCommentsReceived((int) totalComments);
    }

    public Long getTotalShares() {
        return socialMetrics != null ? Long.valueOf(socialMetrics.getSharesReceived() != null ? socialMetrics.getSharesReceived() : 0) : 0L;
    }

    public void setTotalShares(long totalShares) {
        if (socialMetrics == null) {
            socialMetrics = new SocialMetrics();
        }
        socialMetrics.setSharesReceived((int) totalShares);
    }

    public Long getTotalViews() {
        // Views are not directly tracked in SocialMetrics, return 0 for now
        return 0L;
    }

    public void setTotalViews(long totalViews) {
        // Views are not directly tracked in SocialMetrics, do nothing for now
    }

    public Long getTotalFollowers() {
        return socialMetrics != null ? Long.valueOf(socialMetrics.getFollowersCount() != null ? socialMetrics.getFollowersCount() : 0) : 0L;
    }

    public void setTotalFollowers(long totalFollowers) {
        if (socialMetrics == null) {
            socialMetrics = new SocialMetrics();
        }
        socialMetrics.setFollowersCount((int) totalFollowers);
    }

    public Long getTotalFollowing() {
        return socialMetrics != null ? Long.valueOf(socialMetrics.getFollowingCount() != null ? socialMetrics.getFollowingCount() : 0) : 0L;
    }

    public void setTotalFollowing(long totalFollowing) {
        if (socialMetrics == null) {
            socialMetrics = new SocialMetrics();
        }
        socialMetrics.setFollowingCount((int) totalFollowing);
    }

    public BigDecimal getEngagementRate() {
        return socialMetrics != null ? (socialMetrics.getSocialEngagement() != null ? socialMetrics.getSocialEngagement() : BigDecimal.ZERO) : BigDecimal.ZERO;
    }

    public void setEngagementRate(BigDecimal engagementRate) {
        if (socialMetrics == null) {
            socialMetrics = new SocialMetrics();
        }
        socialMetrics.setSocialEngagement(engagementRate);
    }

    public void setEngagementRate(double engagementRate) {
        setEngagementRate(BigDecimal.valueOf(engagementRate));
    }
}
