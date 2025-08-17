# TheRavedApp Database Structure Guide

## 🐘 PostgreSQL Databases (pgAdmin 4)

### Expected Structure:
```
TheRavedApp Development/
└── PostgreSQL (Docker Local)/
    ├── raved_db (Main Database)
    │   ├── Schemas/
    │   │   └── public/
    │   │       ├── Tables/
    │   │       │   ├── users
    │   │       │   ├── user_profiles
    │   │       │   ├── products
    │   │       │   ├── orders
    │   │       │   ├── events
    │   │       │   ├── subscriptions
    │   │       │   └── ... (other tables)
    │   │       └── Sequences/
    │   └── Extensions/
    └── postgres (System Database)
```

### Services Using PostgreSQL:
- **user-service**: User management, authentication
- **ecommerce-service**: Products, orders, payments
- **events-service**: Event management
- **subscription-service**: Subscription billing

## 🍃 MongoDB Databases (MongoDB Compass)

### Expected Structure:
```
TheRavedApp MongoDB (Local)
├── raved_notifications
│   ├── notification_templates
│   ├── notifications
│   ├── delivery_logs
│   └── user_preferences
├── raved_social
│   ├── likes
│   ├── follows
│   ├── comments
│   ├── activities
│   └── leaderboard_scores
├── raved_content
│   ├── posts
│   ├── media_files
│   ├── post_tags
│   └── content_metrics
├── raved_analytics
│   ├── user_analytics
│   ├── page_views
│   ├── user_sessions
│   └── event_tracking
└── admin (System Database)
```

### Services Using MongoDB:
- **notification-service**: Templates, delivery logs
- **social-service**: Likes, follows, comments, activities
- **content-service**: Posts, media, tags
- **analytics-service**: User behavior, metrics

## 🔧 Connection Verification Commands

### PostgreSQL (via pgAdmin Query Tool):
```sql
-- Test connection and show databases
SELECT datname FROM pg_database WHERE datistemplate = false;

-- Show current database
SELECT current_database();

-- Show tables in current database
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public';
```

### MongoDB (via Compass or Shell):
```javascript
// Show all databases
show dbs

// Switch to notification database
use raved_notifications

// Show collections
show collections

// Test document count
db.notification_templates.countDocuments()
```

## 🎯 Troubleshooting

### PostgreSQL Issues:
- **Connection refused**: Check if Docker container is running
- **Authentication failed**: Verify username/password
- **Database not found**: Check if raved_db exists

### MongoDB Issues:
- **Authentication failed**: Verify authSource=admin
- **Connection timeout**: Check if Docker container is running
- **Empty databases**: Run initialization scripts

## 🚀 Next Steps After Setup:
1. Verify both connections work
2. Check that expected databases exist
3. Run database initialization scripts if needed
4. Test basic CRUD operations
5. Start microservices and verify connectivity
