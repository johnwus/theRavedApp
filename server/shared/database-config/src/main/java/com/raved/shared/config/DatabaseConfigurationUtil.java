package com.raved.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Database Configuration Utility for RAvED Microservices
 * Provides helper methods for database configuration management
 */
@Component
public class DatabaseConfigurationUtil {

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Value("${raved.service.name:unknown}")
    private String serviceName;

    /**
     * Get the database URL for the current service based on the active profile
     */
    public String getDatabaseUrl() {
        String envVar = getServiceDatabaseUrlEnvVar();
        String url = System.getenv(envVar);
        
        if (url != null && !url.isEmpty()) {
            return url;
        }
        
        // Fallback to default local URL
        return getDefaultLocalUrl();
    }

    /**
     * Get the environment variable name for the service database URL
     */
    private String getServiceDatabaseUrlEnvVar() {
        String servicePrefix = serviceName.toUpperCase().replace("-", "_");
        return servicePrefix + "_SERVICE_DB_URL";
    }

    /**
     * Get the default local database URL for the service
     */
    private String getDefaultLocalUrl() {
        String dbName = "raved_" + serviceName.replace("-", "_") + "_db";
        return "jdbc:postgresql://localhost:5432/" + dbName;
    }

    /**
     * Check if the current environment is cloud-based
     */
    public boolean isCloudEnvironment() {
        return "staging".equals(activeProfile) || "production".equals(activeProfile);
    }

    /**
     * Check if the current environment is local development
     */
    public boolean isLocalEnvironment() {
        return "local".equals(activeProfile) || "development".equals(activeProfile);
    }

    /**
     * Get the appropriate connection pool size based on environment
     */
    public int getConnectionPoolSize() {
        switch (activeProfile) {
            case "production":
                return 20;
            case "staging":
                return 15;
            case "local":
            case "development":
            default:
                return 10;
        }
    }

    /**
     * Get the appropriate minimum idle connections based on environment
     */
    public int getMinimumIdleConnections() {
        switch (activeProfile) {
            case "production":
                return 10;
            case "staging":
                return 5;
            case "local":
            case "development":
            default:
                return 5;
        }
    }

    /**
     * Get the connection timeout based on environment
     */
    public long getConnectionTimeout() {
        return isCloudEnvironment() ? 30000 : 20000;
    }

    /**
     * Get the idle timeout based on environment
     */
    public long getIdleTimeout() {
        return isCloudEnvironment() ? 600000 : 300000;
    }

    /**
     * Check if SSL should be enabled
     */
    public boolean isSslEnabled() {
        return isCloudEnvironment();
    }

    /**
     * Get SSL mode
     */
    public String getSslMode() {
        return isCloudEnvironment() ? "require" : "disable";
    }

    /**
     * Get the pool name for the service
     */
    public String getPoolName() {
        String env = activeProfile.substring(0, 1).toUpperCase() + activeProfile.substring(1);
        return "RavedHikariPool-" + serviceName + "-" + env;
    }

    /**
     * Check if query logging should be enabled
     */
    public boolean isQueryLoggingEnabled() {
        return isLocalEnvironment();
    }

    /**
     * Get the appropriate batch size for JPA operations
     */
    public int getJpaBatchSize() {
        switch (activeProfile) {
            case "production":
                return 30;
            case "staging":
                return 25;
            case "local":
            case "development":
            default:
                return 20;
        }
    }

    // Getters for injected values
    public String getActiveProfile() {
        return activeProfile;
    }

    public String getServiceName() {
        return serviceName;
    }
}
