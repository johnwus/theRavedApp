# pgAdmin Port Mapping Fix

## 🎯 Problem Summary
**Issue**: pgAdmin web interface not accessible at localhost:5050
**Error**: "This site can't be reached - ERR_CONNECTION_TIMED_OUT"
**Impact**: Complete inability to access pgAdmin GUI for PostgreSQL management

## 🔍 Root Cause Analysis
1. **Port Mapping Mismatch**: Docker port mapping configured as `5050:5050`
2. **Internal Service Port**: pgAdmin runs on port 80 inside the container
3. **Incorrect Configuration**: Should be `5050:80` not `5050:5050`
4. **Service Status**: Container running but web service unreachable

## 🔍 Root Cause Analysis
1. **Port Mapping Mismatch**: Docker port mapping configured as `5050:5050`
2. **Internal Service Port**: pgAdmin gunicorn server listening on port 80 inside container
3. **Configuration Error**: Should be `5050:80` to map host port 5050 to container port 80
4. **Service Status**: Container running but web service unreachable due to port mismatch

## ✅ Complete Solution

### Step 1: Diagnose Port Mapping Issue

```bash
# Check current container status
docker ps --filter "name=pgadmin"

# Check port mapping
docker port pgadmin

# Check what ports are listening inside container
docker exec pgadmin ss -tlnp | grep LISTEN

# Expected output shows port 80 listening, not 5050
```

### Step 2: Fix Port Mapping Configuration

```bash
# Stop current pgAdmin container
docker stop pgadmin

# Remove current pgAdmin container
docker rm pgadmin

# Recreate with correct port mapping (5050:80)
docker run -d --name pgadmin \
  -p 5050:80 \
  -e PGADMIN_DEFAULT_EMAIL=admin@raved.com \
  -e PGADMIN_DEFAULT_PASSWORD=admin123 \
  dpage/pgadmin4

# Wait for container to initialize
sleep 15
```

### Step 3: Verify Fix

```bash
# Check new container status
docker ps --filter "name=pgadmin" --format "{{.Names}}: {{.Status}} | {{.Ports}}"

# Test port accessibility
timeout 5 bash -c '</dev/tcp/127.0.0.1/5050' && echo "Port accessible" || echo "Port not accessible"

# Test HTTP response
curl -s -I http://localhost:5050 | head -3

# Check container logs
docker logs pgadmin --tail 10
```

## 🎯 Key Configuration Changes

### Before (Incorrect):
```yaml
ports:
  - "5050:5050"  # Wrong - pgAdmin doesn't listen on 5050 internally
```

### After (Correct):
```yaml
ports:
  - "5050:80"    # Correct - maps host 5050 to container 80
```

### Port Mapping Explanation:
- **Host Port**: 5050 (external access point)
- **Container Port**: 80 (where pgAdmin gunicorn listens)
- **Format**: `host_port:container_port`

## 🔧 pgAdmin Container Configuration

### Working Container Setup:
```bash
docker run -d --name pgadmin \
  -p 5050:80 \
  -e PGADMIN_DEFAULT_EMAIL=admin@raved.com \
  -e PGADMIN_DEFAULT_PASSWORD=admin123 \
  dpage/pgadmin4
```

### Environment Variables:
- **PGADMIN_DEFAULT_EMAIL**: Default admin email for login
- **PGADMIN_DEFAULT_PASSWORD**: Default admin password for login
- **Port Mapping**: 5050:80 (host:container)

## 📋 Verification Checklist

- [x] Container status: Up and running
- [x] Port mapping: 5050:80 (correct)
- [x] HTTP response: 302 redirect (normal for pgAdmin)
- [x] Service process: gunicorn running on port 80
- [x] External access: localhost:5050 accessible
- [x] Login page: Loads properly in browser

## 🎯 Access Information

### pgAdmin Web Interface:
```
URL: http://localhost:5050
Email: admin@raved.com
Password: admin123
```

### PostgreSQL Connection Settings (for pgAdmin):
```
Host: 172.17.0.3 (container IP)
Port: 5432
Database: postgres
Username: raved_admin
Password: theRAVEDapp#123
SSL Mode: Disable
```

## 🚨 Prevention Measures

1. **Always Check Internal Ports**: Use `docker exec container ss -tlnp` to see what ports services listen on
2. **Verify Port Mapping**: Ensure host:container port mapping matches service configuration
3. **Test After Changes**: Always verify web interface accessibility after container recreation
4. **Document Port Configurations**: Keep track of which services use which internal ports
5. **Use Health Checks**: Implement container health checks for web services

## 📝 Lessons Learned

1. **Port Mapping Format**: Always verify the correct format is `host_port:container_port`
2. **Service Discovery**: Different services listen on different internal ports (pgAdmin=80, PostgreSQL=5432)
3. **Container Recreation**: Sometimes fixing configuration requires complete container recreation
4. **HTTP Status Codes**: 302 redirect is normal for pgAdmin (redirects to login page)
5. **Initialization Time**: Web services need time to fully initialize after container start

## 🎉 Success Metrics

- **Web Interface Access**: 100% accessible at localhost:5050
- **Login Functionality**: Working with configured credentials
- **PostgreSQL Integration**: Ready to connect to PostgreSQL containers
- **Development Workflow**: Unblocked for database administration
- **Container Health**: Stable and properly configured

## 🔄 Future Considerations

1. **Persistent Storage**: Add volume mapping for pgAdmin configuration persistence
2. **SSL Configuration**: Consider enabling HTTPS for production environments
3. **User Management**: Set up additional pgAdmin users for team access
4. **Backup Integration**: Configure automated database backup through pgAdmin
5. **Monitoring**: Add health checks and monitoring for pgAdmin service
