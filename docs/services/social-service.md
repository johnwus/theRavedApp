# Social Service Documentation

## 1. Service Overview

### Primary Purpose
The Social Service manages all social interactions in TheRavedApp, including user connections, follow/unfollow relationships, social engagement tracking, friend recommendations, and social networking features.

### Core Responsibilities
- **Connection Management**: Follow/unfollow relationships between users
- **Social Interactions**: Like, bookmark, share, and comment tracking
- **Friend Recommendations**: Algorithm-based user discovery
- **Social Feed**: Following-based content curation
- **Engagement Analytics**: Social interaction metrics and insights
- **Privacy Controls**: Social visibility and interaction permissions

### Service Boundaries
- **Owns**: User connections, social interactions, engagement metrics, friend suggestions
- **Does NOT Own**: User profiles, content creation, direct messaging
- **Interfaces With**: User Service for profiles, Content Service for engagement, Analytics Service for metrics

### Business Domain Ownership
- Social graph management and relationship tracking
- Social engagement and interaction patterns
- User discovery and recommendation algorithms
- Social privacy and permission controls

### Performance Requirements
- **Follow/Unfollow**: < 200ms response time
- **Social Feed**: < 1s for feed generation
- **Friend Recommendations**: < 500ms for suggestion list
- **Engagement Tracking**: < 100ms for like/bookmark operations
- **Concurrent Users**: Support 25,000+ simultaneous social interactions

## 2. API Specification

### Connection Management

#### POST /api/v1/social/follow
**Purpose**: Follow another user
```json
{
  "targetUserId": "user_456"
}
```

**Response**:
```json
{
  "success": true,
  "connection": {
    "id": "conn_123",
    "followerId": "user_123",
    "followingId": "user_456",
    "status": "following",
    "createdAt": "2024-08-14T10:30:00Z"
  },
  "mutualFollow": false
}
```

#### DELETE /api/v1/social/follow/{userId}
**Purpose**: Unfollow a user

#### GET /api/v1/social/following
**Purpose**: Get list of users the current user is following
**Query Parameters**:
- `page`, `size`: Pagination
- `search`: Search within following list

**Response**:
```json
{
  "success": true,
  "following": [
    {
      "id": "user_456",
      "username": "fashionista_gh",
      "name": "Sarah Miller",
      "avatar": "https://cdn.raved.app/avatars/user_456.jpg",
      "faculty": "Arts",
      "bio": "Fashion enthusiast and style blogger",
      "followedAt": "2024-08-10T14:30:00Z",
      "mutualFollow": true,
      "isOnline": false,
      "lastSeen": "2024-08-14T09:15:00Z"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 45,
    "totalPages": 3,
    "hasNext": true
  }
}
```

#### GET /api/v1/social/followers
**Purpose**: Get list of users following the current user

#### GET /api/v1/social/users/{userId}/following
**Purpose**: Get public following list for specific user

#### GET /api/v1/social/users/{userId}/followers
**Purpose**: Get public followers list for specific user

### Social Interactions

#### POST /api/v1/social/interactions/like
**Purpose**: Like content (post, comment, product)
```json
{
  "targetType": "post",
  "targetId": "post_123"
}
```

#### DELETE /api/v1/social/interactions/like
**Purpose**: Unlike content
```json
{
  "targetType": "post",
  "targetId": "post_123"
}
```

#### POST /api/v1/social/interactions/bookmark
**Purpose**: Bookmark content
```json
{
  "targetType": "post",
  "targetId": "post_123"
}
```

#### GET /api/v1/social/interactions/bookmarks
**Purpose**: Get user's bookmarked content
**Response**:
```json
{
  "success": true,
  "bookmarks": [
    {
      "id": "bookmark_789",
      "targetType": "post",
      "targetId": "post_123",
      "content": {
        "id": "post_123",
        "type": "image",
        "caption": "Perfect outfit for today's presentation!",
        "thumbnailUrl": "https://cdn.raved.app/posts/post_123_thumb.jpg",
        "user": {
          "id": "user_456",
          "username": "fashionista_gh",
          "name": "Sarah Miller",
          "avatar": "https://cdn.raved.app/avatars/user_456.jpg"
        }
      },
      "bookmarkedAt": "2024-08-14T10:30:00Z"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 23,
    "hasNext": true
  }
}
```

#### POST /api/v1/social/interactions/share
**Purpose**: Share content
```json
{
  "targetType": "post",
  "targetId": "post_123",
  "shareType": "story",
  "message": "Check out this amazing outfit!"
}
```

### Friend Recommendations

#### GET /api/v1/social/recommendations/users
**Purpose**: Get friend recommendations
**Query Parameters**:
- `limit`: Number of recommendations (default: 10)
- `faculty`: Filter by faculty
- `interests`: Filter by interests

