package com.raved.analytics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Analytics Service
 */
@Configuration
@ConfigurationProperties(prefix = "analytics")
public class AnalyticsServiceConfig {

    private Processing processing = new Processing();
    private Metrics metrics = new Metrics();
    private Elasticsearch elasticsearch = new Elasticsearch();
    private Cache cache = new Cache();

    // Getters and Setters
    public Processing getProcessing() {
        return processing;
    }

    public void setProcessing(Processing processing) {
        this.processing = processing;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public Elasticsearch getElasticsearch() {
        return elasticsearch;
    }

    public void setElasticsearch(Elasticsearch elasticsearch) {
        this.elasticsearch = elasticsearch;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * Processing configuration
     */
    public static class Processing {
        private int batchSize = 1000;
        private String processingInterval = "60s";
        private int retentionDays = 365;

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public String getProcessingInterval() {
            return processingInterval;
        }

        public void setProcessingInterval(String processingInterval) {
            this.processingInterval = processingInterval;
        }

        public int getRetentionDays() {
            return retentionDays;
        }

        public void setRetentionDays(int retentionDays) {
            this.retentionDays = retentionDays;
        }
    }

    /**
     * Metrics configuration
     */
    public static class Metrics {
        private String calculationInterval = "300s";
        private String aggregationWindow = "3600s";

        public String getCalculationInterval() {
            return calculationInterval;
        }

        public void setCalculationInterval(String calculationInterval) {
            this.calculationInterval = calculationInterval;
        }

        public String getAggregationWindow() {
            return aggregationWindow;
        }

        public void setAggregationWindow(String aggregationWindow) {
            this.aggregationWindow = aggregationWindow;
        }
    }

    /**
     * Elasticsearch configuration
     */
    public static class Elasticsearch {
        private String indexPrefix = "analytics";
        private int bulkSize = 500;
        private String flushInterval = "30s";

        public String getIndexPrefix() {
            return indexPrefix;
        }

        public void setIndexPrefix(String indexPrefix) {
            this.indexPrefix = indexPrefix;
        }

        public int getBulkSize() {
            return bulkSize;
        }

        public void setBulkSize(int bulkSize) {
            this.bulkSize = bulkSize;
        }

        public String getFlushInterval() {
            return flushInterval;
        }

        public void setFlushInterval(String flushInterval) {
            this.flushInterval = flushInterval;
        }
    }

    /**
     * Cache configuration
     */
    public static class Cache {
        private String ttl = "300s";
        private int maxSize = 10000;

        public String getTtl() {
            return ttl;
        }

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
    }
}
