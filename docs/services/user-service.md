# User Service Documentation

## 1. Service Overview

### Primary Purpose
The User Service is the core authentication and user management service for TheRavedApp. It handles user registration, authentication, profile management, and account settings with a focus on university student verification and privacy controls.

### Core Responsibilities
- **Authentication & Authorization**: JWT-based authentication with multi-factor verification
- **User Registration**: 6-step registration process with email/phone verification
- **Profile Management**: Comprehensive user profiles with academic information
- **Account Settings**: Privacy controls, preferences, and security settings
- **Verification System**: Email and phone number verification for student accounts
- **Session Management**: Secure session handling and token refresh

### Service Boundaries
- **Owns**: User accounts, authentication tokens, user profiles, verification status
- **Does NOT Own**: User-generated content, social connections, purchase history
- **Interfaces With**: All other services for user identity verification

### Business Domain Ownership
- User identity and authentication
- Student verification and academic affiliation
- Privacy and security settings
- Account lifecycle management

### Performance Requirements
- **Authentication**: < 200ms response time
- **Registration**: < 2s for complete flow
- **Profile Updates**: < 500ms response time
- **Concurrent Users**: Support 10,000+ simultaneous sessions
- **Availability**: 99.9% uptime SLA

## 2. API Specification

### Authentication Endpoints

#### POST /api/v1/auth/register
**Purpose**: Multi-step user registration process
```json
{
  "step": 1,
  "data": {
    "firstName": "Alex",
    "lastName": "Johnson", 
    "username": "alexj2024"
  }
}
```

**Response**:
```json
{
  "success": true,
  "nextStep": 2,
  "sessionToken": "temp_session_token",
  "validationErrors": []
}
```

#### POST /api/v1/auth/verify-email
**Purpose**: Send and verify email verification codes
```json
{
  "email": "alex@university.edu.gh",
  "code": "123456"
}
```

#### POST /api/v1/auth/verify-phone
**Purpose**: Send and verify SMS verification codes
```json
{
  "phone": "0241234567",
  "code": "654321"
}
```

#### POST /api/v1/auth/login
**Purpose**: User authentication
```json
{
  "identifier": "alexj2024",
  "password": "securePassword123",
  "rememberMe": true
}
```

**Response**:
```json
{
  "success": true,
  "accessToken": "jwt_access_token",
  "refreshToken": "jwt_refresh_token",
  "user": {
    "id": "user_123",
    "username": "alexj2024",
    "name": "Alex Johnson",
    "avatar": "https://cdn.raved.app/avatars/user_123.jpg",
    "emailVerified": true,
    "phoneVerified": true
  }
}
```

### Profile Management Endpoints

#### GET /api/v1/users/profile
**Purpose**: Get current user profile
**Headers**: `Authorization: Bearer {accessToken}`

**Response**:
```json
{
  "id": "user_123",
  "username": "alexj2024",
  "name": "Alex Johnson",
  "email": "alex@university.edu.gh",
  "phone": "0241234567",
  "avatar": "https://cdn.raved.app/avatars/user_123.jpg",
  "bio": "Fashion enthusiast and CS student",
  "university": "University of Ghana",
  "faculty": "Computer Science",
  "studentId": "10123456",
  "location": "Accra, Ghana",
  "website": "https://alexjohnson.dev",
  "joinDate": "2024-01-15T10:30:00Z",
  "emailVerified": true,
  "phoneVerified": true,
  "isPrivate": false,
  "settings": {
    "showActivity": true,
    "readReceipts": true,
    "allowDownloads": false,
    "allowStorySharing": true,
    "analytics": true,
    "personalizedAds": false
  }
}
```

#### PUT /api/v1/users/profile
**Purpose**: Update user profile
```json
{
  "name": "Alex Johnson",
  "bio": "Updated bio text",
  "faculty": "Computer Science",
  "location": "Accra, Ghana",
  "website": "https://alexjohnson.dev"
}
```

#### POST /api/v1/users/avatar
**Purpose**: Upload user avatar
**Content-Type**: `multipart/form-data`
**Body**: `avatar` file field

### Settings Endpoints

