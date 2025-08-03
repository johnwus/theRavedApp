#!/bin/bash

# Update Database Configurations for Hybrid Setup
# This script updates all microservice configurations to support local and cloud databases

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Service configurations
declare -A SERVICES=(
    ["content-service"]="8082"
    ["social-service"]="8083"
    ["realtime-service"]="8084"
    ["ecommerce-service"]="8085"
    ["notification-service"]="8086"
    ["analytics-service"]="8087"
)

echo -e "${PURPLE}🔧 Updating Database Configurations for Hybrid Setup${NC}"
echo -e "${PURPLE}====================================================${NC}"

# Function to update service configuration
update_service_config() {
    local service=$1
    local port=$2
    local config_file="server/$service/src/main/resources/application.yml"
    
    echo -e "${BLUE}📝 Updating $service configuration...${NC}"
    
    if [ ! -f "$config_file" ]; then
        echo -e "${RED}❌ Configuration file not found: $config_file${NC}"
        return 1
    fi
    
    # Create backup
    cp "$config_file" "$config_file.backup"
    
    # Generate the new configuration
    cat > "$config_file" << EOF
server:
  port: $port

spring:
  application:
    name: $service
  profiles:
    active: \${SPRING_PROFILES_ACTIVE:local}

# =============================================================================
# RAVED CONFIGURATION
# =============================================================================
raved:
  service:
    name: $service
  database:
    environment: \${SPRING_PROFILES_ACTIVE:local}

# =============================================================================
# PROFILE-SPECIFIC CONFIGURATIONS
# =============================================================================

---
# LOCAL DEVELOPMENT PROFILE
spring:
  profiles: local
  datasource:
    url: \${$(echo $service | tr '[:lower:]' '[:upper:]' | tr '-' '_')_SERVICE_DB_URL:jdbc:postgresql://localhost:5432/raved_$(echo $service | tr '-' '_')_db}
    username: \${DATABASE_USERNAME:raved_user}
    password: \${DATABASE_PASSWORD:raved_password}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
      leak-detection-threshold: 60000
      pool-name: RavedHikariPool-$(echo $service | sed 's/-//g' | sed 's/\b\w/\u&/g')-Local
  jpa:
    hibernate:
      ddl-auto: validate
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true

---
# STAGING PROFILE (Neon Cloud)
spring:
  profiles: staging
  datasource:
    url: \${$(echo $service | tr '[:lower:]' '[:upper:]' | tr '-' '_')_SERVICE_DB_URL}
    username: \${DATABASE_USERNAME}
    password: \${DATABASE_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 15
      minimum-idle: 5
      idle-timeout: 600000
      connection-timeout: 30000
      leak-detection-threshold: 60000
      pool-name: RavedHikariPool-$(echo $service | sed 's/-//g' | sed 's/\b\w/\u&/g')-Staging
      connection-init-sql: SELECT 1
  jpa:
    hibernate:
      ddl-auto: validate
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: false
        jdbc:
          batch_size: 25
        order_inserts: true
        order_updates: true
        connection:
          provider_disables_autocommit: true
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true

---
# PRODUCTION PROFILE (Neon Cloud)
spring:
  profiles: production
  datasource:
    url: \${$(echo $service | tr '[:lower:]' '[:upper:]' | tr '-' '_')_SERVICE_DB_URL}
    username: \${DATABASE_USERNAME}
    password: \${DATABASE_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 10
      idle-timeout: 600000
      connection-timeout: 30000
      leak-detection-threshold: 60000
      pool-name: RavedHikariPool-$(echo $service | sed 's/-//g' | sed 's/\b\w/\u&/g')-Production
      connection-init-sql: SELECT 1
      data-source-properties:
        cachePrepStmts: true
        prepStmtCacheSize: 250
        prepStmtCacheSqlLimit: 2048
        useServerPrepStmts: true
        useLocalSessionState: true
        rewriteBatchedStatements: true
        cacheResultSetMetadata: true
        cacheServerConfiguration: true
        elideSetAutoCommits: true
        maintainTimeStats: false
  jpa:
    hibernate:
      ddl-auto: validate
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: false
        jdbc:
          batch_size: 30
        order_inserts: true
        order_updates: true
        connection:
          provider_disables_autocommit: true
        cache:
          use_second_level_cache: true
          use_query_cache: true
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
EOF

    # Add service-specific configurations
    add_service_specific_config "$service" "$config_file"
    
    echo -e "${GREEN}✅ Updated $service configuration${NC}"
}

# Function to add service-specific configurations
add_service_specific_config() {
    local service=$1
    local config_file=$2
    
    case $service in
        "realtime-service")
            cat >> "$config_file" << 'EOF'

# =============================================================================
# REALTIME SERVICE SPECIFIC CONFIGURATION
# =============================================================================
spring:
  rabbitmq:
    host: ${RABBITMQ_HOST:localhost}
    port: ${RABBITMQ_PORT:5672}
    username: ${RABBITMQ_USERNAME:guest}
    password: ${RABBITMQ_PASSWORD:guest}
    virtual-host: ${RABBITMQ_VIRTUAL_HOST:/}
EOF
            ;;
        "notification-service")
            cat >> "$config_file" << 'EOF'

# =============================================================================
# NOTIFICATION SERVICE SPECIFIC CONFIGURATION
# =============================================================================
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    consumer:
      group-id: notification-service
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
EOF
            ;;
    esac
    
    # Add common configurations for all services
    cat >> "$config_file" << 'EOF'

# =============================================================================
# COMMON MICROSERVICE CONFIGURATION
# =============================================================================
eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
    fetch-registry: true
    register-with-eureka: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

logging:
  level:
    com.raved: ${LOG_LEVEL:DEBUG}
  file:
    name: ${LOG_FILE_PATH:./logs}/${spring.application.name}.log
EOF
}

# Main execution
main() {
    echo -e "${BLUE}🚀 Starting Database Configuration Update${NC}"
    
    # Check if we're in the right directory
    if [ ! -d "server" ]; then
        echo -e "${RED}❌ Please run this script from the project root directory${NC}"
        exit 1
    fi
    
    # Update each service
    for service in "${!SERVICES[@]}"; do
        port=${SERVICES[$service]}
        update_service_config "$service" "$port"
        echo ""
    done
    
    echo -e "${GREEN}🎉 All service configurations updated successfully!${NC}"
    echo -e "${YELLOW}📋 Next steps:${NC}"
    echo -e "${YELLOW}1. Copy .env.local to .env for local development${NC}"
    echo -e "${YELLOW}2. Set up Neon databases for staging/production${NC}"
    echo -e "${YELLOW}3. Update environment variables in your deployment platform${NC}"
    echo -e "${YELLOW}4. Test the configuration with: ./scripts/setup-databases.sh${NC}"
}

# Run main function
main "$@"
