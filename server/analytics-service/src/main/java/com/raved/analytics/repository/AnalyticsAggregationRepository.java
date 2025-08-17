package com.raved.analytics.repository;

import com.raved.analytics.model.mongo.AnalyticsAggregation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for Analytics Aggregations
 *
 * Provides data access methods for pre-computed analytics aggregations
 * with enhanced querying capabilities for faster reporting and analytics.
 */
@Repository
public interface AnalyticsAggregationRepository extends MongoRepository<AnalyticsAggregation, String> {

    // Basic CRUD Operations
    Optional<AnalyticsAggregation> findById(String id);

    List<AnalyticsAggregation> findAll();

    // Aggregation Type Queries
    List<AnalyticsAggregation> findByAggregationType(String aggregationType);

    List<AnalyticsAggregation> findByAggregationTypeAndPeriodType(String aggregationType, String periodType);

    List<AnalyticsAggregation> findByAggregationTypeAndPeriodTypeAndPeriodValue(String aggregationType, String periodType, String periodValue);

    // Period-based Queries
    List<AnalyticsAggregation> findByPeriodType(String periodType);

    List<AnalyticsAggregation> findByPeriodValue(String periodValue);

    List<AnalyticsAggregation> findByPeriodTypeAndPeriodValue(String periodType, String periodValue);

    // User-based Queries
    List<AnalyticsAggregation> findByUserId(String userId);

    List<AnalyticsAggregation> findByUserIdAndAggregationType(String userId, String aggregationType);

    List<AnalyticsAggregation> findByUserIdAndPeriodType(String userId, String periodType);

    // Entity-based Queries
    List<AnalyticsAggregation> findByEntityType(String entityType);

    List<AnalyticsAggregation> findByEntityTypeAndEntityId(String entityType, String entityId);

    List<AnalyticsAggregation> findByEntityTypeAndAggregationType(String entityType, String aggregationType);

    // Metrics Queries
    List<AnalyticsAggregation> findByTotalEventsGreaterThan(Long count);

    List<AnalyticsAggregation> findByUniqueUsersGreaterThan(Long count);

    List<AnalyticsAggregation> findByTotalRevenueGreaterThan(BigDecimal revenue);

    List<AnalyticsAggregation> findByConversionRateGreaterThan(BigDecimal rate);

    List<AnalyticsAggregation> findByEngagementRateGreaterThan(BigDecimal rate);

    // Time-based Queries
    List<AnalyticsAggregation> findByCalculatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<AnalyticsAggregation> findByCalculatedAtAfter(LocalDateTime date);

    List<AnalyticsAggregation> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // E-commerce Aggregation Queries
    @Query("{'ecommerceAggregations.totalSales': {$gte: ?0}}")
    List<AnalyticsAggregation> findByEcommerceTotalSalesGreaterThan(BigDecimal sales);

    @Query("{'ecommerceAggregations.totalOrders': {$gte: ?0}}")
    List<AnalyticsAggregation> findByEcommerceTotalOrdersGreaterThan(Long orders);

    @Query("{'ecommerceAggregations.averageOrderValue': {$gte: ?0}}")
    List<AnalyticsAggregation> findByEcommerceAverageOrderValueGreaterThan(BigDecimal value);

    @Query("{'ecommerceAggregations.conversionRate': {$gte: ?0}}")
    List<AnalyticsAggregation> findByEcommerceConversionRateGreaterThan(BigDecimal rate);

    // Subscription Aggregation Queries
    @Query("{'subscriptionAggregations.totalRevenue': {$gte: ?0}}")
    List<AnalyticsAggregation> findBySubscriptionTotalRevenueGreaterThan(BigDecimal revenue);

    @Query("{'subscriptionAggregations.totalSubscriptions': {$gte: ?0}}")
    List<AnalyticsAggregation> findBySubscriptionTotalSubscriptionsGreaterThan(Long subscriptions);

    @Query("{'subscriptionAggregations.activeSubscriptions': {$gte: ?0}}")
    List<AnalyticsAggregation> findBySubscriptionActiveSubscriptionsGreaterThan(Long activeSubscriptions);

    @Query("{'subscriptionAggregations.churnRate': {$lte: ?0}}")
    List<AnalyticsAggregation> findBySubscriptionChurnRateLessThan(BigDecimal churnRate);

    @Query("{'subscriptionAggregations.retentionRate': {$gte: ?0}}")
    List<AnalyticsAggregation> findBySubscriptionRetentionRateGreaterThan(BigDecimal retentionRate);

    // Social Aggregation Queries
    @Query("{'socialAggregations.totalPosts': {$gte: ?0}}")
    List<AnalyticsAggregation> findBySocialTotalPostsGreaterThan(Long posts);

