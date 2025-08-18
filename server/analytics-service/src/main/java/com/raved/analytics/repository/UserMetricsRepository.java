package com.raved.analytics.repository;

import com.raved.analytics.model.UserMetrics;
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

/**
 * MongoDB Repository for User Metrics
 *
 * Provides data access methods for user analytics and metrics with enhanced
 * querying capabilities for social, ecommerce, and subscription analytics.
 */
@Repository
public interface UserMetricsRepository extends MongoRepository<UserMetrics, String> {

    // Basic CRUD Operations
    Optional<UserMetrics> findById(String id);

    Optional<UserMetrics> findByUserId(String userId);

    List<UserMetrics> findAll();

    // TODO: Uncomment this when we have a way to save all user metrics
    // List<UserMetrics> saveAll(Iterable<UserMetrics> entities);
    // User Profile Queries
    List<UserMetrics> findByUserSegment(String userSegment);

    List<UserMetrics> findByUserTier(String userTier);

    List<UserMetrics> findByIsPremium(Boolean isPremium);

    List<UserMetrics> findByUserSegmentAndUserTier(String userSegment, String userTier);

    // Engagement Score Queries
    List<UserMetrics> findByEngagementScoreGreaterThan(BigDecimal score);

    List<UserMetrics> findByEngagementScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    List<UserMetrics> findByEngagementScoreGreaterThanOrderByEngagementScoreDesc(BigDecimal score);

    // Influence Score Queries
    List<UserMetrics> findByInfluenceScoreGreaterThan(BigDecimal score);

    List<UserMetrics> findByInfluenceScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    List<UserMetrics> findByInfluenceScoreGreaterThanOrderByInfluenceScoreDesc(BigDecimal score);

    // Retention Score Queries
    List<UserMetrics> findByRetentionScoreGreaterThan(BigDecimal score);

    List<UserMetrics> findByRetentionScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    // Monetization Score Queries
    List<UserMetrics> findByMonetizationScoreGreaterThan(BigDecimal score);

    List<UserMetrics> findByMonetizationScoreBetween(BigDecimal minScore, BigDecimal maxScore);

