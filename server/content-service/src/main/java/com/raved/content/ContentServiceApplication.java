package com.raved.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Content Service Application for TheRavedApp
 * 
 * Handles posts, media files, feeds, and content management.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
        System.out.println("🚀 Content Service started successfully!");
        System.out.println("📝 Content APIs available at: http://localhost:8082/api/posts");
    }
}
