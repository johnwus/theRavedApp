# Events Service Documentation

## 1. Service Overview

### Primary Purpose
The Events Service manages campus event creation, discovery, attendance tracking, and location services for TheRavedApp's university community features.

### Core Responsibilities
- **Event Management**: Create, update, and manage campus events
- **Event Discovery**: Search and filter events by various criteria
- **Attendance Tracking**: RSVP and attendance management
- **Location Services**: Campus-aware location suggestions and mapping
- **Event Categories**: Faculty-specific and general campus events
- **Event Analytics**: Attendance metrics and event performance

### Service Boundaries
- **Owns**: Events, attendance records, locations, event categories
- **Does NOT Own**: User profiles, social interactions, content posts
- **Interfaces With**: User Service for authentication, Social Service for event sharing

### Performance Requirements
- **Event Search**: < 300ms for filtered event results
- **RSVP Operations**: < 200ms for attendance updates
- **Event Creation**: < 1s for event publishing
- **Location Search**: < 500ms for location suggestions
- **Concurrent Users**: Support 5,000+ simultaneous event interactions

## 2. API Specification

### Event Management

#### POST /api/v1/events
**Purpose**: Create new campus event
```json
{
  "title": "Fashion Week UG 2024",
  "description": "Annual fashion show featuring student designers and models from across campus",
  "category": "arts_culture",
  "faculty": "Arts",
  "startDateTime": "2024-09-15T18:00:00Z",
  "endDateTime": "2024-09-15T22:00:00Z",
  "location": {
    "name": "Great Hall",
    "address": "University of Ghana, Legon Campus",
    "coordinates": {
      "latitude": 5.6515,
      "longitude": -0.1870
    },
    "capacity": 500
  },
  "ticketInfo": {
    "isFree": false,
    "price": 20.00,
    "currency": "GHS",
    "ticketUrl": "https://tickets.ug.edu.gh/fashion-week-2024"
  },
  "images": ["event_image_1.jpg", "event_image_2.jpg"],
  "tags": ["fashion", "student", "showcase", "arts"],
  "isPublic": true,
  "requiresApproval": false
}
```

**Response**:
```json
{
  "success": true,
  "event": {
    "id": "event_123",
    "title": "Fashion Week UG 2024",
    "description": "Annual fashion show featuring student designers...",
    "category": "arts_culture",
    "faculty": "Arts",
    "organizer": {
      "id": "user_456",
      "username": "fashion_club_ug",
      "name": "UG Fashion Club",
      "avatar": "https://cdn.raved.app/avatars/user_456.jpg"
    },
    "startDateTime": "2024-09-15T18:00:00Z",
    "endDateTime": "2024-09-15T22:00:00Z",
    "location": {
      "name": "Great Hall",
      "address": "University of Ghana, Legon Campus",
      "coordinates": {
        "latitude": 5.6515,
        "longitude": -0.1870
      },
      "capacity": 500
    },
    "ticketInfo": {
      "isFree": false,
      "price": 20.00,
      "currency": "GHS",
      "ticketUrl": "https://tickets.ug.edu.gh/fashion-week-2024"
    },
    "images": [
      "https://cdn.raved.app/events/event_123_1.jpg",
      "https://cdn.raved.app/events/event_123_2.jpg"
    ],
    "tags": ["fashion", "student", "showcase", "arts"],
    "attendance": {
      "going": 0,
      "interested": 0,
      "capacity": 500,
      "userStatus": null
    },
    "status": "published",
    "createdAt": "2024-08-14T10:30:00Z"
  }
}
```

#### GET /api/v1/events
**Purpose**: Get events with filtering and pagination
**Query Parameters**:
- `faculty`: Filter by faculty
- `category`: Filter by event category
- `startDate`, `endDate`: Date range filter
- `location`: Location-based filter
- `tags`: Tag filters
- `status`: Event status filter
- `sort`: Sort by (date, popularity, distance)
- `page`, `size`: Pagination

