package com.raved.analytics.repository;

import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for Analytics Events
 *
 * Provides data access methods for analytics events with enhanced querying
 * capabilities for ecommerce, subscription, and social analytics.
 */
@Repository
public interface AnalyticsEventRepository extends MongoRepository<AnalyticsEvent, String> {

    // Basic CRUD Operations
    Optional<AnalyticsEvent> findById(String id);

    List<AnalyticsEvent> findByUserId(String userId);

    List<AnalyticsEvent> findByEventType(EventType eventType);

    List<AnalyticsEvent> findByEntityTypeAndEntityId(String entityType, String entityId);

    List<AnalyticsEvent> findByEntityIdAndEventTimestampBetween(String entityId, LocalDateTime start,
            LocalDateTime end);

    long countByEntityIdAndEventType(String entityId, EventType eventType);

    long deleteByEventTimestampBefore(LocalDateTime cutoffDate);

    // Session-based Queries
    List<AnalyticsEvent> findBySessionId(String sessionId);

    List<AnalyticsEvent> findBySessionIdAndEventTimestampBetween(String sessionId, LocalDateTime start,
            LocalDateTime end);

    // User-based Analytics
    List<AnalyticsEvent> findByUserIdAndEventType(String userId, EventType eventType);

    List<AnalyticsEvent> findByUserIdAndEventTimestampBetween(String userId, LocalDateTime start,
            LocalDateTime end);

    // Time-based Queries for Sync Operations
    List<AnalyticsEvent> findByCreatedAtAfter(LocalDateTime since);

    List<AnalyticsEvent> findByUserIdAndEventTypeAndEventTimestampBetween(String userId, EventType eventType,
            LocalDateTime start, LocalDateTime end);

    // Time-based Queries
    List<AnalyticsEvent> findByEventTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<AnalyticsEvent> findByEventDate(String eventDate);

    List<AnalyticsEvent> findByEventDateAndEventHour(String eventDate, Integer eventHour);

    List<AnalyticsEvent> findByEventMonthAndEventWeekday(Integer month, Integer weekday);

    // Platform and Device Queries
    List<AnalyticsEvent> findByPlatform(String platform);

    List<AnalyticsEvent> findByDeviceType(String deviceType);

    List<AnalyticsEvent> findByPlatformAndEventTimestampBetween(String platform, LocalDateTime start,
            LocalDateTime end);

    // E-commerce Analytics
    @Query("{'event_type': {$in: ['PRODUCT_VIEW', 'PRODUCT_PURCHASE', 'CART_ADD', 'CHECKOUT_START', 'CHECKOUT_COMPLETE']}, 'ecommerce_data.category': ?0}")
    List<AnalyticsEvent> findByEcommerceCategory(String category);

    @Query("{'event_type': 'PRODUCT_PURCHASE', 'ecommerce_data.productId': ?0}")
    List<AnalyticsEvent> findProductPurchaseEvents(String productId);

    @Query("{'event_type': 'PRODUCT_PURCHASE', 'ecommerce_data.category': ?0, 'event_timestamp': {$gte: ?1, $lte: ?2}}")
    List<AnalyticsEvent> findProductPurchasesByCategoryAndDateRange(String category, LocalDateTime start,
            LocalDateTime end);

    @Query("{'event_type': 'CART_ABANDON', 'ecommerce_data.cartId': ?0}")
    List<AnalyticsEvent> findCartAbandonmentEvents(String cartId);

    @Query("{'event_type': 'CHECKOUT_COMPLETE', 'ecommerce_data.transactionId': ?0}")
    List<AnalyticsEvent> findCheckoutCompletionEvents(String transactionId);

    // Subscription Analytics
    @Query("{'event_type': {$in: ['SUBSCRIPTION_START', 'SUBSCRIPTION_RENEWAL', 'SUBSCRIPTION_CANCELLATION']}, 'subscription_data.planId': ?0}")
    List<AnalyticsEvent> findSubscriptionEventsByPlan(String planId);

    @Query("{'event_type': 'SUBSCRIPTION_START', 'subscription_data.planType': ?0, 'event_timestamp': {$gte: ?1, $lte: ?2}}")
    List<AnalyticsEvent> findSubscriptionStartsByPlanTypeAndDateRange(String planType, LocalDateTime start,
            LocalDateTime end);

    @Query("{'event_type': 'SUBSCRIPTION_CANCELLATION', 'subscription_data.cancellationReason': ?0}")
    List<AnalyticsEvent> findSubscriptionCancellationsByReason(String reason);

