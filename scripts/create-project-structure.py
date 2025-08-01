#!/usr/bin/env python3
"""
Script to create the complete project structure for TheRavedApp
according to PROJECT_STRUCTURE.md
"""

import os
import sys
from pathlib import Path

def create_directory(path):
    """Create directory if it doesn't exist"""
    Path(path).mkdir(parents=True, exist_ok=True)
    print(f"Created directory: {path}")

def create_file(path, content=""):
    """Create file with optional content"""
    Path(path).parent.mkdir(parents=True, exist_ok=True)
    if not Path(path).exists():
        with open(path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Created file: {path}")
    else:
        print(f"File already exists: {path}")

def create_java_class(path, package, class_name, class_type="class", imports=None, content=""):
    """Create a Java class file with basic structure"""
    imports_str = ""
    if imports:
        imports_str = "\n".join([f"import {imp};" for imp in imports]) + "\n\n"
    
    java_content = f"""package {package};

{imports_str}/**
 * {class_name} for TheRavedApp
 */
public {class_type} {class_name} {{
{content}
}}
"""
    create_file(path, java_content)

def create_server_structure():
    """Create the complete server-side structure"""
    base_path = "server"
    
    # Services to create
    services = [
        "api-gateway",
        "user-service", 
        "content-service",
        "social-service",
        "realtime-service",
        "ecommerce-service",
        "notification-service",
        "analytics-service",
        "eureka-server",
        "config-server"
    ]
    
    # Create each service structure
    for service in services:
        service_path = f"{base_path}/{service}"
        
        # Create basic Maven structure
        create_directory(f"{service_path}/src/main/java/com/raved/{service.replace('-', '')}")
        create_directory(f"{service_path}/src/main/resources")
        create_directory(f"{service_path}/src/main/resources/db/migration")
        create_directory(f"{service_path}/src/test/java/com/raved/{service.replace('-', '')}")
        create_directory(f"{service_path}/src/test/resources")
        
        # Create basic files
        create_file(f"{service_path}/README.md", f"# {service.title()} Service\n\nMicroservice for TheRavedApp")
        create_file(f"{service_path}/Dockerfile", get_dockerfile_content(service))
        
        # Create service-specific structure based on PROJECT_STRUCTURE.md
        if service == "user-service":
            create_user_service_structure(service_path)
        elif service == "content-service":
            create_content_service_structure(service_path)
        elif service == "social-service":
            create_social_service_structure(service_path)
        elif service == "realtime-service":
            create_realtime_service_structure(service_path)
        elif service == "ecommerce-service":
            create_ecommerce_service_structure(service_path)
        elif service == "notification-service":
            create_notification_service_structure(service_path)
        elif service == "analytics-service":
            create_analytics_service_structure(service_path)
        elif service == "api-gateway":
            create_api_gateway_structure(service_path)
        elif service == "eureka-server":
            create_eureka_server_structure(service_path)
        elif service == "config-server":
            create_config_server_structure(service_path)

def create_user_service_structure(base_path):
    """Create user service specific structure"""
    java_base = f"{base_path}/src/main/java/com/raved/user"
    
    # Create directories
    directories = [
        "controller", "service", "repository", "model", "dto/request", "dto/response",
        "config", "security", "exception", "util"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")
    
    # Create controller files
    controllers = ["AuthController", "UserController", "ProfileController", "FacultyController"]
    for controller in controllers:
        create_java_class(
            f"{java_base}/controller/{controller}.java",
            "com.raved.user.controller",
            controller,
            "class",
            ["org.springframework.web.bind.annotation.*"]
        )
    
    # Create service files
    services = ["AuthService", "UserService", "ProfileService", "StudentVerificationService", "JwtService"]
    for service in services:
        create_java_class(
            f"{java_base}/service/{service}.java",
            "com.raved.user.service",
            service,
            "interface" if service != "JwtService" else "class"
        )
    
    # Create repository files
    repositories = ["UserRepository", "StudentVerificationRepository", "FacultyRepository"]
    for repo in repositories:
        create_java_class(
            f"{java_base}/repository/{repo}.java",
            "com.raved.user.repository",
            repo,
            "interface",
            ["org.springframework.data.jpa.repository.JpaRepository", "org.springframework.stereotype.Repository"]
        )

def get_dockerfile_content(service):
    """Get Dockerfile content for a service"""
    return f"""# Multi-stage build for {service}
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="TheRavedApp Team"
LABEL description="{service} for TheRavedApp"

RUN addgroup -g 1001 -S appgroup && \\
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
"""

def create_content_service_structure(base_path):
    """Create content service structure"""
    java_base = f"{base_path}/src/main/java/com/raved/content"
    
    directories = [
        "controller", "service", "repository", "model", "dto/request", "dto/response",
        "config", "algorithm", "exception"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_social_service_structure(base_path):
    """Create social service structure"""
    java_base = f"{base_path}/src/main/java/com/raved/social"
    
    directories = [
        "controller", "service", "repository", "model", "dto/request", "dto/response",
        "config", "exception"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_realtime_service_structure(base_path):
    """Create realtime service structure"""
    java_base = f"{base_path}/src/main/java/com/raved/realtime"
    
    directories = [
        "controller", "service", "repository", "model", "dto/request", "dto/response",
        "config", "websocket", "exception"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_ecommerce_service_structure(base_path):
    """Create ecommerce service structure"""
    java_base = f"{base_path}/src/main/java/com/raved/ecommerce"
    
    directories = [
        "controller", "service", "repository", "model", "dto/request", "dto/response",
        "config", "exception"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_notification_service_structure(base_path):
    """Create notification service structure"""
    java_base = f"{base_path}/src/main/java/com/raved/notification"
    
    directories = [
        "controller", "service", "repository", "model", "dto/request", "dto/response",
        "config", "kafka", "exception"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_analytics_service_structure(base_path):
    """Create analytics service structure"""
    java_base = f"{base_path}/src/main/java/com/raved/analytics"
    
    directories = [
        "controller", "service", "repository", "model", "dto/request", "dto/response",
        "config", "algorithm", "exception"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_api_gateway_structure(base_path):
    """Create API gateway structure"""
    java_base = f"{base_path}/src/main/java/com/raved/gateway"
    
    directories = [
        "config", "filter", "exception"
    ]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_eureka_server_structure(base_path):
    """Create Eureka server structure"""
    java_base = f"{base_path}/src/main/java/com/raved/eureka"
    
    directories = ["config"]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_config_server_structure(base_path):
    """Create config server structure"""
    java_base = f"{base_path}/src/main/java/com/raved/config"
    
    directories = ["config"]
    
    for dir_name in directories:
        create_directory(f"{java_base}/{dir_name}")

def create_shared_structure():
    """Create shared modules structure"""
    base_path = "server/shared"
    
    # Common module
    common_base = f"{base_path}/common/src/main/java/com/raved/common"
    directories = ["dto", "exception", "util", "constants"]
    
    for dir_name in directories:
        create_directory(f"{common_base}/{dir_name}")
    
    # Security module
    security_base = f"{base_path}/security/src/main/java/com/raved/security"
    directories = ["jwt", "oauth", "encryption"]
    
    for dir_name in directories:
        create_directory(f"{security_base}/{dir_name}")

def main():
    """Main function to create the project structure"""
    print("Creating TheRavedApp project structure...")
    
    # Change to the project root directory
    os.chdir("c:/theRavedApp")
    
    # Create server structure
    create_server_structure()
    
    # Create shared modules
    create_shared_structure()
    
    print("Project structure creation completed!")

if __name__ == "__main__":
    main()
