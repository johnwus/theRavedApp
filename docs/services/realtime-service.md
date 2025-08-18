# Realtime Service Documentation

## 1. Service Overview

### Primary Purpose
The Realtime Service manages all real-time communication features in TheRavedApp, including WebSocket connections, live updates, real-time messaging, presence indicators, and live notifications.

### Core Responsibilities
- **WebSocket Management**: Persistent connections for real-time communication
- **Live Updates**: Real-time feed updates, engagement notifications
- **Messaging System**: Direct messages and group conversations
- **Presence Tracking**: Online/offline status and activity indicators
- **Live Notifications**: Instant notification delivery
- **Real-time Events**: Live event broadcasting and updates

### Service Boundaries
- **Owns**: WebSocket connections, messages, presence data, real-time events
- **Does NOT Own**: User profiles, persistent content, notification preferences
- **Interfaces With**: All services for real-time event broadcasting

### Performance Requirements
- **Connection Establishment**: < 500ms WebSocket handshake
- **Message Delivery**: < 100ms end-to-end latency
- **Concurrent Connections**: Support 10,000+ simultaneous WebSocket connections
- **Message Throughput**: 50,000+ messages per second
- **Presence Updates**: < 200ms for status changes

## 2. API Specification

### WebSocket Connection

#### WS /api/v1/realtime/connect
**Purpose**: Establish WebSocket connection
**Authentication**: JWT token in query parameter or header
**Connection URL**: `ws://localhost:8084/api/v1/realtime/connect?token={jwt_token}`

**Connection Response**:
```json
{
  "type": "connection_established",
  "data": {
    "connectionId": "conn_123",
    "userId": "user_456",
    "serverTime": "2024-08-14T10:30:00Z",
    "features": ["messaging", "notifications", "presence", "live_updates"]
  }
}
```

### Real-time Messaging

#### Send Message
**WebSocket Message**:
```json
{
  "type": "send_message",
  "data": {
    "conversationId": "conv_789",
    "message": {
      "text": "Hey! Love your latest post! 😍",
      "type": "text",
      "replyTo": null
    }
  }
}
```

**Response**:
```json
{
  "type": "message_sent",
  "data": {
    "messageId": "msg_123",
    "conversationId": "conv_789",
    "senderId": "user_456",
    "message": {
      "text": "Hey! Love your latest post! 😍",
      "type": "text",
      "replyTo": null
    },
    "timestamp": "2024-08-14T10:30:00Z",
    "status": "sent"
  }
}
```

#### Receive Message
**WebSocket Message**:
```json
{
  "type": "message_received",
  "data": {
    "messageId": "msg_124",
    "conversationId": "conv_789",
    "sender": {
      "id": "user_789",
      "username": "alex_j",
      "name": "Alex Johnson",
      "avatar": "https://cdn.raved.app/avatars/user_789.jpg"
    },
    "message": {
      "text": "Thank you! That means a lot! ❤️",
      "type": "text",
      "replyTo": "msg_123"
    },
    "timestamp": "2024-08-14T10:31:00Z",
    "isRead": false
  }
}
```

### Live Updates

#### Content Engagement Updates
**WebSocket Message**:
```json
{
  "type": "content_engagement",
  "data": {
    "contentId": "post_123",
    "contentType": "post",
    "engagementType": "like",
    "user": {
      "id": "user_789",
      "username": "alex_j",
      "name": "Alex Johnson",
      "avatar": "https://cdn.raved.app/avatars/user_789.jpg"
    },
    "newCounts": {
      "likes": 25,
      "comments": 5,
      "shares": 2
    },
    "timestamp": "2024-08-14T10:30:00Z"
  }
}
```

