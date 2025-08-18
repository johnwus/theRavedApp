# Content Service Documentation

## 1. Service Overview

### Primary Purpose
The Content Service manages all user-generated content in TheRavedApp, including posts (image/video/carousel), stories, comments, and media processing. It provides a flexible, scalable platform for content creation, discovery, and engagement.

### Core Responsibilities
- **Content Management**: CRUD operations for posts, stories, and comments
- **Media Processing**: Image/video upload, processing, and CDN distribution
- **Content Discovery**: Search, filtering, and recommendation algorithms
- **Engagement Tracking**: Likes, comments, shares, and view analytics
- **Content Moderation**: Automated and manual content review systems
- **Feed Generation**: Personalized content feeds and trending algorithms

### Service Boundaries
- **Owns**: Posts, stories, comments, media files, content metadata, engagement data
- **Does NOT Own**: User profiles, social connections, e-commerce transactions
- **Interfaces With**: User Service for authentication, Social Service for engagement, Analytics Service for metrics

### Business Domain Ownership
- Content creation and lifecycle management
- Media storage and processing
- Content discovery and search
- Engagement and interaction tracking

### Performance Requirements
- **Content Upload**: < 5s for image processing, < 30s for video processing
- **Feed Loading**: < 1s for initial feed, < 500ms for pagination
- **Search**: < 300ms for content search results
- **Concurrent Users**: Support 50,000+ simultaneous content interactions
- **Storage**: Scalable media storage with CDN distribution

## 2. API Specification

### Posts Management

#### POST /api/v1/posts
**Purpose**: Create new post (image, video, or carousel)
```json
{
  "type": "image",
  "caption": "Perfect outfit for today's presentation! 💼 #CampusStyle",
  "tags": ["#CampusStyle", "#OOTD", "#Professional"],
  "location": "Campus Library",
  "visibility": "public",
  "isForSale": true,
  "saleDetails": {
    "price": 45.00,
    "condition": "excellent",
    "size": "M",
    "brand": "Zara"
  },
  "mediaFiles": ["file1.jpg"]
}
```

**Response**:
```json
{
  "success": true,
  "post": {
    "id": "post_123",
    "userId": "user_456",
    "type": "image",
    "caption": "Perfect outfit for today's presentation! 💼 #CampusStyle",
    "tags": ["#CampusStyle", "#OOTD", "#Professional"],
    "location": "Campus Library",
    "visibility": "public",
    "mediaUrls": ["https://cdn.raved.app/posts/post_123_1.jpg"],
    "thumbnailUrl": "https://cdn.raved.app/posts/post_123_thumb.jpg",
    "isForSale": true,
    "saleDetails": {
      "price": 45.00,
      "condition": "excellent",
      "size": "M",
      "brand": "Zara"
    },
    "engagement": {
      "likes": 0,
      "comments": 0,
      "shares": 0,
      "views": 0
    },
    "createdAt": "2024-08-14T10:30:00Z",
    "updatedAt": "2024-08-14T10:30:00Z"
  }
}
```

#### GET /api/v1/posts/feed
**Purpose**: Get personalized content feed
**Query Parameters**:
- `page`: Page number (default: 0)
- `size`: Items per page (default: 20)
- `faculty`: Filter by faculty (optional)
- `type`: Filter by post type (optional)

**Response**:
```json
{
  "success": true,
  "posts": [
    {
      "id": "post_123",
      "user": {
        "id": "user_456",
        "username": "alexj2024",
        "name": "Alex Johnson",
        "avatar": "https://cdn.raved.app/avatars/user_456.jpg",
        "faculty": "Computer Science"
      },
      "type": "image",
      "caption": "Perfect outfit for today's presentation! 💼",
      "tags": ["#CampusStyle", "#OOTD"],
      "location": "Campus Library",
      "mediaUrls": ["https://cdn.raved.app/posts/post_123_1.jpg"],
      "thumbnailUrl": "https://cdn.raved.app/posts/post_123_thumb.jpg",
      "engagement": {
        "likes": 24,
        "comments": 5,
        "shares": 2,
        "views": 156,
        "userLiked": false,
        "userBookmarked": true
      },
      "timeAgo": "2h ago",
      "createdAt": "2024-08-14T08:30:00Z"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "hasNext": true
  }
}
```