    @Query("{'event_type': 'SUBSCRIPTION_UPGRADE', 'subscription_data.upgradeFrom': ?0, 'subscription_data.downgradeTo': ?1}")
    List<AnalyticsEvent> findSubscriptionUpgrades(String fromPlan, String toPlan);

    // Social Analytics
    @Query("{'event_type': {$in: ['POST_LIKE', 'POST_COMMENT', 'POST_SHARE']}, 'social_data.postId': ?0}")
    List<AnalyticsEvent> findSocialInteractionsByPost(String postId);

    @Query("{'event_type': 'POST_CREATE', 'social_data.postType': ?0, 'event_timestamp': {$gte: ?1, $lte: ?2}}")
    List<AnalyticsEvent> findPostsByTypeAndDateRange(String postType, LocalDateTime start, LocalDateTime end);

    @Query("{'event_type': 'POST_CREATE', 'social_data.hashtags': {$regex: ?0, $options: 'i'}}")
    List<AnalyticsEvent> findPostsByHashtag(String hashtag);

    // Content Analytics
    @Query("{'eventType': 'CONTENT_VIEW', 'contentData.contentType': ?0, 'contentData.contentCategory': ?1}")
    List<AnalyticsEvent> findContentViewsByTypeAndCategory(String contentType, String category);

    @Query("{'eventType': 'CONTENT_LIKE', 'contentData.contentTags': {$regex: ?0, $options: 'i'}}")
    List<AnalyticsEvent> findContentLikesByTag(String tag);

    // Performance Analytics
    @Query("{'eventType': 'PERFORMANCE_METRIC', 'performanceMetrics.responseTime': {$gte: ?0}}")
    List<AnalyticsEvent> findSlowResponseEvents(Long thresholdMs);

    @Query("{'eventType': 'ERROR_OCCURRED', 'eventTimestamp': {$gte: ?0, $lte: ?1}}")
    List<AnalyticsEvent> findErrorsByDateRange(LocalDateTime start, LocalDateTime end);

    // Missing methods needed by MetricsService
    @Query("{'eventTimestamp': {$gte: ?0}}")
    long countByTimestampAfter(LocalDateTime timestamp);

    @Query("{'userId': ?0, 'eventType': ?1}")
    long countByUserIdAndEventType(String userId, EventType eventType);

    @Query("{'targetId': ?0, 'eventType': ?1}")
    long countByTargetIdAndEventType(String targetId, EventType eventType);

    @Query("{'targetId': ?0, 'eventType': ?1, 'eventTimestamp': {$gte: ?2, $lte: ?3}}")
    long countByTargetIdAndEventTypeAndTimestampBetween(String targetId, EventType eventType, LocalDateTime start,
            LocalDateTime end);

    @Query("{'targetId': ?0, 'eventType': ?1, 'eventTimestamp': {$gte: ?2}}")
    long countByTargetIdAndEventTypeAndTimestampAfter(String targetId, EventType eventType,
            LocalDateTime timestamp);

    @Query("{'targetId': ?0, 'eventType': ?1, 'eventTimestamp': {$gte: ?2, $lte: ?3}}")
    long countUniqueUsers(String targetId, EventType eventType, LocalDateTime start, LocalDateTime end);

    @Query("{'userId': ?0, 'eventType': ?1, 'eventTimestamp': {$gte: ?2, $lte: ?3}}")
    List<Object[]> getDailyEventCounts(String userId, EventType eventType, LocalDateTime start, LocalDateTime end);

    // Overloaded method for getting daily event counts by event type only
    @Query("{'eventType': ?0, 'eventTimestamp': {$gte: ?1, $lte: ?2}}")
    List<Object[]> getDailyEventCounts(EventType eventType, LocalDateTime start, LocalDateTime end);

    // Method for getting daily active users
    @Query("{'eventTimestamp': {$gte: ?0, $lte: ?1}}")
    List<Object[]> getDailyActiveUsers(LocalDateTime start, LocalDateTime end);

    @Query("{'targetId': ?0, 'eventType': ?1, 'eventTimestamp': {$gte: ?2, $lte: ?3}}")
    List<Object[]> getHourlyEventCounts(String targetId, EventType eventType, LocalDateTime start,
            LocalDateTime end);

    @Query("{'userId': ?0}")
    Optional<LocalDateTime> findLastEventTimeForUser(String userId);

    // Additional methods needed by services
    @Query("{'eventTimestamp': {$gte: ?0, $lte: ?1}}")
    List<Object[]> getTrendingTopics(LocalDateTime start, LocalDateTime end, int limit);

