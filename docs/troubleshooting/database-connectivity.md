# Database Connectivity Troubleshooting Guide

## 🐘 PostgreSQL Connectivity Issues

### Step 1: Verify Container Status
```bash
# Check if PostgreSQL container is running
docker ps --filter "name=raved-postgres-dev"

# If not running, start it
docker-compose -f infrastructure/docker/development/docker-compose.yml up -d postgres
```

### Step 2: Check Port Accessibility
```bash
# Test port connectivity
timeout 3 bash -c '</dev/tcp/localhost/5432' && echo "Port accessible" || echo "Port blocked"

# Check what's listening on port 5432
netstat -an | findstr "5432"
```

### Step 3: Test Internal Container Connection
```bash
# Connect from inside the container
docker exec raved-postgres-dev psql -U raved_user -d postgres -c "SELECT current_user;"
```

### Step 4: Verify User and Database
```bash
# List all databases
docker exec raved-postgres-dev psql -U raved_user -d postgres -c "\l"

# Check user permissions
docker exec raved-postgres-dev psql -U raved_user -d postgres -c "SELECT current_user, session_user;"
```

### Step 5: Check Authentication Configuration
```bash
# View pg_hba.conf
docker exec raved-postgres-dev cat /var/lib/postgresql/data/pg_hba.conf | grep -v "^#"
```

### Step 6: Reset User Password
```bash
# Reset password if authentication fails
docker exec raved-postgres-dev psql -U raved_user -d postgres -c "ALTER USER raved_user WITH PASSWORD 'raved_password';"
```

---

## 🍃 MongoDB Connectivity Issues

### Step 1: Verify Container Status
```bash
# Check if MongoDB container is running
docker ps --filter "name=raved-mongodb-dev"

# If not running, start it
docker-compose -f infrastructure/docker/development/docker-compose.yml up -d mongodb
```

### Step 2: Test Port Accessibility
```bash
# Test MongoDB port
timeout 3 bash -c '</dev/tcp/localhost/27017' && echo "MongoDB accessible" || echo "MongoDB not accessible"
```

### Step 3: Test Connection String
```bash
# Test with mongosh (if available)
mongosh "mongodb://raved_admin:raved_password@localhost:27017/?authSource=admin" --eval "db.adminCommand('ping')"

# Test without authentication first
mongosh "mongodb://localhost:27017" --eval "db.adminCommand('ping')"
```

### Step 4: Check MongoDB Logs
```bash
# View MongoDB container logs
docker logs raved-mongodb-dev --tail 20
```

---

## 🔧 Common Connection Issues

### Issue: "Connection Refused"
**Symptoms**: Cannot connect to database port
**Troubleshooting**:
1. Check if container is running
2. Verify port mapping in docker-compose.yml
3. Check for port conflicts with other services
4. Restart Docker if necessary

### Issue: "Authentication Failed"
**Symptoms**: Port accessible but login fails
**Troubleshooting**:
1. Verify username/password combination
2. Check authentication database (authSource for MongoDB)
3. Reset user password
4. Review authentication configuration

### Issue: "Database Not Found"
**Symptoms**: Connection works but database missing
**Troubleshooting**:
1. List all available databases
2. Check initialization scripts
3. Verify database creation in init.sql
4. Recreate container if necessary

### Issue: "Hostname Resolution Failed"
**Symptoms**: Cannot resolve hostname
**Troubleshooting**:
1. Try localhost instead of custom hostnames
2. Try 127.0.0.1 instead of localhost
3. Check hosts file for custom entries
4. Use Docker container IP directly

---

## 🎯 Quick Diagnostic Script

Create and run this script for quick diagnosis:

