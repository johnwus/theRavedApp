package com.raved.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Server Application for TheRavedApp
 * 
 * Centralized configuration management for all microservices.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("🚀 Config Server started successfully!");
        System.out.println("⚙️ Config Server available at: http://localhost:8888");
    }
}
