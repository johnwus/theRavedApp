package com.raved.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * User Service for TheRavedApp
 *
 * Handles user management, authentication, authorization, and user profiles.
 * Provides REST APIs for user registration, login, profile management,
 * and user-related operations.
 *
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.raved.user.client")
@EnableCaching
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("🚀 User Service started successfully!");
        System.out.println("👤 User APIs available at: http://localhost:8081/api/users");
    }
}