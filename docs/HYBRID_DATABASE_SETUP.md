# RAvED Hybrid Database Configuration Guide

This guide explains how to set up and use the hybrid database configuration system that supports both local development and cloud deployment.

## 🎯 Overview

The hybrid configuration system provides:
- **Local Development**: PostgreSQL in Docker containers
- **Staging/Production**: Neon serverless PostgreSQL
- **Automatic Environment Detection**: Based on Spring profiles
- **Optimized Connection Pooling**: Environment-specific settings
- **Easy Migration**: Switch between local and cloud seamlessly

## 🚀 Quick Setup

### 1. Update All Service Configurations

Run the configuration update script:

```bash
./scripts/update-database-configs.sh
```

This will update all microservice configurations with hybrid database support.

### 2. Set Up Local Environment

```bash
# Copy local environment template
cp .env.local .env

# Start local infrastructure
docker-compose up -d postgres redis

# Set up databases
./scripts/setup-databases.sh
```

### 3. For Cloud Deployment (Neon)

```bash
# Use staging environment
cp .env.staging .env

# Update with your Neon database URLs
# See "Neon Setup" section below
```

## 📋 Environment Profiles

### Local Development (`local`)
- **Database**: Local PostgreSQL in Docker
- **Connection Pool**: 10 max connections
- **SQL Logging**: Enabled
- **SSL**: Disabled

### Staging (`staging`)
- **Database**: Neon serverless PostgreSQL
- **Connection Pool**: 15 max connections
- **SQL Logging**: Disabled
- **SSL**: Required

### Production (`production`)
- **Database**: Neon serverless PostgreSQL
- **Connection Pool**: 20 max connections
- **SQL Logging**: Disabled
- **SSL**: Required
- **Query Cache**: Enabled

## 🔧 Configuration Structure

Each service now has profile-specific configurations:

```yaml
# Local Development
spring:
  profiles: local
  datasource:
    url: ${USER_SERVICE_DB_URL:jdbc:postgresql://localhost:5432/raved_user_db}
    username: ${DATABASE_USERNAME:raved_user}
    password: ${DATABASE_PASSWORD:raved_password}

# Staging (Neon Cloud)
spring:
  profiles: staging
  datasource:
    url: ${USER_SERVICE_DB_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}

# Production (Neon Cloud)
spring:
  profiles: production
  datasource:
    url: ${USER_SERVICE_DB_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
```

## 🌐 Neon Database Setup

### 1. Create Neon Account
1. Go to [Neon Console](https://console.neon.tech)
2. Sign up for a free account
3. Create a new project: "RAvED-Staging" or "RAvED-Production"

### 2. Create Databases
For each environment, create separate databases:

**Staging Databases:**
- `raved-user-staging`
- `raved-content-staging`
- `raved-social-staging`
- `raved-realtime-staging`
- `raved-ecommerce-staging`
- `raved-notification-staging`
- `raved-analytics-staging`

**Production Databases:**
- `raved-user-production`
- `raved-content-production`
- `raved-social-production`
- `raved-realtime-production`
- `raved-ecommerce-production`
- `raved-notification-production`
- `raved-analytics-production`

### 3. Get Connection Strings
From each database, copy the connection string and update your environment variables:

```bash
# Staging Environment Variables
export NEON_USER_DB_URL="postgresql://username:password@host/raved-user-staging?sslmode=require"
export NEON_CONTENT_DB_URL="postgresql://username:password@host/raved-content-staging?sslmode=require"
# ... and so on for each service
```

## 📝 Environment Variables

### Local Development (.env.local)
```bash
SPRING_PROFILES_ACTIVE=local
USER_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_user_db
DATABASE_USERNAME=raved_user
DATABASE_PASSWORD=raved_password
```

### Staging (.env.staging)
```bash
SPRING_PROFILES_ACTIVE=staging
USER_SERVICE_DB_URL=${NEON_USER_DB_URL}
DATABASE_USERNAME=${NEON_DB_USERNAME}
DATABASE_PASSWORD=${NEON_DB_PASSWORD}
```

### Production (.env.production)
```bash
SPRING_PROFILES_ACTIVE=production
USER_SERVICE_DB_URL=${NEON_PROD_USER_DB_URL}
DATABASE_USERNAME=${NEON_PROD_DB_USERNAME}
DATABASE_PASSWORD=${NEON_PROD_DB_PASSWORD}
```

## 🔄 Switching Environments

### Switch to Local Development
```bash
export SPRING_PROFILES_ACTIVE=local
# or
cp .env.local .env
```

### Switch to Staging
```bash
export SPRING_PROFILES_ACTIVE=staging
# or
cp .env.staging .env
```

### Switch to Production
```bash
export SPRING_PROFILES_ACTIVE=production
# or
cp .env.production .env
```

## 🛠️ Database Management Commands

### Local Development
```bash
# Setup local databases
./scripts/setup-databases.sh

# Check status
./scripts/setup-databases.sh --status

# Run migrations
./scripts/database/migrate.sh

# Seed data
./scripts/database/seed.sh
```

### Cloud Deployment
```bash
# Set environment
export SPRING_PROFILES_ACTIVE=staging

# Run migrations against cloud databases
./scripts/database/migrate.sh

# Seed cloud databases
./scripts/database/seed.sh
```

## 🔍 Connection Pool Optimization

The system automatically optimizes connection pools based on environment:

| Environment | Max Pool Size | Min Idle | Connection Timeout |
|-------------|---------------|----------|-------------------|
| Local       | 10            | 5        | 20s               |
| Staging     | 15            | 5        | 30s               |
| Production  | 20            | 10       | 30s               |

## 🚨 Troubleshooting

### Local Database Issues
```bash
# Check if PostgreSQL is running
docker ps | grep postgres

# Restart PostgreSQL
docker-compose restart postgres

# Check logs
docker-compose logs postgres
```

### Cloud Database Issues
```bash
# Test connection
psql "${NEON_USER_DB_URL}"

# Check environment variables
env | grep NEON

# Verify SSL connection
psql "${NEON_USER_DB_URL}?sslmode=require"
```

### Migration Issues
```bash
# Check migration status
./scripts/database/migrate.sh

# Reset migrations (caution!)
# This will drop and recreate tables
export FLYWAY_CLEAN_DISABLED=false
mvn flyway:clean flyway:migrate
```

## 📊 Monitoring

### Health Checks
All services expose health endpoints:
```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
# ... for each service
```

### Database Metrics
```bash
curl http://localhost:8081/actuator/metrics/hikaricp.connections.active
curl http://localhost:8081/actuator/metrics/hikaricp.connections.idle
```

## 🔐 Security Best Practices

1. **Never commit real credentials** to version control
2. **Use environment variables** for all sensitive data
3. **Enable SSL** for all cloud connections
4. **Rotate passwords** regularly
5. **Use least privilege** database users
6. **Monitor connection pools** for leaks

## 📈 Performance Tips

1. **Connection Pooling**: Properly configured for each environment
2. **Batch Operations**: Enabled for better performance
3. **Query Caching**: Enabled in production
4. **SSL Optimization**: Configured for cloud environments
5. **Prepared Statements**: Cached for better performance

This hybrid setup gives you the flexibility to develop locally while deploying to the cloud seamlessly!
