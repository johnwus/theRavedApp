# Analytics Service Documentation

## 1. Service Overview

### Primary Purpose
The Analytics Service tracks user activity, engagement metrics, performance analytics, and data aggregation for TheRavedApp's premium features and business intelligence.

### Core Responsibilities
- **User Activity Tracking**: Comprehensive user behavior analytics
- **Engagement Metrics**: Content interaction and social engagement analysis
- **Performance Analytics**: Content and user performance insights
- **Rankings System**: Monthly creator rankings with prize pool
- **Data Aggregation**: Real-time and batch analytics processing
- **Business Intelligence**: Revenue, growth, and usage analytics

### Service Boundaries
- **Owns**: Analytics data, user scores, rankings, performance metrics
- **Does NOT Own**: User profiles, content data, business transactions
- **Interfaces With**: All services for analytics event collection

### Performance Requirements
- **Event Processing**: < 50ms for real-time event ingestion
- **Analytics Queries**: < 2s for dashboard data retrieval
- **Rankings Calculation**: Daily batch processing for 10,000+ users
- **Data Aggregation**: Process 1M+ events per day
- **Report Generation**: < 10s for standard reports

## 2. API Specification

### Analytics Tracking

#### POST /api/v1/analytics/events
**Purpose**: Track user activity events
```json
{
  "userId": "user_123",
  "eventType": "content_view",
  "eventData": {
    "contentId": "post_456",
    "contentType": "post",
    "viewDuration": 5.2,
    "source": "feed",
    "deviceType": "mobile"
  },
  "timestamp": "2024-08-14T10:30:00Z",
  "sessionId": "session_789"
}
```

#### POST /api/v1/analytics/events/batch
**Purpose**: Track multiple events in batch
```json
{
  "events": [
    {
      "userId": "user_123",
      "eventType": "content_like",
      "eventData": {
        "contentId": "post_456",
        "contentType": "post"
      },
      "timestamp": "2024-08-14T10:30:00Z"
    },
    {
      "userId": "user_123",
      "eventType": "content_share",
      "eventData": {
        "contentId": "post_456",
        "shareType": "story"
      },
      "timestamp": "2024-08-14T10:31:00Z"
    }
  ]
}
```

### User Analytics

#### GET /api/v1/analytics/users/{userId}/dashboard
**Purpose**: Get user analytics dashboard
**Response**:
```json
{
  "success": true,
  "analytics": {
    "period": "last_30_days",
    "overview": {
      "totalViews": 1250,
      "totalLikes": 156,
      "totalComments": 23,
      "totalShares": 8,
      "profileViews": 89,
      "followerGrowth": 12
    },
    "contentPerformance": {
      "topPosts": [
        {
          "contentId": "post_123",
          "views": 245,
          "likes": 34,
          "comments": 8,
          "shares": 3,
          "engagementRate": 0.18
        }
      ],
      "averageEngagementRate": 0.12,
      "bestPerformingTime": "18:00-20:00",
      "bestPerformingDay": "Friday"
    },
    "audienceInsights": {
      "topFaculties": [
        { "faculty": "Arts", "percentage": 35 },
        { "faculty": "Business", "percentage": 28 },
        { "faculty": "Engineering", "percentage": 22 }
      ],
      "ageGroups": [
        { "range": "18-20", "percentage": 45 },
        { "range": "21-23", "percentage": 40 },
        { "range": "24+", "percentage": 15 }
      ],
      "genderDistribution": {
        "female": 58,
        "male": 40,
        "other": 2
      }
    },
    "rankings": {
      "currentRank": 15,
      "previousRank": 18,
      "score": 1250,
      "category": "Fashion & Style",
      "percentile": 85
    }
  }
}
```

#### GET /api/v1/analytics/users/{userId}/performance
**Purpose**: Get detailed user performance metrics

#### GET /api/v1/analytics/users/{userId}/engagement
**Purpose**: Get user engagement analytics

### Content Analytics

