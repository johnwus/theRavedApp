# Raved App - Complete Project Structure

## Root Directory Structure

```
theRavedApp/
├── client/                           # React Native Expo frontend
├── server/                           # Spring Boot microservices backend
├── infrastructure/                   # Docker, Kubernetes, deployment configs
├── docs/                            # Documentation
├── scripts/                         # Build and deployment scripts
├── .github/                         # GitHub Actions workflows
├── docker-compose.yml               # Local development environment
├── docker-compose.prod.yml          # Production environment
├── README.md
├── .gitignore
└── .env.example

```

## 1. CLIENT-SIDE STRUCTURE (React Native + Expo)

```
client/
├── src/
│   ├── components/                   # Reusable UI components
│   │   ├── common/
│   │   │   ├── Button/
│   │   │   │   ├── Button.tsx
│   │   │   │   ├── Button.styles.ts
│   │   │   │   └── index.ts
│   │   │   ├── Input/
│   │   │   │   ├── Input.tsx
│   │   │   │   ├── Input.styles.ts
│   │   │   │   └── index.ts
│   │   │   ├── Modal/
│   │   │   ├── Loading/
│   │   │   ├── Avatar/
│   │   │   ├── Badge/
│   │   │   └── index.ts
│   │   ├── forms/
│   │   │   ├── LoginForm/
│   │   │   ├── RegisterForm/
│   │   │   ├── PostForm/
│   │   │   └── ProfileForm/
│   │   ├── media/
│   │   │   ├── ImagePicker/
│   │   │   ├── VideoPlayer/
│   │   │   ├── MediaCarousel/
│   │   │   └── CameraComponent/
│   │   ├── social/
│   │   │   ├── PostCard/
│   │   │   │   ├── PostCard.tsx
│   │   │   │   ├── PostCard.styles.ts
│   │   │   │   ├── PostHeader.tsx
│   │   │   │   ├── PostContent.tsx
│   │   │   │   ├── PostActions.tsx
│   │   │   │   └── index.ts
│   │   │   ├── CommentCard/
│   │   │   ├── LikeButton/
│   │   │   ├── ShareButton/
│   │   │   └── FollowButton/
│   │   ├── navigation/
│   │   │   ├── TabBar/
│   │   │   ├── Header/
│   │   │   └── DrawerContent/
│   │   └── chat/
│   │       ├── MessageBubble/
│   │       ├── ChatInput/
│   │       ├── ChatHeader/
│   │       └── MessageList/
│   ├── screens/
│   │   ├── auth/
│   │   │   ├── LoginScreen.tsx
│   │   │   ├── RegisterScreen.tsx
│   │   │   ├── ForgotPasswordScreen.tsx
│   │   │   └── StudentVerificationScreen.tsx
│   │   ├── main/
│   │   │   ├── HomeScreen.tsx
│   │   │   ├── FeedScreen.tsx
│   │   │   ├── ExploreScreen.tsx
│   │   │   └── TrendingScreen.tsx
│   │   ├── profile/
│   │   │   ├── ProfileScreen.tsx
│   │   │   ├── EditProfileScreen.tsx
│   │   │   ├── SettingsScreen.tsx
│   │   │   └── AnalyticsScreen.tsx
│   │   ├── social/
│   │   │   ├── PostDetailScreen.tsx
│   │   │   ├── CreatePostScreen.tsx
│   │   │   ├── CommentsScreen.tsx
│   │   │   └── FollowersScreen.tsx
│   │   ├── chat/
│   │   │   ├── ChatListScreen.tsx
│   │   │   ├── ChatScreen.tsx
│   │   │   └── NewChatScreen.tsx
│   │   ├── ecommerce/
│   │   │   ├── StoreScreen.tsx
│   │   │   ├── ProductDetailScreen.tsx
│   │   │   ├── CartScreen.tsx
│   │   │   ├── CheckoutScreen.tsx
│   │   │   └── OrderHistoryScreen.tsx
│   │   └── faculty/
│   │       ├── FacultyFeedScreen.tsx
│   │       ├── FacultyMembersScreen.tsx
│   │       └── FacultyEventsScreen.tsx
│   ├── navigation/
│   │   ├── AppNavigator.tsx
│   │   ├── AuthNavigator.tsx
│   │   ├── MainNavigator.tsx
│   │   ├── TabNavigator.tsx
│   │   ├── StackNavigator.tsx
│   │   └── types.ts
│   ├── store/
│   │   ├── index.ts                  # Redux store configuration
│   │   ├── rootReducer.ts
│   │   ├── middleware.ts
│   │   ├── slices/
│   │   │   ├── authSlice.ts
│   │   │   ├── userSlice.ts
│   │   │   ├── postsSlice.ts
│   │   │   ├── socialSlice.ts
│   │   │   ├── chatSlice.ts
│   │   │   ├── ecommerceSlice.ts
│   │   │   └── uiSlice.ts
│   │   └── api/
│   │       ├── baseApi.ts            # RTK Query base API
│   │       ├── authApi.ts
│   │       ├── userApi.ts
│   │       ├── postsApi.ts
│   │       ├── socialApi.ts
│   │       ├── chatApi.ts
│   │       ├── ecommerceApi.ts
│   │       └── analyticsApi.ts
│   ├── services/
│   │   ├── api/
│   │   │   ├── client.ts             # Axios/Fetch client configuration
│   │   │   ├── endpoints.ts
│   │   │   └── interceptors.ts
│   │   ├── auth/
│   │   │   ├── authService.ts
│   │   │   ├── tokenService.ts
│   │   │   └── biometricService.ts
│   │   ├── storage/
│   │   │   ├── mmkvStorage.ts        # MMKV local storage
│   │   │   ├── secureStorage.ts
│   │   │   └── cacheService.ts
│   │   ├── media/
│   │   │   ├── imageService.ts
│   │   │   ├── videoService.ts
│   │   │   ├── uploadService.ts
│   │   │   └── compressionService.ts
│   │   ├── socket/
│   │   │   ├── socketService.ts      # Socket.io client
│   │   │   ├── chatSocket.ts
│   │   │   └── notificationSocket.ts
│   │   ├── notifications/
│   │   │   ├── pushNotifications.ts
│   │   │   ├── localNotifications.ts
│   │   │   └── notificationService.ts
│   │   └── analytics/
│   │       ├── analyticsService.ts
│   │       ├── trackingService.ts
│   │       └── metricsService.ts
│   ├── utils/
│   │   ├── constants/
│   │   │   ├── api.ts
│   │   │   ├── colors.ts
│   │   │   ├── dimensions.ts
│   │   │   ├── fonts.ts
│   │   │   └── routes.ts
│   │   ├── helpers/
│   │   │   ├── dateHelpers.ts
│   │   │   ├── formatHelpers.ts
│   │   │   ├── validationHelpers.ts
│   │   │   ├── imageHelpers.ts
│   │   │   └── networkHelpers.ts
│   │   ├── hooks/
│   │   │   ├── useAuth.ts
│   │   │   ├── useSocket.ts
│   │   │   ├── useCamera.ts
│   │   │   ├── useLocation.ts
│   │   │   ├── useDebounce.ts
│   │   │   └── useInfiniteScroll.ts
│   │   └── types/
│   │       ├── api.ts
│   │       ├── user.ts
│   │       ├── post.ts
│   │       ├── chat.ts
│   │       ├── ecommerce.ts
│   │       └── navigation.ts
│   ├── styles/
│   │   ├── theme/
│   │   │   ├── colors.ts
│   │   │   ├── typography.ts
│   │   │   ├── spacing.ts
│   │   │   ├── shadows.ts
│   │   │   └── index.ts
│   │   ├── global/
│   │   │   ├── globalStyles.ts
│   │   │   └── nativewind.config.js
│   │   └── components/
│   │       ├── buttonStyles.ts
│   │       ├── inputStyles.ts
│   │       └── cardStyles.ts
│   └── assets/
│       ├── images/
│       │   ├── icons/
│       │   ├── logos/
│       │   ├── placeholders/
│       │   └── backgrounds/
│       ├── fonts/
│       ├── videos/
│       └── sounds/
├── __tests__/
│   ├── components/
│   ├── screens/
│   ├── services/
│   ├── utils/
│   └── __mocks__/
├── android/
├── ios/
├── app.json
├── App.tsx
├── babel.config.js
├── metro.config.js
├── package.json
├── package-lock.json
├── tsconfig.json
├── tailwind.config.js
├── .expo/
├── .gitignore
└── README.md
```

