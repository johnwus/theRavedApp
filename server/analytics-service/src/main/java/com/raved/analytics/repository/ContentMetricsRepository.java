package com.raved.analytics.repository;

import com.raved.analytics.model.ContentMetrics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;

/**
 * MongoDB Repository for Content Metrics
 *
 * Provides data access methods for content analytics and metrics with enhanced
 * querying capabilities for social content, ecommerce products, and
 * subscription plans.
 */
@Repository
public interface ContentMetricsRepository extends MongoRepository<ContentMetrics, String> {

    // Basic CRUD Operations
    Optional<ContentMetrics> findById(String id);

    List<ContentMetrics> findAll();

    // TODO: Add saveAll method
    // List<ContentMetrics> saveAll(Iterable<ContentMetrics> entities);
    // Content Identification Queries
    List<ContentMetrics> findByContentId(String contentId);

    List<ContentMetrics> findByContentType(String contentType);

    List<ContentMetrics> findByContentCategory(String contentCategory);

    List<ContentMetrics> findByContentSubcategory(String contentSubcategory);

    List<ContentMetrics> findByContentOwnerId(String contentOwnerId);

    List<ContentMetrics> findByContentOwnerType(String contentOwnerType);

    // Content Type and Category Combinations
    List<ContentMetrics> findByContentTypeAndContentCategory(String contentType, String contentCategory);

    List<ContentMetrics> findByContentTypeAndContentSubcategory(String contentType, String contentSubcategory);

    List<ContentMetrics> findByContentCategoryAndContentSubcategory(String contentCategory,
            String contentSubcategory);

    // Engagement Metrics Queries
    List<ContentMetrics> findByViewsCountGreaterThan(Integer count);

    List<ContentMetrics> findByLikesCountGreaterThan(Integer count);

    List<ContentMetrics> findByCommentsCountGreaterThan(Integer count);

    List<ContentMetrics> findBySharesCountGreaterThan(Integer count);

    List<ContentMetrics> findBySavesCountGreaterThan(Integer count);

    // Engagement Rate Queries
    List<ContentMetrics> findByEngagementRateGreaterThan(BigDecimal rate);

    List<ContentMetrics> findByEngagementRateBetween(BigDecimal minRate, BigDecimal maxRate);

    List<ContentMetrics> findByEngagementRateGreaterThanOrderByEngagementRateDesc(BigDecimal rate);

    // Viral Score Queries
    List<ContentMetrics> findByViralScoreGreaterThan(BigDecimal score);

    List<ContentMetrics> findByViralScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    List<ContentMetrics> findByViralScoreGreaterThanOrderByViralScoreDesc(BigDecimal score);

    // Reach and Impressions Queries
    List<ContentMetrics> findByReachGreaterThan(Integer reach);

    List<ContentMetrics> findByImpressionsGreaterThan(Integer impressions);

    List<ContentMetrics> findByReachBetween(Integer minReach, Integer maxReach);

    // Click Through Rate Queries
    List<ContentMetrics> findByClickThroughRateGreaterThan(BigDecimal rate);

    List<ContentMetrics> findByClickThroughRateBetween(BigDecimal minRate, BigDecimal maxRate);

    // Content Quality Score Queries
    List<ContentMetrics> findByContentQualityScoreGreaterThan(BigDecimal score);

    List<ContentMetrics> findByContentQualityScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    List<ContentMetrics> findByContentQualityScoreGreaterThanOrderByContentQualityScoreDesc(BigDecimal score);

    // Content Relevance Score Queries
    List<ContentMetrics> findByContentRelevanceScoreGreaterThan(BigDecimal score);

    List<ContentMetrics> findByContentRelevanceScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    // Missing methods needed by MetricsService
    @Query("{}")
    long getTotalViews();

    @Query("{}")
    long getTotalLikes();

    @Query("{}")
    long getTotalComments();

    @Query("{}")
    long getTotalShares();

    @Query(value = "{}", sort = "{'engagement_rate': -1}")
    Page<ContentMetrics> findTopByEngagementScore(Pageable pageable);

