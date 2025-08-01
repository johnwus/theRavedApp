#!/usr/bin/env python3
"""
Script to create ALL missing files according to PROJECT_STRUCTURE.md
"""

import os
from pathlib import Path

def create_file(path, content=""):
    """Create file with content"""
    Path(path).parent.mkdir(parents=True, exist_ok=True)
    if not Path(path).exists():
        with open(path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Created: {path}")

def get_java_class_template(package, class_name, class_type="class", imports=None, extends=None, implements=None):
    """Generate Java class template"""
    imports_str = ""
    if imports:
        imports_str = "\n".join([f"import {imp};" for imp in imports]) + "\n\n"
    
    extends_str = f" extends {extends}" if extends else ""
    implements_str = f" implements {implements}" if implements else ""
    
    return f"""package {package};

{imports_str}/**
 * {class_name} for TheRavedApp
 */
public {class_type} {class_name}{extends_str}{implements_str} {{
    // Implementation
}}
"""

def create_content_service_files():
    """Create all Content Service files"""
    base = "server/content-service/src/main/java/com/raved/content"
    
    # Service files
    services = [
        "PostService", "MediaService", "FeedService", "TagService", 
        "ContentModerationService", "S3Service"
    ]
    for service in services:
        create_file(f"{base}/service/{service}.java", 
                   get_java_class_template(f"com.raved.content.service", service, "interface"))
    
    # Repository files
    repos = ["PostRepository", "MediaFileRepository", "PostTagRepository"]
    for repo in repos:
        create_file(f"{base}/repository/{repo}.java",
                   get_java_class_template(f"com.raved.content.repository", repo, "interface",
                   ["org.springframework.data.jpa.repository.JpaRepository"]))
    
    # Model files
    models = ["Post", "MediaFile", "PostTag", "ContentType"]
    for model in models:
        create_file(f"{base}/model/{model}.java",
                   get_java_class_template(f"com.raved.content.model", model))
    
    # DTO files
    request_dtos = ["CreatePostRequest", "UpdatePostRequest", "MediaUploadRequest"]
    for dto in request_dtos:
        create_file(f"{base}/dto/request/{dto}.java",
                   get_java_class_template(f"com.raved.content.dto.request", dto))
    
    response_dtos = ["PostResponse", "FeedResponse", "MediaResponse"]
    for dto in response_dtos:
        create_file(f"{base}/dto/response/{dto}.java",
                   get_java_class_template(f"com.raved.content.dto.response", dto))
    
    # Config files
    configs = ["DatabaseConfig", "S3Config", "RedisConfig"]
    for config in configs:
        create_file(f"{base}/config/{config}.java",
                   get_java_class_template(f"com.raved.content.config", config))
    
    # Algorithm files
    algorithms = ["FeedAlgorithm", "TrendingAlgorithm", "FacultyFeedAlgorithm"]
    for algo in algorithms:
        create_file(f"{base}/algorithm/{algo}.java",
                   get_java_class_template(f"com.raved.content.algorithm", algo))
    
    # Exception files
    exceptions = ["PostNotFoundException", "MediaUploadException", "ContentModerationException"]
    for exc in exceptions:
        create_file(f"{base}/exception/{exc}.java",
                   get_java_class_template(f"com.raved.content.exception", exc, "class", 
                   None, "RuntimeException"))

def create_social_service_files():
    """Create all Social Service files"""
    base = "server/social-service/src/main/java/com/raved/social"
    
    # Controllers
    controllers = ["LikeController", "CommentController", "FollowController", "ActivityController"]
    for controller in controllers:
        create_file(f"{base}/controller/{controller}.java",
                   get_java_class_template(f"com.raved.social.controller", controller))
    
    # Services
    services = ["LikeService", "CommentService", "FollowService", "ActivityService", "EngagementAnalyticsService"]
    for service in services:
        create_file(f"{base}/service/{service}.java",
                   get_java_class_template(f"com.raved.social.service", service, "interface"))
    
    # Repositories
    repos = ["LikeRepository", "CommentRepository", "FollowRepository", "ActivityRepository"]
    for repo in repos:
        create_file(f"{base}/repository/{repo}.java",
                   get_java_class_template(f"com.raved.social.repository", repo, "interface"))
    
    # Models
    models = ["Like", "Comment", "Follow", "Activity"]
    for model in models:
        create_file(f"{base}/model/{model}.java",
                   get_java_class_template(f"com.raved.social.model", model))
    
    # DTOs
    request_dtos = ["LikeRequest", "CommentRequest", "FollowRequest"]
    for dto in request_dtos:
        create_file(f"{base}/dto/request/{dto}.java",
                   get_java_class_template(f"com.raved.social.dto.request", dto))
    
    response_dtos = ["LikeResponse", "CommentResponse", "ActivityResponse"]
    for dto in response_dtos:
        create_file(f"{base}/dto/response/{dto}.java",
                   get_java_class_template(f"com.raved.social.dto.response", dto))
    
    # Config
    configs = ["DatabaseConfig", "RedisConfig"]
    for config in configs:
        create_file(f"{base}/config/{config}.java",
                   get_java_class_template(f"com.raved.social.config", config))
    
    # Exceptions
    exceptions = ["CommentNotFoundException", "DuplicateLikeException"]
    for exc in exceptions:
        create_file(f"{base}/exception/{exc}.java",
                   get_java_class_template(f"com.raved.social.exception", exc, "class", None, "RuntimeException"))

def create_realtime_service_files():
    """Create all Realtime Service files"""
    base = "server/realtime-service/src/main/java/com/raved/realtime"
    
    # Controllers
    controllers = ["ChatController", "WebSocketController", "NotificationController"]
    for controller in controllers:
        create_file(f"{base}/controller/{controller}.java",
                   get_java_class_template(f"com.raved.realtime.controller", controller))
    
    # Services
    services = ["ChatService", "WebSocketService", "MessageService", "PresenceService"]
    for service in services:
        create_file(f"{base}/service/{service}.java",
                   get_java_class_template(f"com.raved.realtime.service", service, "interface"))
    
    # Repositories
    repos = ["ChatRoomRepository", "MessageRepository", "UserSessionRepository"]
    for repo in repos:
        create_file(f"{base}/repository/{repo}.java",
                   get_java_class_template(f"com.raved.realtime.repository", repo, "interface"))
    
    # Models
    models = ["ChatRoom", "Message", "UserSession", "MessageType"]
    for model in models:
        create_file(f"{base}/model/{model}.java",
                   get_java_class_template(f"com.raved.realtime.model", model))
    
    # DTOs
    request_dtos = ["SendMessageRequest", "CreateChatRoomRequest"]
    for dto in request_dtos:
        create_file(f"{base}/dto/request/{dto}.java",
                   get_java_class_template(f"com.raved.realtime.dto.request", dto))
    
    response_dtos = ["MessageResponse", "ChatRoomResponse"]
    for dto in response_dtos:
        create_file(f"{base}/dto/response/{dto}.java",
                   get_java_class_template(f"com.raved.realtime.dto.response", dto))
    
    # Config
    configs = ["WebSocketConfig", "RabbitMQConfig", "RedisConfig"]
    for config in configs:
        create_file(f"{base}/config/{config}.java",
                   get_java_class_template(f"com.raved.realtime.config", config))
    
    # WebSocket
    websocket_files = ["ChatWebSocketHandler", "WebSocketSessionManager", "MessageBroker"]
    for ws in websocket_files:
        create_file(f"{base}/websocket/{ws}.java",
                   get_java_class_template(f"com.raved.realtime.websocket", ws))
    
    # Exceptions
    exceptions = ["ChatRoomNotFoundException", "MessageDeliveryException"]
    for exc in exceptions:
        create_file(f"{base}/exception/{exc}.java",
                   get_java_class_template(f"com.raved.realtime.exception", exc, "class", None, "RuntimeException"))

def create_ecommerce_service_files():
    """Create all Ecommerce Service files"""
    base = "server/ecommerce-service/src/main/java/com/raved/ecommerce"
    
    # Controllers
    controllers = ["ProductController", "OrderController", "PaymentController", "InventoryController"]
    for controller in controllers:
        create_file(f"{base}/controller/{controller}.java",
                   get_java_class_template(f"com.raved.ecommerce.controller", controller))
    
    # Services
    services = ["ProductService", "OrderService", "PaymentService", "InventoryService", "CartService"]
    for service in services:
        create_file(f"{base}/service/{service}.java",
                   get_java_class_template(f"com.raved.ecommerce.service", service, "interface"))
    
    # Repositories
    repos = ["ProductRepository", "OrderRepository", "PaymentRepository", "InventoryRepository"]
    for repo in repos:
        create_file(f"{base}/repository/{repo}.java",
                   get_java_class_template(f"com.raved.ecommerce.repository", repo, "interface"))
    
    # Models
    models = ["Product", "Order", "OrderItem", "Payment", "Inventory"]
    for model in models:
        create_file(f"{base}/model/{model}.java",
                   get_java_class_template(f"com.raved.ecommerce.model", model))
    
    # DTOs
    request_dtos = ["CreateProductRequest", "CreateOrderRequest", "PaymentRequest"]
    for dto in request_dtos:
        create_file(f"{base}/dto/request/{dto}.java",
                   get_java_class_template(f"com.raved.ecommerce.dto.request", dto))
    
    response_dtos = ["ProductResponse", "OrderResponse", "PaymentResponse"]
    for dto in response_dtos:
        create_file(f"{base}/dto/response/{dto}.java",
                   get_java_class_template(f"com.raved.ecommerce.dto.response", dto))
    
    # Config
    configs = ["DatabaseConfig", "PaymentConfig"]
    for config in configs:
        create_file(f"{base}/config/{config}.java",
                   get_java_class_template(f"com.raved.ecommerce.config", config))
    
    # Exceptions
    exceptions = ["ProductNotFoundException", "InsufficientInventoryException", "PaymentProcessingException"]
    for exc in exceptions:
        create_file(f"{base}/exception/{exc}.java",
                   get_java_class_template(f"com.raved.ecommerce.exception", exc, "class", None, "RuntimeException"))

def create_notification_service_files():
    """Create all Notification Service files"""
    base = "server/notification-service/src/main/java/com/raved/notification"
    
    # Controllers
    controllers = ["NotificationController", "TemplateController"]
    for controller in controllers:
        create_file(f"{base}/controller/{controller}.java",
                   get_java_class_template(f"com.raved.notification.controller", controller))
    
    # Services
    services = ["NotificationService", "PushNotificationService", "EmailService", "SmsService", "TemplateService"]
    for service in services:
        create_file(f"{base}/service/{service}.java",
                   get_java_class_template(f"com.raved.notification.service", service, "interface"))
    
    # Repositories
    repos = ["NotificationRepository", "NotificationTemplateRepository", "DeliveryLogRepository"]
    for repo in repos:
        create_file(f"{base}/repository/{repo}.java",
                   get_java_class_template(f"com.raved.notification.repository", repo, "interface"))
    
    # Models
    models = ["Notification", "NotificationTemplate", "DeliveryLog", "NotificationType"]
    for model in models:
        create_file(f"{base}/model/{model}.java",
                   get_java_class_template(f"com.raved.notification.model", model))
    
    # DTOs
    request_dtos = ["SendNotificationRequest", "CreateTemplateRequest"]
    for dto in request_dtos:
        create_file(f"{base}/dto/request/{dto}.java",
                   get_java_class_template(f"com.raved.notification.dto.request", dto))
    
    response_dtos = ["NotificationResponse", "DeliveryResponse"]
    for dto in response_dtos:
        create_file(f"{base}/dto/response/{dto}.java",
                   get_java_class_template(f"com.raved.notification.dto.response", dto))
    
    # Config
    configs = ["KafkaConfig", "FirebaseConfig", "EmailConfig"]
    for config in configs:
        create_file(f"{base}/config/{config}.java",
                   get_java_class_template(f"com.raved.notification.config", config))
    
    # Kafka
    kafka_files = ["NotificationConsumer", "NotificationProducer"]
    for kafka in kafka_files:
        create_file(f"{base}/kafka/{kafka}.java",
                   get_java_class_template(f"com.raved.notification.kafka", kafka))
    
    # Exceptions
    exceptions = ["NotificationDeliveryException", "TemplateNotFoundException"]
    for exc in exceptions:
        create_file(f"{base}/exception/{exc}.java",
                   get_java_class_template(f"com.raved.notification.exception", exc, "class", None, "RuntimeException"))

def create_analytics_service_files():
    """Create all Analytics Service files"""
    base = "server/analytics-service/src/main/java/com/raved/analytics"
    
    # Controllers
    controllers = ["AnalyticsController", "MetricsController", "ReportsController"]
    for controller in controllers:
        create_file(f"{base}/controller/{controller}.java",
                   get_java_class_template(f"com.raved.analytics.controller", controller))
    
    # Services
    services = ["AnalyticsService", "MetricsService", "TrendingService", "ReportService"]
    for service in services:
        create_file(f"{base}/service/{service}.java",
                   get_java_class_template(f"com.raved.analytics.service", service, "interface"))
    
    # Repositories
    repos = ["AnalyticsEventRepository", "UserMetricsRepository", "ContentMetricsRepository"]
    for repo in repos:
        create_file(f"{base}/repository/{repo}.java",
                   get_java_class_template(f"com.raved.analytics.repository", repo, "interface"))
    
    # Models
    models = ["AnalyticsEvent", "UserMetrics", "ContentMetrics", "EventType"]
    for model in models:
        create_file(f"{base}/model/{model}.java",
                   get_java_class_template(f"com.raved.analytics.model", model))
    
    # DTOs
    request_dtos = ["TrackEventRequest", "ReportRequest"]
    for dto in request_dtos:
        create_file(f"{base}/dto/request/{dto}.java",
                   get_java_class_template(f"com.raved.analytics.dto.request", dto))
    
    response_dtos = ["AnalyticsResponse", "MetricsResponse", "TrendingResponse"]
    for dto in response_dtos:
        create_file(f"{base}/dto/response/{dto}.java",
                   get_java_class_template(f"com.raved.analytics.dto.response", dto))
    
    # Config
    configs = ["DatabaseConfig", "RedisConfig"]
    for config in configs:
        create_file(f"{base}/config/{config}.java",
                   get_java_class_template(f"com.raved.analytics.config", config))
    
    # Algorithm
    algorithms = ["TrendingAlgorithm", "EngagementCalculator"]
    for algo in algorithms:
        create_file(f"{base}/algorithm/{algo}.java",
                   get_java_class_template(f"com.raved.analytics.algorithm", algo))
    
    # Exceptions
    exceptions = ["MetricsNotFoundException", "AnalyticsProcessingException"]
    for exc in exceptions:
        create_file(f"{base}/exception/{exc}.java",
                   get_java_class_template(f"com.raved.analytics.exception", exc, "class", None, "RuntimeException"))

def create_api_gateway_files():
    """Create API Gateway files"""
    base = "server/api-gateway/src/main/java/com/raved/gateway"
    
    # Config files
    configs = ["SecurityConfig", "CorsConfig", "RateLimitConfig"]
    for config in configs:
        create_file(f"{base}/config/{config}.java",
                   get_java_class_template(f"com.raved.gateway.config", config))
    
    # Filter files
    filters = ["AuthenticationFilter", "LoggingFilter", "RateLimitFilter"]
    for filter_name in filters:
        create_file(f"{base}/filter/{filter_name}.java",
                   get_java_class_template(f"com.raved.gateway.filter", filter_name))
    
    # Exception files
    exceptions = ["GlobalExceptionHandler", "GatewayException"]
    for exc in exceptions:
        create_file(f"{base}/exception/{exc}.java",
                   get_java_class_template(f"com.raved.gateway.exception", exc))

def create_eureka_config():
    """Create Eureka config"""
    base = "server/eureka-server/src/main/java/com/raved/eureka"
    create_file(f"{base}/config/EurekaConfig.java",
               get_java_class_template("com.raved.eureka.config", "EurekaConfig"))

def create_config_server_config():
    """Create Config Server config"""
    base = "server/config-server/src/main/java/com/raved/config"
    create_file(f"{base}/config/ConfigServerConfig.java",
               get_java_class_template("com.raved.config.config", "ConfigServerConfig"))

def create_shared_files():
    """Create shared module files"""
    # Common module
    common_base = "server/shared/common/src/main/java/com/raved/common"
    
    # DTOs
    dtos = ["ErrorResponse", "PageResponse"]
    for dto in dtos:
        create_file(f"{common_base}/dto/{dto}.java",
                   get_java_class_template("com.raved.common.dto", dto))
    
    # Exceptions
    exceptions = ["ValidationException", "ServiceException"]
    for exc in exceptions:
        create_file(f"{common_base}/exception/{exc}.java",
                   get_java_class_template("com.raved.common.exception", exc, "class", None, "BaseException"))
    
    # Utils
    utils = ["DateUtils", "StringUtils", "ValidationUtils"]
    for util in utils:
        create_file(f"{common_base}/util/{util}.java",
                   get_java_class_template("com.raved.common.util", util))
    
    # Constants
    constants = ["ErrorCodes"]
    for const in constants:
        create_file(f"{common_base}/constants/{const}.java",
                   get_java_class_template("com.raved.common.constants", const))
    
    # Security module
    security_base = "server/shared/security/src/main/java/com/raved/security"
    
    # JWT
    jwt_files = ["JwtUtils", "JwtValidator"]
    for jwt in jwt_files:
        create_file(f"{security_base}/jwt/{jwt}.java",
                   get_java_class_template("com.raved.security.jwt", jwt))
    
    # OAuth
    oauth_files = ["OAuth2Utils"]
    for oauth in oauth_files:
        create_file(f"{security_base}/oauth/{oauth}.java",
                   get_java_class_template("com.raved.security.oauth", oauth))
    
    # Encryption
    encryption_files = ["EncryptionUtils"]
    for enc in encryption_files:
        create_file(f"{security_base}/encryption/{enc}.java",
                   get_java_class_template("com.raved.security.encryption", enc))

def main():
    """Main function"""
    print("Creating all missing files according to PROJECT_STRUCTURE.md...")
    
    os.chdir("c:/theRavedApp")
    
    create_content_service_files()
    create_social_service_files()
    create_realtime_service_files()
    create_ecommerce_service_files()
    create_notification_service_files()
    create_analytics_service_files()
    create_api_gateway_files()
    create_eureka_config()
    create_config_server_config()
    create_shared_files()
    
    print("All missing files created successfully!")

if __name__ == "__main__":
    main()