## 2. SERVER-SIDE STRUCTURE (Spring Boot Microservices)

```
server/
├── api-gateway/                      # Spring Cloud Gateway (Port 8080)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/gateway/
│   │   │   │   ├── GatewayApplication.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── GatewayConfig.java
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   └── RateLimitConfig.java
│   │   │   │   ├── filter/
│   │   │   │   │   ├── AuthenticationFilter.java
│   │   │   │   │   ├── LoggingFilter.java
│   │   │   │   │   └── RateLimitFilter.java
│   │   │   │   └── exception/
│   │   │   │       ├── GlobalExceptionHandler.java
│   │   │   │       └── GatewayException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-prod.yml
│   │   │       └── bootstrap.yml
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── user-service/                     # User Authentication Service (Port 8081)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/user/
│   │   │   │   ├── UserServiceApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── UserController.java
│   │   │   │   │   ├── ProfileController.java
│   │   │   │   │   └── FacultyController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── AuthService.java
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   ├── ProfileService.java
│   │   │   │   │   ├── StudentVerificationService.java
│   │   │   │   │   └── JwtService.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── StudentVerificationRepository.java
│   │   │   │   │   └── FacultyRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── StudentVerification.java
│   │   │   │   │   ├── Faculty.java
│   │   │   │   │   └── University.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   │   └── VerificationRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── AuthResponse.java
│   │   │   │   │       ├── UserResponse.java
│   │   │   │   │       └── ProfileResponse.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── DatabaseConfig.java
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── JwtConfig.java
│   │   │   │   │   └── RedisConfig.java
│   │   │   │   ├── security/
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   │   └── CustomUserDetailsService.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── UserNotFoundException.java
│   │   │   │   │   ├── InvalidCredentialsException.java
│   │   │   │   │   └── VerificationFailedException.java
│   │   │   │   └── util/
│   │   │   │       ├── PasswordEncoder.java
│   │   │   │       └── ValidationUtils.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-prod.yml
│   │   │       └── db/migration/
│   │   │           ├── V1__Create_users_table.sql
│   │   │           ├── V2__Create_faculties_table.sql
│   │   │           └── V3__Create_student_verifications_table.sql
│   │   └── test/
│   │       ├── java/com/raved/user/
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   └── repository/
│   │       └── resources/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── content-service/                  # Content Management Service (Port 8082)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/content/
│   │   │   │   ├── ContentServiceApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── PostController.java
│   │   │   │   │   ├── MediaController.java
│   │   │   │   │   ├── FeedController.java
│   │   │   │   │   └── TagController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── PostService.java
│   │   │   │   │   ├── MediaService.java
│   │   │   │   │   ├── FeedService.java
│   │   │   │   │   ├── TagService.java
│   │   │   │   │   ├── ContentModerationService.java
│   │   │   │   │   └── S3Service.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── PostRepository.java
│   │   │   │   │   ├── MediaFileRepository.java
│   │   │   │   │   └── PostTagRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Post.java
│   │   │   │   │   ├── MediaFile.java
│   │   │   │   │   ├── PostTag.java
│   │   │   │   │   └── ContentType.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── CreatePostRequest.java
│   │   │   │   │   │   ├── UpdatePostRequest.java
│   │   │   │   │   │   └── MediaUploadRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── PostResponse.java
│   │   │   │   │       ├── FeedResponse.java
│   │   │   │   │       └── MediaResponse.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── DatabaseConfig.java
│   │   │   │   │   ├── S3Config.java
│   │   │   │   │   └── RedisConfig.java
│   │   │   │   ├── algorithm/
│   │   │   │   │   ├── FeedAlgorithm.java
│   │   │   │   │   ├── TrendingAlgorithm.java
│   │   │   │   │   └── FacultyFeedAlgorithm.java
│   │   │   │   └── exception/
│   │   │   │       ├── PostNotFoundException.java
│   │   │   │       ├── MediaUploadException.java
│   │   │   │       └── ContentModerationException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   │           ├── V1__Create_posts_table.sql
│   │   │           ├── V2__Create_media_files_table.sql
│   │   │           └── V3__Create_post_tags_table.sql
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── social-service/                   # Social Interaction Service (Port 8083)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/social/
│   │   │   │   ├── SocialServiceApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── LikeController.java
│   │   │   │   │   ├── CommentController.java
│   │   │   │   │   ├── FollowController.java
│   │   │   │   │   └── ActivityController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── LikeService.java
│   │   │   │   │   ├── CommentService.java
│   │   │   │   │   ├── FollowService.java
│   │   │   │   │   ├── ActivityService.java
│   │   │   │   │   └── EngagementAnalyticsService.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── LikeRepository.java
│   │   │   │   │   ├── CommentRepository.java
│   │   │   │   │   ├── FollowRepository.java
│   │   │   │   │   └── ActivityRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Like.java
│   │   │   │   │   ├── Comment.java
│   │   │   │   │   ├── Follow.java
│   │   │   │   │   └── Activity.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── LikeRequest.java
│   │   │   │   │   │   ├── CommentRequest.java
│   │   │   │   │   │   └── FollowRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── LikeResponse.java
│   │   │   │   │       ├── CommentResponse.java
│   │   │   │   │       └── ActivityResponse.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── DatabaseConfig.java
│   │   │   │   │   └── RedisConfig.java
│   │   │   │   └── exception/
│   │   │   │       ├── CommentNotFoundException.java
│   │   │   │       └── DuplicateLikeException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   │           ├── V1__Create_likes_table.sql
│   │   │           ├── V2__Create_comments_table.sql
│   │   │           └── V3__Create_follows_table.sql
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── realtime-service/                 # Real-time Communication Service (Port 8084)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/realtime/
│   │   │   │   ├── RealtimeServiceApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── ChatController.java
│   │   │   │   │   ├── WebSocketController.java
│   │   │   │   │   └── NotificationController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── ChatService.java
│   │   │   │   │   ├── WebSocketService.java
│   │   │   │   │   ├── MessageService.java
│   │   │   │   │   └── PresenceService.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── ChatRoomRepository.java
│   │   │   │   │   ├── MessageRepository.java
│   │   │   │   │   └── UserSessionRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── ChatRoom.java
│   │   │   │   │   ├── Message.java
│   │   │   │   │   ├── UserSession.java
│   │   │   │   │   └── MessageType.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── SendMessageRequest.java
│   │   │   │   │   │   └── CreateChatRoomRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── MessageResponse.java
│   │   │   │   │       └── ChatRoomResponse.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── WebSocketConfig.java
│   │   │   │   │   ├── RabbitMQConfig.java
│   │   │   │   │   └── RedisConfig.java
│   │   │   │   ├── websocket/
│   │   │   │   │   ├── ChatWebSocketHandler.java
│   │   │   │   │   ├── WebSocketSessionManager.java
│   │   │   │   │   └── MessageBroker.java
│   │   │   │   └── exception/
│   │   │   │       ├── ChatRoomNotFoundException.java
│   │   │   │       └── MessageDeliveryException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   │           ├── V1__Create_chat_rooms_table.sql
│   │   │           ├── V2__Create_messages_table.sql
│   │   │           └── V3__Create_user_sessions_table.sql
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── ecommerce-service/                # E-commerce Service (Port 8085)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/ecommerce/
│   │   │   │   ├── EcommerceServiceApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── ProductController.java
│   │   │   │   │   ├── OrderController.java
│   │   │   │   │   ├── PaymentController.java
│   │   │   │   │   └── InventoryController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── ProductService.java
│   │   │   │   │   ├── OrderService.java
│   │   │   │   │   ├── PaymentService.java
│   │   │   │   │   ├── InventoryService.java
│   │   │   │   │   └── CartService.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── ProductRepository.java
│   │   │   │   │   ├── OrderRepository.java
│   │   │   │   │   ├── PaymentRepository.java
│   │   │   │   │   └── InventoryRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Product.java
│   │   │   │   │   ├── Order.java
│   │   │   │   │   ├── OrderItem.java
│   │   │   │   │   ├── Payment.java
│   │   │   │   │   └── Inventory.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── CreateProductRequest.java
│   │   │   │   │   │   ├── CreateOrderRequest.java
│   │   │   │   │   │   └── PaymentRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── ProductResponse.java
│   │   │   │   │       ├── OrderResponse.java
│   │   │   │   │       └── PaymentResponse.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── DatabaseConfig.java
│   │   │   │   │   └── PaymentConfig.java
│   │   │   │   └── exception/
│   │   │   │       ├── ProductNotFoundException.java
│   │   │   │       ├── InsufficientInventoryException.java
│   │   │   │       └── PaymentProcessingException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   │           ├── V1__Create_products_table.sql
│   │   │           ├── V2__Create_orders_table.sql
│   │   │           └── V3__Create_payments_table.sql
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── notification-service/             # Notification Service (Port 8086)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/notification/
│   │   │   │   ├── NotificationServiceApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── NotificationController.java
│   │   │   │   │   └── TemplateController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── NotificationService.java
│   │   │   │   │   ├── PushNotificationService.java
│   │   │   │   │   ├── EmailService.java
│   │   │   │   │   ├── SmsService.java
│   │   │   │   │   └── TemplateService.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── NotificationRepository.java
│   │   │   │   │   ├── NotificationTemplateRepository.java
│   │   │   │   │   └── DeliveryLogRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Notification.java
│   │   │   │   │   ├── NotificationTemplate.java
│   │   │   │   │   ├── DeliveryLog.java
│   │   │   │   │   └── NotificationType.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── SendNotificationRequest.java
│   │   │   │   │   │   └── CreateTemplateRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── NotificationResponse.java
│   │   │   │   │       └── DeliveryResponse.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── KafkaConfig.java
│   │   │   │   │   ├── FirebaseConfig.java
│   │   │   │   │   └── EmailConfig.java
│   │   │   │   ├── kafka/
│   │   │   │   │   ├── NotificationConsumer.java
│   │   │   │   │   └── NotificationProducer.java
│   │   │   │   └── exception/
│   │   │   │       ├── NotificationDeliveryException.java
│   │   │   │       └── TemplateNotFoundException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   │           ├── V1__Create_notifications_table.sql
│   │   │           ├── V2__Create_templates_table.sql
│   │   │           └── V3__Create_delivery_logs_table.sql
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── analytics-service/                # Analytics Service (Port 8087)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/analytics/
│   │   │   │   ├── AnalyticsServiceApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AnalyticsController.java
│   │   │   │   │   ├── MetricsController.java
│   │   │   │   │   └── ReportsController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── AnalyticsService.java
│   │   │   │   │   ├── MetricsService.java
│   │   │   │   │   ├── TrendingService.java
│   │   │   │   │   └── ReportService.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── AnalyticsEventRepository.java
│   │   │   │   │   ├── UserMetricsRepository.java
│   │   │   │   │   └── ContentMetricsRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── AnalyticsEvent.java
│   │   │   │   │   ├── UserMetrics.java
│   │   │   │   │   ├── ContentMetrics.java
│   │   │   │   │   └── EventType.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── TrackEventRequest.java
│   │   │   │   │   │   └── ReportRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── AnalyticsResponse.java
│   │   │   │   │       ├── MetricsResponse.java
│   │   │   │   │       └── TrendingResponse.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── DatabaseConfig.java
│   │   │   │   │   └── RedisConfig.java
│   │   │   │   ├── algorithm/
│   │   │   │   │   ├── TrendingAlgorithm.java
│   │   │   │   │   └── EngagementCalculator.java
│   │   │   │   └── exception/
│   │   │   │       ├── MetricsNotFoundException.java
│   │   │   │       └── AnalyticsProcessingException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   │           ├── V1__Create_analytics_events_table.sql
│   │   │           ├── V2__Create_user_metrics_table.sql
│   │   │           └── V3__Create_content_metrics_table.sql
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── eureka-server/                    # Service Discovery (Port 8761)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/eureka/
│   │   │   │   ├── EurekaServerApplication.java
│   │   │   │   └── config/
│   │   │   │       └── EurekaConfig.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── bootstrap.yml
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── config-server/                    # Spring Cloud Config (Port 8888)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raved/config/
│   │   │   │   ├── ConfigServerApplication.java
│   │   │   │   └── config/
│   │   │   │       └── ConfigServerConfig.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── bootstrap.yml
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── shared/                           # Shared libraries and utilities
│   ├── common/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/raved/common/
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── BaseResponse.java
│   │   │   │   │   │   ├── ErrorResponse.java
│   │   │   │   │   │   └── PageResponse.java
│   │   │   │   │   ├── exception/
│   │   │   │   │   │   ├── BaseException.java
│   │   │   │   │   │   ├── ValidationException.java
│   │   │   │   │   │   └── ServiceException.java
│   │   │   │   │   ├── util/
│   │   │   │   │   │   ├── DateUtils.java
│   │   │   │   │   │   ├── StringUtils.java
│   │   │   │   │   │   └── ValidationUtils.java
│   │   │   │   │   └── constants/
│   │   │   │   │       ├── ApiConstants.java
│   │   │   │   │       └── ErrorCodes.java
│   │   │   │   └── resources/
│   │   │   └── test/
│   │   └── pom.xml
│   └── security/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/raved/security/
│       │   │   │   ├── jwt/
│       │   │   │   │   ├── JwtUtils.java
│       │   │   │   │   └── JwtValidator.java
│       │   │   │   ├── oauth/
│       │   │   │   │   └── OAuth2Utils.java
│       │   │   │   └── encryption/
│       │   │   │       └── EncryptionUtils.java
│       │   │   └── resources/
│       │   └── test/
│       └── pom.xml
├── pom.xml                           # Parent POM
└── README.md
```

