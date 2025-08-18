package com.raved.analytics.controller;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.model.EventType;
import com.raved.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AnalyticsController for TheRavedApp
 *
 * Comprehensive REST API for analytics tracking and reporting with enhanced
 * support for ecommerce, subscription, and social analytics.
 */
@RestController
@RequestMapping({"/api/analytics", "/api/v1/analytics"})
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // Event Tracking Endpoints
    @PostMapping("/events")
    public ResponseEntity<AnalyticsEventResponse> trackEvent(@Valid @RequestBody TrackEventRequest request) {
        AnalyticsEventResponse response = analyticsService.trackEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/events/batch")
    public ResponseEntity<List<AnalyticsEventResponse>> trackEvents(
            @Valid @RequestBody List<TrackEventRequest> requests) {
        List<AnalyticsEventResponse> responses = analyticsService.trackEvents(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    // Event Retrieval Endpoints
    @GetMapping("/events/user/{userId}")
    public ResponseEntity<Page<AnalyticsEventResponse>> getEventsByUser(
            @PathVariable String userId,
            Pageable pageable) {
        Page<AnalyticsEventResponse> events = analyticsService.getEventsByUser(userId, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/type/{eventType}")
    public ResponseEntity<Page<AnalyticsEventResponse>> getEventsByType(
            @PathVariable EventType eventType,
            Pageable pageable) {
        Page<AnalyticsEventResponse> events = analyticsService.getEventsByType(eventType, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/date-range")
    public ResponseEntity<Page<AnalyticsEventResponse>> getEventsInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        // Apply default sorting by eventTimestamp descending if no sort is specified
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "eventTimestamp"));
        }
        Page<AnalyticsEventResponse> events = analyticsService.getEventsInDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/entity/{entityType}/{entityId}")
    public ResponseEntity<Page<AnalyticsEventResponse>> getEventsByEntity(
            @PathVariable String entityType,
            @PathVariable String entityId,
            Pageable pageable) {
        Page<AnalyticsEventResponse> events = analyticsService.getEventsByEntity(entityType, entityId, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/session/{sessionId}")
    public ResponseEntity<List<AnalyticsEventResponse>> getEventsBySession(@PathVariable String sessionId) {
        List<AnalyticsEventResponse> events = analyticsService.getEventsBySession(sessionId);
        return ResponseEntity.ok(events);
    }

    // Event Analytics Endpoints
    @GetMapping("/events/counts/type")
    public ResponseEntity<Map<String, Long>> getEventCountsByType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Long> counts = analyticsService.getEventCountsByType(startDate, endDate);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/events/counts/daily")
    public ResponseEntity<Map<String, Long>> getDailyEventCounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Long> counts = analyticsService.getDailyEventCounts(startDate, endDate);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/events/counts/hourly")
    public ResponseEntity<Map<String, Long>> getHourlyEventCounts(@RequestParam String date) {
        Map<String, Long> counts = analyticsService.getHourlyEventCounts(date);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/events/counts/weekly")
    public ResponseEntity<Map<String, Long>> getWeeklyEventCounts(
            @RequestParam int year,
            @RequestParam int week) {
        Map<String, Long> counts = analyticsService.getWeeklyEventCounts(year, week);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/events/counts/monthly")
    public ResponseEntity<Map<String, Long>> getMonthlyEventCounts(
            @RequestParam int year,
            @RequestParam int month) {
        Map<String, Long> counts = analyticsService.getMonthlyEventCounts(year, month);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/events/counts/total")
    public ResponseEntity<Long> getTotalEventCount() {
        long count = analyticsService.getTotalEventCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/events/counts/user/{userId}")
    public ResponseEntity<Long> getEventCountForUser(@PathVariable String userId) {
        long count = analyticsService.getEventCountForUser(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/events/counts/user/{userId}/type/{eventType}")
    public ResponseEntity<Long> getEventCountForUserByType(
            @PathVariable String userId,
            @PathVariable EventType eventType) {
        long count = analyticsService.getEventCountForUser(userId, eventType);
        return ResponseEntity.ok(count);
    }

    // User Analytics Endpoints
    @GetMapping("/users/{userId}/engagement")
    public ResponseEntity<Map<String, Object>> getUserEngagementMetrics(@PathVariable String userId) {
        Map<String, Object> metrics = analyticsService.getUserEngagementMetrics(userId);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/users/{userId}/behavior")
    public ResponseEntity<Map<String, Object>> getUserBehaviorPatterns(@PathVariable String userId) {
        Map<String, Object> patterns = analyticsService.getUserBehaviorPatterns(userId);
        return ResponseEntity.ok(patterns);
    }

    @GetMapping("/users/{userId}/journey")
    public ResponseEntity<Map<String, Object>> getUserJourneyAnalysis(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> journey = analyticsService.getUserJourneyAnalysis(userId, startDate, endDate);
        return ResponseEntity.ok(journey);
    }

    @GetMapping("/users/segmentation")
    public ResponseEntity<Map<String, Object>> getUserSegmentationData() {
        Map<String, Object> segmentation = analyticsService.getUserSegmentationData();
        return ResponseEntity.ok(segmentation);
    }

    // Content Analytics Endpoints
    @GetMapping("/content/{contentId}/engagement")
    public ResponseEntity<Map<String, Object>> getContentEngagementMetrics(@PathVariable String contentId) {
        Map<String, Object> metrics = analyticsService.getContentEngagementMetrics(contentId);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/content/{contentId}/performance")
    public ResponseEntity<Map<String, Object>> getContentPerformanceAnalysis(
            @PathVariable String contentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> performance = analyticsService.getContentPerformanceAnalysis(contentId, startDate, endDate);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/content/{contentId}/quality")
    public ResponseEntity<Map<String, Object>> getContentQualityMetrics(@PathVariable String contentId) {
        Map<String, Object> quality = analyticsService.getContentQualityMetrics(contentId);
        return ResponseEntity.ok(quality);
    }

    @GetMapping("/content/trending")
    public ResponseEntity<List<Map<String, Object>>> getTrendingContent(
            @RequestParam String contentType,
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> trending = analyticsService.getTrendingContent(contentType, limit);
        return ResponseEntity.ok(trending);
    }

    // E-commerce Analytics Endpoints
    @GetMapping("/ecommerce/products/{productId}/performance")
    public ResponseEntity<Map<String, Object>> getProductPerformanceMetrics(
            @PathVariable String productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> performance = analyticsService.getProductPerformanceMetrics(productId, startDate, endDate);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/ecommerce/sales")
    public ResponseEntity<Map<String, Object>> getSalesAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> sales = analyticsService.getSalesAnalytics(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/ecommerce/conversions")
    public ResponseEntity<Map<String, Object>> getConversionAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> conversions = analyticsService.getConversionAnalytics(startDate, endDate);
        return ResponseEntity.ok(conversions);
    }

    @GetMapping("/ecommerce/cart-abandonment")
    public ResponseEntity<Map<String, Object>> getCartAbandonmentAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> abandonment = analyticsService.getCartAbandonmentAnalysis(startDate, endDate);
        return ResponseEntity.ok(abandonment);
    }

    @GetMapping("/ecommerce/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> revenue = analyticsService.getRevenueAnalytics(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/ecommerce/categories/{category}/performance")
    public ResponseEntity<Map<String, Object>> getProductCategoryPerformance(
            @PathVariable String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> performance = analyticsService.getProductCategoryPerformance(category, startDate, endDate);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/ecommerce/checkout-funnel")
    public ResponseEntity<Map<String, Object>> getCheckoutFunnelAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> funnel = analyticsService.getCheckoutFunnelAnalysis(startDate, endDate);
        return ResponseEntity.ok(funnel);
    }

    // Subscription Analytics Endpoints
    @GetMapping("/subscriptions/plans/{planId}/performance")
    public ResponseEntity<Map<String, Object>> getSubscriptionPerformanceMetrics(
            @PathVariable String planId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> performance = analyticsService.getSubscriptionPerformanceMetrics(planId, startDate,
                endDate);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/subscriptions/analytics")
    public ResponseEntity<Map<String, Object>> getSubscriptionAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> analytics = analyticsService.getSubscriptionAnalytics(startDate, endDate);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/subscriptions/churn")
    public ResponseEntity<Map<String, Object>> getChurnAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> churn = analyticsService.getChurnAnalysis(startDate, endDate);
        return ResponseEntity.ok(churn);
    }

    @GetMapping("/subscriptions/retention")
    public ResponseEntity<Map<String, Object>> getRetentionAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> retention = analyticsService.getRetentionAnalytics(startDate, endDate);
        return ResponseEntity.ok(retention);
    }

    @GetMapping("/subscriptions/funnel")
    public ResponseEntity<Map<String, Object>> getSubscriptionFunnelAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> funnel = analyticsService.getSubscriptionFunnelAnalysis(startDate, endDate);
        return ResponseEntity.ok(funnel);
    }

    @GetMapping("/subscriptions/plans/comparison")
    public ResponseEntity<Map<String, Object>> getPlanPerformanceComparison(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> comparison = analyticsService.getPlanPerformanceComparison(startDate, endDate);
        return ResponseEntity.ok(comparison);
    }

    // Real-time Analytics Endpoints
    @GetMapping("/realtime/metrics")
    public ResponseEntity<Map<String, Object>> getRealTimeMetrics() {
        Map<String, Object> metrics = analyticsService.getRealTimeMetrics();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/realtime/user-activity")
    public ResponseEntity<List<Map<String, Object>>> getRealTimeUserActivity(
            @RequestParam(defaultValue = "50") int limit) {
        List<Map<String, Object>> activity = analyticsService.getRealTimeUserActivity(limit);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/realtime/content-performance")
    public ResponseEntity<List<Map<String, Object>>> getRealTimeContentPerformance(
            @RequestParam(defaultValue = "20") int limit) {
        List<Map<String, Object>> performance = analyticsService.getRealTimeContentPerformance(limit);
        return ResponseEntity.ok(performance);
    }

    // Search Endpoints (Elasticsearch)
    @GetMapping("/search/user/{userId}")
    public ResponseEntity<Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse>> searchByUser(
            @PathVariable String userId, Pageable pageable) {
        return ResponseEntity.ok(analyticsService.searchByUser(userId, pageable));
    }

    @GetMapping("/search/event-type/{eventType}")
    public ResponseEntity<Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse>> searchByEventType(
            @PathVariable String eventType, Pageable pageable) {
        return ResponseEntity.ok(analyticsService.searchByEventType(eventType, pageable));
    }

    @GetMapping("/search/hashtags")
    public ResponseEntity<Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse>> searchByHashtag(
            @RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(analyticsService.searchByHashtag(q, pageable));
    }

    @GetMapping("/search/content-tags")
    public ResponseEntity<Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse>> searchByContentTag(
            @RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(analyticsService.searchByContentTag(q, pageable));
    }

    // Metrics Update Endpoints
    @PostMapping("/metrics/users/{userId}/update")
    public ResponseEntity<Void> updateUserMetrics(@PathVariable String userId) {
        analyticsService.updateUserMetrics(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/metrics/content/{contentId}/update")
    public ResponseEntity<Void> updateContentMetrics(@PathVariable String contentId) {
        analyticsService.updateContentMetrics(contentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/metrics/products/{productId}/update")
    public ResponseEntity<Void> updateProductMetrics(@PathVariable String productId) {
        analyticsService.updateProductMetrics(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/metrics/subscriptions/{subscriptionId}/update")
    public ResponseEntity<Void> updateSubscriptionMetrics(@PathVariable String subscriptionId) {
        analyticsService.updateSubscriptionMetrics(subscriptionId);
        return ResponseEntity.ok().build();
    }
}
