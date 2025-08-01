package com.raved.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Notification Service Application for TheRavedApp
 * 
 * Handles push notifications, emails, SMS, and notification templates.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
        System.out.println("🚀 Notification Service started successfully!");
        System.out.println("🔔 Notification APIs available at: http://localhost:8086/api/notifications");
    }
}