## 3. INFRASTRUCTURE STRUCTURE (Docker, Kubernetes, CI/CD)

```
infrastructure/
├── docker/
│   ├── development/
│   │   ├── docker-compose.yml        # Local development environment
│   │   ├── docker-compose.override.yml
│   │   ├── postgres/
│   │   │   ├── Dockerfile
│   │   │   ├── init.sql
│   │   │   └── postgresql.conf
│   │   ├── redis/
│   │   │   ├── Dockerfile
│   │   │   └── redis.conf
│   │   ├── kafka/
│   │   │   ├── Dockerfile
│   │   │   └── server.properties
│   │   ├── rabbitmq/
│   │   │   ├── Dockerfile
│   │   │   └── rabbitmq.conf
│   │   └── nginx/
│   │       ├── Dockerfile
│   │       ├── nginx.conf
│   │       └── default.conf
│   ├── production/
│   │   ├── docker-compose.prod.yml
│   │   ├── docker-compose.monitoring.yml
│   │   └── docker-compose.logging.yml
│   └── base/
│       ├── java-base/
│       │   └── Dockerfile            # Base Java image for microservices
│       └── node-base/
│           └── Dockerfile            # Base Node.js image for client builds
├── kubernetes/
│   ├── namespaces/
│   │   ├── raved-dev.yaml
│   │   ├── raved-staging.yaml
│   │   └── raved-prod.yaml
│   ├── configmaps/
│   │   ├── api-gateway-config.yaml
│   │   ├── user-service-config.yaml
│   │   ├── content-service-config.yaml
│   │   ├── social-service-config.yaml
│   │   ├── realtime-service-config.yaml
│   │   ├── ecommerce-service-config.yaml
│   │   ├── notification-service-config.yaml
│   │   └── analytics-service-config.yaml
│   ├── secrets/
│   │   ├── database-secrets.yaml
│   │   ├── jwt-secrets.yaml
│   │   ├── s3-secrets.yaml
│   │   └── notification-secrets.yaml
│   ├── services/
│   │   ├── api-gateway/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   ├── ingress.yaml
│   │   │   └── hpa.yaml              # Horizontal Pod Autoscaler
│   │   ├── user-service/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── hpa.yaml
│   │   ├── content-service/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── hpa.yaml
│   │   ├── social-service/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── hpa.yaml
│   │   ├── realtime-service/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── hpa.yaml
│   │   ├── ecommerce-service/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── hpa.yaml
│   │   ├── notification-service/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── hpa.yaml
│   │   ├── analytics-service/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── hpa.yaml
│   │   ├── eureka-server/
│   │   │   ├── deployment.yaml
│   │   │   └── service.yaml
│   │   └── config-server/
│   │       ├── deployment.yaml
│   │       └── service.yaml
│   ├── databases/
│   │   ├── postgres/
│   │   │   ├── statefulset.yaml
│   │   │   ├── service.yaml
│   │   │   ├── pvc.yaml              # Persistent Volume Claim
│   │   │   └── configmap.yaml
│   │   └── redis/
│   │       ├── deployment.yaml
│   │       ├── service.yaml
│   │       └── configmap.yaml
│   ├── messaging/
│   │   ├── kafka/
│   │   │   ├── statefulset.yaml
│   │   │   ├── service.yaml
│   │   │   └── configmap.yaml
│   │   └── rabbitmq/
│   │       ├── deployment.yaml
│   │       ├── service.yaml
│   │       └── configmap.yaml
│   ├── monitoring/
│   │   ├── prometheus/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   ├── configmap.yaml
│   │   │   └── servicemonitor.yaml
│   │   ├── grafana/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   ├── configmap.yaml
│   │   │   └── dashboards/
│   │   │       ├── microservices-dashboard.json
│   │   │       ├── database-dashboard.json
│   │   │       └── application-dashboard.json
│   │   └── jaeger/
│   │       ├── deployment.yaml
│   │       └── service.yaml
│   ├── logging/
│   │   ├── elasticsearch/
│   │   │   ├── statefulset.yaml
│   │   │   ├── service.yaml
│   │   │   └── configmap.yaml
│   │   ├── logstash/
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── configmap.yaml
│   │   └── kibana/
│   │       ├── deployment.yaml
│   │       ├── service.yaml
│   │       └── configmap.yaml
│   └── ingress/
│       ├── nginx-ingress-controller.yaml
│       ├── cert-manager.yaml
│       └── ssl-certificates.yaml
├── terraform/                        # Infrastructure as Code
│   ├── environments/
│   │   ├── dev/
│   │   │   ├── main.tf
│   │   │   ├── variables.tf
│   │   │   ├── outputs.tf
│   │   │   └── terraform.tfvars
│   │   ├── staging/
│   │   │   ├── main.tf
│   │   │   ├── variables.tf
│   │   │   ├── outputs.tf
│   │   │   └── terraform.tfvars
│   │   └── prod/
│   │       ├── main.tf
│   │       ├── variables.tf
│   │       ├── outputs.tf
│   │       └── terraform.tfvars
│   ├── modules/
│   │   ├── eks-cluster/
│   │   │   ├── main.tf
│   │   │   ├── variables.tf
│   │   │   └── outputs.tf
│   │   ├── rds-postgres/
│   │   │   ├── main.tf
│   │   │   ├── variables.tf
│   │   │   └── outputs.tf
│   │   ├── elasticache-redis/
│   │   │   ├── main.tf
│   │   │   ├── variables.tf
│   │   │   └── outputs.tf
│   │   ├── s3-storage/
│   │   │   ├── main.tf
│   │   │   ├── variables.tf
│   │   │   └── outputs.tf
│   │   └── vpc-networking/
│   │       ├── main.tf
│   │       ├── variables.tf
│   │       └── outputs.tf
│   └── scripts/
│       ├── deploy.sh
│       ├── destroy.sh
│       └── plan.sh
├── helm/                             # Helm Charts
│   ├── raved-app/
│   │   ├── Chart.yaml
│   │   ├── values.yaml
│   │   ├── values-dev.yaml
│   │   ├── values-staging.yaml
│   │   ├── values-prod.yaml
│   │   └── templates/
│   │       ├── deployment.yaml
│   │       ├── service.yaml
│   │       ├── ingress.yaml
│   │       ├── configmap.yaml
│   │       ├── secret.yaml
│   │       └── hpa.yaml
│   └── dependencies/
│       ├── postgresql/
│       ├── redis/
│       ├── kafka/
│       └── monitoring/
└── scripts/
    ├── build/
    │   ├── build-all.sh
    │   ├── build-client.sh
    │   ├── build-services.sh
    │   └── build-docker-images.sh
    ├── deploy/
    │   ├── deploy-dev.sh
    │   ├── deploy-staging.sh
    │   ├── deploy-prod.sh
    │   └── rollback.sh
    ├── database/
    │   ├── migrate.sh
    │   ├── seed.sh
    │   ├── backup.sh
    │   └── restore.sh
    └── monitoring/
        ├── health-check.sh
        ├── logs.sh
        └── metrics.sh
```

