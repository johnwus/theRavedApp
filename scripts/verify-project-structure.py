#!/usr/bin/env python3
"""
Script to verify all files from PROJECT_STRUCTURE.md have been created
"""

import os
from pathlib import Path

def check_file_exists(path):
    """Check if file exists"""
    exists = Path(path).exists()
    status = "✅" if exists else "❌"
    print(f"{status} {path}")
    return exists

def verify_user_service():
    """Verify User Service files"""
    print("\n🔍 Verifying User Service...")
    base = "server/user-service/src/main/java/com/raved/user"
    
    files = [
        f"{base}/UserServiceApplication.java",
        f"{base}/controller/AuthController.java",
        f"{base}/controller/UserController.java", 
        f"{base}/controller/ProfileController.java",
        f"{base}/controller/FacultyController.java",
        f"{base}/service/AuthService.java",
        f"{base}/service/UserService.java",
        f"{base}/service/ProfileService.java",
        f"{base}/service/StudentVerificationService.java",
        f"{base}/service/JwtService.java",
        f"{base}/repository/UserRepository.java",
        f"{base}/repository/StudentVerificationRepository.java",
        f"{base}/repository/FacultyRepository.java",
        f"{base}/model/User.java",
        f"{base}/model/StudentVerification.java",
        f"{base}/model/Faculty.java",
        f"{base}/model/University.java",
        f"{base}/dto/request/LoginRequest.java",
        f"{base}/dto/request/RegisterRequest.java",
        f"{base}/dto/request/VerificationRequest.java",
        f"{base}/dto/response/AuthResponse.java",
        f"{base}/dto/response/UserResponse.java",
        f"{base}/dto/response/ProfileResponse.java",
        f"{base}/config/DatabaseConfig.java",
        f"{base}/config/SecurityConfig.java",
        f"{base}/config/JwtConfig.java",
        f"{base}/config/RedisConfig.java",
        f"{base}/security/JwtAuthenticationFilter.java",
        f"{base}/security/JwtTokenProvider.java",
        f"{base}/security/CustomUserDetailsService.java",
        f"{base}/exception/UserNotFoundException.java",
        f"{base}/exception/InvalidCredentialsException.java",
        f"{base}/exception/VerificationFailedException.java",
        f"{base}/util/PasswordEncoder.java",
        f"{base}/util/ValidationUtils.java"
    ]
    
    return all(check_file_exists(f) for f in files)

def verify_content_service():
    """Verify Content Service files"""
    print("\n🔍 Verifying Content Service...")
    base = "server/content-service/src/main/java/com/raved/content"
    
    files = [
        f"{base}/ContentServiceApplication.java",
        f"{base}/controller/PostController.java",
        f"{base}/controller/MediaController.java",
        f"{base}/controller/FeedController.java",
        f"{base}/controller/TagController.java",
        f"{base}/service/PostService.java",
        f"{base}/service/MediaService.java",
        f"{base}/service/FeedService.java",
        f"{base}/service/TagService.java",
        f"{base}/service/ContentModerationService.java",
        f"{base}/service/S3Service.java",
        f"{base}/repository/PostRepository.java",
        f"{base}/repository/MediaFileRepository.java",
        f"{base}/repository/PostTagRepository.java",
        f"{base}/model/Post.java",
        f"{base}/model/MediaFile.java",
        f"{base}/model/PostTag.java",
        f"{base}/model/ContentType.java"
    ]
    
    return all(check_file_exists(f) for f in files)

def verify_all_services():
    """Verify all services"""
    print("🔍 Verifying TheRavedApp Project Structure...")
    
    services = [
        "user-service",
        "content-service", 
        "social-service",
        "realtime-service",
        "ecommerce-service",
        "notification-service",
        "analytics-service",
        "api-gateway",
        "eureka-server",
        "config-server"
    ]
    
    all_good = True
    
    for service in services:
        app_file = f"server/{service}/src/main/java/com/raved/{service.replace('-', '')}/{service.replace('-', '').title()}Application.java"
        if service == "api-gateway":
            app_file = f"server/{service}/src/main/java/com/raved/gateway/GatewayApplication.java"
        elif service == "eureka-server":
            app_file = f"server/{service}/src/main/java/com/raved/eureka/EurekaServerApplication.java"
        elif service == "config-server":
            app_file = f"server/{service}/src/main/java/com/raved/config/ConfigServerApplication.java"
        
        exists = check_file_exists(app_file)
        if not exists:
            all_good = False
    
    # Check shared modules
    print("\n🔍 Verifying Shared Modules...")
    shared_files = [
        "server/shared/common/src/main/java/com/raved/common/dto/BaseResponse.java",
        "server/shared/common/src/main/java/com/raved/common/exception/BaseException.java",
        "server/shared/security/src/main/java/com/raved/security/jwt/JwtUtils.java"
    ]
    
    for file in shared_files:
        exists = check_file_exists(file)
        if not exists:
            all_good = False
    
    return all_good

def main():
    """Main function"""
    os.chdir("c:/theRavedApp")
    
    user_service_ok = verify_user_service()
    content_service_ok = verify_content_service()
    all_services_ok = verify_all_services()
    
    print(f"\n📊 Verification Results:")
    print(f"User Service: {'✅ Complete' if user_service_ok else '❌ Missing files'}")
    print(f"Content Service: {'✅ Complete' if content_service_ok else '❌ Missing files'}")
    print(f"All Services: {'✅ Complete' if all_services_ok else '❌ Missing files'}")
    
    if user_service_ok and content_service_ok and all_services_ok:
        print("\n🎉 All files from PROJECT_STRUCTURE.md have been created successfully!")
    else:
        print("\n⚠️ Some files are still missing. Please check the output above.")

if __name__ == "__main__":
    main()
