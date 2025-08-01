#!/usr/bin/env python3
"""
Comprehensive verification script to check EVERY file mentioned in PROJECT_STRUCTURE.md
"""

import os
from pathlib import Path

def check_file_exists(path, section=""):
    """Check if file exists and report"""
    exists = Path(path).exists()
    status = "✅" if exists else "❌"
    if not exists:
        print(f"{status} MISSING: {path} ({section})")
    return exists

def verify_client_structure_complete():
    """Verify COMPLETE client structure from PROJECT_STRUCTURE.md lines 21-234"""
    print("\n🔍 COMPREHENSIVE CLIENT VERIFICATION (Lines 21-234)")
    print("="*60)
    
    missing_files = []
    
    # Root client files (lines 223-233)
    root_files = [
        "client/app.json",
        "client/App.tsx", 
        "client/babel.config.js",
        "client/metro.config.js",
        "client/package.json",
        "client/package-lock.json",
        "client/tsconfig.json",
        "client/tailwind.config.js",
        "client/.gitignore",
        "client/README.md"
    ]
    
    for file in root_files:
        if not check_file_exists(file, "Root Client"):
            missing_files.append(file)
    
    # Components structure (lines 26-71)
    component_files = [
        # Common components (lines 27-40)
        "client/src/components/common/Button/Button.tsx",
        "client/src/components/common/Button/Button.styles.ts", 
        "client/src/components/common/Button/index.ts",
        "client/src/components/common/Input/Input.tsx",
        "client/src/components/common/Input/Input.styles.ts",
        "client/src/components/common/Input/index.ts",
        "client/src/components/common/Modal/Modal.tsx",
        "client/src/components/common/Loading/Loading.tsx",
        "client/src/components/common/Avatar/Avatar.tsx",
        "client/src/components/common/Badge/Badge.tsx",
        "client/src/components/common/index.ts",
        
        # Forms (lines 41-45)
        "client/src/components/forms/LoginForm/LoginForm.tsx",
        "client/src/components/forms/RegisterForm/RegisterForm.tsx",
        "client/src/components/forms/PostForm/PostForm.tsx",
        "client/src/components/forms/ProfileForm/ProfileForm.tsx",
        
        # Media (lines 46-50)
        "client/src/components/media/ImagePicker/ImagePicker.tsx",
        "client/src/components/media/VideoPlayer/VideoPlayer.tsx",
        "client/src/components/media/MediaCarousel/MediaCarousel.tsx",
        "client/src/components/media/CameraComponent/CameraComponent.tsx",
        
        # Social (lines 51-62)
        "client/src/components/social/PostCard/PostCard.tsx",
        "client/src/components/social/PostCard/PostCard.styles.ts",
        "client/src/components/social/PostCard/PostHeader.tsx",
        "client/src/components/social/PostCard/PostContent.tsx",
        "client/src/components/social/PostCard/PostActions.tsx",
        "client/src/components/social/PostCard/index.ts",
        "client/src/components/social/CommentCard/CommentCard.tsx",
        "client/src/components/social/LikeButton/LikeButton.tsx",
        "client/src/components/social/ShareButton/ShareButton.tsx",
        "client/src/components/social/FollowButton/FollowButton.tsx",
        
        # Navigation (lines 63-66)
        "client/src/components/navigation/TabBar/TabBar.tsx",
        "client/src/components/navigation/Header/Header.tsx",
        "client/src/components/navigation/DrawerContent/DrawerContent.tsx",
        
        # Chat (lines 67-71)
        "client/src/components/chat/MessageBubble/MessageBubble.tsx",
        "client/src/components/chat/ChatInput/ChatInput.tsx",
        "client/src/components/chat/ChatHeader/ChatHeader.tsx",
        "client/src/components/chat/MessageList/MessageList.tsx"
    ]
    
    for file in component_files:
        if not check_file_exists(file, "Components"):
            missing_files.append(file)
    
    # Screens (lines 72-106)
    screen_files = [
        # Auth screens (lines 73-77)
        "client/src/screens/auth/LoginScreen.tsx",
        "client/src/screens/auth/RegisterScreen.tsx",
        "client/src/screens/auth/ForgotPasswordScreen.tsx",
        "client/src/screens/auth/StudentVerificationScreen.tsx",
        
        # Main screens (lines 78-82)
        "client/src/screens/main/HomeScreen.tsx",
        "client/src/screens/main/FeedScreen.tsx",
        "client/src/screens/main/ExploreScreen.tsx",
        "client/src/screens/main/TrendingScreen.tsx",
        
        # Profile screens (lines 83-87)
        "client/src/screens/profile/ProfileScreen.tsx",
        "client/src/screens/profile/EditProfileScreen.tsx",
        "client/src/screens/profile/SettingsScreen.tsx",
        "client/src/screens/profile/AnalyticsScreen.tsx",
        
        # Social screens (lines 88-92)
        "client/src/screens/social/PostDetailScreen.tsx",
        "client/src/screens/social/CreatePostScreen.tsx",
        "client/src/screens/social/CommentsScreen.tsx",
        "client/src/screens/social/FollowersScreen.tsx",
        
        # Chat screens (lines 93-96)
        "client/src/screens/chat/ChatListScreen.tsx",
        "client/src/screens/chat/ChatScreen.tsx",
        "client/src/screens/chat/NewChatScreen.tsx",
        
        # Ecommerce screens (lines 97-102)
        "client/src/screens/ecommerce/StoreScreen.tsx",
        "client/src/screens/ecommerce/ProductDetailScreen.tsx",
        "client/src/screens/ecommerce/CartScreen.tsx",
        "client/src/screens/ecommerce/CheckoutScreen.tsx",
        "client/src/screens/ecommerce/OrderHistoryScreen.tsx",
        
        # Faculty screens (lines 103-106)
        "client/src/screens/faculty/FacultyFeedScreen.tsx",
        "client/src/screens/faculty/FacultyMembersScreen.tsx",
        "client/src/screens/faculty/FacultyEventsScreen.tsx"
    ]
    
    for file in screen_files:
        if not check_file_exists(file, "Screens"):
            missing_files.append(file)
    
    # Navigation (lines 107-113)
    navigation_files = [
        "client/src/navigation/AppNavigator.tsx",
        "client/src/navigation/AuthNavigator.tsx",
        "client/src/navigation/MainNavigator.tsx",
        "client/src/navigation/TabNavigator.tsx",
        "client/src/navigation/StackNavigator.tsx",
        "client/src/navigation/types.ts"
    ]
    
    for file in navigation_files:
        if not check_file_exists(file, "Navigation"):
            missing_files.append(file)
    
    # Store (lines 114-134)
    store_files = [
        "client/src/store/index.ts",
        "client/src/store/rootReducer.ts",
        "client/src/store/middleware.ts",
        "client/src/store/slices/authSlice.ts",
        "client/src/store/slices/userSlice.ts",
        "client/src/store/slices/postsSlice.ts",
        "client/src/store/slices/socialSlice.ts",
        "client/src/store/slices/chatSlice.ts",
        "client/src/store/slices/ecommerceSlice.ts",
        "client/src/store/slices/uiSlice.ts",
        "client/src/store/api/baseApi.ts",
        "client/src/store/api/authApi.ts",
        "client/src/store/api/userApi.ts",
        "client/src/store/api/postsApi.ts",
        "client/src/store/api/socialApi.ts",
        "client/src/store/api/chatApi.ts",
        "client/src/store/api/ecommerceApi.ts",
        "client/src/store/api/analyticsApi.ts"
    ]
    
    for file in store_files:
        if not check_file_exists(file, "Store"):
            missing_files.append(file)
    
    return missing_files

