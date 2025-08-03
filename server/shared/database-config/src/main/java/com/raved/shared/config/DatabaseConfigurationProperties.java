package com.raved.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Database Configuration Properties for RAvED Microservices
 * Provides centralized database configuration management
 */
@Configuration
@ConfigurationProperties(prefix = "raved.database")
public class DatabaseConfigurationProperties {

    private String environment = "local";
    private ConnectionPool connectionPool = new ConnectionPool();
    private Security security = new Security();
    private Monitoring monitoring = new Monitoring();

    // Getters and Setters
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(Monitoring monitoring) {
        this.monitoring = monitoring;
    }

    /**
     * Connection Pool Configuration
     */
    public static class ConnectionPool {
        private int maximumPoolSize = 10;
        private int minimumIdle = 5;
        private long idleTimeout = 300000;
        private long connectionTimeout = 20000;
        private long leakDetectionThreshold = 60000;
        private String poolName = "RavedHikariPool";

        // Getters and Setters
        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public int getMinimumIdle() {
            return minimumIdle;
        }

        public void setMinimumIdle(int minimumIdle) {
            this.minimumIdle = minimumIdle;
        }

        public long getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(long idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public long getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public long getLeakDetectionThreshold() {
            return leakDetectionThreshold;
        }

        public void setLeakDetectionThreshold(long leakDetectionThreshold) {
            this.leakDetectionThreshold = leakDetectionThreshold;
        }

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String poolName) {
            this.poolName = poolName;
        }
    }

    /**
     * Security Configuration
     */
    public static class Security {
        private boolean sslEnabled = false;
        private String sslMode = "disable";
        private boolean encryptionEnabled = false;

        // Getters and Setters
        public boolean isSslEnabled() {
            return sslEnabled;
        }

        public void setSslEnabled(boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
        }

        public String getSslMode() {
            return sslMode;
        }

        public void setSslMode(String sslMode) {
            this.sslMode = sslMode;
        }

        public boolean isEncryptionEnabled() {
            return encryptionEnabled;
        }

        public void setEncryptionEnabled(boolean encryptionEnabled) {
            this.encryptionEnabled = encryptionEnabled;
        }
    }

    /**
     * Monitoring Configuration
     */
    public static class Monitoring {
        private boolean metricsEnabled = true;
        private boolean healthCheckEnabled = true;
        private long slowQueryThreshold = 1000;

        // Getters and Setters
        public boolean isMetricsEnabled() {
            return metricsEnabled;
        }

        public void setMetricsEnabled(boolean metricsEnabled) {
            this.metricsEnabled = metricsEnabled;
        }

        public boolean isHealthCheckEnabled() {
            return healthCheckEnabled;
        }

        public void setHealthCheckEnabled(boolean healthCheckEnabled) {
            this.healthCheckEnabled = healthCheckEnabled;
        }

        public long getSlowQueryThreshold() {
            return slowQueryThreshold;
        }

        public void setSlowQueryThreshold(long slowQueryThreshold) {
            this.slowQueryThreshold = slowQueryThreshold;
        }
    }
}
