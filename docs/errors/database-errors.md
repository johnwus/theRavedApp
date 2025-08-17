# Database Errors Log

## 🐘 PostgreSQL Errors

### Error #1: Password Authentication Failed
**Date**: 2025-08-14  
**Service**: PostgreSQL (Docker)  
**Error Message**:
```
connection failed: connection to server at "127.0.0.1", port 5432 failed: 
FATAL: password authentication failed for user "raved_user"
Multiple connection attempts failed. All failures were:
- host: 'localhost', port: '5432', hostaddr: '::1': connection failed: connection to server at "::1", port 5432 failed: FATAL: password authentication failed for user "raved_user"
- host: 'localhost', port: '5432', hostaddr: '127.0.0.1': connection failed: connection to server at "127.0.0.1", port 5432 failed: FATAL: password authentication failed for user "raved_user"
```

**Context**: 
- Trying to connect to PostgreSQL from pgAdmin 4
- Docker container running but authentication failing
- User credentials: raved_user / raved_password

**Root Cause**: 
- PostgreSQL container created with incorrect authentication setup
- User password not properly set during initialization
- pg_hba.conf authentication method mismatch

**Impact**: 
- Unable to connect to PostgreSQL from external tools
- Blocked development workflow
- Cannot access application databases

---

### Error #2: Hostname Resolution Failed
**Date**: 2025-08-14  
**Service**: PostgreSQL (Docker)  
**Error Message**:
```
[Errno 11001] getaddrinfo failed
```

**Context**: 
- Trying to use "host" as hostname in pgAdmin
- Docker networking configuration issue

**Root Cause**: 
- "host" hostname not resolvable in Windows environment
- Docker networking using different hostname resolution

**Impact**: 
- Connection attempts fail with hostname resolution error
- Confusion about correct hostname to use

---

## 🍃 MongoDB Errors

### Error #1: Authentication Source Missing
**Date**: 2025-08-14  
**Service**: MongoDB (Docker)  
**Error Message**:
```
Authentication failed
```

**Context**: 
- MongoDB connection string without authSource parameter
- Using: mongodb://raved_admin:raved_password@localhost:27017/raved_notifications

**Root Cause**: 
- Missing authSource=admin in connection string
- MongoDB requires explicit authentication database specification

**Impact**: 
- MongoDB services unable to connect
- Application startup failures

---

## 🔧 Docker Errors

### Error #1: Container Not Running
**Date**: 2025-08-14  
**Service**: PostgreSQL Container  
**Error Message**:
```
Error response from daemon: container ed9c0ed72fe885b2ca938879446cb273038d53bf4d7508841c0ae9da38d43d8d is not running
```

**Context**: 
- PostgreSQL container stopped unexpectedly
- Initialization script errors causing container exit

**Root Cause**: 
- Duplicate database creation in init.sql
- Script errors preventing successful startup

**Impact**:
- Database services unavailable
- Development environment broken

---

### Error #2: Incorrect Polyglot Database Structure
**Date**: 2025-08-14
**Service**: PostgreSQL (Docker)
**Error Message**:
```
PostgreSQL container created with 9 databases instead of 4
Unnecessary databases for MongoDB services present
```

**Context**:
- Polyglot architecture implementation
- PostgreSQL container had databases for services converted to MongoDB
- notification-service, social-service, content-service, analytics-service should use MongoDB only

**Root Cause**:
- Initialization script created databases for all services
- Did not account for MongoDB conversion
- Mixed database technologies in single container

**Impact**:
- Architectural inconsistency
- Confusion about which services use which databases
- Unnecessary resource usage

---

### Error #3: PostgreSQL CREATE DATABASE IF NOT EXISTS Syntax Error
**Date**: 2025-08-14
**Service**: PostgreSQL (Docker)
**Error Message**:
```
ERROR: syntax error at or near "NOT" at character 20
STATEMENT: CREATE DATABASE IF NOT EXISTS raved_user_db;
```

**Context**:
- Attempting to use MySQL/SQLite syntax in PostgreSQL
- PostgreSQL doesn't support IF NOT EXISTS for CREATE DATABASE

**Root Cause**:
- Incorrect SQL syntax for PostgreSQL
- Assumption that PostgreSQL supports MySQL-style conditional creation

**Impact**:
- Container initialization failure
- Database creation script errors

---

### Error #4: PostgreSQL External Authentication Failure (raved_admin)
**Date**: 2025-08-14
**Service**: PostgreSQL (Docker)
**Error Message**:
```
connection failed: connection to server at "127.0.0.1", port 5432 failed:
FATAL: password authentication failed for user "raved_admin"
Multiple connection attempts failed. All failures were:
- host: 'localhost', port: '5432', hostaddr: '::1': connection failed: connection to server at "::1", port 5432 failed: FATAL: password authentication failed for user "raved_admin"
- host: 'localhost', port: '5432', hostaddr: '127.0.0.1': connection failed: connection to server at "127.0.0.1", port 5432 failed: FATAL: password authentication failed for user "raved_admin"
```

**Context**:
- Trying to connect to PostgreSQL from pgAdmin 4 with new standardized credentials
- Internal connection working but external authentication failing
- User credentials: raved_admin / theRAVEDapp#123

**Root Cause**:
- pg_hba.conf configured with `trust` authentication for localhost connections
- External tools (pgAdmin) expecting password authentication
- Conflicting authentication methods in pg_hba.conf
- Password not properly hashed for SCRAM-SHA-256

**Impact**:
- Unable to connect to PostgreSQL from external database management tools
- Development workflow blocked for database administration
- Cannot access application databases from pgAdmin/DBeaver

---

### Error #5: pgAdmin Port Mapping Configuration Issue
**Date**: 2025-08-14
**Service**: pgAdmin 4 (Docker)
**Error Message**:
```
This site can't be reached
localhost:5050 took too long to respond
ERR_CONNECTION_TIMED_OUT
```

**Context**:
- pgAdmin container running but web interface not accessible
- Port 5050 mapped but service not responding
- Container logs showing gunicorn listening on port 80

**Root Cause**:
- Incorrect Docker port mapping configuration
- pgAdmin container listening on port 80 internally
- Docker port mapping configured as 5050:5050 instead of 5050:80
- Port mismatch preventing external access to web interface

**Impact**:
- pgAdmin web interface completely inaccessible
- Cannot manage PostgreSQL databases through GUI
- Development workflow blocked for database administration

---

### Error #5: pgAdmin Port Mapping Configuration Issue
**Date**: 2025-08-14
**Service**: pgAdmin 4 (Docker)
**Error Message**:
```
This site can't be reached
localhost:5050 took too long to respond
ERR_CONNECTION_TIMED_OUT
```

**Context**:
- pgAdmin container running but web interface not accessible
- Port 5050 mapped but service not responding
- Container logs showing gunicorn listening on port 80

**Root Cause**:
- Incorrect Docker port mapping configuration
- pgAdmin container listening on port 80 internally
- Docker port mapping configured as 5050:5050 instead of 5050:80
- Port mismatch preventing external access to web interface

**Impact**:
- pgAdmin web interface completely inaccessible
- Cannot manage PostgreSQL databases through GUI
- Development workflow blocked for database administration