def verify_services_complete():
    """Verify COMPLETE services structure (lines 135-164)"""
    print("\n🔍 COMPREHENSIVE SERVICES VERIFICATION (Lines 135-164)")
    print("="*60)
    
    missing_files = []
    
    # API services (lines 136-139)
    api_files = [
        "client/src/services/api/client.ts",
        "client/src/services/api/endpoints.ts",
        "client/src/services/api/interceptors.ts"
    ]
    
    # Auth services (lines 140-143)
    auth_files = [
        "client/src/services/auth/authService.ts",
        "client/src/services/auth/tokenService.ts",
        "client/src/services/auth/biometricService.ts"
    ]
    
    # Storage services (lines 144-147)
    storage_files = [
        "client/src/services/storage/mmkvStorage.ts",
        "client/src/services/storage/secureStorage.ts",
        "client/src/services/storage/cacheService.ts"
    ]
    
    # Media services (lines 148-152)
    media_files = [
        "client/src/services/media/imageService.ts",
        "client/src/services/media/videoService.ts",
        "client/src/services/media/uploadService.ts",
        "client/src/services/media/compressionService.ts"
    ]
    
    # Socket services (lines 153-156)
    socket_files = [
        "client/src/services/socket/socketService.ts",
        "client/src/services/socket/chatSocket.ts",
        "client/src/services/socket/notificationSocket.ts"
    ]
    
    # Notification services (lines 157-160)
    notification_files = [
        "client/src/services/notifications/pushNotifications.ts",
        "client/src/services/notifications/localNotifications.ts",
        "client/src/services/notifications/notificationService.ts"
    ]
    
    # Analytics services (lines 161-164)
    analytics_files = [
        "client/src/services/analytics/analyticsService.ts",
        "client/src/services/analytics/trackingService.ts",
        "client/src/services/analytics/metricsService.ts"
    ]
    
    all_service_files = api_files + auth_files + storage_files + media_files + socket_files + notification_files + analytics_files
    
    for file in all_service_files:
        if not check_file_exists(file, "Services"):
            missing_files.append(file)
    
    return missing_files

