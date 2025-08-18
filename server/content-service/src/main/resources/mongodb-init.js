// MongoDB Initialization Script for Content Service
// This script sets up the initial database structure, collections, and indexes

// Switch to the content database
// use raved_content;

print("🚀 Initializing MongoDB for Content Service...");

// =============================================================================
// COLLECTION CREATION
// =============================================================================

// Create posts collection
if (!db.getCollectionNames().includes("posts")) {
    db.createCollection("posts");
    print("✅ Created posts collection");
}

// Create media_files collection
if (!db.getCollectionNames().includes("media_files")) {
    db.createCollection("media_files");
    print("✅ Created media_files collection");
}

// Create post_tags collection
if (!db.getCollectionNames().includes("post_tags")) {
    db.createCollection("post_tags");
    print("✅ Created post_tags collection");
}

// Create comments collection
if (!db.getCollectionNames().includes("comments")) {
    db.createCollection("comments");
    print("✅ Created comments collection");
}

// Create content_metrics collection
if (!db.getCollectionNames().includes("content_metrics")) {
    db.createCollection("content_metrics");
    print("✅ Created content_metrics collection");
}

// =============================================================================
// INDEX CREATION
// =============================================================================

print("📊 Creating indexes...");

// Posts collection indexes
db.posts.createIndex({ "userId": 1 });
db.posts.createIndex({ "facultyId": 1 });
db.posts.createIndex({ "createdAt": -1 });
db.posts.createIndex({ "publishStatus": 1 });
db.posts.createIndex({ "moderationStatus": 1 });
db.posts.createIndex({ "isDeleted": 1 });
db.posts.createIndex({ "isFeatured": 1 });
db.posts.createIndex({ "category": 1 });
db.posts.createIndex({ "language": 1 });
db.posts.createIndex({ "tags": 1 });
db.posts.createIndex({ "engagementScore": -1 });
db.posts.createIndex({ "trendingScore": -1 });
db.posts.createIndex({ "viralityScore": -1 });
db.posts.createIndex({ "scheduledAt": 1 });
db.posts.createIndex({ "expiresAt": 1 });
db.posts.createIndex({ "moderatedAt": 1 });

// Text search index for posts
db.posts.createIndex({
    "title": "text",
    "content": "text",
    "tags": "text"
}, {
    "weights": {
        "title": 10,
        "content": 5,
        "tags": 3
    },
    "name": "posts_text_search"
});

// Media files collection indexes
db.media_files.createIndex({ "postId": 1 });
db.media_files.createIndex({ "uploaderId": 1 });
db.media_files.createIndex({ "mediaType": 1 });
db.media_files.createIndex({ "status": 1 });
db.media_files.createIndex({ "uploadedAt": -1 });
db.media_files.createIndex({ "processingStatus": 1 });
db.media_files.createIndex({ "accessLevel": 1 });
db.media_files.createIndex({ "isPublic": 1 });
db.media_files.createIndex({ "category": 1 });
db.media_files.createIndex({ "tags": 1 });
db.media_files.createIndex({ "viewCount": -1 });
db.media_files.createIndex({ "downloadCount": -1 });
db.media_files.createIndex({ "shareCount": -1 });

// Post tags collection indexes
db.post_tags.createIndex({ "tagName": 1 });
db.post_tags.createIndex({ "postId": 1 });
db.post_tags.createIndex({ "category": 1 });
db.post_tags.createIndex({ "status": 1 });
db.post_tags.createIndex({ "usageCount": -1 });
db.post_tags.createIndex({ "viewCount": -1 });
db.post_tags.createIndex({ "clickCount": -1 });
db.post_tags.createIndex({ "searchCount": -1 });
db.post_tags.createIndex({ "createdAt": -1 });
db.post_tags.createIndex({ "language": 1 });
db.post_tags.createIndex({ "region": 1 });
db.post_tags.createIndex({ "createdBy": 1 });

// Text search index for tags
db.post_tags.createIndex({
    "tagName": "text",
    "description": "text"
}, {
    "weights": {
        "tagName": 10,
        "description": 5
    },
    "name": "post_tags_text_search"
});

// Comments collection indexes
db.comments.createIndex({ "postId": 1 });
db.comments.createIndex({ "authorId": 1 });
db.comments.createIndex({ "createdAt": -1 });
db.comments.createIndex({ "status": 1 });
db.comments.createIndex({ "isDeleted": 1 });
db.comments.createIndex({ "moderationStatus": 1 });

// Content metrics collection indexes
db.content_metrics.createIndex({ "contentId": 1 });
db.content_metrics.createIndex({ "contentType": 1 });
db.content_metrics.createIndex({ "timestamp": -1 });
db.content_metrics.createIndex({ "metricType": 1 });
db.content_metrics.createIndex({ "userId": 1 });