#### GET /api/v1/users/settings
**Purpose**: Get user settings and preferences

#### PUT /api/v1/users/settings/privacy
**Purpose**: Update privacy settings
```json
{
  "isPrivate": false,
  "showActivity": true,
  "readReceipts": true,
  "allowDownloads": false,
  "allowStorySharing": true,
  "analytics": true,
  "personalizedAds": false
}
```

#### PUT /api/v1/users/settings/preferences
**Purpose**: Update user preferences
```json
{
  "language": "en",
  "dateFormat": "DD/MM/YYYY",
  "currency": "GHS",
  "theme": "default"
}
```

### Validation Endpoints

#### GET /api/v1/users/validate/username/{username}
**Purpose**: Check username availability
**Response**:
```json
{
  "available": true,
  "suggestions": ["alexj2025", "alex_johnson", "alexj_ug"]
}
```

#### GET /api/v1/users/validate/email/{email}
**Purpose**: Check email availability and format

### Error Responses
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Username already exists",
    "field": "username",
    "details": {}
  }
}
```

### Rate Limiting
- **Authentication**: 5 attempts per minute per IP
- **Verification**: 3 codes per 5 minutes per phone/email
- **Profile Updates**: 10 requests per minute per user
- **Avatar Upload**: 3 uploads per hour per user

## 3. Data Models & Database Schema

### Database Choice: PostgreSQL
**Rationale**: User data requires ACID compliance for authentication integrity, complex queries for user management, and strong consistency for security operations.

### Core Tables

#### users
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    avatar_url TEXT,
    bio TEXT,
    university VARCHAR(255),
    faculty VARCHAR(100),
    student_id VARCHAR(50),
    location VARCHAR(255),
    website VARCHAR(255),
    date_of_birth DATE,
    gender VARCHAR(20),
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    is_private BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_login_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT valid_username CHECK (username ~ '^[a-zA-Z0-9_]{3,50}$'),
    CONSTRAINT valid_email CHECK (email ~ '^[^@\s]+@[^@\s]+\.[^@\s]+$'),
    CONSTRAINT valid_phone CHECK (phone ~ '^0[0-9]{9}$')
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_faculty ON users(faculty);
CREATE INDEX idx_users_created_at ON users(created_at);
```

#### user_settings
```sql
CREATE TABLE user_settings (
    user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    show_activity BOOLEAN DEFAULT TRUE,
    read_receipts BOOLEAN DEFAULT TRUE,
    allow_downloads BOOLEAN DEFAULT FALSE,
    allow_story_sharing BOOLEAN DEFAULT TRUE,
    analytics_enabled BOOLEAN DEFAULT TRUE,
    personalized_ads BOOLEAN DEFAULT FALSE,
    language VARCHAR(10) DEFAULT 'en',
    date_format VARCHAR(20) DEFAULT 'DD/MM/YYYY',
    currency VARCHAR(10) DEFAULT 'GHS',
    theme VARCHAR(50) DEFAULT 'default',
    notification_preferences JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

#### user_verification
```sql
CREATE TABLE user_verification (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    verification_type VARCHAR(20) NOT NULL, -- 'email' or 'phone'
    verification_code VARCHAR(10) NOT NULL,
    contact_info VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    attempts INTEGER DEFAULT 0,
    verified_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_verification_type CHECK (verification_type IN ('email', 'phone'))
);

CREATE INDEX idx_verification_user_type ON user_verification(user_id, verification_type);
CREATE INDEX idx_verification_expires ON user_verification(expires_at);
```

#### user_sessions
```sql
CREATE TABLE user_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    access_token_hash VARCHAR(255) NOT NULL,
    refresh_token_hash VARCHAR(255) NOT NULL,
    device_info JSONB,
    ip_address INET,
    user_agent TEXT,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_used_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_sessions_expires ON user_sessions(expires_at);
```

#### user_login_attempts
```sql
CREATE TABLE user_login_attempts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    identifier VARCHAR(255) NOT NULL, -- username, email, or phone
    ip_address INET NOT NULL,
    success BOOLEAN NOT NULL,
    failure_reason VARCHAR(100),
    attempted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_login_attempts_identifier ON user_login_attempts(identifier, attempted_at);