```bash
#!/bin/bash
echo "=== Database Connectivity Diagnosis ==="
echo

echo "1. Container Status:"
docker ps --filter "name=raved-postgres-dev" --format "PostgreSQL: {{.Status}}"
docker ps --filter "name=raved-mongodb-dev" --format "MongoDB: {{.Status}}"

echo
echo "2. Port Accessibility:"
timeout 2 bash -c '</dev/tcp/localhost/5432' && echo "PostgreSQL: ✅" || echo "PostgreSQL: ❌"
timeout 2 bash -c '</dev/tcp/localhost/27017' && echo "MongoDB: ✅" || echo "MongoDB: ❌"

echo
echo "3. Internal Connections:"
docker exec raved-postgres-dev psql -U raved_user -d postgres -c "SELECT 'PostgreSQL Internal: ✅';" 2>/dev/null || echo "PostgreSQL Internal: ❌"

echo
echo "Diagnosis complete!"
```

---

## 📞 Escalation Path

If troubleshooting doesn't resolve the issue:

1. **Document the error** in the errors/ directory
2. **Capture logs** from affected containers
3. **Note system configuration** (OS, Docker version, etc.)
4. **Try container recreation** as last resort
5. **Consult team** or external resources

---

## 🏗️ **Polyglot Architecture Troubleshooting**

### Issue: "Too Many PostgreSQL Databases"
**Symptoms**: PostgreSQL container has databases for MongoDB services
**Troubleshooting**:
1. List all databases: `docker exec raved-postgres-dev psql -U raved_user -d postgres -c '\l'`
2. Check service-database mapping in architecture documentation
3. Identify which services should use MongoDB vs PostgreSQL
4. Update initialization script to create only necessary databases
5. Recreate container with corrected structure

### Issue: "Service Can't Find Database"
**Symptoms**: Service expects PostgreSQL database but it was removed for MongoDB conversion
**Troubleshooting**:
1. Verify service configuration points to correct database technology
2. Check if service was converted to MongoDB but still configured for PostgreSQL
3. Update service configuration to use MongoDB connection string
4. Ensure MongoDB container is running and accessible

### Issue: "Mixed Database Technologies"
**Symptoms**: Service using both PostgreSQL and MongoDB connections
**Troubleshooting**:
1. Review service dependencies and configuration
2. Choose appropriate database technology for service requirements
3. Remove unnecessary database dependencies
4. Update service implementation to use single database technology

### Polyglot Architecture Verification Script:
```bash
#!/bin/bash
echo "=== Polyglot Architecture Verification ==="

echo "PostgreSQL Databases:"
docker exec raved-postgres-dev psql -U raved_user -d postgres -c "SELECT datname FROM pg_database WHERE datname LIKE 'raved%';"

echo "Expected PostgreSQL Services:"
echo "- user-service (raved_user_db)"
echo "- ecommerce-service (raved_ecommerce_db)"
echo "- realtime-service (raved_realtime_db)"

echo "Expected MongoDB Services:"
echo "- notification-service"
echo "- social-service"
echo "- content-service"
echo "- analytics-service"

echo "MongoDB Container Status:"
docker ps --filter "name=raved-mongodb-dev" --format "{{.Status}}"
```

---

## 🔐 **PostgreSQL External Authentication Troubleshooting**

### Issue: "Password Authentication Failed for External Connections"
**Symptoms**: pgAdmin/DBeaver cannot connect but internal connections work
**Error**: `FATAL: password authentication failed for user "raved_admin"`

**Troubleshooting Steps**:

#### Step 1: Check pg_hba.conf Configuration
```bash
# View current authentication configuration
docker exec raved-postgres-dev cat /var/lib/postgresql/data/pg_hba.conf | grep -E "^host|^local"

# Look for conflicting authentication methods
# Problem: host all all 127.0.0.1/32 trust
# Solution: host all all 127.0.0.1/32 scram-sha-256
```