print("✅ All indexes created successfully");

// =============================================================================
// VALIDATION SCHEMAS
// =============================================================================

print("🔍 Setting up validation schemas...");

// Posts collection validation
db.runCommand({
    collMod: "posts",
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId", "content", "postType", "visibility", "publishStatus"],
            properties: {
                userId: { bsonType: "string" },
                content: { bsonType: "string", minLength: 1 },
                postType: { enum: ["OUTFIT", "GENERAL", "POLL", "EVENT", "LINK", "PRODUCT"] },
                visibility: { enum: ["PUBLIC", "FACULTY_ONLY", "FOLLOWERS_ONLY", "CONNECTIONS_ONLY", "PRIVATE"] },
                publishStatus: { enum: ["DRAFT", "SCHEDULED", "PUBLISHED", "ARCHIVED"] },
                isDeleted: { bsonType: "bool" },
                isFeatured: { bsonType: "bool" },
                isPinned: { bsonType: "bool" },
                engagementScore: { bsonType: "double" },
                trendingScore: { bsonType: "double" },
                viralityScore: { bsonType: "double" }
            }
        }
    }
});

// Media files collection validation
db.runCommand({
    collMod: "media_files",
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["fileName", "fileType", "mimeType", "fileSize", "uploaderId"],
            properties: {
                fileName: { bsonType: "string", minLength: 1 },
                fileType: { enum: ["IMAGE", "VIDEO", "AUDIO", "DOCUMENT"] },
                mimeType: { bsonType: "string" },
                fileSize: { bsonType: "long", minimum: 0 },
                uploaderId: { bsonType: "string" },
                status: { enum: ["UPLOADING", "PROCESSING", "READY", "FAILED"] },
                accessLevel: { enum: ["PUBLIC", "PRIVATE", "RESTRICTED"] }
            }
        }
    }
});

// Post tags collection validation
db.runCommand({
    collMod: "post_tags",
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["tagName"],
            properties: {
                tagName: { bsonType: "string", minLength: 1, maxLength: 100 },
                category: { bsonType: "string" },
                status: { enum: ["ACTIVE", "INACTIVE", "MODERATED", "BLOCKED"] },
                usageCount: { bsonType: "int", minimum: 0 },
                viewCount: { bsonType: "int", minimum: 0 },
                clickCount: { bsonType: "int", minimum: 0 },
                searchCount: { bsonType: "int", minimum: 0 }
            }
        }
    }
});

print("✅ Validation schemas configured successfully");

// =============================================================================
// INITIAL DATA SETUP (Optional)
// =============================================================================

print("📝 Setting up initial data...");

// Create default categories if they don't exist
const defaultCategories = [
    "Technology", "Science", "Arts", "Business", "Health", "Education", 
    "Sports", "Entertainment", "Politics", "Environment", "Travel", "Food"
];

defaultCategories.forEach(category => {
    if (db.post_tags.countDocuments({ "tagName": category, "category": "SYSTEM" }) === 0) {
        db.post_tags.insertOne({
            tagName: category,
            category: "SYSTEM",
            status: "ACTIVE",
            usageCount: 0,
            viewCount: 0,
            clickCount: 0,
            searchCount: 0,
            language: "en",
            region: "global",
            createdBy: "system",
            createdAt: new Date(),
            updatedAt: new Date()
        });
    }
});

// Create system tags if they don't exist
const systemTags = [
    "featured", "trending", "popular", "verified", "official", "announcement"
];

systemTags.forEach(tag => {
    if (db.post_tags.countDocuments({ "tagName": tag, "category": "SYSTEM" }) === 0) {
        db.post_tags.insertOne({
            tagName: tag,
            category: "SYSTEM",
            status: "ACTIVE",
            usageCount: 0,
            viewCount: 0,
            clickCount: 0,
            searchCount: 0,
            language: "en",
            region: "global",
            createdBy: "system",
            createdAt: new Date(),
            updatedAt: new Date()
        });
    }
});

print("✅ Initial data setup completed");

// =============================================================================
// COLLECTION STATISTICS
// =============================================================================

print("\n📊 Collection Statistics:");
print("========================");

const collections = ["posts", "media_files", "post_tags", "comments", "content_metrics"];
collections.forEach(collectionName => {
    const count = db[collectionName].countDocuments();
    const size = db[collectionName].stats().size;
    print(`${collectionName}: ${count} documents, ${(size / 1024).toFixed(2)} KB`);
});

print("\n🎉 MongoDB initialization completed successfully!");
print("The content service is ready to use MongoDB as the primary database.");
print("You can now start the application and test the MongoDB integration.");
