package com.raved.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("🚀 User Service started successfully!");
        System.out.println("👤 User APIs available at: http://localhost:8081/api/users");
    }
}