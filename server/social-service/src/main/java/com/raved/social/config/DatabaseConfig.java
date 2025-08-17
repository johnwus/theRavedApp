package com.raved.social.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.beans.factory.annotation.Value;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Arrays;

/**
 * MongoDB Configuration for TheRavedApp Social Service
 *
 * Configures MongoDB connection and repository scanning. Converted from JPA
 * configuration to MongoDB configuration.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.raved.social.repository")
public class DatabaseConfig {

    @Value("${spring.data.mongodb.database:raved_social}")
    private String databaseName;

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), databaseName);

        // Configure custom conversions if needed
        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        converter.setCustomConversions(customConversions());
        converter.afterPropertiesSet();

        return mongoTemplate;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList( // Add custom converters here if needed
                ));
    }
}