#### GET /api/v1/analytics/content/{contentId}
**Purpose**: Get content performance analytics
**Response**:
```json
{
  "success": true,
  "analytics": {
    "contentId": "post_123",
    "contentType": "post",
    "performance": {
      "views": 245,
      "uniqueViews": 198,
      "likes": 34,
      "comments": 8,
      "shares": 3,
      "bookmarks": 12,
      "engagementRate": 0.18,
      "reachRate": 0.85
    },
    "demographics": {
      "topFaculties": ["Arts", "Business", "Engineering"],
      "ageGroups": [
        { "range": "18-20", "views": 98 },
        { "range": "21-23", "views": 87 },
        { "range": "24+", "views": 13 }
      ],
      "genderDistribution": {
        "female": 145,
        "male": 89,
        "other": 11
      }
    },
    "timeline": {
      "hourlyViews": [
        { "hour": "08:00", "views": 12 },
        { "hour": "09:00", "views": 18 },
        { "hour": "10:00", "views": 25 }
      ],
      "peakEngagementTime": "18:30"
    },
    "sources": {
      "feed": 156,
      "profile": 45,
      "search": 23,
      "direct": 21
    }
  }
}
```

### Rankings System

#### GET /api/v1/analytics/rankings
**Purpose**: Get current creator rankings
**Query Parameters**:
- `category`: Ranking category
- `faculty`: Faculty filter
- `period`: Time period (monthly, weekly)
- `limit`: Number of results

**Response**:
```json
{
  "success": true,
  "rankings": {
    "period": "august_2024",
    "category": "overall",
    "prizePool": 150.00,
    "currency": "GHS",
    "lastUpdated": "2024-08-14T10:30:00Z",
    "leaderboard": [
      {
        "rank": 1,
        "user": {
          "id": "user_456",
          "username": "fashionista_gh",
          "name": "Sarah Miller",
          "avatar": "https://cdn.raved.app/avatars/user_456.jpg",
          "faculty": "Arts"
        },
        "score": 2450,
        "metrics": {
          "totalViews": 5200,
          "totalLikes": 456,
          "totalComments": 89,
          "engagementRate": 0.22,
          "followerGrowth": 45
        },
        "prize": 75.00,
        "badge": "gold"
      },
      {
        "rank": 2,
        "user": {
          "id": "user_789",
          "username": "style_maven",
          "name": "Emma Wilson",
          "avatar": "https://cdn.raved.app/avatars/user_789.jpg",
          "faculty": "Business"
        },
        "score": 2180,
        "prize": 45.00,
        "badge": "silver"
      }
    ],
    "userRank": {
      "rank": 15,
      "score": 1250,
      "percentile": 85,
      "pointsToNextRank": 150
    }
  }
}
```

#### GET /api/v1/analytics/rankings/history
**Purpose**: Get historical rankings data

#### GET /api/v1/analytics/rankings/categories
**Purpose**: Get available ranking categories

### Business Analytics

#### GET /api/v1/analytics/business/overview
**Purpose**: Get business overview analytics (admin only)
**Response**:
```json
{
  "success": true,
  "analytics": {
    "period": "last_30_days",
    "users": {
      "totalUsers": 5420,
      "activeUsers": 3240,
      "newUsers": 234,
      "retentionRate": 0.78
    },
    "content": {
      "totalPosts": 12450,
      "newPosts": 890,
      "totalViews": 245000,
      "averageEngagement": 0.15
    },
    "ecommerce": {
      "totalOrders": 156,
      "totalRevenue": 4250.00,
      "averageOrderValue": 27.24,
      "conversionRate": 0.048
    },
    "subscriptions": {
      "totalSubscribers": 89,
      "subscriptionRevenue": 890.00,
      "churnRate": 0.12
    }
  }
}
```

### Custom Reports

#### POST /api/v1/analytics/reports/generate
**Purpose**: Generate custom analytics report
```json
{
  "reportType": "user_engagement",
  "dateRange": {
    "startDate": "2024-08-01T00:00:00Z",
    "endDate": "2024-08-31T23:59:59Z"
  },
  "filters": {
    "faculty": "Arts",
    "contentType": "post",
    "userSegment": "premium"
  },
  "metrics": ["views", "likes", "comments", "shares"],
  "format": "json"
}
```

## 3. Data Models & Database Schema

### Database Choice: MongoDB
**Rationale**: Analytics data requires flexible schema for different event types, high write performance for event ingestion, and efficient aggregation for reporting.

### Collections Schema

