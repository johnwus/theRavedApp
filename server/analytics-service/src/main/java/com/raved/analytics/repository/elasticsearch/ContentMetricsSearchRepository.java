package com.raved.analytics.repository.elasticsearch;

import com.raved.analytics.model.elasticsearch.ContentMetricsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Elasticsearch repository for Content Metrics search documents
 * Provides methods for content analytics and performance search
 */
@Repository
public interface ContentMetricsSearchRepository extends ElasticsearchRepository<ContentMetricsDocument, String> {

    /**
     * Find metrics by content ID
     */
    List<ContentMetricsDocument> findByContentId(String contentId);

    /**
     * Find metrics by content type
     */
    List<ContentMetricsDocument> findByContentType(String contentType);

    /**
     * Find metrics by content category
     */
    List<ContentMetricsDocument> findByContentCategory(String contentCategory);

    /**
     * Find metrics by content owner
     */
    List<ContentMetricsDocument> findByContentOwnerId(String contentOwnerId);

    /**
     * Find metrics within a date range
     */
    List<ContentMetricsDocument> findByMetricsDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find metrics by content type within a date range
     */
    List<ContentMetricsDocument> findByContentTypeAndMetricsDateBetween(String contentType, LocalDateTime start, LocalDateTime end);

    /**
     * Find metrics by content owner within a date range
     */
    List<ContentMetricsDocument> findByContentOwnerIdAndMetricsDateBetween(String contentOwnerId, LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for top performing content by views
     */
    @Query("{\"bool\": {\"must\": [{\"range\": {\"metricsDate\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}]}, \"sort\": [{\"viewsCount\": {\"order\": \"desc\"}}]}")
    List<ContentMetricsDocument> findTopContentByViews(LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for top performing content by engagement
     */
    @Query("{\"bool\": {\"must\": [{\"range\": {\"metricsDate\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}]}, \"sort\": [{\"engagementRate\": {\"order\": \"desc\"}}]}")
    List<ContentMetricsDocument> findTopContentByEngagement(LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for content performance aggregation by type
     */
    @Query("{\"aggs\": {\"content_types\": {\"terms\": {\"field\": \"contentType\", \"size\": 50}, \"aggs\": {\"avg_engagement\": {\"avg\": {\"field\": \"engagementRate\"}}, \"total_views\": {\"sum\": {\"field\": \"viewsCount\"}}}}}}")
    List<ContentMetricsDocument> aggregatePerformanceByContentType();

    /**
     * Custom query for content performance aggregation by category
     */
    @Query("{\"aggs\": {\"categories\": {\"terms\": {\"field\": \"contentCategory\", \"size\": 50}, \"aggs\": {\"avg_engagement\": {\"avg\": {\"field\": \"engagementRate\"}}, \"total_views\": {\"sum\": {\"field\": \"viewsCount\"}}}}}}")
    List<ContentMetricsDocument> aggregatePerformanceByCategory();

    /**
     * Custom query for trending content analysis
     */
    @Query("{\"bool\": {\"must\": [{\"range\": {\"metricsDate\": {\"gte\": \"now-24h\"}}}, {\"range\": {\"viewsCount\": {\"gte\": 100}}}]}, \"sort\": [{\"engagementRate\": {\"order\": \"desc\"}}]}")
    List<ContentMetricsDocument> findTrendingContent();

    /**
     * Custom query for viral content detection
     */
    @Query("{\"bool\": {\"must\": [{\"range\": {\"metricsDate\": {\"gte\": \"now-7d\"}}}, {\"range\": {\"sharesCount\": {\"gte\": 50}}}, {\"range\": {\"engagementRate\": {\"gte\": 0.1}}}]}}")
    List<ContentMetricsDocument> findViralContent();

    /**
     * Custom query for content performance comparison
     */
    @Query("{\"bool\": {\"must\": [{\"term\": {\"contentOwnerId\": \"?0\"}}, {\"range\": {\"metricsDate\": {\"gte\": \"?1\", \"lte\": \"?2\"}}}]}, \"sort\": [{\"engagementRate\": {\"order\": \"desc\"}}]}")
    List<ContentMetricsDocument> findOwnerContentPerformance(String contentOwnerId, LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for low performing content identification
     */
    @Query("{\"bool\": {\"must\": [{\"range\": {\"metricsDate\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}, {\"range\": {\"viewsCount\": {\"lt\": 10}}}, {\"range\": {\"engagementRate\": {\"lt\": 0.01}}}]}}")
    List<ContentMetricsDocument> findLowPerformingContent(LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for content reach analysis
     */
    @Query("{\"aggs\": {\"reach_stats\": {\"stats\": {\"field\": \"reach\"}}, \"impression_stats\": {\"stats\": {\"field\": \"impressions\"}}}}")
    List<ContentMetricsDocument> aggregateReachAndImpressions();

    /**
     * Custom query for engagement rate distribution
     */
    @Query("{\"aggs\": {\"engagement_histogram\": {\"histogram\": {\"field\": \"engagementRate\", \"interval\": 0.01}}}}")
    List<ContentMetricsDocument> aggregateEngagementDistribution();

    /**
     * Custom query for content lifecycle analysis
     */
    @Query("{\"bool\": {\"must\": [{\"term\": {\"contentId\": \"?0\"}}]}, \"sort\": [{\"metricsDate\": {\"order\": \"asc\"}}]}")
    List<ContentMetricsDocument> findContentLifecycle(String contentId);

    /**
     * Index management methods for cleanup
     */
    long deleteByMetricsDateBefore(LocalDateTime cutoffDate);
}
