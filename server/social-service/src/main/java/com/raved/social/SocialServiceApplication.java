package com.raved.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Social Service Application for TheRavedApp
 * 
 * Handles likes, comments, follows, and social interactions.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class SocialServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialServiceApplication.class, args);
        System.out.println("🚀 Social Service started successfully!");
        System.out.println("👥 Social APIs available at: http://localhost:8083/api/social");
    }
}