#### GET /api/v1/posts/{postId}
**Purpose**: Get specific post details

#### PUT /api/v1/posts/{postId}
**Purpose**: Update post (owner only)

#### DELETE /api/v1/posts/{postId}
**Purpose**: Delete post (owner only)

### Stories Management

#### POST /api/v1/stories
**Purpose**: Create new story
```json
{
  "type": "image",
  "mediaFile": "story_image.jpg",
  "text": "Having a great day! ✨",
  "template": "gradient_blue",
  "duration": 24
}
```

#### GET /api/v1/stories/feed
**Purpose**: Get stories feed for following users

#### GET /api/v1/stories/user/{userId}
**Purpose**: Get user's active stories

### Comments Management

#### POST /api/v1/posts/{postId}/comments
**Purpose**: Add comment to post
```json
{
  "text": "Love this outfit! Where did you get the jacket?",
  "parentCommentId": null
}
```

#### GET /api/v1/posts/{postId}/comments
**Purpose**: Get post comments with pagination

#### PUT /api/v1/comments/{commentId}
**Purpose**: Update comment (owner only)

#### DELETE /api/v1/comments/{commentId}
**Purpose**: Delete comment (owner only)

### Engagement Endpoints

#### POST /api/v1/posts/{postId}/like
**Purpose**: Toggle like on post

#### POST /api/v1/posts/{postId}/bookmark
**Purpose**: Toggle bookmark on post

#### POST /api/v1/posts/{postId}/share
**Purpose**: Share post

#### GET /api/v1/posts/{postId}/engagement
**Purpose**: Get detailed engagement metrics

### Search & Discovery

#### GET /api/v1/content/search
**Purpose**: Search posts and stories
**Query Parameters**:
- `q`: Search query
- `type`: Content type filter
- `faculty`: Faculty filter
- `tags`: Tag filters
- `dateRange`: Date range filter

#### GET /api/v1/content/trending
**Purpose**: Get trending content

#### GET /api/v1/content/explore
**Purpose**: Get explore/discovery feed

### Media Upload

#### POST /api/v1/media/upload
**Purpose**: Upload media files
**Content-Type**: `multipart/form-data`
**Body**: Media files with metadata

**Response**:
```json
{
  "success": true,
  "files": [
    {
      "id": "media_789",
      "originalName": "outfit.jpg",
      "url": "https://cdn.raved.app/uploads/media_789.jpg",
      "thumbnailUrl": "https://cdn.raved.app/uploads/media_789_thumb.jpg",
      "type": "image",
      "size": 2048576,
      "dimensions": {
        "width": 1080,
        "height": 1350
      },
      "processingStatus": "completed"
    }
  ]
}
```

### Rate Limiting
- **Post Creation**: 10 posts per hour per user
- **Story Creation**: 20 stories per day per user
- **Comments**: 100 comments per hour per user
- **Media Upload**: 50MB per hour per user
- **Search**: 100 requests per minute per user

## 3. Data Models & Database Schema

### Database Choice: MongoDB
**Rationale**: Content data requires flexible schema for different post types, horizontal scaling for large media collections, and optimized read performance for feed generation.

### Collections Schema

#### posts
```javascript
{
  _id: ObjectId,
  userId: String, // Reference to User Service
  type: String, // 'image', 'video', 'carousel'
  caption: String,
  tags: [String],
  location: String,
  visibility: String, // 'public', 'followers', 'private'
  
  // Media data
  mediaUrls: [String],
  thumbnailUrl: String,
  mediaMetadata: {
    dimensions: { width: Number, height: Number },
    duration: Number, // for videos
    fileSize: Number,
    format: String
  },
  
  // Sale information
  isForSale: Boolean,
  saleDetails: {
    price: Number,
    condition: String,
    size: String,
    brand: String,
    category: String
  },
  
  // Engagement metrics
  engagement: {
    likes: Number,
    comments: Number,
    shares: Number,
    views: Number,
    bookmarks: Number
  },
  
  // Moderation
  moderationStatus: String, // 'pending', 'approved', 'rejected'
  moderationFlags: [String],
  
  // Timestamps
  createdAt: Date,
  updatedAt: Date,
  
  // Indexes
  indexes: [
    { userId: 1, createdAt: -1 },
    { tags: 1, createdAt: -1 },
    { location: 1, createdAt: -1 },
    { "engagement.likes": -1, createdAt: -1 },
    { isForSale: 1, createdAt: -1 },
    { moderationStatus: 1 }
  ]
}
```

