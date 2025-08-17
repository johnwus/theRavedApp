package com.raved.analytics.service;

import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.ContentMetrics;
import com.raved.analytics.model.elasticsearch.AnalyticsEventDocument;
import com.raved.analytics.model.elasticsearch.ContentMetricsDocument;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.elasticsearch.AnalyticsEventSearchRepository;
import com.raved.analytics.repository.elasticsearch.ContentMetricsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for synchronizing data between MongoDB and Elasticsearch Handles
 * real-time indexing and bulk synchronization
 */
@Service
public class ElasticsearchSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchSyncService.class);

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    private ContentMetricsRepository contentMetricsRepository;

    @Autowired
    private AnalyticsEventSearchRepository analyticsEventSearchRepository;

    @Autowired
    private ContentMetricsSearchRepository contentMetricsSearchRepository;

    @Value("${elasticsearch.index.management.enabled:true}")
    private boolean indexManagementEnabled;

    @Value("${elasticsearch.index.retention.days:90}")
    private int indexRetentionDays;

    /**
     * Sync a single analytics event to Elasticsearch
     */
    @Async
    public void syncAnalyticsEvent(AnalyticsEvent event) {
        try {
            AnalyticsEventDocument document = convertToDocument(event);
            analyticsEventSearchRepository.save(document);
            logger.debug("Synced analytics event {} to Elasticsearch", event.getId());
        } catch (Exception e) {
            logger.error("Failed to sync analytics event {} to Elasticsearch", event.getId(), e);
        }
    }

    /**
     * Sync a single content metrics to Elasticsearch
     */
    @Async
    public void syncContentMetrics(ContentMetrics metrics) {
        try {
            ContentMetricsDocument document = convertToDocument(metrics);
            contentMetricsSearchRepository.save(document);
            logger.debug("Synced content metrics {} to Elasticsearch", metrics.getId());
        } catch (Exception e) {
            logger.error("Failed to sync content metrics {} to Elasticsearch", metrics.getId(), e);
        }
    }

    /**
     * Bulk sync analytics events to Elasticsearch
     */
    public void bulkSyncAnalyticsEvents(LocalDateTime since) {
        try {
            logger.info("Starting bulk sync of analytics events since {}", since);

            List<AnalyticsEvent> events = analyticsEventRepository.findByCreatedAtAfter(since);
            List<AnalyticsEventDocument> documents = events.stream()
                    .map(this::convertToDocument)
                    .collect(Collectors.toList());

            analyticsEventSearchRepository.saveAll(documents);
            logger.info("Bulk synced {} analytics events to Elasticsearch", documents.size());
        } catch (Exception e) {
            logger.error("Failed to bulk sync analytics events", e);
        }
    }

    /**
     * Bulk sync content metrics to Elasticsearch
     */
    public void bulkSyncContentMetrics(LocalDateTime since) {
        try {
            logger.info("Starting bulk sync of content metrics since {}", since);

            List<ContentMetrics> metrics = contentMetricsRepository.findByLastCalculatedAtAfter(since);
            List<ContentMetricsDocument> documents = metrics.stream()
                    .map(this::convertToDocument)
                    .collect(Collectors.toList());

            contentMetricsSearchRepository.saveAll(documents);
            logger.info("Bulk synced {} content metrics to Elasticsearch", documents.size());
        } catch (Exception e) {
            logger.error("Failed to bulk sync content metrics", e);
        }
    }

    /**
     * Delete analytics event from Elasticsearch
     */
    @Async
    public void deleteAnalyticsEvent(String eventId) {
        try {
            analyticsEventSearchRepository.deleteById(eventId);
            logger.debug("Deleted analytics event {} from Elasticsearch", eventId);
        } catch (Exception e) {
            logger.error("Failed to delete analytics event {} from Elasticsearch", eventId, e);
        }
    }

    /**
     * Delete content metrics from Elasticsearch
     */
    @Async
    public void deleteContentMetrics(String metricsId) {
        try {
            contentMetricsSearchRepository.deleteById(metricsId);
            logger.debug("Deleted content metrics {} from Elasticsearch", metricsId);
        } catch (Exception e) {
            logger.error("Failed to delete content metrics {} from Elasticsearch", metricsId, e);
        }
    }

    /**
     * Convert AnalyticsEvent to AnalyticsEventDocument
     */
    private AnalyticsEventDocument convertToDocument(AnalyticsEvent event) {
        AnalyticsEventDocument document = new AnalyticsEventDocument();
        document.setId(event.getId());
        document.setUserId(event.getUserId());
        document.setEventType(event.getEventType() != null ? event.getEventType().name() : null);
        document.setEntityType(event.getEntityType());
        document.setEntityId(event.getEntityId());
        document.setEventTimestamp(event.getEventTimestamp());
        document.setSessionId(event.getSessionId());
        document.setUserAgent(event.getUserAgent());
        document.setIpAddress(event.getIpAddress());
        document.setEventData(event.getEventData());
        document.setCreatedAt(event.getCreatedAt());
        return document;
    }

    /**
     * Convert ContentMetrics to ContentMetricsDocument
     */
    private ContentMetricsDocument convertToDocument(ContentMetrics metrics) {
        ContentMetricsDocument document = new ContentMetricsDocument();
        document.setId(metrics.getId());
        document.setContentId(metrics.getContentId());
        document.setContentType(metrics.getContentType());
        document.setContentCategory(metrics.getContentCategory());
        document.setContentOwnerId(metrics.getContentOwnerId());
        document.setViewsCount(metrics.getViewCount());
        document.setLikesCount(metrics.getLikeCount());
        document.setCommentsCount(metrics.getCommentCount());
        document.setSharesCount(metrics.getShareCount());
        document.setEngagementRate(metrics.getEngagementScore());
        document.setReach(metrics.getReachCount());
        document.setImpressions(metrics.getImpressionCount());
        // Convert String date to LocalDateTime (metrics.getMetricsDate() returns String in YYYY-MM-DD format)
        if (metrics.getMetricsDate() != null) {
            try {
                document.setMetricsDate(java.time.LocalDate.parse(metrics.getMetricsDate()).atStartOfDay());
            } catch (Exception e) {
                document.setMetricsDate(LocalDateTime.now());
            }
        } else {
            document.setMetricsDate(LocalDateTime.now());
        }
        document.setLastCalculatedAt(metrics.getLastCalculatedAt());
        return document;
    }

    /**
     * Initialize Elasticsearch indices with proper mappings and settings
     */
    public void initializeIndices() {
        if (!indexManagementEnabled) {
            logger.info("Index management is disabled, skipping index initialization");
            return;
        }

        try {
            logger.info("Initializing Elasticsearch indices for analytics service");

            // Check if indices exist and create them if they don't
            // Note: Spring Data Elasticsearch will auto-create indices based on @Document annotations
            // This method can be enhanced to create custom mappings and settings

            // Verify analytics events index
            long analyticsEventCount = analyticsEventSearchRepository.count();
            logger.info("Analytics events index initialized with {} documents", analyticsEventCount);

            // Verify content metrics index
            long contentMetricsCount = contentMetricsSearchRepository.count();
            logger.info("Content metrics index initialized with {} documents", contentMetricsCount);

            logger.info("Elasticsearch indices initialization completed successfully");

        } catch (Exception e) {
            logger.error("Failed to initialize Elasticsearch indices: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch index initialization failed", e);
        }
    }

    /**
     * Cleanup old indices based on retention policy
     * Runs daily at 2 AM
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOldIndices() {
        if (!indexManagementEnabled) {
            logger.debug("Index management is disabled, skipping cleanup");
            return;
        }

        try {
            logger.info("Starting cleanup of old Elasticsearch indices (retention: {} days)", indexRetentionDays);

            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(indexRetentionDays);

            // Delete old analytics events
            long deletedEvents = analyticsEventSearchRepository.deleteByEventTimestampBefore(cutoffDate);
            logger.info("Deleted {} old analytics events before {}", deletedEvents, cutoffDate);

            // Delete old content metrics
            long deletedMetrics = contentMetricsSearchRepository.deleteByMetricsDateBefore(cutoffDate);
            logger.info("Deleted {} old content metrics before {}", deletedMetrics, cutoffDate);

            logger.info("Elasticsearch index cleanup completed successfully");

        } catch (Exception e) {
            logger.error("Failed to cleanup old Elasticsearch indices: {}", e.getMessage(), e);
        }
    }

    /**
     * Reindex all data from MongoDB to Elasticsearch
     * Use with caution - this is a heavy operation
     */
    public void reindexAllData() {
        if (!indexManagementEnabled) {
            logger.warn("Index management is disabled, cannot perform reindexing");
            return;
        }

        try {
            logger.info("Starting full reindex of all analytics data to Elasticsearch");

            // Clear existing indices
            analyticsEventSearchRepository.deleteAll();
            contentMetricsSearchRepository.deleteAll();

            // Reindex analytics events
            List<AnalyticsEvent> allEvents = analyticsEventRepository.findAll();
            logger.info("Reindexing {} analytics events", allEvents.size());

            for (AnalyticsEvent event : allEvents) {
                try {
                    syncAnalyticsEvent(event);
                } catch (Exception e) {
                    logger.warn("Failed to reindex event {}: {}", event.getId(), e.getMessage());
                }
            }

            // Reindex content metrics
            List<ContentMetrics> allMetrics = contentMetricsRepository.findAll();
            logger.info("Reindexing {} content metrics", allMetrics.size());

            for (ContentMetrics metrics : allMetrics) {
                try {
                    syncContentMetrics(metrics);
                } catch (Exception e) {
                    logger.warn("Failed to reindex metrics {}: {}", metrics.getId(), e.getMessage());
                }
            }

            logger.info("Full reindex completed successfully");

        } catch (Exception e) {
            logger.error("Failed to reindex all data: {}", e.getMessage(), e);
            throw new RuntimeException("Full reindex failed", e);
        }
    }

    /**
     * Get index health and statistics
     */
    public IndexHealthInfo getIndexHealth() {
        try {
            IndexHealthInfo health = new IndexHealthInfo();

            // Get document counts
            health.setAnalyticsEventCount(analyticsEventSearchRepository.count());
            health.setContentMetricsCount(contentMetricsSearchRepository.count());

            // Calculate index sizes and other metrics
            health.setIndexManagementEnabled(indexManagementEnabled);
            health.setRetentionDays(indexRetentionDays);
            health.setLastChecked(LocalDateTime.now());

            return health;

        } catch (Exception e) {
            logger.error("Failed to get index health: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get index health", e);
        }
    }

    /**
     * Inner class for index health information
     */
    public static class IndexHealthInfo {
        private long analyticsEventCount;
        private long contentMetricsCount;
        private boolean indexManagementEnabled;
        private int retentionDays;
        private LocalDateTime lastChecked;

        // Getters and setters
        public long getAnalyticsEventCount() { return analyticsEventCount; }
        public void setAnalyticsEventCount(long analyticsEventCount) { this.analyticsEventCount = analyticsEventCount; }

        public long getContentMetricsCount() { return contentMetricsCount; }
        public void setContentMetricsCount(long contentMetricsCount) { this.contentMetricsCount = contentMetricsCount; }

        public boolean isIndexManagementEnabled() { return indexManagementEnabled; }
        public void setIndexManagementEnabled(boolean indexManagementEnabled) { this.indexManagementEnabled = indexManagementEnabled; }

        public int getRetentionDays() { return retentionDays; }
        public void setRetentionDays(int retentionDays) { this.retentionDays = retentionDays; }

        public LocalDateTime getLastChecked() { return lastChecked; }
        public void setLastChecked(LocalDateTime lastChecked) { this.lastChecked = lastChecked; }
    }
}
