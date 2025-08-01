#!/usr/bin/env python3
"""
Script to create ALL the truly missing files from PROJECT_STRUCTURE.md
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

def create_missing_client_files():
    """Create missing client files"""
    print("🔨 Creating Missing Client Files...")
    
    # Test directories and files (lines 215-220)
    test_files = [
        "client/__tests__/components/Button.test.tsx",
        "client/__tests__/components/PostCard.test.tsx",
        "client/__tests__/screens/LoginScreen.test.tsx",
        "client/__tests__/screens/HomeScreen.test.tsx",
        "client/__tests__/services/authService.test.ts",
        "client/__tests__/services/apiClient.test.ts",
        "client/__tests__/utils/dateHelpers.test.ts",
        "client/__tests__/utils/validationHelpers.test.ts",
        "client/__tests__/__mocks__/expo-secure-store.js",
        "client/__tests__/__mocks__/react-native.js"
    ]
    
    for test_file in test_files:
        create_file(test_file, get_test_template(test_file))
    
    # Missing service files that weren't created
    missing_service_files = [
        "client/src/services/api/interceptors.ts",
        "client/src/services/auth/biometricService.ts",
        "client/src/services/storage/mmkvStorage.ts",
        "client/src/services/storage/secureStorage.ts",
        "client/src/services/storage/cacheService.ts",
        "client/src/services/media/imageService.ts",
        "client/src/services/media/videoService.ts",
        "client/src/services/media/uploadService.ts",
        "client/src/services/media/compressionService.ts",
        "client/src/services/socket/socketService.ts",
        "client/src/services/socket/chatSocket.ts",
        "client/src/services/socket/notificationSocket.ts",
        "client/src/services/notifications/pushNotifications.ts",
        "client/src/services/notifications/localNotifications.ts",
        "client/src/services/notifications/notificationService.ts",
        "client/src/services/analytics/analyticsService.ts",
        "client/src/services/analytics/trackingService.ts",
        "client/src/services/analytics/metricsService.ts"
    ]
    
    for service_file in missing_service_files:
        create_file(service_file, get_service_template(service_file))
    
    # Missing utils files
    missing_utils_files = [
        "client/src/store/middleware.ts",
        "client/src/utils/constants/dimensions.ts",
        "client/src/utils/constants/fonts.ts",
        "client/src/utils/constants/routes.ts",
        "client/src/utils/helpers/formatHelpers.ts",
        "client/src/utils/helpers/validationHelpers.ts",
        "client/src/utils/helpers/imageHelpers.ts",
        "client/src/utils/helpers/networkHelpers.ts",
        "client/src/utils/hooks/useSocket.ts",
        "client/src/utils/hooks/useCamera.ts",
        "client/src/utils/hooks/useLocation.ts",
        "client/src/utils/hooks/useDebounce.ts",
        "client/src/utils/hooks/useInfiniteScroll.ts",
        "client/src/utils/types/api.ts",
        "client/src/utils/types/post.ts",
        "client/src/utils/types/chat.ts",
        "client/src/utils/types/ecommerce.ts",
        "client/src/utils/types/navigation.ts"
    ]
    
    for utils_file in missing_utils_files:
        create_file(utils_file, get_utils_template(utils_file))
    
    # Missing styles files
    missing_styles_files = [
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
    
    for styles_file in missing_styles_files:
        create_file(styles_file, get_styles_template(styles_file))
    
    # Create asset directories with placeholder files
    asset_dirs = [
        "client/src/assets/images/icons",
        "client/src/assets/images/logos",
        "client/src/assets/images/placeholders", 
        "client/src/assets/images/backgrounds",
        "client/src/assets/fonts",
        "client/src/assets/videos",
        "client/src/assets/sounds"
    ]
    
    for asset_dir in asset_dirs:
        Path(asset_dir).mkdir(parents=True, exist_ok=True)
        create_file(f"{asset_dir}/.gitkeep", "# Keep this directory in git")

def create_missing_server_files():
    """Create missing server files"""
    print("🔨 Creating Missing Server Files...")
    
    # Missing API Gateway files
    api_gateway_files = [
        "server/api-gateway/src/main/resources/application-dev.yml",
        "server/api-gateway/src/main/resources/application-prod.yml", 
        "server/api-gateway/src/main/resources/bootstrap.yml",
        "server/api-gateway/pom.xml"
    ]
    
    for file in api_gateway_files:
        create_file(file, get_server_config_template(file))
    
    # Missing User Service files
    user_service_files = [
        "server/user-service/src/main/resources/application-dev.yml",
        "server/user-service/src/main/resources/application-prod.yml",
        "server/user-service/src/main/resources/bootstrap.yml",
        "server/user-service/pom.xml"
    ]
    
    for file in user_service_files:
        create_file(file, get_server_config_template(file))
    
    # Create missing pom.xml files for all services
    services = [
        "content-service", "social-service", "realtime-service", 
        "ecommerce-service", "notification-service", "analytics-service",
        "eureka-server", "config-server"
    ]
    
    for service in services:
        pom_path = f"server/{service}/pom.xml"
        if not Path(pom_path).exists():
            create_file(pom_path, get_pom_template(service))
        
        # Create missing application config files
        config_files = [
            f"server/{service}/src/main/resources/application-dev.yml",
            f"server/{service}/src/main/resources/application-prod.yml",
            f"server/{service}/src/main/resources/bootstrap.yml"
        ]
        
        for config_file in config_files:
            if not Path(config_file).exists():
                create_file(config_file, get_application_config_template(service, config_file))

def get_test_template(test_file):
    """Get test file template"""
    test_name = Path(test_file).stem.replace('.test', '')
    return f"""import React from 'react';