#### analytics_events
```javascript
{
  _id: ObjectId,
  userId: String, // Reference to User Service
  sessionId: String,
  eventType: String, // 'content_view', 'content_like', 'profile_view', etc.
  
  // Event data (flexible schema)
  eventData: {
    contentId: String,
    contentType: String,
    duration: Number,
    source: String,
    deviceType: String,
    // ... other event-specific data
  },
  
  // Context
  timestamp: Date,
  userAgent: String,
  ipAddress: String,
  location: {
    country: String,
    city: String,
    coordinates: [Number, Number]
  },
  
  // Processing status
  processed: Boolean,
  processedAt: Date,
  
  // Indexes
  indexes: [
    { userId: 1, timestamp: -1 },
    { eventType: 1, timestamp: -1 },
    { sessionId: 1, timestamp: 1 },
    { timestamp: -1 }, // TTL index for data retention
    { processed: 1, timestamp: 1 }
  ]
}
```

#### user_analytics
```javascript
{
  _id: ObjectId,
  userId: String, // Reference to User Service
  period: String, // 'daily', 'weekly', 'monthly'
  date: Date, // Period start date
  
  // Aggregated metrics
  metrics: {
    contentViews: Number,
    profileViews: Number,
    likes: Number,
    comments: Number,
    shares: Number,
    bookmarks: Number,
    followers: Number,
    following: Number,
    posts: Number,
    stories: Number
  },
  
  // Engagement metrics
  engagement: {
    totalEngagements: Number,
    engagementRate: Number,
    averageViewDuration: Number,
    topContentId: String,
    bestPerformingHour: Number
  },
  
  // Rankings
  rankings: {
    overallScore: Number,
    categoryScore: Number,
    rank: Number,
    percentile: Number
  },
  
  // Audience insights
  audience: {
    topFaculties: [{ faculty: String, count: Number }],
    ageGroups: [{ range: String, count: Number }],
    genderDistribution: { male: Number, female: Number, other: Number }
  },
  
  // Timestamps
  createdAt: Date,
  updatedAt: Date,
  
  // Indexes
  indexes: [
    { userId: 1, period: 1, date: -1 },
    { period: 1, date: -1 },
    { "rankings.overallScore": -1, period: 1, date: -1 }
  ]
}
```

#### content_analytics
```javascript
{
  _id: ObjectId,
  contentId: String,
  contentType: String, // 'post', 'story', 'product'
  authorId: String,
  
  // Performance metrics
  metrics: {
    views: Number,
    uniqueViews: Number,
    likes: Number,
    comments: Number,
    shares: Number,
    bookmarks: Number,
    clickThroughs: Number,
    averageViewDuration: Number
  },
  
  // Calculated metrics
  calculated: {
    engagementRate: Number,
    reachRate: Number,
    viralityScore: Number,
    qualityScore: Number
  },
  
  // Demographics
  demographics: {
    faculties: [{ faculty: String, views: Number }],
    ageGroups: [{ range: String, views: Number }],
    genderDistribution: { male: Number, female: Number, other: Number }
  },
  
  // Timeline data
  timeline: {
    hourlyViews: [{ hour: Number, views: Number }],
    dailyViews: [{ date: Date, views: Number }],
    peakEngagementTime: Date
  },
  
  // Traffic sources
  sources: {
    feed: Number,
    profile: Number,
    search: Number,
    direct: Number,
    external: Number
  },
  
  // Timestamps
  createdAt: Date,
  lastUpdated: Date,
  
  // Indexes
  indexes: [
    { contentId: 1 }, // Unique
    { authorId: 1, createdAt: -1 },
    { contentType: 1, "calculated.engagementRate": -1 },
    { "metrics.views": -1, createdAt: -1 }
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
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
</dependencies>
```

### Configuration
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://raved_admin:theRAVEDapp%23123@mongodb:27017/raved_analytics?authSource=admin
  
  kafka:
    consumer:
      group-id: analytics-service
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer

# Analytics configuration
analytics:
  batch-processing:
    enabled: true
    schedule: "0 0 2 * * ?" # Daily at 2 AM
  
  rankings:
    prize-pool: 150.00
    currency: GHS
    calculation-schedule: "0 0 1 * * ?" # Daily at 1 AM
  
  data-retention:
    raw-events: 90 # days
    aggregated-data: 365 # days
```

### Performance Optimizations
- **Event Streaming**: Kafka for real-time event processing
- **Batch Processing**: Scheduled aggregation jobs
- **Caching**: Redis for frequently accessed analytics
- **Data Partitioning**: MongoDB sharding by userId and date