## 4. DOCUMENTATION STRUCTURE

```
docs/
├── api/
│   ├── openapi/
│   │   ├── api-gateway.yaml
│   │   ├── user-service.yaml
│   │   ├── content-service.yaml
│   │   ├── social-service.yaml
│   │   ├── realtime-service.yaml
│   │   ├── ecommerce-service.yaml
│   │   ├── notification-service.yaml
│   │   └── analytics-service.yaml
│   ├── postman/
│   │   ├── Raved-API.postman_collection.json
│   │   └── Raved-Environments.postman_environment.json
│   └── examples/
│       ├── authentication.md
│       ├── posts.md
│       ├── social-interactions.md
│       ├── real-time-chat.md
│       └── ecommerce.md
├── architecture/
│   ├── system-design.md
│   ├── microservices-architecture.md
│   ├── database-design.md
│   ├── security-architecture.md
│   ├── deployment-architecture.md
│   └── diagrams/
│       ├── system-overview.png
│       ├── microservices-diagram.png
│       ├── database-erd.png
│       └── deployment-diagram.png
├── development/
│   ├── getting-started.md
│   ├── local-setup.md
│   ├── coding-standards.md
│   ├── testing-guidelines.md
│   ├── git-workflow.md
│   └── troubleshooting.md
├── deployment/
│   ├── docker-deployment.md
│   ├── kubernetes-deployment.md
│   ├── aws-deployment.md
│   ├── monitoring-setup.md
│   └── backup-recovery.md
├── user-guides/
│   ├── mobile-app-guide.md
│   ├── admin-panel-guide.md
│   └── api-integration-guide.md
└── README.md
```