#### Step 2: Verify User and Password
```bash
# Check if user exists and can login
docker exec raved-postgres-dev psql -U raved_admin -d postgres -c "SELECT rolname, rolcanlogin FROM pg_roles WHERE rolname = 'raved_admin';"

# Verify password is set
docker exec raved-postgres-dev psql -U raved_admin -d postgres -c "SELECT rolname, rolpassword IS NOT NULL as has_password FROM pg_authid WHERE rolname = 'raved_admin';"

# Check password encryption method
docker exec raved-postgres-dev psql -U raved_admin -d postgres -c "SHOW password_encryption;"
```

#### Step 3: Fix Authentication Configuration
```bash
# Create corrected pg_hba.conf
cat > /tmp/pg_hba.conf << 'EOF'
local   all             all                                     trust
host    all             all             127.0.0.1/32            scram-sha-256
host    all             all             ::1/128                 scram-sha-256
host    all             all             172.16.0.0/12           scram-sha-256
host    all             all             0.0.0.0/0               scram-sha-256
local   replication     all                                     trust
host    replication     all             127.0.0.1/32            scram-sha-256
host    replication     all             ::1/128                 scram-sha-256
EOF

# Apply configuration
docker cp /tmp/pg_hba.conf raved-postgres-dev:/var/lib/postgresql/data/pg_hba.conf
docker exec raved-postgres-dev chown postgres:postgres /var/lib/postgresql/data/pg_hba.conf
docker exec raved-postgres-dev chmod 600 /var/lib/postgresql/data/pg_hba.conf

# Reload configuration
docker exec raved-postgres-dev psql -U raved_admin -d postgres -c "SELECT pg_reload_conf();"
```

#### Step 4: Reset Password
```bash
# Reset password with proper hashing
docker exec raved-postgres-dev psql -U raved_admin -d postgres -c "ALTER USER raved_admin WITH PASSWORD 'theRAVEDapp#123';"

# Test internal connection
docker exec raved-postgres-dev psql -U raved_admin -d postgres -c "SELECT current_user;"
```

#### Step 5: Test External Connection
```bash
# Verify port accessibility
timeout 3 bash -c '</dev/tcp/localhost/5432' && echo "Port accessible" || echo "Port blocked"

# Try connecting with pgAdmin using:
# Host: localhost, Port: 5432, Database: postgres
# Username: raved_admin, Password: theRAVEDapp#123
```

### Issue: "Connection Refused on Port 5432"
**Symptoms**: Cannot reach PostgreSQL port from external tools
**Troubleshooting**:
1. Check container status: `docker ps --filter "name=raved-postgres-dev"`
2. Verify port mapping in docker-compose.yml
3. Test port accessibility: `timeout 3 bash -c '</dev/tcp/localhost/5432'`
4. Check firewall settings on host system
5. Restart container if necessary

### Issue: "SSL Connection Required"
**Symptoms**: Connection fails with SSL-related errors
**Troubleshooting**:
1. Set SSL Mode to "Prefer" or "Disable" in pgAdmin
2. Check PostgreSQL SSL configuration
3. For development, disable SSL requirement temporarily
4. Verify certificate configuration if SSL is required

---

## 🌐 **pgAdmin Web Interface Troubleshooting**

### Issue: "This site can't be reached" at localhost:5050
**Symptoms**: pgAdmin web interface not loading, connection timeout
**Error**: `ERR_CONNECTION_TIMED_OUT`

**Troubleshooting Steps**:

#### Step 1: Check Container Status
```bash
# Verify pgAdmin container is running
docker ps --filter "name=pgadmin"

# Check container logs
docker logs pgadmin --tail 20
```

#### Step 2: Verify Port Mapping
```bash
# Check current port mapping
docker port pgadmin

# Should show: 5050/tcp -> 0.0.0.0:5050
# If shows 5050/tcp -> 0.0.0.0:5050 -> 5050/tcp, this is wrong
```

#### Step 3: Check Internal Service Port
```bash
# Check what port pgAdmin is listening on inside container
docker exec pgadmin ss -tlnp | grep LISTEN

# pgAdmin typically listens on port 80 internally
```