**Response**:
```json
{
  "success": true,
  "recommendations": [
    {
      "user": {
        "id": "user_789",
        "username": "style_maven",
        "name": "Emma Wilson",
        "avatar": "https://cdn.raved.app/avatars/user_789.jpg",
        "faculty": "Arts",
        "bio": "Fashion design student",
        "followersCount": 234,
        "followingCount": 156
      },
      "reason": "mutual_friends",
      "mutualFriends": [
        {
          "id": "user_456",
          "name": "Sarah Miller",
          "avatar": "https://cdn.raved.app/avatars/user_456.jpg"
        }
      ],
      "mutualFriendsCount": 3,
      "score": 0.85
    }
  ],
  "metadata": {
    "algorithm": "collaborative_filtering",
    "generatedAt": "2024-08-14T10:30:00Z"
  }
}
```

#### POST /api/v1/social/recommendations/feedback
**Purpose**: Provide feedback on recommendations
```json
{
  "recommendationId": "rec_123",
  "action": "followed",
  "feedback": "relevant"
}
```

### Social Feed

#### GET /api/v1/social/feed
**Purpose**: Get social feed based on following relationships
**Query Parameters**:
- `page`, `size`: Pagination
- `type`: Content type filter
- `timeRange`: Time range filter

**Response**:
```json
{
  "success": true,
  "feed": [
    {
      "id": "feed_item_123",
      "type": "post",
      "content": {
        "id": "post_123",
        "user": {
          "id": "user_456",
          "username": "fashionista_gh",
          "name": "Sarah Miller",
          "avatar": "https://cdn.raved.app/avatars/user_456.jpg"
        },
        "type": "image",
        "caption": "Perfect outfit for today's presentation!",
        "mediaUrls": ["https://cdn.raved.app/posts/post_123_1.jpg"],
        "engagement": {
          "likes": 24,
          "comments": 5,
          "userLiked": false,
          "userBookmarked": true
        },
        "createdAt": "2024-08-14T08:30:00Z"
      },
      "feedReason": "following",
      "priority": 0.8,
      "insertedAt": "2024-08-14T10:30:00Z"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "hasNext": true
  }
}
```

### Social Analytics

#### GET /api/v1/social/analytics/engagement
**Purpose**: Get user's social engagement analytics
**Response**:
```json
{
  "success": true,
  "analytics": {
    "period": "last_30_days",
    "engagement": {
      "totalLikes": 156,
      "totalComments": 23,
      "totalShares": 8,
      "totalBookmarks": 45
    },
    "growth": {
      "newFollowers": 12,
      "newFollowing": 8,
      "unfollowers": 2
    },
    "topContent": [
      {
        "contentId": "post_123",
        "type": "post",
        "likes": 24,
        "comments": 5,
        "shares": 2
      }
    ],
    "audienceInsights": {
      "topFaculties": ["Arts", "Business", "Engineering"],
      "engagementByTime": {
        "morning": 0.3,
        "afternoon": 0.5,
        "evening": 0.2
      }
    }
  }
}
```

### Privacy & Settings

#### GET /api/v1/social/settings
**Purpose**: Get user's social privacy settings

#### PUT /api/v1/social/settings
**Purpose**: Update social privacy settings
```json
{
  "profileVisibility": "public",
  "followersVisible": true,
  "followingVisible": true,
  "allowRecommendations": true,
  "showOnlineStatus": false,
  "allowTagging": true
}
```

### Rate Limiting
- **Follow/Unfollow**: 100 actions per hour per user
- **Like/Unlike**: 500 actions per hour per user
- **Bookmark**: 200 actions per hour per user
- **Share**: 50 actions per hour per user
- **Feed Requests**: 200 requests per hour per user

## 3. Data Models & Database Schema

### Database Choice: MongoDB
**Rationale**: Social data requires flexible schema for different interaction types, high read performance for feed generation, and horizontal scaling for large social graphs.

### Collections Schema

#### connections
```javascript
{
  _id: ObjectId,
  followerId: String, // User ID who is following
  followingId: String, // User ID being followed
  status: String, // 'following', 'blocked', 'muted'
  
  // Relationship metadata
  mutualFollow: Boolean,
  connectionStrength: Number, // Algorithm-calculated strength
  
  // Interaction tracking
  lastInteraction: Date,
  interactionCount: Number,
  
  // Privacy
  isPrivate: Boolean,
  
  // Timestamps
  createdAt: Date,
  updatedAt: Date,
  
  // Indexes
  indexes: [
    { followerId: 1, followingId: 1 }, // Unique compound
    { followerId: 1, createdAt: -1 },
    { followingId: 1, createdAt: -1 },
    { status: 1 },
    { mutualFollow: 1 }
  ]
}
```

#### social_interactions
```javascript
{
  _id: ObjectId,
  userId: String, // User performing the action
  targetType: String, // 'post', 'comment', 'product', 'story'
  targetId: String, // ID of the target content
  interactionType: String, // 'like', 'bookmark', 'share'
  
  // Interaction metadata
  metadata: {
    shareType: String, // for shares: 'story', 'direct', 'external'
    shareMessage: String,
    platform: String // for external shares
  },
  
  // Context
  sourceLocation: String, // where the interaction happened
  deviceType: String,
  
  // Timestamps
  createdAt: Date,
  
  // Indexes
  indexes: [
    { userId: 1, interactionType: 1, createdAt: -1 },
    { targetType: 1, targetId: 1, interactionType: 1 },
    { userId: 1, targetId: 1, interactionType: 1 }, // Unique compound
    { createdAt: -1 }
  ]
}
```