## 5. CI/CD STRUCTURE (GitHub Actions)

```
.github/
├── workflows/
│   ├── client-ci.yml                 # React Native CI/CD
│   ├── server-ci.yml                 # Spring Boot services CI/CD
│   ├── infrastructure-ci.yml         # Infrastructure deployment
│   ├── security-scan.yml             # Security scanning
│   ├── performance-test.yml          # Performance testing
│   └── release.yml                   # Release automation
├── ISSUE_TEMPLATE/
│   ├── bug_report.md
│   ├── feature_request.md
│   └── performance_issue.md
├── PULL_REQUEST_TEMPLATE.md
├── CODEOWNERS
└── dependabot.yml
```

## 6. SCRIPTS AND UTILITIES

```
scripts/
├── setup/
│   ├── install-dependencies.sh
│   ├── setup-development.sh
│   ├── setup-database.sh
│   └── setup-monitoring.sh
├── testing/
│   ├── run-unit-tests.sh
│   ├── run-integration-tests.sh
│   ├── run-e2e-tests.sh
│   └── performance-tests.sh
├── utilities/
│   ├── generate-jwt-secret.sh
│   ├── create-ssl-certs.sh
│   ├── backup-database.sh
│   └── cleanup-docker.sh
└── maintenance/
    ├── update-dependencies.sh
    ├── security-audit.sh
    └── performance-optimization.sh
```

