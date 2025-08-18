# MongoDB Conversion Troubleshooting Guide

## 🔄 Systematic MongoDB Conversion Process

### Phase 1: Model Conversion
**Objective**: Convert JPA entities to MongoDB documents

#### Step 1: Update Entity Annotations
```java
// Before (JPA)
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

// After (MongoDB)
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
}
```

#### Step 2: Handle ID Type Changes
- Change `Long id` to `String id`
- Remove `@GeneratedValue` annotations
- Update constructors and getters/setters

#### Common Issues:
- **Foreign Key References**: Convert to String references
- **Embedded Objects**: Use `@DBRef` or embed directly
- **Indexes**: Add `@Indexed` annotations where needed

---

### Phase 2: Repository Conversion
**Objective**: Convert JPA repositories to MongoDB repositories

#### Step 1: Update Repository Interface
```java
// Before (JPA)
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}

// After (MongoDB)
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}
```

#### Step 2: Update Custom Queries
```java
// Before (JPQL)
@Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.isRead = false")
List<Notification> findUnreadByUserId(@Param("userId") Long userId);

// After (MongoDB)
List<Notification> findByUserIdAndIsReadFalse(String userId);
```

#### Common Issues:
- **Query Methods**: Update parameter types from Long to String
- **Custom Queries**: Convert JPQL to MongoDB query methods
- **Pagination**: Ensure Pageable works with String IDs

---

### Phase 3: Service Layer Conversion
**Objective**: Update service implementations for MongoDB

#### Step 1: Create ID Converter Utility
```java
@Component
public class MongoIdConverter {
    public static String toStringId(Long id) {
        return id != null ? id.toString() : null;
    }
    
    public static Long toLongId(String id) {
        return id != null ? Long.parseLong(id) : null;
    }
}
```

#### Step 2: Update Service Methods
```java
// Before
public Notification getById(Long id) {
    return repository.findById(id).orElse(null);
}

// After
public Notification getById(Long id) {
    return repository.findById(MongoIdConverter.toStringId(id)).orElse(null);
}
```

#### Common Issues:
- **Type Mismatches**: Use MongoIdConverter for all ID operations
- **Null Handling**: Ensure converter handles null values
- **Batch Operations**: Update all bulk operations

---

## 🚨 Common Conversion Errors

### Error 1: Compilation Type Mismatch
**Symptoms**:
```
incompatible types: java.lang.Long cannot be converted to java.lang.String
```

**Solution**:
1. Identify all Long ID usages
2. Wrap with `MongoIdConverter.toStringId()`
3. Add import statement for converter

### Error 2: Missing MongoDB Dependencies
**Symptoms**:
```
package org.springframework.data.mongodb does not exist
```

**Solution**:
1. Add MongoDB starter dependency to pom.xml
2. Remove JPA dependencies if not needed
3. Update Spring Boot configuration

### Error 3: Repository Method Not Found
**Symptoms**:
```
cannot find symbol: method findByUserId(java.lang.Long)
```

**Solution**:
1. Update repository method signatures
2. Change parameter types to String
3. Update method implementations

---

## 🔧 Debugging Techniques

### 1. Incremental Compilation
```bash
# Compile one service at a time
mvn compile -pl notification-service

# Check for errors before proceeding
mvn compile -pl social-service
```

### 2. Error Pattern Analysis
```bash
# Count compilation errors by type
mvn compile 2>&1 | grep "incompatible types" | wc -l
mvn compile 2>&1 | grep "cannot find symbol" | wc -l
```

### 3. Systematic Fix Approach
1. **Fix imports first** - Add all necessary import statements
2. **Fix type conversions** - Apply MongoIdConverter systematically
3. **Fix method signatures** - Update repository and service methods
4. **Test compilation** - Verify each service compiles individually

---

## 📋 Conversion Checklist

### Models ✅
- [ ] Replace `@Entity` with `@Document`
- [ ] Change `Long id` to `String id`
- [ ] Remove `@GeneratedValue`
- [ ] Update foreign key references
- [ ] Add MongoDB-specific annotations

### Repositories ✅
- [ ] Extend `MongoRepository<Entity, String>`
- [ ] Update method parameter types
- [ ] Convert JPQL to MongoDB queries
- [ ] Add missing repository methods
- [ ] Test repository functionality

### Services ✅
- [ ] Add MongoIdConverter import
- [ ] Wrap all ID operations with converter
- [ ] Update method implementations
- [ ] Handle null ID cases
- [ ] Test service functionality

### Configuration ✅
- [ ] Add MongoDB connection properties
- [ ] Remove JPA configuration
- [ ] Update database URLs
- [ ] Configure authentication
- [ ] Test database connectivity

---

## 🎯 Validation Steps

### 1. Compilation Test
```bash
mvn clean compile -f server/pom.xml
```

### 2. Service Startup Test
```bash
mvn spring-boot:run -pl notification-service
```

### 3. Database Connection Test
```bash
# Check MongoDB connectivity in application logs
grep "MongoDB" logs/application.log
```

### 4. Basic CRUD Test
```bash
# Test basic operations via REST API
curl -X POST http://localhost:8086/api/notifications \
  -H "Content-Type: application/json" \
  -d '{"userId":"1","message":"Test"}'
```

---

## 🔄 Rollback Strategy

If conversion fails:

1. **Revert to Git checkpoint**:
   ```bash
   git checkout HEAD~1
   ```

2. **Restore original dependencies**:
   - Remove MongoDB dependencies
   - Restore JPA dependencies

3. **Revert configuration changes**:
   - Restore PostgreSQL URLs
   - Remove MongoDB configuration

4. **Test original functionality**:
   ```bash
   mvn clean compile
   mvn spring-boot:run -pl notification-service
   ```