    // Missing methods needed by ReportService
    @Query(value = "{'createdAt': {$gte: ?0, $lte: ?1}}", count = true)
    long countContentCreatedBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "{'createdAt': {$gte: ?0, $lte: ?1}}", fields = "{'viewsCount': 1}")
    List<ContentMetrics> findContentBetween(LocalDateTime start, LocalDateTime end);

    default long getTotalViewsBetween(LocalDateTime start, LocalDateTime end) {
        List<ContentMetrics> content = findContentBetween(start, end);
        return content.stream()
                .filter(c -> c.getViewsCount() != null)
                .mapToLong(c -> c.getViewsCount().longValue())
                .sum();
    }

    default long getTotalLikesBetween(LocalDateTime start, LocalDateTime end) {
        List<ContentMetrics> content = findContentBetween(start, end);
        return content.stream()
                .filter(c -> c.getLikesCount() != null)
                .mapToLong(c -> c.getLikesCount().longValue())
                .sum();
    }

    default long getTotalCommentsBetween(LocalDateTime start, LocalDateTime end) {
        List<ContentMetrics> content = findContentBetween(start, end);
        return content.stream()
                .filter(c -> c.getCommentsCount() != null)
                .mapToLong(c -> c.getCommentsCount().longValue())
                .sum();
    }

    default long getTotalSharesBetween(LocalDateTime start, LocalDateTime end) {
        List<ContentMetrics> content = findContentBetween(start, end);
        return content.stream()
                .filter(c -> c.getSharesCount() != null)
                .mapToLong(c -> c.getSharesCount().longValue())
                .sum();
    }

    default List<Object[]> getTopContentByEngagement(int limit) {
        Page<ContentMetrics> topContent = findTopByEngagementScore(PageRequest.of(0, limit));
        return topContent.getContent().stream()
                .map(content -> new Object[]{
            content.getContentId(),
            content.getContentType(),
            content.getViewsCount(),
            content.getLikesCount(),
            content.getCommentsCount(),
            content.getEngagementRate()
        })
                .collect(Collectors.toList());
    }

    // Additional methods needed by services
    @Query("{'createdAt': {$gte: ?0}}")
    List<Object[]> getContentWithMetrics(LocalDateTime since, int limit);

    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Object[]> getContentWithMetrics(LocalDateTime start, LocalDateTime end, int limit);

    @Query("{'contentCategory': ?0, 'createdAt': {$gte: ?1}}")
    List<Object[]> getContentWithMetricsByCategory(String category, LocalDateTime since, int limit);

    @Query("{'createdAt': {$gte: ?0}}")
    long countTrendingContent(LocalDateTime since);

    @Query("{'createdAt': {$gte: ?0}}")
    long countContentCreatedAfter(LocalDateTime since);

    @Query("{'createdAt': {$gte: ?0}}")
    List<Object[]> getTopTrendingCategories(LocalDateTime since, int limit);

    @Query("{'engagementScore': {$gte: 0.7}, 'createdAt': {$gte: ?0}}")
    List<Object[]> getHighEngagementContent(LocalDateTime since, int limit);

    @Query("{'createdAt': {$gte: ?0}}")
    List<Object[]> getTrendingContentWithCreationTime(LocalDateTime since);

    @Query("{'contentId': ?0}")
    void updateTrendingScore(String contentId, double trendingScore);

    // Content Freshness Score Queries
    List<ContentMetrics> findByContentFreshnessScoreGreaterThan(BigDecimal score);

    List<ContentMetrics> findByContentFreshnessScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    // Content Completeness Score Queries
    List<ContentMetrics> findByContentCompletenessScoreGreaterThan(BigDecimal score);

    List<ContentMetrics> findByContentCompletenessScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    // Time-based Queries
    List<ContentMetrics> findByMetricsDate(String metricsDate);

    List<ContentMetrics> findByMetricsDateAndMetricsHour(String metricsDate, Integer metricsHour);

    List<ContentMetrics> findByMetricsMonthAndMetricsWeekday(Integer month, Integer weekday);

    List<ContentMetrics> findByPeakEngagementTimeBetween(LocalDateTime start, LocalDateTime end);

    // Last Calculated Date Queries
    List<ContentMetrics> findByLastCalculatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<ContentMetrics> findByLastCalculatedAtBefore(LocalDateTime date);

    List<ContentMetrics> findByLastCalculatedAtAfter(LocalDateTime date);

    // E-commerce Metrics Queries
    @Query("{'ecommerce_metrics.conversionRate': {$gte: ?0}}")
    List<ContentMetrics> findByEcommerceConversionRateGreaterThan(BigDecimal rate);

    @Query("{'ecommerce_metrics.revenueGenerated': {$gte: ?0}}")
    List<ContentMetrics> findByEcommerceRevenueGreaterThan(BigDecimal revenue);

    @Query("{'ecommerce_metrics.addToCartCount': {$gte: ?0}}")
    List<ContentMetrics> findByEcommerceAddToCartCountGreaterThan(Integer count);

    @Query("{'ecommerce_metrics.purchaseCount': {$gte: ?0}}")
    List<ContentMetrics> findByEcommercePurchaseCountGreaterThan(Integer count);

    @Query("{'ecommerce_metrics.averageOrderValue': {$gte: ?0}}")
    List<ContentMetrics> findByEcommerceAverageOrderValueGreaterThan(BigDecimal value);

    @Query("{'ecommerce_metrics.wishlistAdds': {$gte: ?0}}")
    List<ContentMetrics> findByEcommerceWishlistAddsGreaterThan(Integer count);

    // Subscription Metrics Queries
    @Query("{'subscription_metrics.subscriptionRate': {$gte: ?0}}")
    List<ContentMetrics> findBySubscriptionRateGreaterThan(BigDecimal rate);

    @Query("{'subscription_metrics.subscriptionConversions': {$gte: ?0}}")
    List<ContentMetrics> findBySubscriptionConversionsGreaterThan(Integer count);

    @Query("{'subscription_metrics.averageSubscriptionValue': {$gte: ?0}}")
    List<ContentMetrics> findBySubscriptionAverageValueGreaterThan(BigDecimal value);

    @Query("{'subscription_metrics.trialSignups': {$gte: ?0}}")
    List<ContentMetrics> findBySubscriptionTrialSignupsGreaterThan(Integer count);

    @Query("{'subscription_metrics.customerLifetimeValue': {$gte: ?0}}")
    List<ContentMetrics> findBySubscriptionLifetimeValueGreaterThan(BigDecimal value);

    // Social Metrics Queries
    @Query("{'social_metrics.postEngagementRate': {$gte: ?0}}")
    List<ContentMetrics> findBySocialPostEngagementRateGreaterThan(BigDecimal rate);

    @Query("{'social_metrics.postReach': {$gte: ?0}}")
    List<ContentMetrics> findBySocialPostReachGreaterThan(Integer reach);

    @Query("{'social_metrics.postImpressions': {$gte: ?0}}")
    List<ContentMetrics> findBySocialPostImpressionsGreaterThan(Integer impressions);

    @Query("{'social_metrics.hashtagClicks': {$gte: ?0}}")
    List<ContentMetrics> findBySocialHashtagClicksGreaterThan(Integer clicks);

    @Query("{'social_metrics.profileVisits': {$gte: ?0}}")
    List<ContentMetrics> findBySocialProfileVisitsGreaterThan(Integer visits);

    // Content Analytics Queries
    List<ContentMetrics> findByContentAnalyticsContentLanguage(String language);

    List<ContentMetrics> findByContentAnalyticsContentFormat(String format);

    List<ContentMetrics> findByContentAnalyticsContentQuality(String quality);

    List<ContentMetrics> findByContentAnalyticsContentSource(String source);

    List<ContentMetrics> findByContentAnalyticsIsOriginal(Boolean isOriginal);

    List<ContentMetrics> findByContentAnalyticsContentStatus(String status);

    // Complex Combined Queries
    @Query("{'content_type': ?0, 'engagement_rate': {$gte: ?1}, 'viral_score': {$gte: ?2}}")
    List<ContentMetrics> findByTypeAndMinScores(String contentType, BigDecimal minEngagement, BigDecimal minViral);

    @Query("{'content_type': 'PRODUCT', 'ecommerce_metrics.conversionRate': {$gte: ?0}, 'engagement_rate': {$gte: ?1}}")
    List<ContentMetrics> findHighConvertingEngagingProducts(BigDecimal minConversionRate,
            BigDecimal minEngagementRate);

    @Query("{'content_type': 'SUBSCRIPTION_PLAN', 'subscription_metrics.subscriptionRate': {$gte: ?0}, 'viral_score': {$gte: ?1}}")
    List<ContentMetrics> findHighConvertingViralSubscriptionPlans(BigDecimal minSubscriptionRate,
            BigDecimal minViralScore);

    @Query("{'content_type': 'POST', 'social_metrics.postEngagementRate': {$gte: ?0}, 'content_quality_score': {$gte: ?1}}")
    List<ContentMetrics> findHighQualityEngagingPosts(BigDecimal minEngagementRate, BigDecimal minQualityScore);

    @Query("{'content_type': ?0, 'content_category': ?1, 'engagement_rate': {$gte: ?2}, 'content_quality_score': {$gte: ?3}}")
    List<ContentMetrics> findHighQualityEngagingContentByTypeAndCategory(String contentType, String category,
            BigDecimal minEngagement, BigDecimal minQuality);

    // Top Performers Queries
    @Query(value = "{}", sort = "{'engagement_rate': -1}")
    List<ContentMetrics> findTopContentByEngagement(Pageable pageable);

    @Query(value = "{}", sort = "{'viral_score': -1}")
    List<ContentMetrics> findTopContentByViralScore(Pageable pageable);

    @Query(value = "{}", sort = "{'views_count': -1}")
    List<ContentMetrics> findTopContentByViews(Pageable pageable);

    @Query(value = "{}", sort = "{'likes_count': -1}")
    List<ContentMetrics> findTopContentByLikes(Pageable pageable);

    @Query(value = "{}", sort = "{'shares_count': -1}")
    List<ContentMetrics> findTopContentByShares(Pageable pageable);

    // E-commerce Top Performers
    @Query(value = "{}", sort = "{'ecommerce_metrics.conversionRate': -1}")
    List<ContentMetrics> findTopContentByEcommerceConversion(Pageable pageable);

    @Query(value = "{}", sort = "{'ecommerce_metrics.revenueGenerated': -1}")
    List<ContentMetrics> findTopContentByEcommerceRevenue(Pageable pageable);

    @Query(value = "{}", sort = "{'ecommerce_metrics.addToCartCount': -1}")
    List<ContentMetrics> findTopContentByEcommerceAddToCart(Pageable pageable);

    // Subscription Top Performers
    @Query(value = "{}", sort = "{'subscription_metrics.subscriptionRate': -1}")
    List<ContentMetrics> findTopContentBySubscriptionRate(Pageable pageable);

    @Query(value = "{}", sort = "{'subscription_metrics.subscriptionConversions': -1}")
    List<ContentMetrics> findTopContentBySubscriptionConversions(Pageable pageable);

    // Social Top Performers
    @Query(value = "{}", sort = "{'social_metrics.postEngagementRate': -1}")
    List<ContentMetrics> findTopContentBySocialEngagement(Pageable pageable);

    @Query(value = "{}", sort = "{'social_metrics.postReach': -1}")
    List<ContentMetrics> findTopContentBySocialReach(Pageable pageable);

    // Content Quality Top Performers
    @Query(value = "{}", sort = "{'content_quality_score': -1}")
    List<ContentMetrics> findTopContentByQuality(Pageable pageable);

    @Query(value = "{}", sort = "{'content_relevance_score': -1}")
    List<ContentMetrics> findTopContentByRelevance(Pageable pageable);

    // Type-specific Top Performers
    @Query(value = "{'content_type': ?0}", sort = "{'engagement_rate': -1}")
    List<ContentMetrics> findTopContentByTypeAndEngagement(String contentType, Pageable pageable);

    @Query(value = "{'content_type': ?0}", sort = "{'viral_score': -1}")
    List<ContentMetrics> findTopContentByTypeAndViralScore(String contentType, Pageable pageable);

    @Query(value = "{'content_category': ?0}", sort = "{'engagement_rate': -1}")
    List<ContentMetrics> findTopContentByCategoryAndEngagement(String category, Pageable pageable);

    // Pagination Support
    Page<ContentMetrics> findAll(Pageable pageable);

    Page<ContentMetrics> findByContentType(String contentType, Pageable pageable);

    Page<ContentMetrics> findByContentCategory(String contentCategory, Pageable pageable);

    Page<ContentMetrics> findByEngagementRateGreaterThan(BigDecimal rate, Pageable pageable);

    Page<ContentMetrics> findByViralScoreGreaterThan(BigDecimal score, Pageable pageable);

    // Count Queries
    long countByContentType(String contentType);

    long countByContentCategory(String contentCategory);

    long countByContentOwnerId(String contentOwnerId);

    long countByEngagementRateGreaterThan(BigDecimal rate);

    long countByViralScoreGreaterThan(BigDecimal score);

    long countByViewsCountGreaterThan(Integer count);

    long countByLikesCountGreaterThan(Integer count);

    // E-commerce Counts
    @Query(value = "{'ecommerceMetrics.conversionRate': {$gte: ?0}}", count = true)
    long countByEcommerceConversionRateGreaterThan(BigDecimal rate);

    @Query(value = "{'ecommerceMetrics.revenueGenerated': {$gte: ?0}}", count = true)
    long countByEcommerceRevenueGreaterThan(BigDecimal revenue);

    // Subscription Counts
    @Query(value = "{'subscriptionMetrics.subscriptionRate': {$gte: ?0}}", count = true)
    long countBySubscriptionRateGreaterThan(BigDecimal rate);

    @Query(value = "{'subscriptionMetrics.subscriptionConversions': {$gte: ?0}}", count = true)
    long countBySubscriptionConversionsGreaterThan(Integer count);

    // Social Counts
    @Query(value = "{'socialMetrics.postEngagementRate': {$gte: ?0}}", count = true)
    long countBySocialPostEngagementRateGreaterThan(BigDecimal rate);

    @Query(value = "{'socialMetrics.postReach': {$gte: ?0}}", count = true)
    long countBySocialPostReachGreaterThan(Integer reach);
}
