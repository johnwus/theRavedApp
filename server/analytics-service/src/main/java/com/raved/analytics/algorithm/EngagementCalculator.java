package com.raved.analytics.algorithm;

import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
import com.raved.analytics.repository.AnalyticsEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enhanced Engagement Calculator for TheRavedApp
 *
 * Provides comprehensive engagement calculations for users, content, ecommerce
 * products, and subscription plans with MongoDB optimization.
 */
@Component
public class EngagementCalculator {

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    // User Engagement Calculations
    public BigDecimal calculateUserEngagementRate(String userId) {
        return calculateUserEngagementRate(userId, LocalDateTime.now().minusDays(30), LocalDateTime.now());
    }

    public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<AnalyticsEvent> userEvents = analyticsEventRepository.findByUserIdAndEventTimestampBetween(userId,
                startDate,
                endDate);

        if (userEvents.isEmpty()) {
            return BigDecimal.ZERO;
        }

        long totalEvents = userEvents.size();
        long engagementEvents = userEvents.stream()
                .mapToLong(event -> isEngagementEvent(event.getEventType()) ? 1 : 0)
                .sum();

        return BigDecimal.valueOf(engagementEvents)
                .divide(BigDecimal.valueOf(totalEvents), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    // Content Engagement Calculations
    public BigDecimal calculateContentEngagementScore(String contentId) {
        return calculateContentEngagementScore(contentId, LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    public BigDecimal calculateContentEngagementScore(String contentId, LocalDateTime startDate,
            LocalDateTime endDate) {
        List<AnalyticsEvent> contentEvents = analyticsEventRepository.findByEntityIdAndEventTimestampBetween(contentId,
                startDate, endDate);

        if (contentEvents.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Weight different engagement types
        BigDecimal score = BigDecimal.ZERO;
        for (AnalyticsEvent event : contentEvents) {
            score = score.add(getEngagementWeight(event.getEventType()));
        }

        // Normalize by unique users
        long uniqueUsers = contentEvents.stream()
                .map(AnalyticsEvent::getUserId)
                .distinct()
                .count();

        if (uniqueUsers == 0) {
            return BigDecimal.ZERO;
        }

        return score.divide(BigDecimal.valueOf(uniqueUsers), 4, RoundingMode.HALF_UP);
    }

    // Ecommerce Engagement Calculations
    public BigDecimal calculateProductEngagementScore(String productId) {
        return calculateProductEngagementScore(productId, LocalDateTime.now().minusDays(30), LocalDateTime.now());
    }

    public BigDecimal calculateProductEngagementScore(String productId, LocalDateTime startDate,
            LocalDateTime endDate) {
        List<AnalyticsEvent> productEvents = analyticsEventRepository.findByEntityIdAndEventTimestampBetween(productId,
                startDate, endDate);

        if (productEvents.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal score = BigDecimal.ZERO;
        long views = 0;
        long addToCarts = 0;
        long purchases = 0;

        for (AnalyticsEvent event : productEvents) {
            switch (event.getEventType()) {
                case PRODUCT_VIEW:
                    views++;
                    score = score.add(BigDecimal.valueOf(1));
                    break;
                case PRODUCT_ADD_TO_CART:
                    addToCarts++;
                    score = score.add(BigDecimal.valueOf(3));
                    break;
                case PRODUCT_PURCHASE:
                    purchases++;
                    score = score.add(BigDecimal.valueOf(10));
                    break;
                default:
                    break;
            }
        }

        // Calculate conversion funnel score
        BigDecimal conversionScore = BigDecimal.ZERO;
        if (views > 0) {
            BigDecimal cartConversion = BigDecimal.valueOf(addToCarts).divide(BigDecimal.valueOf(views), 4,
                    RoundingMode.HALF_UP);
            BigDecimal purchaseConversion = addToCarts > 0
                    ? BigDecimal.valueOf(purchases).divide(BigDecimal.valueOf(addToCarts), 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            conversionScore = cartConversion.multiply(BigDecimal.valueOf(50))
                    .add(purchaseConversion.multiply(BigDecimal.valueOf(100)));
        }

        return score.add(conversionScore);
    }

    // Subscription Engagement Calculations
    public BigDecimal calculateSubscriptionEngagementScore(String subscriptionPlanId) {
        return calculateSubscriptionEngagementScore(subscriptionPlanId, LocalDateTime.now().minusDays(30),
                LocalDateTime.now());
    }

    public BigDecimal calculateSubscriptionEngagementScore(String subscriptionPlanId, LocalDateTime startDate,
            LocalDateTime endDate) {
        List<AnalyticsEvent> subscriptionEvents = analyticsEventRepository
                .findByEntityIdAndEventTimestampBetween(subscriptionPlanId, startDate, endDate);

        if (subscriptionEvents.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal score = BigDecimal.ZERO;
        long views = 0;
        long subscriptions = 0;
        long renewals = 0;
        long cancellations = 0;

        for (AnalyticsEvent event : subscriptionEvents) {
            switch (event.getEventType()) {
                case SUBSCRIPTION_VIEW:
                    views++;
                    score = score.add(BigDecimal.valueOf(1));
                    break;
                case SUBSCRIPTION_START:
                    subscriptions++;
                    score = score.add(BigDecimal.valueOf(20));
                    break;
                case SUBSCRIPTION_RENEWAL:
                    renewals++;
                    score = score.add(BigDecimal.valueOf(15));
                    break;
                case SUBSCRIPTION_CANCELLATION:
                    cancellations++;
                    score = score.subtract(BigDecimal.valueOf(10));
                    break;
                default:
                    break;
            }
        }

        // Calculate retention score
        BigDecimal retentionScore = BigDecimal.ZERO;
        if (subscriptions > 0) {
            BigDecimal retentionRate = BigDecimal.valueOf(renewals)
                    .divide(BigDecimal.valueOf(subscriptions), 4, RoundingMode.HALF_UP);
            retentionScore = retentionRate.multiply(BigDecimal.valueOf(100));
        }

        return score.add(retentionScore);
    }

    // Helper Methods
    private boolean isEngagementEvent(EventType eventType) {
        switch (eventType) {
            case POST_LIKE:
            case CONTENT_LIKE:
            case PRODUCT_LIKE:
            case POST_COMMENT:
            case CONTENT_COMMENT:
            case POST_SHARE:
            case CONTENT_SHARE:
            case PRODUCT_ADD_TO_CART:
            case PRODUCT_PURCHASE:
            case SUBSCRIPTION_START:
            case SUBSCRIPTION_RENEWAL:
                return true;
            default:
                return false;
        }
    }

    private BigDecimal getEngagementWeight(EventType eventType) {
        switch (eventType) {
            case PAGE_VIEW:
            case POST_VIEW:
            case PRODUCT_VIEW:
            case CONTENT_VIEW:
                return BigDecimal.valueOf(1);
            case POST_LIKE:
            case CONTENT_LIKE:
            case PRODUCT_LIKE:
                return BigDecimal.valueOf(2);
            case POST_COMMENT:
            case CONTENT_COMMENT:
                return BigDecimal.valueOf(3);
            case POST_SHARE:
            case CONTENT_SHARE:
                return BigDecimal.valueOf(4);
            case GROUP_JOIN:
                return BigDecimal.valueOf(5);
            case PRODUCT_ADD_TO_CART:
            case CART_ADD:
                return BigDecimal.valueOf(6);
            case PRODUCT_PURCHASE:
            case ORDER_PLACED:
                return BigDecimal.valueOf(10);
            case SUBSCRIPTION_START:
                return BigDecimal.valueOf(15);
            case SUBSCRIPTION_RENEWAL:
                return BigDecimal.valueOf(12);
            case SUBSCRIPTION_CANCELLATION:
                return BigDecimal.valueOf(-5);
            default:
                return BigDecimal.valueOf(1);
        }
    }

    // Advanced Analytics Methods
    public Map<String, BigDecimal> calculateEngagementTrends(String entityId, int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<AnalyticsEvent> events = analyticsEventRepository.findByEntityIdAndEventTimestampBetween(entityId,
                startDate,
                endDate);

        return events.stream()
                .collect(Collectors.groupingBy(
                        event -> event.getEventTimestamp().toLocalDate().toString(),
                        Collectors.reducing(BigDecimal.ZERO,
                                event -> getEngagementWeight(event.getEventType()),
                                BigDecimal::add)));
    }

    public BigDecimal calculateConversionRate(String entityId, EventType fromEvent, EventType toEvent, int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<AnalyticsEvent> events = analyticsEventRepository.findByEntityIdAndEventTimestampBetween(entityId,
                startDate,
                endDate);

        long fromCount = events.stream()
                .filter(event -> event.getEventType() == fromEvent)
                .count();

        long toCount = events.stream()
                .filter(event -> event.getEventType() == toEvent)
                .count();

        if (fromCount == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(toCount)
                .divide(BigDecimal.valueOf(fromCount), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal calculateRetentionRate(String userId, int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<AnalyticsEvent> events = analyticsEventRepository.findByUserIdAndEventTimestampBetween(userId, startDate,
                endDate);

        if (events.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Calculate days with activity
        long activeDays = events.stream()
                .map(event -> event.getEventTimestamp().toLocalDate())
                .distinct()
                .count();

        return BigDecimal.valueOf(activeDays)
                .divide(BigDecimal.valueOf(days), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