    @Query("{'eventTimestamp': {$gte: ?0, $lte: ?1}}")
    List<Object[]> getPeakEngagementHours(LocalDateTime start, LocalDateTime end);

    // Advanced Analytics Queries
    @Query("{'eventType': ?0, 'eventTimestamp': {$gte: ?1, $lte: ?2}, 'userId': {$exists: true}}")
    List<AnalyticsEvent> findAuthenticatedUserEvents(EventType eventType, LocalDateTime start, LocalDateTime end);

    @Query("{'eventType': ?0, 'eventTimestamp': {$gte: ?1, $lte: ?2}, 'userId': {$exists: false}}")
    List<AnalyticsEvent> findAnonymousUserEvents(EventType eventType, LocalDateTime start, LocalDateTime end);

    @Query("{'eventType': ?0, 'location.country': ?1, 'eventTimestamp': {$gte: ?2, $lte: ?3}}")
    List<AnalyticsEvent> findEventsByTypeAndCountry(EventType eventType, String country, LocalDateTime start,
            LocalDateTime end);

    // Pagination and Aggregation Support
    Page<AnalyticsEvent> findByEventType(EventType eventType, Pageable pageable);

    Page<AnalyticsEvent> findByUserId(String userId, Pageable pageable);

    Page<AnalyticsEvent> findByEventTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Custom Complex Queries
    @Query("{'$or': [{'eventType': 'PRODUCT_PURCHASE'}, {'eventType': 'SUBSCRIPTION_START'}], 'eventTimestamp': {$gte: ?0, $lte: ?1}}")
    List<AnalyticsEvent> findMonetizationEvents(LocalDateTime start, LocalDateTime end);

    @Query("{'eventType': 'USER_LOGIN', 'userContext.userSegment': ?0, 'eventTimestamp': {$gte: ?1, $lte: ?2}}")
    List<AnalyticsEvent> findLoginsByUserSegment(String userSegment, LocalDateTime start, LocalDateTime end);

    @Query("{'eventType': 'PAGE_VIEW', 'deviceContext.deviceModel': ?0, 'eventTimestamp': {$gte: ?1, $lte: ?2}}")
    List<AnalyticsEvent> findPageViewsByDeviceModel(String deviceModel, LocalDateTime start, LocalDateTime end);

    // Count Queries for Analytics
    long countByEventType(EventType eventType);

    long countByUserId(String userId);

    long countByEventTypeAndEventTimestampBetween(EventType eventType, LocalDateTime start, LocalDateTime end);

    long countByEventTimestampBetween(LocalDateTime start, LocalDateTime end);

    long countByEntityTypeAndEntityId(String entityType, String entityId);

    // E-commerce Count Queries
    @Query(value = "{'eventType': 'PRODUCT_PURCHASE', 'ecommerceData.category': ?0}", count = true)
    long countProductPurchasesByCategory(String category);

    @Query(value = "{'eventType': 'CART_ABANDON', 'eventTimestamp': {$gte: ?0, $lte: ?1}}", count = true)
    long countCartAbandonmentsByDateRange(LocalDateTime start, LocalDateTime end);

    // Subscription Count Queries
    @Query(value = "{'eventType': 'SUBSCRIPTION_START', 'subscriptionData.planType': ?0, 'eventTimestamp': {$gte: ?1, $lte: ?2}}", count = true)
    long countSubscriptionStartsByPlanTypeAndDateRange(String planType, LocalDateTime start, LocalDateTime end);

    @Query(value = "{'eventType': 'SUBSCRIPTION_CANCELLATION', 'eventTimestamp': {$gte: ?0, $lte: ?1}}", count = true)
    long countSubscriptionCancellationsByDateRange(LocalDateTime start, LocalDateTime end);

    // Social Count Queries
    @Query(value = "{'eventType': 'POST_CREATE', 'socialData.postType': ?0, 'eventTimestamp': {$gte: ?1, $lte: ?2}}", count = true)
    long countPostsByTypeAndDateRange(String postType, LocalDateTime start, LocalDateTime end);

    @Query(value = "{'eventType': 'POST_LIKE', 'socialData.postId': ?0}", count = true)
    long countLikesByPost(String postId);

    // Performance Count Queries
    @Query(value = "{'eventType': 'ERROR_OCCURRED', 'eventTimestamp': {$gte: ?0, $lte: ?1}}", count = true)
    long countErrorsByDateRange(LocalDateTime start, LocalDateTime end);

    @Query(value = "{'eventType': 'PERFORMANCE_METRIC', 'performanceMetrics.responseTime': {$gte: ?0}}", count = true)
    long countSlowResponseEvents(Long thresholdMs);
}
