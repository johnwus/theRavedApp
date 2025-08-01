#!/usr/bin/env python3
"""
Script to create missing server configuration files
"""

import os
from pathlib import Path

def create_file(path, content=""):
    """Create file with content"""
    Path(path).parent.mkdir(parents=True, exist_ok=True)
    if not Path(path).exists():
        with open(path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Created: {path}")

def create_server_configs():
    """Create missing server configuration files"""
    print("🔨 Creating Server Configuration Files...")
    
    # API Gateway configs
    create_file("server/api-gateway/src/main/resources/application.yml", """server:
  port: 8080

spring:
  application:
    name: api-gateway
  profiles:
    active: development
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/auth/**, /api/users/**
        - id: content-service
          uri: lb://content-service
          predicates:
            - Path=/api/posts/**, /api/media/**, /api/feed/**
        - id: social-service
          uri: lb://social-service
          predicates:
            - Path=/api/social/**
        - id: realtime-service
          uri: lb://realtime-service
          predicates:
            - Path=/api/chat/**, /api/realtime/**
        - id: ecommerce-service
          uri: lb://ecommerce-service
          predicates:
            - Path=/api/store/**
        - id: notification-service
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**
        - id: analytics-service
          uri: lb://analytics-service
          predicates:
            - Path=/api/analytics/**
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods: "*"
            allowedHeaders: "*"

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    fetch-registry: true
    register-with-eureka: true
  instance:
    prefer-ip-address: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,gateway
  endpoint:
    health:
      show-details: always

logging:
  level:
    org.springframework.cloud.gateway: DEBUG
  file:
    name: logs/api-gateway.log""")

    create_file("server/api-gateway/src/main/java/com/raved/gateway/config/GatewayConfig.java", """package com.raved.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway routing configuration
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r.path("/api/auth/**", "/api/users/**")
                .uri("lb://user-service"))
            .route("content-service", r -> r.path("/api/posts/**", "/api/media/**", "/api/feed/**")
                .uri("lb://content-service"))
            .route("social-service", r -> r.path("/api/social/**")
                .uri("lb://social-service"))
            .route("realtime-service", r -> r.path("/api/chat/**", "/api/realtime/**")
                .uri("lb://realtime-service"))
            .route("ecommerce-service", r -> r.path("/api/store/**")
                .uri("lb://ecommerce-service"))
            .route("notification-service", r -> r.path("/api/notifications/**")
                .uri("lb://notification-service"))
            .route("analytics-service", r -> r.path("/api/analytics/**")
                .uri("lb://analytics-service"))
            .build();
    }
}""")

    # Eureka Server configs
    create_file("server/eureka-server/src/main/resources/application.yml", """server:
  port: 8761

spring:
  application:
    name: eureka-server
  profiles:
    active: development

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

logging:
  level:
    com.netflix.eureka: DEBUG
  file:
    name: logs/eureka-server.log""")

    # Missing database migrations
    create_file("server/content-service/src/main/resources/db/migration/V2__Create_media_files_table.sql", """-- Create media_files table
CREATE TABLE IF NOT EXISTS media_files (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    width INTEGER,
    height INTEGER,
    duration INTEGER,
    thumbnail_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_media_files_post_id ON media_files(post_id);
CREATE INDEX idx_media_files_file_type ON media_files(file_type);
CREATE INDEX idx_media_files_created_at ON media_files(created_at);""")

    create_file("server/content-service/src/main/resources/db/migration/V3__Create_post_tags_table.sql", """-- Create post_tags table
CREATE TABLE IF NOT EXISTS post_tags (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    tag_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    UNIQUE(post_id, tag_name)
);

-- Create indexes
CREATE INDEX idx_post_tags_post_id ON post_tags(post_id);
CREATE INDEX idx_post_tags_tag_name ON post_tags(tag_name);
CREATE INDEX idx_post_tags_created_at ON post_tags(created_at);""")

    create_file("server/social-service/src/main/resources/db/migration/V2__Create_comments_table.sql", """-- Create comments table
CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_comment_id BIGINT,
    content TEXT NOT NULL,
    like_count BIGINT DEFAULT 0,
    reply_count BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_comments_parent_comment_id ON comments(parent_comment_id);
CREATE INDEX idx_comments_created_at ON comments(created_at);""")

    create_file("server/social-service/src/main/resources/db/migration/V3__Create_follows_table.sql", """-- Create follows table
CREATE TABLE IF NOT EXISTS follows (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(follower_id, following_id)
);

-- Create indexes
CREATE INDEX idx_follows_follower_id ON follows(follower_id);
CREATE INDEX idx_follows_following_id ON follows(following_id);
CREATE INDEX idx_follows_created_at ON follows(created_at);

-- Create activities table
CREATE TABLE IF NOT EXISTS activities (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_user_id BIGINT,
    post_id BIGINT,
    comment_id BIGINT,
    activity_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_activities_user_id ON activities(user_id);
CREATE INDEX idx_activities_target_user_id ON activities(target_user_id);
CREATE INDEX idx_activities_post_id ON activities(post_id);
CREATE INDEX idx_activities_activity_type ON activities(activity_type);
CREATE INDEX idx_activities_created_at ON activities(created_at);""")

    # Additional service implementations
    create_file("server/user-service/src/main/java/com/raved/user/service/impl/AuthServiceImpl.java", """package com.raved.user.service.impl;

import com.raved.user.dto.request.LoginRequest;
import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * Implementation of AuthService
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse login(LoginRequest request) {
        // Implementation
        return null;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Implementation
        return null;
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        // Implementation
        return null;
    }

    @Override
    public void logout(String accessToken) {
        // Implementation
    }

    @Override
    public void verifyEmail(String token) {
        // Implementation
    }

    @Override
    public void forgotPassword(String email) {
        // Implementation
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // Implementation
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        // Implementation
    }
}""")

def create_missing_pom_files():
    """Create missing pom.xml files"""
    print("🔨 Creating Missing POM Files...")
    
    # Shared common pom.xml
    create_file("server/shared/common/pom.xml", """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.raved</groupId>
        <artifactId>raved-parent</artifactId>
        <version>1.0.0</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>
    
    <artifactId>raved-common</artifactId>
    <packaging>jar</packaging>
    <name>Raved Common</name>
    <description>Common utilities and DTOs for Raved microservices</description>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
        </dependency>
    </dependencies>
</project>""")

    # Shared security pom.xml
    create_file("server/shared/security/pom.xml", """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.raved</groupId>
        <artifactId>raved-parent</artifactId>
        <version>1.0.0</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>
    
    <artifactId>raved-security</artifactId>
    <packaging>jar</packaging>
    <name>Raved Security</name>
    <description>Security utilities for Raved microservices</description>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
        </dependency>
    </dependencies>
</project>""")

def main():
    """Main function"""
    print("🚀 Creating Server Configuration Files...")
    
    os.chdir("c:/theRavedApp")
    
    create_server_configs()
    create_missing_pom_files()
    
    print("✅ Server configuration files created successfully!")

if __name__ == "__main__":
    main()
