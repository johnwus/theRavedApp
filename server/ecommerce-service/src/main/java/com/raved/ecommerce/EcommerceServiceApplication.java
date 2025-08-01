package com.raved.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Ecommerce Service Application for TheRavedApp
 * 
 * Handles products, orders, payments, and e-commerce functionality.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EcommerceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceServiceApplication.class, args);
        System.out.println("🚀 Ecommerce Service started successfully!");
        System.out.println("🛒 Store APIs available at: http://localhost:8085/api/store");
    }
}
