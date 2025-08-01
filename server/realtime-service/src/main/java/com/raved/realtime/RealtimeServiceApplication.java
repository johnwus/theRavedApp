package com.raved.realtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Realtime Service Application for TheRavedApp
 * 
 * Handles real-time communication, chat, and WebSocket connections.
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RealtimeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealtimeServiceApplication.class, args);
        System.out.println("🚀 Realtime Service started successfully!");
        System.out.println("💬 Chat APIs available at: http://localhost:8084/api/chat");
    }
}
