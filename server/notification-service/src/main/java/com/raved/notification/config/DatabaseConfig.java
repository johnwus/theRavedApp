package com.raved.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration for Notification Service
 * 
 * Configures MongoDB connection, repositories, and custom settings
 * optimized for notification data storage and retrieval.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.raved.notification.repository")
public class DatabaseConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:raved_notifications}")
    private String databaseName;

    @Value("${spring.data.mongodb.connection-timeout:10000}")
    private int connectionTimeout;

    @Value("${spring.data.mongodb.socket-timeout:30000}")
    private int socketTimeout;

    @Value("${spring.data.mongodb.max-pool-size:100}")
    private int maxPoolSize;

    @Value("${spring.data.mongodb.min-pool-size:10}")
    private int minPoolSize;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true; // Enable automatic index creation for MongoDB
    }
}