**Response**:
```json
{
  "success": true,
  "events": [
    {
      "id": "event_123",
      "title": "Fashion Week UG 2024",
      "description": "Annual fashion show featuring student designers...",
      "category": "arts_culture",
      "faculty": "Arts",
      "organizer": {
        "id": "user_456",
        "name": "UG Fashion Club",
        "avatar": "https://cdn.raved.app/avatars/user_456.jpg"
      },
      "startDateTime": "2024-09-15T18:00:00Z",
      "endDateTime": "2024-09-15T22:00:00Z",
      "location": {
        "name": "Great Hall",
        "address": "University of Ghana, Legon Campus"
      },
      "ticketInfo": {
        "isFree": false,
        "price": 20.00,
        "currency": "GHS"
      },
      "images": ["https://cdn.raved.app/events/event_123_1.jpg"],
      "attendance": {
        "going": 45,
        "interested": 123,
        "userStatus": "interested"
      },
      "distance": 0.5, // km from user's location
      "timeUntilStart": "31 days"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 25,
    "hasNext": true
  },
  "filters": {
    "categories": ["arts_culture", "academic", "sports", "social"],
    "faculties": ["Arts", "Business", "Engineering", "Science"],
    "dateRange": {
      "earliest": "2024-08-15T00:00:00Z",
      "latest": "2024-12-31T23:59:59Z"
    }
  }
}
```

#### GET /api/v1/events/{eventId}
**Purpose**: Get detailed event information

#### PUT /api/v1/events/{eventId}
**Purpose**: Update event (organizer only)

#### DELETE /api/v1/events/{eventId}
**Purpose**: Delete event (organizer only)

### Attendance Management

#### POST /api/v1/events/{eventId}/attendance
**Purpose**: RSVP to event
```json
{
  "status": "going"
}
```

**Response**:
```json
{
  "success": true,
  "attendance": {
    "eventId": "event_123",
    "userId": "user_789",
    "status": "going",
    "rsvpAt": "2024-08-14T10:30:00Z"
  },
  "eventAttendance": {
    "going": 46,
    "interested": 123,
    "capacity": 500
  }
}
```

#### GET /api/v1/events/{eventId}/attendees
**Purpose**: Get event attendees list
**Query Parameters**:
- `status`: Filter by attendance status
- `page`, `size`: Pagination

#### GET /api/v1/users/{userId}/events
**Purpose**: Get user's events (attending, organizing, interested)

### Location Services

#### GET /api/v1/locations/campus
**Purpose**: Get campus location suggestions
**Query Parameters**:
- `query`: Search term
- `type`: Location type filter

**Response**:
```json
{
  "success": true,
  "locations": [
    {
      "id": "loc_123",
      "name": "Great Hall",
      "type": "venue",
      "address": "University of Ghana, Legon Campus",
      "coordinates": {
        "latitude": 5.6515,
        "longitude": -0.1870
      },
      "capacity": 500,
      "facilities": ["projector", "sound_system", "air_conditioning"],
      "images": ["https://cdn.raved.app/locations/loc_123.jpg"],
      "popularityScore": 0.85
    }
  ]
}
```

#### GET /api/v1/locations/nearby
**Purpose**: Get nearby locations
**Query Parameters**:
- `lat`, `lng`: User coordinates
- `radius`: Search radius in km
- `type`: Location type filter

### Event Categories

#### GET /api/v1/events/categories
**Purpose**: Get available event categories
**Response**:
```json
{
  "success": true,
  "categories": [
    {
      "id": "arts_culture",
      "name": "Arts & Culture",
      "description": "Creative events, performances, and cultural activities",
      "icon": "palette",
      "color": "#FF6B6B"
    },
    {
      "id": "academic",
      "name": "Academic",
      "description": "Lectures, seminars, and educational events",
      "icon": "book",
      "color": "#4ECDC4"
    },
    {
      "id": "sports",
      "name": "Sports & Recreation",
      "description": "Sports events, fitness activities, and recreational events",
      "icon": "sports",
      "color": "#45B7D1"
    }
  ]
}
```

## 3. Data Models & Database Schema

### Database Choice: PostgreSQL
**Rationale**: Event data requires ACID compliance for attendance tracking, complex queries for event discovery, and strong consistency for capacity management.

### Core Tables