## 7. ROOT LEVEL CONFIGURATION FILES

```
theRavedApp/
├── .env.example                      # Environment variables template
├── .env.development                  # Development environment
├── .env.staging                      # Staging environment
├── .env.production                   # Production environment
├── .gitignore                        # Git ignore rules
├── .gitattributes                    # Git attributes
├── .editorconfig                     # Editor configuration
├── .prettierrc                       # Code formatting
├── .eslintrc.js                      # Linting rules
├── docker-compose.yml                # Local development
├── docker-compose.prod.yml           # Production deployment
├── docker-compose.override.yml       # Local overrides
├── Makefile                          # Build automation
├── package.json                      # Root package.json for scripts
├── README.md                         # Project documentation
├── CONTRIBUTING.md                   # Contribution guidelines
├── LICENSE                           # Project license
├── CHANGELOG.md                      # Version history
└── SECURITY.md                       # Security policy
```

## 8. KEY CONFIGURATION FILES CONTENT STRUCTURE

### Root package.json (for scripts)
```json
{
  "name": "raved-app",
  "version": "1.0.0",
  "scripts": {
    "dev": "docker-compose up -d",
    "dev:client": "cd client && npm start",
    "dev:services": "docker-compose -f docker-compose.services.yml up",
    "build": "./scripts/build/build-all.sh",
    "test": "./scripts/testing/run-unit-tests.sh",
    "test:integration": "./scripts/testing/run-integration-tests.sh",
    "deploy:dev": "./scripts/deploy/deploy-dev.sh",
    "deploy:staging": "./scripts/deploy/deploy-staging.sh",
    "deploy:prod": "./scripts/deploy/deploy-prod.sh"
  }
}
```