    @Query("{'socialAggregations.totalInteractions': {$gte: ?0}}")
    List<AnalyticsAggregation> findBySocialTotalInteractionsGreaterThan(Long interactions);

    @Query("{'socialAggregations.engagementRate': {$gte: ?0}}")
    List<AnalyticsAggregation> findBySocialEngagementRateGreaterThan(BigDecimal rate);

    // Complex Combined Queries
    @Query("{'aggregationType': ?0, 'periodType': ?1, 'totalRevenue': {$gte: ?2}, 'conversionRate': {$gte: ?3}}")
    List<AnalyticsAggregation> findHighPerformingAggregations(String aggregationType, String periodType,
            BigDecimal minRevenue, BigDecimal minConversionRate);

    @Query("{'aggregationType': 'ecommerce_metrics', 'ecommerceAggregations.totalSales': {$gte: ?0}, 'ecommerceAggregations.conversionRate': {$gte: ?1}}")
    List<AnalyticsAggregation> findHighPerformingEcommerceAggregations(BigDecimal minSales, BigDecimal minConversionRate);

    @Query("{'aggregationType': 'subscription_metrics', 'subscriptionAggregations.totalRevenue': {$gte: ?0}, 'subscriptionAggregations.retentionRate': {$gte: ?1}}")
    List<AnalyticsAggregation> findHighPerformingSubscriptionAggregations(BigDecimal minRevenue, BigDecimal minRetentionRate);

    // Top Performers Queries
    @Query(value = "{}", sort = "{'totalRevenue': -1}")
    List<AnalyticsAggregation> findTopAggregationsByRevenue(Pageable pageable);

    @Query(value = "{}", sort = "{'conversionRate': -1}")
    List<AnalyticsAggregation> findTopAggregationsByConversionRate(Pageable pageable);

    @Query(value = "{}", sort = "{'engagementRate': -1}")
    List<AnalyticsAggregation> findTopAggregationsByEngagementRate(Pageable pageable);

    @Query(value = "{}", sort = "{'totalEvents': -1}")
    List<AnalyticsAggregation> findTopAggregationsByTotalEvents(Pageable pageable);

    // Type-specific Top Performers
    @Query(value = "{'aggregationType': ?0}", sort = "{'totalRevenue': -1}")
    List<AnalyticsAggregation> findTopAggregationsByTypeAndRevenue(String aggregationType, Pageable pageable);

    @Query(value = "{'aggregationType': ?0}", sort = "{'conversionRate': -1}")
    List<AnalyticsAggregation> findTopAggregationsByTypeAndConversionRate(String aggregationType, Pageable pageable);

    @Query(value = "{'periodType': ?0}", sort = "{'totalRevenue': -1}")
    List<AnalyticsAggregation> findTopAggregationsByPeriodAndRevenue(String periodType, Pageable pageable);

    // Pagination Support
    Page<AnalyticsAggregation> findAll(Pageable pageable);

    Page<AnalyticsAggregation> findByAggregationType(String aggregationType, Pageable pageable);

    Page<AnalyticsAggregation> findByPeriodType(String periodType, Pageable pageable);

    Page<AnalyticsAggregation> findByTotalRevenueGreaterThan(BigDecimal revenue, Pageable pageable);

    // Count Queries
    long countByAggregationType(String aggregationType);

    long countByPeriodType(String periodType);

    long countByUserId(String userId);

    long countByEntityType(String entityType);

    long countByTotalRevenueGreaterThan(BigDecimal revenue);

    long countByConversionRateGreaterThan(BigDecimal rate);

    // E-commerce Counts
    @Query(value = "{'ecommerceAggregations.totalSales': {$gte: ?0}}", count = true)
    long countByEcommerceTotalSalesGreaterThan(BigDecimal sales);

    @Query(value = "{'ecommerceAggregations.conversionRate': {$gte: ?0}}", count = true)
    long countByEcommerceConversionRateGreaterThan(BigDecimal rate);

    // Subscription Counts
    @Query(value = "{'subscriptionAggregations.totalRevenue': {$gte: ?0}}", count = true)
    long countBySubscriptionTotalRevenueGreaterThan(BigDecimal revenue);

    @Query(value = "{'subscriptionAggregations.activeSubscriptions': {$gte: ?0}}", count = true)
    long countBySubscriptionActiveSubscriptionsGreaterThan(Long activeSubscriptions);

    // Social Counts
    @Query(value = "{'socialAggregations.totalPosts': {$gte: ?0}}", count = true)
    long countBySocialTotalPostsGreaterThan(Long posts);

    @Query(value = "{'socialAggregations.engagementRate': {$gte: ?0}}", count = true)
    long countBySocialEngagementRateGreaterThan(BigDecimal rate);
}