#### stories
```javascript
{
  _id: ObjectId,
  userId: String,
  type: String, // 'image', 'video', 'text'
  mediaUrl: String,
  thumbnailUrl: String,
  text: String,
  template: String,
  
  // Story-specific data
  duration: Number, // hours until expiry
  expiresAt: Date,
  
  // Engagement
  views: [{
    userId: String,
    viewedAt: Date
  }],
  viewCount: Number,
  
  // Timestamps
  createdAt: Date,
  
  // Indexes
  indexes: [
    { userId: 1, expiresAt: 1 },
    { expiresAt: 1 }, // for cleanup
    { createdAt: -1 }
  ]
}
```

#### comments
```javascript
{
  _id: ObjectId,
  postId: String,
  userId: String,
  text: String,
  parentCommentId: String, // for threaded comments
  
  // Engagement
  likes: Number,
  likedBy: [String], // user IDs
  
  // Moderation
  moderationStatus: String,
  moderationFlags: [String],
  
  // Timestamps
  createdAt: Date,
  updatedAt: Date,
  
  // Indexes
  indexes: [
    { postId: 1, createdAt: 1 },
    { userId: 1, createdAt: -1 },
    { parentCommentId: 1, createdAt: 1 }
  ]
}
```

#### media_files
```javascript
{
  _id: ObjectId,
  userId: String,
  originalName: String,
  fileName: String,
  url: String,
  thumbnailUrl: String,
  type: String, // 'image', 'video'
  mimeType: String,
  size: Number,

  // Processing status
  processingStatus: String, // 'pending', 'processing', 'completed', 'failed'
  processingError: String,

  // Metadata
  metadata: {
    dimensions: { width: Number, height: Number },
    duration: Number,
    bitrate: Number,
    format: String,
    colorProfile: String
  },

  // CDN information
  cdnUrls: {
    original: String,
    large: String,
    medium: String,
    small: String,
    thumbnail: String
  },

  // Usage tracking
  usedInPosts: [String], // post IDs
  usedInStories: [String], // story IDs

  createdAt: Date,

  indexes: [
    { userId: 1, createdAt: -1 },
    { processingStatus: 1 },
    { type: 1, createdAt: -1 }
  ]
}
```

### Data Validation Rules
- **Caption**: Maximum 2000 characters
- **Tags**: Maximum 10 tags per post, each tag 50 characters max
- **Media Files**: Maximum 10MB per image, 100MB per video
- **Story Duration**: 1-48 hours
- **Comment Text**: Maximum 500 characters

## 4. Service Discovery & Communication

### Eureka Configuration
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    instance-id: ${spring.application.name}:${server.port}
    prefer-ip-address: true

spring:
  application:
    name: content-service
```

### Inter-Service Communication

#### Synchronous REST Calls
```java
// User Service - Get user profile data
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{userId}/profile")
    UserProfile getUserProfile(@PathVariable String userId);

    @GetMapping("/api/v1/users/{userId}/permissions")
    UserPermissions getUserPermissions(@PathVariable String userId);
}

// Social Service - Get engagement data
@FeignClient(name = "social-service")
public interface SocialServiceClient {
    @GetMapping("/api/v1/users/{userId}/following")
    List<String> getUserFollowing(@PathVariable String userId);

    @PostMapping("/api/v1/engagement/like")
    void recordLike(@RequestBody LikeEvent likeEvent);
}
```

#### Asynchronous Messaging (Kafka)
```yaml
# Published Topics
- content.created: New post/story created
- content.updated: Content modified
- content.deleted: Content removed
- content.engagement: Like/comment/share events
- content.moderation.required: Content flagged for review

# Consumed Topics
- user.updated: User profile changes (update content author info)
- social.follow: Update feed algorithms
- moderation.decision: Content moderation results
```

### Circuit Breaker Configuration
```yaml
resilience4j:
  circuitbreaker:
    instances:
      user-service:
        failure-rate-threshold: 60
        wait-duration-in-open-state: 30s
      social-service:
        failure-rate-threshold: 70
        wait-duration-in-open-state: 20s
