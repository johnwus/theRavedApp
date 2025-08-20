package com.raved.notification.test; 
 
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
@ActiveProfiles("test")
public class MongoConnectionTest {

    @Autowired
    private MongoTemplate mongoTemplate;
 
    @Test 
    public void testMongoConnection() { 
        System.out.println("Testing MongoDB connection..."); 
        String dbName = mongoTemplate.getDb().getName(); 
        System.out.println("Connected to MongoDB database: " + dbName); 
        System.out.println("MongoDB connection test: SUCCESS!"); 
    } 
} 
