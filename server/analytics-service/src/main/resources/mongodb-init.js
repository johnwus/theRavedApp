// MongoDB initialization script for Analytics Service
// This script creates the analytics database, collections, and indexes

// Switch to analytics database
db = db.getSiblingDB('raved_analytics');

// Create collections with validation schemas
db.createCollection('analytics_events', {
    validator: {
        $jsonSchema: {
            bsonType: 'object',
            required: ['userId', 'eventType', 'eventTimestamp'],
            properties: {
                _id: { bsonType: 'objectId' },
                userId: { bsonType: 'string' },
                eventType: { bsonType: 'string' },
                entityType: { bsonType: 'string' },
                entityId: { bsonType: 'string' },
                eventTimestamp: { bsonType: 'date' },
                sessionId: { bsonType: 'string' },
                userAgent: { bsonType: 'string' },
                ipAddress: { bsonType: 'string' },
                eventData: { bsonType: 'object' },
                createdAt: { bsonType: 'date' },
                updatedAt: { bsonType: 'date' }
            }
        }
    }
});

db.createCollection('content_metrics', {
    validator: {
        $jsonSchema: {
            bsonType: 'object',
            required: ['contentId', 'contentType'],
            properties: {
                _id: { bsonType: 'objectId' },
                contentId: { bsonType: 'string' },
                contentType: { bsonType: 'string' },
                contentCategory: { bsonType: 'string' },
                contentOwnerId: { bsonType: 'string' },
                viewsCount: { bsonType: 'int' },
                likesCount: { bsonType: 'int' },
                commentsCount: { bsonType: 'int' },
                sharesCount: { bsonType: 'int' },
                engagementRate: { bsonType: 'decimal' },
                reach: { bsonType: 'int' },
                impressions: { bsonType: 'int' },
                metricsDate: { bsonType: 'date' },
                lastCalculatedAt: { bsonType: 'date' }
            }
        }
    }
});

db.createCollection('user_metrics', {
    validator: {
        $jsonSchema: {
            bsonType: 'object',
            required: ['userId'],
            properties: {
                _id: { bsonType: 'objectId' },
                userId: { bsonType: 'string' },
                totalPosts: { bsonType: 'int' },
                totalLikes: { bsonType: 'int' },
                totalComments: { bsonType: 'int' },
                totalShares: { bsonType: 'int' },
                followersCount: { bsonType: 'int' },
                followingCount: { bsonType: 'int' },
                engagementRate: { bsonType: 'decimal' },
                createdAt: { bsonType: 'date' },
                lastUpdatedAt: { bsonType: 'date' }
            }
        }
    }
});

db.createCollection('user_analytics');
db.createCollection('content_analytics');
db.createCollection('page_views');
db.createCollection('user_sessions');

// Create indexes for analytics_events
db.analytics_events.createIndex({ 'userId': 1 });
db.analytics_events.createIndex({ 'eventType': 1 });
db.analytics_events.createIndex({ 'entityType': 1, 'entityId': 1 });
db.analytics_events.createIndex({ 'eventTimestamp': 1 });
db.analytics_events.createIndex({ 'sessionId': 1 });
db.analytics_events.createIndex({ 'createdAt': 1 });
db.analytics_events.createIndex({ 'user_id': 1, 'event_type': 1 });
db.analytics_events.createIndex({ 'user_id': 1, 'event_timestamp': 1 });
db.analytics_events.createIndex({ 'event_type': 1, 'event_timestamp': 1 });

// Create compound indexes for analytics_events
db.analytics_events.createIndex({
    'event_type': 1,
    'event_timestamp': 1,
    'user_id': 1
}, { name: 'idx_event_time_user' });

db.analytics_events.createIndex({
    'entity_type': 1,
    'entity_id': 1,
    'event_timestamp': 1
}, { name: 'idx_entity_time' });

// Create indexes for content_metrics
db.content_metrics.createIndex({ 'content_id': 1 }, { unique: true });
db.content_metrics.createIndex({ 'content_type': 1 });
db.content_metrics.createIndex({ 'content_category': 1 });
db.content_metrics.createIndex({ 'content_owner_id': 1 });
db.content_metrics.createIndex({ 'metrics_date': 1 });
db.content_metrics.createIndex({ 'last_calculated_at': 1 });
db.content_metrics.createIndex({ 'engagement_rate': -1 });
db.content_metrics.createIndex({ 'views_count': -1 });

// Create compound indexes for content_metrics
db.content_metrics.createIndex({
    'content_type': 1,
    'metrics_date': 1
}, { name: 'idx_content_type_date' });

db.content_metrics.createIndex({
    'content_type': 1,
    'engagement_rate': -1
}, { name: 'idx_content_engagement' });

db.content_metrics.createIndex({
    'content_owner_id': 1,
    'metrics_date': 1
}, { name: 'idx_owner_date' });

// Create indexes for user_metrics
db.user_metrics.createIndex({ 'user_id': 1 }, { unique: true });
db.user_metrics.createIndex({ 'engagement_score': -1 });
db.user_metrics.createIndex({ 'social_metrics.followersCount': -1 });
db.user_metrics.createIndex({ 'social_metrics.postsCount': -1 });
db.user_metrics.createIndex({ 'updated_at': 1 });

// New collections indexes
// user_analytics
db.user_analytics.createIndex({ 'userId': 1, 'period': 1, 'date': 1 }, { name: 'idx_user_period_date' });

// content_analytics
db.content_analytics.createIndex({ 'contentId': 1, 'period': 1, 'date': 1 }, { name: 'idx_content_period_date' });

// page_views
db.page_views.createIndex({ 'userId': 1, 'viewedAt': -1 }, { name: 'idx_user_time' });
db.page_views.createIndex({ 'path': 1, 'viewedAt': -1 }, { name: 'idx_path_time' });

// user_sessions
db.user_sessions.createIndex({ 'userId': 1, 'isActive': 1 }, { name: 'idx_user_active' });
db.user_sessions.createIndex({ 'expiresAt': 1 }, { name: 'idx_expires_at' });

// rankings_snapshots
db.createCollection('rankings_snapshots');
db.rankings_snapshots.createIndex({ 'snapshotType': 1, 'metric': 1, 'period': 1, 'date': 1 }, { name: 'idx_type_metric_date' });

// Create TTL index for analytics_events (optional - remove old events after 2 years)
db.analytics_events.createIndex({ 'createdAt': 1 }, { expireAfterSeconds: 63072000 }); // 2 years

print('Analytics database initialized successfully');
print('Collections created: analytics_events, content_metrics, user_metrics, user_analytics, content_analytics, page_views, user_sessions, rankings_snapshots');
print('Indexes created for optimal query performance');