#### events
```sql
CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organizer_id UUID NOT NULL, -- Reference to User Service
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    faculty VARCHAR(100),
    
    -- Date and time
    start_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    timezone VARCHAR(50) DEFAULT 'Africa/Accra',
    
    -- Location
    location_id UUID REFERENCES locations(id),
    location_name VARCHAR(255) NOT NULL,
    location_address TEXT,
    location_coordinates POINT,
    capacity INTEGER,
    
    -- Ticket information
    is_free BOOLEAN DEFAULT TRUE,
    ticket_price DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'GHS',
    ticket_url TEXT,
    
    -- Media
    images TEXT[], -- Array of image URLs
    
    -- Settings
    is_public BOOLEAN DEFAULT TRUE,
    requires_approval BOOLEAN DEFAULT FALSE,
    allow_guests BOOLEAN DEFAULT TRUE,
    
    -- Status
    status VARCHAR(50) DEFAULT 'draft', -- draft, published, cancelled, completed
    
    -- SEO and discovery
    tags TEXT[],
    slug VARCHAR(255) UNIQUE,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    published_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT valid_dates CHECK (end_date_time > start_date_time),
    CONSTRAINT valid_capacity CHECK (capacity IS NULL OR capacity > 0),
    CONSTRAINT valid_price CHECK (ticket_price IS NULL OR ticket_price >= 0),
    CONSTRAINT valid_status CHECK (status IN ('draft', 'published', 'cancelled', 'completed'))
);

CREATE INDEX idx_events_organizer ON events(organizer_id);
CREATE INDEX idx_events_category ON events(category);
CREATE INDEX idx_events_faculty ON events(faculty);
CREATE INDEX idx_events_dates ON events(start_date_time, end_date_time);
CREATE INDEX idx_events_location ON events USING GIST(location_coordinates);
CREATE INDEX idx_events_status ON events(status, start_date_time);
CREATE INDEX idx_events_tags ON events USING GIN(tags);
```

#### locations
```sql
CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL, -- 'venue', 'building', 'outdoor', 'virtual'
    address TEXT,
    coordinates POINT,
    capacity INTEGER,
    
    -- Facilities and amenities
    facilities TEXT[], -- Array of available facilities
    accessibility_features TEXT[],
    
    -- Media
    images TEXT[],
    description TEXT,
    
    -- Contact information
    contact_phone VARCHAR(20),
    contact_email VARCHAR(255),
    website_url TEXT,
    
    -- Availability
    is_bookable BOOLEAN DEFAULT FALSE,
    booking_url TEXT,
    
    -- Popularity and ratings
    popularity_score DECIMAL(3,2) DEFAULT 0,
    total_events INTEGER DEFAULT 0,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_type CHECK (type IN ('venue', 'building', 'outdoor', 'virtual')),
    CONSTRAINT valid_capacity CHECK (capacity IS NULL OR capacity > 0)
);

CREATE INDEX idx_locations_type ON locations(type);
CREATE INDEX idx_locations_coordinates ON locations USING GIST(coordinates);
CREATE INDEX idx_locations_popularity ON locations(popularity_score);
```

#### event_attendance
```sql
CREATE TABLE event_attendance (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID REFERENCES events(id) ON DELETE CASCADE,
    user_id UUID NOT NULL, -- Reference to User Service
    status VARCHAR(50) NOT NULL, -- 'going', 'interested', 'not_going'
    
    -- RSVP details
    rsvp_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    -- Additional information
    guest_count INTEGER DEFAULT 0,
    special_requirements TEXT,
    
    -- Check-in information
    checked_in BOOLEAN DEFAULT FALSE,
    checked_in_at TIMESTAMP WITH TIME ZONE,
    
    UNIQUE(event_id, user_id),
    CONSTRAINT valid_status CHECK (status IN ('going', 'interested', 'not_going')),
    CONSTRAINT valid_guest_count CHECK (guest_count >= 0)
);

CREATE INDEX idx_attendance_event ON event_attendance(event_id, status);
CREATE INDEX idx_attendance_user ON event_attendance(user_id, status);
CREATE INDEX idx_attendance_checkin ON event_attendance(event_id, checked_in);
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
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
</dependencies>
```

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/raved_events_db
    username: raved_admin
    password: theRAVEDapp#123
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Performance Optimizations
- **Spatial Indexing**: PostGIS for location-based queries
- **Event Caching**: Redis for popular events
- **Search Optimization**: Full-text search indexes
- **Capacity Management**: Real-time attendance tracking