### Main docker-compose.yml
```yaml
version: '3.8'
services:
  # Databases
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: raved_db
      POSTGRES_USER: raved_user
      POSTGRES_PASSWORD: raved_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  # Message Queues
  kafka:
    image: confluentinc/cp-kafka:latest
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
    ports:
      - "9092:9092"

  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"

  # Microservices
  eureka-server:
    build: ./server/eureka-server
    ports:
      - "8761:8761"

  config-server:
    build: ./server/config-server
    ports:
      - "8888:8888"
    depends_on:
      - eureka-server

  api-gateway:
    build: ./server/api-gateway
    ports:
      - "8080:8080"
    depends_on:
      - eureka-server
      - config-server

volumes:
  postgres_data:
  redis_data:
```

This comprehensive structure provides:

1. **Scalable Architecture**: Microservices-based backend with clear separation of concerns
2. **Modern Frontend**: React Native with Expo, Redux Toolkit, and NativeWind
3. **Production Ready**: Docker, Kubernetes, monitoring, and CI/CD pipelines
4. **Developer Friendly**: Clear folder structure, documentation, and development tools
5. **Security Focused**: JWT authentication, OAuth2, and security scanning
6. **Performance Optimized**: Caching layers, load balancing, and auto-scaling
7. **Maintainable**: Shared libraries, common utilities, and coding standards

The structure supports the 7-month development timeline with clear phases and allows for independent scaling of each microservice as the user base grows.