#### New Content Notification
**WebSocket Message**:
```json
{
  "type": "new_content",
  "data": {
    "contentId": "post_124",
    "contentType": "post",
    "author": {
      "id": "user_456",
      "username": "fashionista_gh",
      "name": "Sarah Miller",
      "avatar": "https://cdn.raved.app/avatars/user_456.jpg"
    },
    "preview": {
      "caption": "New outfit for the weekend! 🌟",
      "thumbnailUrl": "https://cdn.raved.app/posts/post_124_thumb.jpg"
    },
    "timestamp": "2024-08-14T10:30:00Z"
  }
}
```

### Presence Management

#### Update Presence
**WebSocket Message**:
```json
{
  "type": "update_presence",
  "data": {
    "status": "online",
    "activity": "browsing_feed",
    "lastSeen": "2024-08-14T10:30:00Z"
  }
}
```

#### Presence Update Notification
**WebSocket Message**:
```json
{
  "type": "presence_update",
  "data": {
    "userId": "user_456",
    "status": "online",
    "activity": "creating_post",
    "lastSeen": "2024-08-14T10:30:00Z"
  }
}
```

### Live Notifications

#### Instant Notification
**WebSocket Message**:
```json
{
  "type": "notification",
  "data": {
    "id": "notif_123",
    "type": "like",
    "title": "New Like",
    "message": "Alex Johnson liked your post",
    "user": {
      "id": "user_789",
      "username": "alex_j",
      "name": "Alex Johnson",
      "avatar": "https://cdn.raved.app/avatars/user_789.jpg"
    },
    "content": {
      "id": "post_123",
      "type": "post",
      "thumbnailUrl": "https://cdn.raved.app/posts/post_123_thumb.jpg"
    },
    "timestamp": "2024-08-14T10:30:00Z",
    "isRead": false
  }
}
```

### REST API Endpoints

#### GET /api/v1/realtime/conversations
**Purpose**: Get user's conversations list
**Response**:
```json
{
  "success": true,
  "conversations": [
    {
      "id": "conv_789",
      "type": "direct",
      "participants": [
        {
          "id": "user_456",
          "username": "fashionista_gh",
          "name": "Sarah Miller",
          "avatar": "https://cdn.raved.app/avatars/user_456.jpg",
          "isOnline": true,
          "lastSeen": "2024-08-14T10:30:00Z"
        }
      ],
      "lastMessage": {
        "id": "msg_124",
        "senderId": "user_456",
        "text": "Thank you! That means a lot! ❤️",
        "timestamp": "2024-08-14T10:31:00Z",
        "isRead": false
      },
      "unreadCount": 2,
      "updatedAt": "2024-08-14T10:31:00Z"
    }
  ]
}
```

#### GET /api/v1/realtime/conversations/{conversationId}/messages
**Purpose**: Get conversation message history
**Query Parameters**:
- `page`, `size`: Pagination
- `before`: Get messages before specific timestamp

#### POST /api/v1/realtime/conversations
**Purpose**: Create new conversation
```json
{
  "type": "direct",
  "participantIds": ["user_456"]
}
```

#### PUT /api/v1/realtime/messages/{messageId}/read
**Purpose**: Mark message as read

#### GET /api/v1/realtime/presence/{userId}
**Purpose**: Get user's current presence status

## 3. Data Models & Database Schema

### Database Choice: PostgreSQL
**Rationale**: Real-time messaging requires ACID compliance for message delivery guarantees, complex queries for conversation management, and strong consistency for message ordering.

### Core Tables

#### conversations
```sql
CREATE TABLE conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(20) NOT NULL, -- 'direct', 'group'
    name VARCHAR(255), -- For group conversations
    description TEXT,
    created_by UUID NOT NULL, -- Reference to User Service
    
    -- Settings
    is_active BOOLEAN DEFAULT TRUE,
    settings JSONB DEFAULT '{}',
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_type CHECK (type IN ('direct', 'group'))
);

CREATE INDEX idx_conversations_type ON conversations(type);
CREATE INDEX idx_conversations_created_by ON conversations(created_by);
CREATE INDEX idx_conversations_updated_at ON conversations(updated_at);
```