    // Registration Date Queries
    List<UserMetrics> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end);

    List<UserMetrics> findByRegistrationDateAfter(LocalDateTime date);

    List<UserMetrics> findByRegistrationDateBefore(LocalDateTime date);

    // Last Active Date Queries
    List<UserMetrics> findByLastActiveDateBetween(LocalDateTime start, LocalDateTime end);

    List<UserMetrics> findByLastActiveDateAfter(LocalDateTime date);

    List<UserMetrics> findByLastActiveDateBefore(LocalDateTime date);

    // Last Calculated Date Queries
    List<UserMetrics> findByLastCalculatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<UserMetrics> findByLastCalculatedAtBefore(LocalDateTime date);

    // Social Metrics Queries
    @Query("{'social_metrics.followersCount': {$gte: ?0}}")
    List<UserMetrics> findByFollowersCountGreaterThan(Integer count);

    @Query("{'social_metrics.postsCount': {$gte: ?0}}")
    List<UserMetrics> findByPostsCountGreaterThan(Integer count);

    @Query("{'social_metrics.postsCount': {$gte: ?0, $lte: ?1}}")
    List<UserMetrics> findByPostsCountBetween(Integer minCount, Integer maxCount);

    @Query("{'social_metrics.likesReceived': {$gte: ?0}}")
    List<UserMetrics> findByLikesReceivedGreaterThan(Integer count);

    @Query("{'social_metrics.commentsReceived': {$gte: ?0}}")
    List<UserMetrics> findByCommentsReceivedGreaterThan(Integer count);

    // Missing methods needed by MetricsService
    @Query("{'last_active_date': {$gte: ?0}}")
    long countActiveUsers(LocalDateTime since);

    @Query("{}")
    double getAverageEngagementRate();

    // Missing methods needed by ReportService
    @Query(value = "{'registration_date': {$gte: ?0, $lte: ?1}}", count = true)
    long countNewUsersBetween(LocalDateTime start, LocalDateTime end);

    @Query("{'last_active_date': {$gte: ?0, $lte: ?1}}")
    long countActiveUsersBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "{'last_active_date': {$gte: ?0, $lte: ?1}}", fields = "{'engagement_score': 1}")
    List<UserMetrics> findActiveUsersBetween(LocalDateTime start, LocalDateTime end);

    default double getAverageEngagementRateBetween(LocalDateTime start, LocalDateTime end) {
        List<UserMetrics> users = findActiveUsersBetween(start, end);
        if (users.isEmpty()) {
            return 0.0;
        }
        return users.stream()
                .filter(user -> user.getEngagementScore() != null)
                .mapToDouble(user -> user.getEngagementScore().doubleValue())
                .average()
                .orElse(0.0);
    }

    @Query(value = "{'engagement_score': {$exists: true}}", sort = "{'engagement_score': -1}")
    Page<UserMetrics> findTopByEngagementRate(Pageable pageable);

    default List<Object[]> getTopUsersByEngagement(Pageable pageable) {
        Page<UserMetrics> topUsers = findTopByEngagementRate(pageable);
        return topUsers.getContent().stream()
                .map(user -> new Object[]{
            user.getUserId(),
            user.getEngagementScore() != null ? user.getEngagementScore().doubleValue() : 0.0,
            user.getTotalPosts(),
            user.getTotalLikes(),
            user.getTotalComments()
        })
                .collect(Collectors.toList());
    }

    // E-commerce Metrics Queries
    @Query("{'ecommerceMetrics.totalSalesAmount': {$gte: ?0}}")
    List<UserMetrics> findByTotalSalesAmountGreaterThan(BigDecimal amount);

    @Query("{'ecommerceMetrics.totalSalesAmount': {$gte: ?0, $lte: ?1}}")
    List<UserMetrics> findByTotalSalesAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    @Query("{'ecommerceMetrics.productsSold': {$gte: ?0}}")
    List<UserMetrics> findByProductsSoldGreaterThan(Integer count);

    @Query("{'ecommerceMetrics.ordersPlaced': {$gte: ?0}}")
    List<UserMetrics> findByOrdersPlacedGreaterThan(Integer count);

    @Query("{'ecommerceMetrics.averageOrderValue': {$gte: ?0}}")
    List<UserMetrics> findByAverageOrderValueGreaterThan(BigDecimal amount);

    // Subscription Metrics Queries
    @Query("{'subscriptionMetrics.subscriptionValue': {$gte: ?0}}")
    List<UserMetrics> findBySubscriptionValueGreaterThan(BigDecimal value);

    @Query("{'subscriptionMetrics.activeSubscriptions': {$gte: ?0}}")
    List<UserMetrics> findByActiveSubscriptionsGreaterThan(Integer count);

    @Query("{'subscriptionMetrics.monthlyRecurringRevenue': {$gte: ?0}}")
    List<UserMetrics> findByMonthlyRecurringRevenueGreaterThan(BigDecimal amount);

    @Query("{'subscriptionMetrics.customerLifetimeValue': {$gte: ?0}}")
    List<UserMetrics> findByCustomerLifetimeValueGreaterThan(BigDecimal value);

    // Content Metrics Queries
    @Query("{'contentMetrics.totalContent': {$gte: ?0}}")
    List<UserMetrics> findByTotalContentGreaterThan(Integer count);

    @Query("{'contentMetrics.publishedContent': {$gte: ?0}}")
    List<UserMetrics> findByPublishedContentGreaterThan(Integer count);

    @Query("{'contentMetrics.contentEngagementRate': {$gte: ?0}}")
    List<UserMetrics> findByContentEngagementRateGreaterThan(BigDecimal rate);

    // Activity Metrics Queries
    @Query("{'activityMetrics.loginStreak': {$gte: ?0}}")
    List<UserMetrics> findByLoginStreakGreaterThan(Integer streak);

    @Query("{'activityMetrics.totalSessions': {$gte: ?0}}")
    List<UserMetrics> findByTotalSessionsGreaterThan(Integer count);

    @Query("{'activityMetrics.totalTimeSpent': {$gte: ?0}}")
    List<UserMetrics> findByTotalTimeSpentGreaterThan(Long seconds);

    @Query("{'activityMetrics.averageSessionDuration': {$gte: ?0}}")
    List<UserMetrics> findByAverageSessionDurationGreaterThan(BigDecimal duration);

    // Performance Metrics Queries
    @Query("{'performanceMetrics.errorCount': {$gte: ?0}}")
    List<UserMetrics> findByErrorCountGreaterThan(Integer count);

    @Query("{'performanceMetrics.errorRate': {$gte: ?0}}")
    List<UserMetrics> findByErrorRateGreaterThan(BigDecimal rate);

    // Behavioral Patterns Queries
    List<UserMetrics> findByBehavioralPatternsPreferredContentType(String contentType);

    List<UserMetrics> findByBehavioralPatternsPreferredDevice(String device);

    List<UserMetrics> findByBehavioralPatternsPreferredPlatform(String platform);

    List<UserMetrics> findByBehavioralPatternsUserPersona(String persona);

    // Complex Combined Queries
    @Query("{'userSegment': ?0, 'engagementScore': {$gte: ?1}, 'monetizationScore': {$gte: ?2}}")
    List<UserMetrics> findBySegmentAndMinScores(String userSegment, BigDecimal minEngagement,
            BigDecimal minMonetization);

    @Query("{'isPremium': true, 'subscriptionMetrics.subscriptionValue': {$gte: ?0}, 'ecommerceMetrics.totalSalesAmount': {$gte: ?1}}")
    List<UserMetrics> findHighValuePremiumUsers(BigDecimal minSubscriptionValue, BigDecimal minSalesAmount);

    @Query("{'socialMetrics.followersCount': {$gte: ?0}, 'influenceScore': {$gte: ?1}, 'contentMetrics.publishedContent': {$gte: ?2}}")
    List<UserMetrics> findInfluentialContentCreators(Integer minFollowers, BigDecimal minInfluence,
            Integer minContent);

    @Query("{'ecommerceMetrics.totalSalesAmount': {$gte: ?0}, 'subscriptionMetrics.subscriptionValue': {$gte: ?1}, 'retentionScore': {$gte: ?2}}")
    List<UserMetrics> findHighValueRetainedUsers(BigDecimal minSales, BigDecimal minSubscription,
            BigDecimal minRetention);

    // Top Performers Queries
    @Query(value = "{}", sort = "{'engagementScore': -1}")
    List<UserMetrics> findTopUsersByEngagement(Pageable pageable);

    @Query(value = "{}", sort = "{'influenceScore': -1}")
    List<UserMetrics> findTopUsersByInfluence(Pageable pageable);

    @Query(value = "{}", sort = "{'monetizationScore': -1}")
    List<UserMetrics> findTopUsersByMonetization(Pageable pageable);

    @Query(value = "{}", sort = "{'retentionScore': -1}")
    List<UserMetrics> findTopUsersByRetention(Pageable pageable);

    // E-commerce Top Performers
    @Query(value = "{}", sort = "{'ecommerceMetrics.totalSalesAmount': -1}")
    List<UserMetrics> findTopSellers(Pageable pageable);

    @Query(value = "{}", sort = "{'ecommerceMetrics.averageOrderValue': -1}")
    List<UserMetrics> findTopUsersByAverageOrderValue(Pageable pageable);

    // Subscription Top Performers
    @Query(value = "{}", sort = "{'subscriptionMetrics.subscriptionValue': -1}")
    List<UserMetrics> findTopUsersBySubscriptionValue(Pageable pageable);

    @Query(value = "{}", sort = "{'subscriptionMetrics.customerLifetimeValue': -1}")
    List<UserMetrics> findTopUsersByLifetimeValue(Pageable pageable);

    // Social Top Performers
    @Query(value = "{}", sort = "{'socialMetrics.followersCount': -1}")
    List<UserMetrics> findTopUsersByFollowers(Pageable pageable);

    @Query(value = "{}", sort = "{'socialMetrics.postsCount': -1}")
    List<UserMetrics> findTopUsersByPosts(Pageable pageable);

    // Pagination Support
    Page<UserMetrics> findAll(Pageable pageable);

    Page<UserMetrics> findByUserSegment(String userSegment, Pageable pageable);

    Page<UserMetrics> findByIsPremium(Boolean isPremium, Pageable pageable);

    Page<UserMetrics> findByEngagementScoreGreaterThan(BigDecimal score, Pageable pageable);

    // Count Queries
    long countByUserSegment(String userSegment);

    long countByUserTier(String userTier);

    long countByIsPremium(Boolean isPremium);

    long countByEngagementScoreGreaterThan(BigDecimal score);

    long countByInfluenceScoreGreaterThan(BigDecimal score);

    long countByMonetizationScoreGreaterThan(BigDecimal score);

    // E-commerce Counts
    @Query(value = "{'ecommerceMetrics.totalSalesAmount': {$gte: ?0}}", count = true)
    long countByTotalSalesAmountGreaterThan(BigDecimal amount);

    @Query(value = "{'ecommerceMetrics.productsSold': {$gte: ?0}}", count = true)
    long countByProductsSoldGreaterThan(Integer count);

    // Subscription Counts
    @Query(value = "{'subscriptionMetrics.activeSubscriptions': {$gte: ?0}}", count = true)
    long countByActiveSubscriptionsGreaterThan(Integer count);

    @Query(value = "{'subscriptionMetrics.subscriptionValue': {$gte: ?0}}", count = true)
    long countBySubscriptionValueGreaterThan(BigDecimal value);

    // Social Counts
    @Query(value = "{'socialMetrics.followersCount': {$gte: ?0}}", count = true)
    long countByFollowersCountGreaterThan(Integer count);

    @Query(value = "{'socialMetrics.postsCount': {$gte: ?0}}", count = true)
    long countByPostsCountGreaterThan(Integer count);
}
