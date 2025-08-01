package com.raved.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway for TheRavedApp Microservices
 * 
 * This service acts as the single entry point for all client requests,
 * providing routing, load balancing, authentication, and rate limiting.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("🚀 API Gateway started successfully!");
        System.out.println("🌐 Gateway available at: http://localhost:8080");
    }
}