def verify_utils_complete():
    """Verify COMPLETE utils structure (lines 165-191)"""
    print("\n🔍 COMPREHENSIVE UTILS VERIFICATION (Lines 165-191)")
    print("="*60)
    
    missing_files = []
    
    # Constants (lines 166-171)
    constants_files = [
        "client/src/utils/constants/api.ts",
        "client/src/utils/constants/colors.ts",
        "client/src/utils/constants/dimensions.ts",
        "client/src/utils/constants/fonts.ts",
        "client/src/utils/constants/routes.ts"
    ]
    
    # Helpers (lines 172-177)
    helpers_files = [
        "client/src/utils/helpers/dateHelpers.ts",
        "client/src/utils/helpers/formatHelpers.ts",
        "client/src/utils/helpers/validationHelpers.ts",
        "client/src/utils/helpers/imageHelpers.ts",
        "client/src/utils/helpers/networkHelpers.ts"
    ]
    
    # Hooks (lines 178-184)
    hooks_files = [
        "client/src/utils/hooks/useAuth.ts",
        "client/src/utils/hooks/useSocket.ts",
        "client/src/utils/hooks/useCamera.ts",
        "client/src/utils/hooks/useLocation.ts",
        "client/src/utils/hooks/useDebounce.ts",
        "client/src/utils/hooks/useInfiniteScroll.ts"
    ]
    
    # Types (lines 185-191)
    types_files = [
        "client/src/utils/types/api.ts",
        "client/src/utils/types/user.ts",
        "client/src/utils/types/post.ts",
        "client/src/utils/types/chat.ts",
        "client/src/utils/types/ecommerce.ts",
        "client/src/utils/types/navigation.ts"
    ]
    
    all_utils_files = constants_files + helpers_files + hooks_files + types_files
    
    for file in all_utils_files:
        if not check_file_exists(file, "Utils"):
            missing_files.append(file)
    
    return missing_files

def verify_styles_and_assets():
    """Verify styles and assets (lines 192-214)"""
    print("\n🔍 COMPREHENSIVE STYLES & ASSETS VERIFICATION (Lines 192-214)")
    print("="*60)
    
    missing_files = []
    
    # Styles (lines 192-205)
    styles_files = [
        "client/src/styles/theme/colors.ts",
        "client/src/styles/theme/typography.ts",
        "client/src/styles/theme/spacing.ts",
        "client/src/styles/theme/shadows.ts",
        "client/src/styles/theme/index.ts",
        "client/src/styles/global/globalStyles.ts",
        "client/src/styles/global/nativewind.config.js",
        "client/src/styles/components/buttonStyles.ts",
        "client/src/styles/components/inputStyles.ts",
        "client/src/styles/components/cardStyles.ts"
    ]
    
    for file in styles_files:
        if not check_file_exists(file, "Styles"):
            missing_files.append(file)
    
    # Assets directories (lines 206-214)
    asset_dirs = [
        "client/src/assets/images/icons",
        "client/src/assets/images/logos", 
        "client/src/assets/images/placeholders",
        "client/src/assets/images/backgrounds",
        "client/src/assets/fonts",
        "client/src/assets/videos",
        "client/src/assets/sounds"
    ]
    
    for dir_path in asset_dirs:
        if not Path(dir_path).exists():
            missing_files.append(f"{dir_path}/ (directory)")
            print(f"❌ MISSING: {dir_path}/ (Assets Directory)")
    
    return missing_files

def main():
    """Main comprehensive verification"""
    print("🔍 COMPREHENSIVE PROJECT STRUCTURE VERIFICATION")
    print("Checking EVERY file mentioned in PROJECT_STRUCTURE.md")
    print("="*70)
    
    os.chdir("c:/theRavedApp")
    
    # Verify all client sections
    client_missing = verify_client_structure_complete()
    services_missing = verify_services_complete()
    utils_missing = verify_utils_complete()
    styles_missing = verify_styles_and_assets()
    
    # Combine all missing files
    all_missing = client_missing + services_missing + utils_missing + styles_missing
    
    print(f"\n📊 COMPREHENSIVE VERIFICATION RESULTS:")
    print(f"="*70)
    print(f"Client Structure Missing: {len(client_missing)} files")
    print(f"Services Missing: {len(services_missing)} files") 
    print(f"Utils Missing: {len(utils_missing)} files")
    print(f"Styles/Assets Missing: {len(styles_missing)} files")
    print(f"\n🔍 TOTAL MISSING FILES: {len(all_missing)}")
    
    if len(all_missing) == 0:
        print(f"\n🎉 ALL FILES FROM PROJECT_STRUCTURE.md ARE PRESENT!")
        print(f"✅ Project structure is 100% complete")
    else:
        print(f"\n⚠️ MISSING FILES DETECTED:")
        print(f"The following {len(all_missing)} files need to be created:")
        for missing in all_missing[:20]:  # Show first 20
            print(f"  - {missing}")
        if len(all_missing) > 20:
            print(f"  ... and {len(all_missing) - 20} more files")
    
    return len(all_missing) == 0

if __name__ == "__main__":
    main()