CREATE INDEX idx_login_attempts_ip ON user_login_attempts(ip_address, attempted_at);
```

### Data Validation Rules
- **Username**: 3-50 characters, alphanumeric and underscore only
- **Email**: Valid email format, must be unique
- **Phone**: Ghana format (0XXXXXXXXX), must be unique
- **Password**: Minimum 8 characters, must contain uppercase, lowercase, number
- **Bio**: Maximum 150 characters
- **Student ID**: Optional, alphanumeric, 6-12 characters

### Migration Strategy
```sql
-- V1__Initial_user_schema.sql
-- V2__Add_user_settings.sql
-- V3__Add_verification_system.sql
-- V4__Add_session_management.sql
-- V5__Add_login_attempts_tracking.sql
```

## 4. Service Discovery & Communication

### Eureka Configuration
```yaml
# application.yml
eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    instance-id: ${spring.application.name}:${server.port}
    prefer-ip-address: true
    health-check-url-path: /actuator/health

spring:
  application:
    name: user-service
```

### Inter-Service Communication

#### Synchronous REST Calls
- **Content Service**: Validate user ownership of posts/comments
- **E-commerce Service**: Verify seller identity for product listings
- **Social Service**: Provide user profile data for connections

#### Asynchronous Messaging (Kafka)
```yaml
# Kafka Topics Published
- user.created: New user registration completed
- user.updated: User profile information changed
- user.verified: Email/phone verification completed
- user.deactivated: User account deactivated

# Kafka Topics Consumed
- content.moderation.required: User content flagged for review
- payment.verification.required: User identity verification for payments
```

### Circuit Breaker Configuration
```yaml
resilience4j:
  circuitbreaker:
    instances:
      user-service:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
        sliding-window-size: 10
        minimum-number-of-calls: 5
```

## 5. Integration Points

### Dependencies on Other Services

#### Content Service Integration
```java
// Validate user can create content
@FeignClient(name = "content-service")
public interface ContentServiceClient {
    @GetMapping("/api/v1/users/{userId}/content-permissions")
    ContentPermissions getUserContentPermissions(@PathVariable String userId);
}
```

#### Notification Service Integration
```java
// Send verification codes and notifications
@FeignClient(name = "notification-service")
public interface NotificationServiceClient {
    @PostMapping("/api/v1/notifications/email/verification")
    void sendEmailVerification(@RequestBody EmailVerificationRequest request);

    @PostMapping("/api/v1/notifications/sms/verification")
    void sendSmsVerification(@RequestBody SmsVerificationRequest request);
}
```

### Event-Driven Communication

#### Published Events
```java
// User registration completed
@EventHandler
public void publishUserCreatedEvent(User user) {
    UserCreatedEvent event = UserCreatedEvent.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .faculty(user.getFaculty())
        .createdAt(user.getCreatedAt())
        .build();

    kafkaTemplate.send("user.created", event);
}
```

#### Consumed Events
```java
// Handle content moderation results
@KafkaListener(topics = "content.moderation.completed")
public void handleContentModerationResult(ContentModerationEvent event) {
    if (event.getViolationSeverity() == ViolationSeverity.SEVERE) {
        userService.flagUserForReview(event.getUserId());
    }
}
```

### Data Consistency Strategy
- **Strong Consistency**: User authentication and profile data
- **Eventual Consistency**: User activity metrics and social connections
- **Saga Pattern**: User registration process across multiple services

## 6. Technology Stack & Infrastructure

### Framework & Dependencies
```xml
<!-- Spring Boot User Service -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/raved_user_db
    username: raved_admin
    password: theRAVEDapp#123
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

### Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}
```

### Docker Configuration
```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app
COPY target/user-service-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Monitoring & Logging
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always

logging:
  level:
    com.raved.userservice: INFO
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

### Performance Optimizations
- **Connection Pooling**: HikariCP with 20 max connections
- **Caching**: Redis for session storage and user profile caching
- **Database Indexing**: Optimized indexes for frequent queries
- **JWT Optimization**: Short-lived access tokens with refresh token rotation
