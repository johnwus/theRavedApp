// MongoDB Test Data Setup for TheRavedApp
// This script creates test data for development and testing

// Switch to the content database
// use raved_content;

// Create test posts for content service
db.posts.insertMany([
    {
        _id: ObjectId(),
        title: "Test Post 1",
        content: "This is a test post for development purposes. It contains some sample content to test the content service.",
        authorId: "1",
        tags: ["test", "development", "sample"],
        createdAt: new Date(),
        updatedAt: new Date(),
        status: "PUBLISHED",
        metadata: {
            viewCount: 0,
            likeCount: 0,
            commentCount: 0
        }
    },
    {
        _id: ObjectId(),
        title: "Test Post 2",
        content: "Another test post with different content to test various scenarios in the content service.",
        authorId: "2",
        tags: ["test", "content", "scenarios"],
        createdAt: new Date(),
        updatedAt: new Date(),
        status: "PUBLISHED",
        metadata: {
            viewCount: 0,
            likeCount: 0,
            commentCount: 0
        }
    },
    {
        _id: ObjectId(),
        title: "Test Post 3",
        content: "A third test post to ensure we have enough data for testing pagination and search functionality.",
        authorId: "3",
        tags: ["test", "pagination", "search"],
        createdAt: new Date(),
        updatedAt: new Date(),
        status: "DRAFT",
        metadata: {
            viewCount: 0,
            likeCount: 0,
            commentCount: 0
        }
    }
]);

// Create test comments
db.comments.insertMany([
    {
        _id: ObjectId(),
        postId: db.posts.findOne({title: "Test Post 1"})._id,
        authorId: "2",
        content: "This is a test comment on the first post.",
        createdAt: new Date(),
        status: "ACTIVE"
    },
    {
        _id: ObjectId(),
        postId: db.posts.findOne({title: "Test Post 1"})._id,
        authorId: "3",
        content: "Another test comment to test multiple comments.",
        createdAt: new Date(),
        status: "ACTIVE"
    }
]);

// Create test media files
db.media_files.insertMany([
    {
        _id: ObjectId(),
        fileName: "test-image-1.jpg",
        fileType: "IMAGE",
        fileSize: 1024000,
        uploaderId: "1",
        url: "https://example.com/images/test-image-1.jpg",
        metadata: {
            width: 1920,
            height: 1080,
            format: "JPEG"
        },
        uploadedAt: new Date(),
        status: "ACTIVE"
    },
    {
        _id: ObjectId(),
        fileName: "test-video-1.mp4",
        fileType: "VIDEO",
        fileSize: 52428800,
        uploaderId: "2",
        url: "https://example.com/videos/test-video-1.mp4",
        metadata: {
            duration: 120,
            format: "MP4",
            resolution: "1080p"
        },
        uploadedAt: new Date(),
        status: "ACTIVE"
    }
]);

// Create test analytics events
db.user_analytics.insertMany([
    {
        _id: ObjectId(),
        userId: "1",
        eventType: "PAGE_VIEW",
        eventData: {
            page: "/posts",
            referrer: "https://google.com",
            userAgent: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
        },
        timestamp: new Date(),
        sessionId: "session-1"
    },
    {
        _id: ObjectId(),
        userId: "1",
        eventType: "POST_LIKE",
        eventData: {
            postId: "post-1",
            action: "LIKE"
        },
        timestamp: new Date(),
        sessionId: "session-1"
    },
    {
        _id: ObjectId(),
        userId: "2",
        eventType: "USER_SIGNUP",
        eventData: {
            source: "WEB",
            campaign: "organic"
        },
        timestamp: new Date(),
        sessionId: "session-2"
    }
]);

// Create test social interactions
db.social_interactions.insertMany([
    {
        _id: ObjectId(),
        userId: "1",
        targetUserId: "2",
        interactionType: "FOLLOW",
        createdAt: new Date(),
        status: "ACTIVE"
    },
    {
        _id: ObjectId(),
        userId: "2",
        targetUserId: "3",
        interactionType: "FOLLOW",
        createdAt: new Date(),
        status: "ACTIVE"
    },
    {
        _id: ObjectId(),
        userId: "1",
        targetPostId: db.posts.findOne({title: "Test Post 2"})._id,
        interactionType: "LIKE",
        createdAt: new Date(),
        status: "ACTIVE"
    }
]);

// Create test notification templates
db.notification_templates.insertMany([
    {
        _id: ObjectId(),
        name: "welcome_email",
        type: "EMAIL",
        subject: "Welcome to TheRavedApp!",
        content: "Hi {{userName}}, welcome to TheRavedApp! We're excited to have you on board.",
        variables: ["userName"],
        createdAt: new Date(),
        status: "ACTIVE"
    },
    {
        _id: ObjectId(),
        name: "post_like_notification",
        type: "PUSH",
        title: "New Like",
        content: "{{userName}} liked your post",
        variables: ["userName"],
        createdAt: new Date(),
        status: "ACTIVE"
    }
]);

print("MongoDB test data setup completed successfully!");
print("Created:");
print("- " + db.posts.count() + " posts");
print("- " + db.comments.count() + " comments");
print("- " + db.media_files.count() + " media files");
print("- " + db.user_analytics.count() + " analytics events");
print("- " + db.social_interactions.count() + " social interactions");
print("- " + db.notification_templates.count() + " notification templates");