#### conversation_participants
```sql
CREATE TABLE conversation_participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID REFERENCES conversations(id) ON DELETE CASCADE,
    user_id UUID NOT NULL, -- Reference to User Service
    role VARCHAR(20) DEFAULT 'member', -- 'admin', 'member'
    
    -- Participation status
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    left_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Message tracking
    last_read_message_id UUID,
    last_read_at TIMESTAMP WITH TIME ZONE,
    
    -- Settings
    notifications_enabled BOOLEAN DEFAULT TRUE,
    
    UNIQUE(conversation_id, user_id)
);

CREATE INDEX idx_participants_conversation ON conversation_participants(conversation_id);
CREATE INDEX idx_participants_user ON conversation_participants(user_id);
CREATE INDEX idx_participants_active ON conversation_participants(user_id, is_active);
```

#### messages
```sql
CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL, -- Reference to User Service
    
    -- Message content
    message_type VARCHAR(20) NOT NULL DEFAULT 'text', -- 'text', 'image', 'file', 'system'
    content TEXT NOT NULL,
    metadata JSONB, -- Store type-specific data
    
    -- Threading
    reply_to_message_id UUID REFERENCES messages(id),
    
    -- Status
    status VARCHAR(20) DEFAULT 'sent', -- 'sent', 'delivered', 'read', 'failed'
    edited_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_message_type CHECK (message_type IN ('text', 'image', 'file', 'system')),
    CONSTRAINT valid_status CHECK (status IN ('sent', 'delivered', 'read', 'failed'))
);

CREATE INDEX idx_messages_conversation ON messages(conversation_id, created_at);
CREATE INDEX idx_messages_sender ON messages(sender_id);
CREATE INDEX idx_messages_reply_to ON messages(reply_to_message_id);
CREATE INDEX idx_messages_status ON messages(status);
```

#### user_presence
```sql
CREATE TABLE user_presence (
    user_id UUID PRIMARY KEY, -- Reference to User Service
    status VARCHAR(20) NOT NULL DEFAULT 'offline', -- 'online', 'away', 'busy', 'offline'
    activity VARCHAR(100), -- Current activity description
    last_seen TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    -- Connection info
    connection_id VARCHAR(255), -- WebSocket connection ID
    device_type VARCHAR(50),
    user_agent TEXT,
    
    -- Settings
    show_online_status BOOLEAN DEFAULT TRUE,
    
    -- Timestamps
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_status CHECK (status IN ('online', 'away', 'busy', 'offline'))
);

CREATE INDEX idx_presence_status ON user_presence(status);
CREATE INDEX idx_presence_last_seen ON user_presence(last_seen);
CREATE INDEX idx_presence_connection ON user_presence(connection_id);
```

#### websocket_connections
```sql
CREATE TABLE websocket_connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    connection_id VARCHAR(255) UNIQUE NOT NULL,
    user_id UUID NOT NULL, -- Reference to User Service
    
    -- Connection details
    server_instance VARCHAR(100) NOT NULL,
    ip_address INET,
    user_agent TEXT,
    
    -- Status
    status VARCHAR(20) DEFAULT 'active', -- 'active', 'inactive', 'disconnected'
    last_ping TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    -- Timestamps
    connected_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    disconnected_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT valid_connection_status CHECK (status IN ('active', 'inactive', 'disconnected'))
);

CREATE INDEX idx_connections_user ON websocket_connections(user_id);
CREATE INDEX idx_connections_server ON websocket_connections(server_instance);
CREATE INDEX idx_connections_status ON websocket_connections(status);
```

## 4. Technology Stack & Infrastructure

### Framework & Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
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
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```

### WebSocket Configuration
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new RealtimeWebSocketHandler(), "/api/v1/realtime/connect")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
```

### Performance Optimizations
- **Connection Pooling**: Redis for session management
- **Message Queuing**: Kafka for reliable message delivery
- **Horizontal Scaling**: Sticky sessions with load balancer
- **Connection Management**: Automatic cleanup of inactive connections