```

## 5. Integration Points

### Dependencies on Other Services

#### User Service Integration
```java
// Validate content ownership and permissions
@Service
public class ContentAuthorizationService {

    @Autowired
    private UserServiceClient userServiceClient;

    public boolean canUserCreateContent(String userId) {
        UserPermissions permissions = userServiceClient.getUserPermissions(userId);
        return permissions.isActive() && permissions.canCreateContent();
    }

    public boolean canUserModerateContent(String userId) {
        UserPermissions permissions = userServiceClient.getUserPermissions(userId);
        return permissions.hasRole("MODERATOR") || permissions.hasRole("ADMIN");
    }
}
```

#### Analytics Service Integration
```java
// Send content analytics events
@EventListener
public void handleContentEngagement(ContentEngagementEvent event) {
    AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
        .eventType("content_engagement")
        .userId(event.getUserId())
        .contentId(event.getContentId())
        .action(event.getAction()) // like, comment, share, view
        .timestamp(event.getTimestamp())
        .metadata(event.getMetadata())
        .build();

    kafkaTemplate.send("analytics.content.engagement", analyticsEvent);
}
```

### Event-Driven Communication

#### Published Events
```java
// Content creation event
@EventHandler
public void publishContentCreatedEvent(Post post) {
    ContentCreatedEvent event = ContentCreatedEvent.builder()
        .contentId(post.getId())
        .userId(post.getUserId())
        .contentType(post.getType())
        .tags(post.getTags())
        .isForSale(post.getIsForSale())
        .createdAt(post.getCreatedAt())
        .build();

    kafkaTemplate.send("content.created", event);
}

// Engagement event
@EventHandler
public void publishEngagementEvent(String postId, String userId, String action) {
    ContentEngagementEvent event = ContentEngagementEvent.builder()
        .contentId(postId)
        .userId(userId)
        .action(action)
        .timestamp(Instant.now())
        .build();

    kafkaTemplate.send("content.engagement", event);
}
```

#### Consumed Events
```java
// Handle user profile updates
@KafkaListener(topics = "user.updated")
public void handleUserProfileUpdate(UserUpdatedEvent event) {
    // Update cached user data in posts and comments
    contentService.updateUserDataInContent(event.getUserId(), event.getUserProfile());
}

// Handle moderation decisions
@KafkaListener(topics = "moderation.decision")
public void handleModerationDecision(ModerationDecisionEvent event) {
    if (event.getDecision() == ModerationDecision.APPROVED) {
        contentService.approveContent(event.getContentId());
    } else {
        contentService.rejectContent(event.getContentId(), event.getReason());
    }
}
```

### Data Consistency Strategy
- **Eventual Consistency**: User profile data in posts (cached and updated via events)
- **Strong Consistency**: Content ownership and permissions
- **Optimistic Locking**: Concurrent engagement updates (likes, comments)

## 6. Technology Stack & Infrastructure

### Framework & Dependencies
```xml
<!-- Spring Boot Content Service -->
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
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-java-sdk-s3</artifactId>
    </dependency>
</dependencies>
```

### Database Configuration
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://raved_admin:theRAVEDapp%23123@mongodb:27017/raved_content?authSource=admin
      database: raved_content

  redis:
    host: redis
    port: 6379
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 20
        max-idle: 10
```

### Media Processing Configuration
```yaml
# Media processing settings
media:
  upload:
    max-file-size: 100MB
    allowed-types: image/jpeg,image/png,image/webp,video/mp4,video/quicktime

  processing:
    image:
      thumbnail-size: 300x300
      sizes: [400x400, 800x800, 1200x1200]
      quality: 85
    video:
      thumbnail-time: 1s
      max-duration: 300s
      formats: [mp4, webm]

  storage:
    provider: aws-s3
    bucket: raved-media-content
    cdn-domain: https://cdn.raved.app
```

### Docker Configuration
```dockerfile
FROM openjdk:17-jre-slim

# Install FFmpeg for video processing
RUN apt-get update && apt-get install -y ffmpeg && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY target/content-service-1.0.0.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Performance Optimizations
- **MongoDB Sharding**: Shard by userId for horizontal scaling
- **Redis Caching**: Cache popular posts and user feeds
- **CDN Integration**: AWS CloudFront for media delivery
- **Async Processing**: Background media processing with queues
- **Database Indexing**: Optimized compound indexes for feed queries
