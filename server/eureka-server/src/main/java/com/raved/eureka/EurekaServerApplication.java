package com.raved.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Discovery Server for TheRavedApp Microservices
 * 
 * This service acts as a service registry where all microservices register themselves
 * and discover other services. It provides service discovery capabilities for the
 * entire microservices ecosystem.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        System.out.println("🚀 Eureka Discovery Server started successfully!");
        System.out.println("📊 Dashboard available at: http://localhost:8761");
    }
}
