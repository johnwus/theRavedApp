# 🐳 **RAvED Docker Quick Start Guide**

## 🚀 **Quick Test Commands**

### **Option 1: Full Automated Test**
```bash
# Run the complete test suite
./scripts/test-docker-deployment.sh
```

### **Option 2: Manual Step-by-Step**
```bash
# 1. Start infrastructure only
docker-compose up -d postgres redis rabbitmq

# 2. Setup databases (wait 30 seconds first)
sleep 30
./scripts/setup-hybrid-databases.sh

# 3. Start discovery services
docker-compose up -d eureka-server config-server

# 4. Start API Gateway
docker-compose up -d api-gateway

# 5. Start business services
docker-compose up -d user-service content-service social-service

# 6. Check all services are running
docker-compose ps
```

### **Option 3: Start Everything at Once**
```bash
# Start all services (may take longer to stabilize)
docker-compose up -d

# Wait for services to be ready
sleep 60

# Check health
curl http://localhost:8080/actuator/health
```

---

## 🔍 **Service Endpoints**

| Service | Port | Health Check | Description |
|---------|------|--------------|-------------|
| API Gateway | 8080 | `/actuator/health` | Main entry point |
| Eureka Server | 8761 | `/actuator/health` | Service discovery |
| Config Server | 8888 | `/actuator/health` | Configuration |
| User Service | 8081 | `/actuator/health` | Authentication & Users |
| Content Service | 8082 | `/actuator/health` | Posts & Media |
| Social Service | 8083 | `/actuator/health` | Likes & Comments |
| Realtime Service | 8084 | `/actuator/health` | Chat & Messaging |
| Ecommerce Service | 8085 | `/actuator/health` | Products & Orders |
| Notification Service | 8086 | `/actuator/health` | Push Notifications |
| Analytics Service | 8087 | `/actuator/health` | Metrics & Reports |

---

## 🧪 **API Testing Examples**

### **1. User Registration**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### **2. User Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### **3. Create Post (with JWT token)**
```bash
# Replace YOUR_JWT_TOKEN with actual token from login
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "content": "Hello World from RAvED!",
    "visibility": "PUBLIC",
    "allowComments": true,
    "allowSharing": true
  }'
```

### **4. Like a Post**
```bash
# Replace POST_ID and JWT_TOKEN
curl -X POST http://localhost:8080/api/social/likes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "postId": 1,
    "userId": 1
  }'
```

---

## 🔧 **Troubleshooting**

### **Common Issues**

#### **Services Not Starting**
```bash
# Check logs
docker-compose logs [service-name]

# Restart specific service
docker-compose restart [service-name]

# Rebuild and restart
docker-compose up -d --build [service-name]
```

#### **Database Connection Issues**
```bash
# Check PostgreSQL is running
docker-compose ps postgres

# Check database logs
docker-compose logs postgres

# Recreate databases
docker-compose down
docker volume rm workspace_postgres_data
docker-compose up -d postgres
./scripts/setup-hybrid-databases.sh
```

#### **Service Discovery Issues**
```bash
# Check Eureka dashboard
open http://localhost:8761

# Restart Eureka and dependent services
docker-compose restart eureka-server
docker-compose restart api-gateway user-service content-service social-service
```

### **Health Check Commands**
```bash
# Check all service health
for port in 8080 8081 8082 8083 8084 8085 8086 8087 8761 8888; do
  echo "Checking port $port:"
  curl -s http://localhost:$port/actuator/health | jq '.status' || echo "Not available"
done
```

---

## 📊 **Monitoring**

### **View Service Logs**
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service

# Last 100 lines
docker-compose logs --tail=100 user-service
```

### **Check Resource Usage**
```bash
# Container stats
docker stats

# Service status
docker-compose ps
```

### **Database Access**
```bash
# Connect to PostgreSQL
docker-compose exec postgres psql -U raved_user -d raved_user_db

# List databases
docker-compose exec postgres psql -U raved_user -c "\l"
```

---

## 🛑 **Cleanup**

### **Stop Services**
```bash
# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: This deletes all data)
docker-compose down -v

# Remove all images
docker-compose down --rmi all
```

### **Reset Everything**
```bash
# Complete cleanup
docker-compose down -v --rmi all
docker system prune -f
docker volume prune -f
```

---

## ✅ **Success Indicators**

### **All Services Running**
- ✅ All containers show "Up" status in `docker-compose ps`
- ✅ All health endpoints return `{"status":"UP"}`
- ✅ Eureka dashboard shows all services registered
- ✅ API Gateway routes requests successfully

### **API Functionality**
- ✅ User registration works
- ✅ User login returns JWT token
- ✅ Protected endpoints accept JWT tokens
- ✅ Posts can be created and retrieved
- ✅ Social interactions (likes) work

---

## 🎯 **Next Steps**

1. **Test the APIs** using the provided curl commands
2. **Check service logs** for any errors
3. **Monitor resource usage** during testing
4. **Implement additional features** as needed
5. **Set up CI/CD pipeline** for automated deployment

**Your RAvED microservices are now ready for testing and development!** 🚀
