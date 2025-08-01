package com.raved.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Analytics Service Application for TheRavedApp
 * 
 * Handles analytics, metrics, trending algorithms, and reporting.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
        System.out.println("🚀 Analytics Service started successfully!");
        System.out.println("📊 Analytics APIs available at: http://localhost:8087/api/analytics");
    }
}
