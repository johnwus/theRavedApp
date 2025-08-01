package com.raved.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database configuration for User Service
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.raved.user.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    // Database configuration
}
