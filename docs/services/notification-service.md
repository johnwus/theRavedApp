# Notification Service Documentation

## 1. Service Overview

### Primary Purpose
The Notification Service manages all notification delivery in TheRavedApp, including push notifications, email notifications, SMS alerts, and notification preferences management.

### Core Responsibilities
- **Push Notifications**: Mobile and web push notification delivery
- **Email Notifications**: Transactional and marketing email delivery
- **SMS Notifications**: SMS alerts and verification codes
- **Notification Preferences**: User notification settings and preferences
- **Delivery Tracking**: Notification delivery status and analytics
- **Template Management**: Notification templates and personalization

### Service Boundaries
- **Owns**: Notifications, delivery status, preferences, templates
- **Does NOT Own**: User profiles, content data, business logic
- **Interfaces With**: All services for notification triggers

### Performance Requirements
- **Push Delivery**: < 2s for push notification delivery
- **Email Delivery**: < 30s for transactional emails
- **SMS Delivery**: < 10s for SMS delivery
- **Batch Processing**: 100,000+ notifications per hour
- **Template Rendering**: < 500ms for personalized content

## 2. API Specification

### Notification Management

#### POST /api/v1/notifications/send
**Purpose**: Send notification to user
```json
{
  "userId": "user_123",
  "type": "like",
  "channels": ["push", "email"],
  "data": {
    "title": "New Like",
    "message": "Alex Johnson liked your post",
    "actionUrl": "/posts/post_123",
    "imageUrl": "https://cdn.raved.app/posts/post_123_thumb.jpg",
    "metadata": {
      "postId": "post_123",
      "likerId": "user_456"
    }
  }
}
```

#### GET /api/v1/notifications
**Purpose**: Get user's notifications
**Response**:
```json
{
  "success": true,
  "notifications": [
    {
      "id": "notif_123",
      "type": "like",
      "title": "New Like",
      "message": "Alex Johnson liked your post",
      "user": {
        "id": "user_456",
        "username": "alex_j",
        "name": "Alex Johnson",
        "avatar": "https://cdn.raved.app/avatars/user_456.jpg"
      },
      "content": {
        "id": "post_123",
        "type": "post",
        "thumbnailUrl": "https://cdn.raved.app/posts/post_123_thumb.jpg"
      },
      "actionUrl": "/posts/post_123",
      "isRead": false,
      "createdAt": "2024-08-14T10:30:00Z"
    }
  ],
  "unreadCount": 5,
  "pagination": {
    "page": 0,
    "size": 20,
    "hasNext": true
  }
}
```

#### PUT /api/v1/notifications/{notificationId}/read
**Purpose**: Mark notification as read

#### PUT /api/v1/notifications/read-all
**Purpose**: Mark all notifications as read

### Notification Preferences

#### GET /api/v1/notifications/preferences
**Purpose**: Get user's notification preferences
**Response**:
```json
{
  "success": true,
  "preferences": {
    "push": {
      "enabled": true,
      "likes": true,
      "comments": true,
      "follows": true,
      "messages": true,
      "orders": true,
      "marketing": false
    },
    "email": {
      "enabled": true,
      "likes": false,
      "comments": true,
      "follows": true,
      "messages": false,
      "orders": true,
      "marketing": true,
      "digest": "weekly"
    },
    "sms": {
      "enabled": true,
      "verification": true,
      "orders": true,
      "security": true,
      "marketing": false
    },
    "quietHours": {
      "enabled": true,
      "startTime": "22:00",
      "endTime": "08:00",
      "timezone": "Africa/Accra"
    }
  }
}
```

#### PUT /api/v1/notifications/preferences
**Purpose**: Update notification preferences

### Email Notifications

#### POST /api/v1/notifications/email/verification
**Purpose**: Send email verification
```json
{
  "email": "user@university.edu.gh",
  "code": "123456",
  "userName": "Alex Johnson"
}
```

#### POST /api/v1/notifications/email/welcome
**Purpose**: Send welcome email

#### POST /api/v1/notifications/email/digest
**Purpose**: Send weekly digest email

### SMS Notifications

#### POST /api/v1/notifications/sms/verification
**Purpose**: Send SMS verification code
```json
{
  "phone": "0241234567",
  "code": "654321",
  "userName": "Alex"
}
```

#### POST /api/v1/notifications/sms/order
**Purpose**: Send order status SMS

## 3. Data Models & Database Schema

### Database Choice: MongoDB
**Rationale**: Notification data requires flexible schema for different notification types, high write performance for notification logging, and horizontal scaling for large notification volumes.