#### Step 4: Fix Port Mapping (if incorrect)
```bash
# Stop and remove current container
docker stop pgadmin && docker rm pgadmin

# Recreate with correct port mapping (5050:80)
docker run -d --name pgadmin -p 5050:80 \
  -e PGADMIN_DEFAULT_EMAIL=admin@raved.com \
  -e PGADMIN_DEFAULT_PASSWORD=admin123 \
  dpage/pgadmin4

# Wait for initialization
sleep 15

# Test accessibility
curl -s -I http://localhost:5050 | head -3
```

#### Step 5: Verify Fix
```bash
# Test port accessibility
nc -z localhost 5050 && echo "Port accessible" || echo "Port blocked"

# Check HTTP response (should be 302 redirect)
curl -s -o /dev/null -w "HTTP Status: %{http_code}" http://localhost:5050
```

### Issue: pgAdmin Login Page Not Loading
**Symptoms**: Port accessible but page doesn't load properly
**Troubleshooting**:
1. Wait for full container initialization (can take 30-60 seconds)
2. Check container logs for initialization errors
3. Verify gunicorn process is running: `docker exec pgadmin ps aux | grep gunicorn`
4. Clear browser cache and try incognito mode
5. Try accessing via container IP directly

---

## 🌐 **pgAdmin Web Interface Troubleshooting**

### Issue: "This site can't be reached" at localhost:5050
**Symptoms**: pgAdmin web interface not loading, connection timeout errors
**Error**: `ERR_CONNECTION_TIMED_OUT` or `This site can't be reached`

**Troubleshooting Steps**:

#### Step 1: Check Container Status
```bash
# Verify pgAdmin container is running
docker ps --filter "name=pgadmin"

# Check container logs for errors
docker logs pgadmin --tail 20
```

#### Step 2: Verify Port Mapping
```bash
# Check current port mapping
docker port pgadmin

# Should show: 5050/tcp -> 0.0.0.0:5050 (incorrect)
# Should be: 5050/tcp -> 0.0.0.0:80 (correct)
```

#### Step 3: Check Internal Service Port
```bash
# Check what ports are listening inside container
docker exec pgadmin ss -tlnp | grep LISTEN

# pgAdmin typically listens on port 80, not 5050
```

#### Step 4: Fix Port Mapping
```bash
# Stop and remove current container
docker stop pgadmin && docker rm pgadmin

# Recreate with correct port mapping
docker run -d --name pgadmin \
  -p 5050:80 \
  -e PGADMIN_DEFAULT_EMAIL=admin@raved.com \
  -e PGADMIN_DEFAULT_PASSWORD=admin123 \
  dpage/pgadmin4

# Wait for initialization
sleep 15

# Test access
curl -s -I http://localhost:5050
```

#### Step 5: Verify Fix
```bash
# Test port accessibility
timeout 5 bash -c '</dev/tcp/127.0.0.1/5050' && echo "Port accessible" || echo "Port blocked"

# Check HTTP response (should get 302 redirect)
curl -s -o /dev/null -w "HTTP Status: %{http_code}" http://localhost:5050
```

### Issue: pgAdmin Login Page Not Loading
**Symptoms**: Port accessible but login page doesn't appear
**Troubleshooting**:
1. Wait for full initialization (can take 30-60 seconds)
2. Check container logs for Python/gunicorn errors
3. Verify environment variables are set correctly
4. Clear browser cache and try incognito mode
5. Try accessing via container IP directly

### Issue: Cannot Connect to PostgreSQL from pgAdmin
**Symptoms**: pgAdmin loads but cannot connect to PostgreSQL
**Troubleshooting**:
1. Use container IP instead of localhost: `172.17.0.3`
2. Verify PostgreSQL container is running: `docker ps --filter "name=postgres"`
3. Test PostgreSQL connectivity: `docker exec postgres-container psql -U user -c "SELECT 1;"`
4. Check network connectivity between containers
5. Verify credentials and database names