import {{ render, screen }} from '@testing-library/react-native';
import {test_name} from '../{test_name}';

describe('{test_name}', () => {{
  it('should render correctly', () => {{
    render(<{test_name} />);
    expect(screen).toBeTruthy();
  }});
}});"""

def get_service_template(service_file):
    """Get service file template"""
    service_name = Path(service_file).stem
    return f"""/**
 * {service_name} for TheRavedApp
 */

class {service_name.replace('Service', '').title()}Service {{
  // Service implementation
}}

export const {service_name} = new {service_name.replace('Service', '').title()}Service();"""

def get_utils_template(utils_file):
    """Get utils file template"""
    if 'constants' in utils_file:
        return f"""export const CONSTANTS = {{
  // Constants for {Path(utils_file).stem}
}};"""
    elif 'helpers' in utils_file:
        return f"""/**
 * Helper functions for {Path(utils_file).stem}
 */

export const helper = () => {{
  // Helper implementation
}};"""
    elif 'hooks' in utils_file:
        return f"""import {{ useState, useEffect }} from 'react';

/**
 * Custom hook: {Path(utils_file).stem}
 */
export const {Path(utils_file).stem} = () => {{
  // Hook implementation
  return {{}};
}};"""
    elif 'types' in utils_file:
        return f"""/**
 * Type definitions for {Path(utils_file).stem}
 */

export interface {Path(utils_file).stem.title()} {{
  // Type definitions
}}"""
    else:
        return f"""// {Path(utils_file).stem} utilities"""

def get_styles_template(styles_file):
    """Get styles file template"""
    return f"""/**
 * Styles for {Path(styles_file).stem}
 */

export const styles = {{
  // Style definitions
}};"""

def get_server_config_template(config_file):
    """Get server config template"""
    if config_file.endswith('pom.xml'):
        service_name = config_file.split('/')[1]
        return get_pom_template(service_name)
    elif 'application-dev.yml' in config_file:
        return """# Development configuration
spring:
  profiles:
    active: development
logging:
  level:
    root: DEBUG"""
    elif 'application-prod.yml' in config_file:
        return """# Production configuration
spring:
  profiles:
    active: production
logging:
  level:
    root: INFO"""
    elif 'bootstrap.yml' in config_file:
        return """# Bootstrap configuration
spring:
  application:
    name: ${spring.application.name}
  cloud:
    config:
      uri: http://localhost:8888"""
    else:
        return "# Configuration file"

def get_pom_template(service_name):
    """Get pom.xml template"""
    artifact_id = service_name.replace('-', '')
    class_name = ''.join(word.capitalize() for word in service_name.split('-'))
    
    return f"""<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.5</version>
        <relativePath/>
    </parent>
    
    <groupId>com.raved</groupId>
    <artifactId>{artifact_id}</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    <name>{class_name}</name>
    <description>{class_name} for TheRavedApp</description>
    
    <properties>
        <java.version>21</java.version>
        <spring-cloud.version>2022.0.4</spring-cloud.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${{spring-cloud.version}}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>"""

def get_application_config_template(service, config_file):
    """Get application config template"""
    if 'dev.yml' in config_file:
        return f"""# Development configuration for {service}
spring:
  profiles:
    active: development
logging:
  level:
    com.raved: DEBUG"""
    elif 'prod.yml' in config_file:
        return f"""# Production configuration for {service}
spring:
  profiles:
    active: production
logging:
  level:
    com.raved: INFO"""
    elif 'bootstrap.yml' in config_file:
        return f"""# Bootstrap configuration for {service}
spring:
  application:
    name: {service}
  cloud:
    config:
      uri: http://localhost:8888"""
    else:
        return f"# Configuration for {service}"

def main():
    """Main function"""
    print("🚀 Creating ALL Truly Missing Files...")
    
    os.chdir("c:/theRavedApp")
    
    create_missing_client_files()
    create_missing_server_files()
    
    print("✅ All truly missing files created successfully!")

if __name__ == "__main__":
    main()
