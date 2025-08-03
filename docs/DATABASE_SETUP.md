# RAvED Database Setup Guide

This guide explains how to set up all the databases for the RAvED microservices architecture.

## Overview

RAvED uses a microservices architecture where each service has its own dedicated database:

- **user-service** → `raved_user_db`
- **content-service** → `raved_content_db`
- **social-service** → `raved_social_db`
- **realtime-service** → `raved_realtime_db`
- **ecommerce-service** → `raved_ecommerce_db`
- **notification-service** → `raved_notification_db`
- **analytics-service** → `raved_analytics_db`

## Quick Setup

### 1. Automated Setup (Recommended)

Run the complete database setup script:

```bash
./scripts/setup-databases.sh
```

This script will:
- ✅ Check prerequisites (Docker, Maven, PostgreSQL client)
- ✅ Start PostgreSQL container if not running
- ✅ Create all databases with extensions
- ✅ Run Flyway migrations for all services
- ✅ Seed databases with initial data
- ✅ Verify setup completion

### 2. Manual Setup

If you prefer to run each step manually:

```bash
# 1. Start infrastructure
docker-compose up -d postgres redis

# 2. Setup databases and run migrations
./scripts/setup/setup-database.sh

# 3. Seed with initial data
./scripts/database/seed.sh
```

## Database Structure

### User Service Database (`raved_user_db`)
- `universities` - University information
- `faculties` - Faculty/college information
- `departments` - Department information
- `users` - User profiles and authentication
- `student_verifications` - Student ID verification
- `user_sessions` - User session management

### Content Service Database (`raved_content_db`)
- `posts` - User posts and content
- `media_files` - Media attachments
- `post_tags` - Content tags
- `post_mentions` - User mentions

### Social Service Database (`raved_social_db`)
- `likes` - Post and comment likes
- `comments` - Post comments with threading
- `follows` - User follow relationships
- `user_connections` - Friend connections
- `activities` - Social activity feed

### Realtime Service Database (`raved_realtime_db`)
- `chat_rooms` - Chat room information
- `chat_room_members` - Room membership
- `messages` - Chat messages
- `message_reactions` - Message reactions

### Ecommerce Service Database (`raved_ecommerce_db`)
- `product_categories` - Product categorization
- `products` - Product listings
- `product_images` - Product photos
- `orders` - Purchase orders
- `order_items` - Order line items
- `payments` - Payment transactions

### Notification Service Database (`raved_notification_db`)
- `notification_templates` - Message templates
- `notifications` - User notifications
- `device_tokens` - Push notification tokens

### Analytics Service Database (`raved_analytics_db`)
- `analytics_events` - User interaction events
- `user_metrics` - User engagement metrics
- `content_metrics` - Content performance metrics

## Configuration

### Environment Variables

```bash
POSTGRES_HOST=localhost      # PostgreSQL host
POSTGRES_PORT=5432          # PostgreSQL port
POSTGRES_USER=raved_user    # Database user
POSTGRES_PASSWORD=raved_password  # Database password
```

### Database URLs

Each service connects to its own database:

```yaml
# user-service
spring.datasource.url=jdbc:postgresql://localhost:5432/raved_user_db

# content-service
spring.datasource.url=jdbc:postgresql://localhost:5432/raved_content_db

# And so on for each service...
```

## Management Commands

### Check Database Status
```bash
./scripts/setup-databases.sh --status
```

### Re-run Migrations
```bash
./scripts/database/migrate.sh
```

### Re-seed Data
```bash
./scripts/database/seed.sh
```

### Backup Databases
```bash
make db-backup
```

## Troubleshooting

### PostgreSQL Connection Issues
1. Ensure Docker is running: `docker ps`
2. Check PostgreSQL container: `docker-compose ps postgres`
3. Verify connection: `psql -h localhost -U raved_user -d raved_db`

### Migration Failures
1. Check service-specific logs in `server/{service}/target/`
2. Verify migration files exist in `server/{service}/src/main/resources/db/migration/`
3. Run migrations individually per service

### Permission Issues
1. Ensure scripts are executable: `chmod +x scripts/**/*.sh`
2. Check PostgreSQL user permissions
3. Verify Docker volume permissions

## Initial Data

The seeding script creates:

### Universities
- University of Ghana (UG)
- KNUST
- University of Cape Coast (UCC)
- GIMPA

### Faculties & Departments
- Various faculties and departments for each university

### Product Categories
- Clothing, Accessories, Shoes, Bags
- Electronics, Books, Beauty, Sports

### Notification Templates
- Like, comment, follow notifications
- Order status notifications
- Welcome emails

## Next Steps

After database setup:

1. **Start Services**: `docker-compose up -d`
2. **Check Health**: `make health`
3. **View Logs**: `make logs`
4. **Access APIs**: http://localhost:8080 (API Gateway)

## Support

For issues or questions:
1. Check the logs: `docker-compose logs postgres`
2. Verify database connectivity
3. Review migration files for syntax errors
4. Consult the main project documentation