### Collections Schema

#### notifications
```javascript
{
  _id: ObjectId,
  userId: String, // Reference to User Service
  type: String, // 'like', 'comment', 'follow', 'message', 'order', 'system'
  
  // Content
  title: String,
  message: String,
  actionUrl: String,
  imageUrl: String,
  
  // Related entities
  triggerUserId: String, // User who triggered the notification
  contentId: String, // Related content ID
  contentType: String, // 'post', 'comment', 'order', etc.
  
  // Delivery channels
  channels: [String], // 'push', 'email', 'sms'
  deliveryStatus: {
    push: { status: String, deliveredAt: Date, error: String },
    email: { status: String, deliveredAt: Date, error: String },
    sms: { status: String, deliveredAt: Date, error: String }
  },
  
  // Status
  isRead: Boolean,
  readAt: Date,
  
  // Metadata
  metadata: Object, // Additional data specific to notification type
  priority: String, // 'low', 'normal', 'high', 'urgent'
  
  // Timestamps
  createdAt: Date,
  expiresAt: Date, // For temporary notifications
  
  // Indexes
  indexes: [
    { userId: 1, createdAt: -1 },
    { userId: 1, isRead: 1, createdAt: -1 },
    { type: 1, createdAt: -1 },
    { expiresAt: 1 }, // TTL index
    { triggerUserId: 1, createdAt: -1 }
  ]
}
```

#### notification_preferences
```javascript
{
  _id: ObjectId,
  userId: String, // Reference to User Service
  
  // Channel preferences
  push: {
    enabled: Boolean,
    likes: Boolean,
    comments: Boolean,
    follows: Boolean,
    messages: Boolean,
    orders: Boolean,
    marketing: Boolean
  },
  
  email: {
    enabled: Boolean,
    likes: Boolean,
    comments: Boolean,
    follows: Boolean,
    messages: Boolean,
    orders: Boolean,
    marketing: Boolean,
    digest: String // 'daily', 'weekly', 'monthly', 'never'
  },
  
  sms: {
    enabled: Boolean,
    verification: Boolean,
    orders: Boolean,
    security: Boolean,
    marketing: Boolean
  },
  
  // Quiet hours
  quietHours: {
    enabled: Boolean,
    startTime: String, // "22:00"
    endTime: String, // "08:00"
    timezone: String // "Africa/Accra"
  },
  
  // Device tokens for push notifications
  deviceTokens: [{
    token: String,
    platform: String, // 'ios', 'android', 'web'
    deviceId: String,
    isActive: Boolean,
    registeredAt: Date
  }],
  
  // Timestamps
  createdAt: Date,
  updatedAt: Date,
  
  // Indexes
  indexes: [
    { userId: 1 }, // Unique
    { "deviceTokens.token": 1 },
    { "deviceTokens.platform": 1 }
  ]
}
```

#### notification_templates
```javascript
{
  _id: ObjectId,
  name: String, // Template identifier
  type: String, // Notification type
  channel: String, // 'push', 'email', 'sms'
  
  // Template content
  subject: String, // For email
  title: String, // For push
  body: String, // Template with placeholders
  htmlBody: String, // For email HTML version
  
  // Localization
  language: String,
  
  // Template variables
  variables: [String], // List of available variables
  
  // Status
  isActive: Boolean,
  version: Number,
  
  // Timestamps
  createdAt: Date,
  updatedAt: Date,
  
  // Indexes
  indexes: [
    { name: 1, channel: 1, language: 1 }, // Unique compound
    { type: 1, channel: 1 },
    { isActive: 1 }
  ]
}
```

## 4. Technology Stack & Infrastructure

### Framework & Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>com.google.firebase</groupId>
        <artifactId>firebase-admin</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    <dependency>
        <groupId>com.twilio.sdk</groupId>
        <artifactId>twilio</artifactId>
    </dependency>
</dependencies>
```

### Configuration
```yaml
# Push notification configuration
firebase:
  credentials-path: /config/firebase-credentials.json
  
# Email configuration
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

# SMS configuration
twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  phone-number: ${TWILIO_PHONE_NUMBER}

# MongoDB configuration
spring:
  data:
    mongodb:
      uri: mongodb://raved_admin:theRAVEDapp%23123@mongodb:27017/raved_notifications?authSource=admin
```

### Performance Optimizations
- **Batch Processing**: Queue notifications for batch delivery
- **Template Caching**: Cache frequently used templates
- **Delivery Retry**: Exponential backoff for failed deliveries
- **Rate Limiting**: Prevent notification spam per user
