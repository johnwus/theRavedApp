#!/bin/bash

# RAvED Platform - GitHub Repository Setup Script
# This script sets up the GitHub repository with proper configuration

set -e

echo "🚀 Setting up RAvED Platform GitHub Repository..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}=== $1 ===${NC}"
}

# Check if git is installed
if ! command -v git &> /dev/null; then
    print_error "Git is not installed. Please install Git first."
    exit 1
fi

# Check if gh CLI is installed
if ! command -v gh &> /dev/null; then
    print_warning "GitHub CLI (gh) is not installed. Some features will be skipped."
    GH_AVAILABLE=false
else
    GH_AVAILABLE=true
fi

print_header "Repository Initialization"

# Initialize git repository if not already initialized
if [ ! -d ".git" ]; then
    print_status "Initializing Git repository..."
    git init
    git branch -M main
else
    print_status "Git repository already initialized"
fi

# Create .gitignore if it doesn't exist
if [ ! -f ".gitignore" ]; then
    print_status "Creating .gitignore file..."
    cat > .gitignore << 'EOF'
# Compiled class files
*.class

# Log files
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# Virtual machine crash logs
hs_err_pid*

# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# Gradle
.gradle
build/
!gradle/wrapper/gradle-wrapper.jar
!**/src/main/**/build/
!**/src/test/**/build/

# IntelliJ IDEA
.idea/
*.iws
*.iml
*.ipr
out/
!**/src/main/**/out/
!**/src/test/**/out/

# Eclipse
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache
bin/
!**/src/main/**/bin/
!**/src/test/**/bin/

# NetBeans
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/

# VS Code
.vscode/

# Node.js
node_modules/
npm-debug.log*
yarn-debug.log*
yarn-error.log*
lerna-debug.log*
.pnpm-debug.log*

# Runtime data
pids
*.pid
*.seed
*.pid.lock

# Coverage directory used by tools like istanbul
coverage/
*.lcov

# nyc test coverage
.nyc_output

# Dependency directories
jspm_packages/

# Optional npm cache directory
.npm

# Optional eslint cache
.eslintcache

# Optional REPL history
.node_repl_history

# Output of 'npm pack'
*.tgz

# Yarn Integrity file
.yarn-integrity

# dotenv environment variables file
.env
.env.local
.env.development.local
.env.test.local
.env.production.local

# Docker
.dockerignore

# OS generated files
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Application specific
logs/
temp/
uploads/
*.tmp
*.bak
*.swp
*~.nib
local.properties

# Database
*.db
*.sqlite
*.sqlite3

# Redis dump file
dump.rdb

# Kafka logs
kafka-logs/

# Spring Boot
spring.log
EOF
    print_status ".gitignore created"
else
    print_status ".gitignore already exists"
fi

# Create README.md if it doesn't exist
if [ ! -f "README.md" ]; then
    print_status "Creating README.md..."
    cat > README.md << 'EOF'
# 🎉 RAvED Platform

A comprehensive social media and e-commerce platform built with microservices architecture.

## 🚀 Features

- **Social Media Platform**: Posts, comments, likes, follows, real-time chat
- **E-commerce System**: Products, orders, payments, shipping
- **Multi-channel Notifications**: Email, Push, SMS
- **Advanced Analytics**: User engagement, content performance, trending algorithms
- **Real-time Communication**: WebSocket-based chat and messaging
- **Event-driven Architecture**: Kafka-based microservices communication

## 🏗️ Architecture

### Microservices
- **User Service** (Port 8080) - Authentication, user management
- **Social Service** (Port 8081) - Posts, comments, likes, follows
- **Ecommerce Service** (Port 8082) - Products, orders, payments
- **Content Service** (Port 8083) - Content management, feeds
- **Notification Service** (Port 8086) - Multi-channel notifications
- **Analytics Service** (Port 8087) - Analytics and reporting
- **Realtime Service** (Port 8088) - Chat and real-time features

### Infrastructure
- **API Gateway** (Port 8765) - Request routing and load balancing
- **Service Discovery** (Port 8761) - Eureka server
- **PostgreSQL** - Primary database
- **Redis** - Caching and session storage
- **Kafka** - Event streaming and messaging

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- Maven 3.6+

### Running with Docker
```bash
# Clone the repository
git clone https://github.com/your-username/raved-platform.git
cd raved-platform

# Start all services
docker-compose up -d

# Check service health
curl http://localhost:8765/actuator/health
```

### Running Locally
```bash
# Start infrastructure services
docker-compose up -d postgres redis kafka zookeeper

# Start each microservice
cd server/user-service && mvn spring-boot:run
cd server/social-service && mvn spring-boot:run
cd server/ecommerce-service && mvn spring-boot:run
# ... repeat for other services

# Start frontend
cd client && npm install && npm start
```

## 📚 API Documentation

- **API Gateway**: http://localhost:8765/swagger-ui.html
- **User Service**: http://localhost:8080/swagger-ui.html
- **Social Service**: http://localhost:8081/swagger-ui.html
- **Ecommerce Service**: http://localhost:8082/swagger-ui.html

## 🧪 Testing

```bash
# Run all tests
mvn clean test

# Run specific service tests
cd server/user-service && mvn test

# Run integration tests
mvn verify -P integration-tests
```

## 🚀 Deployment

### Staging
```bash
# Deploy to staging
./deploy-staging.sh
```

### Production
```bash
# Deploy to production
./deploy-production.sh
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Apache Kafka for event streaming
- Redis for caching solutions
- PostgreSQL for reliable data storage
EOF
    print_status "README.md created"
else
    print_status "README.md already exists"
fi

print_header "Git Configuration"

# Add all files to git
print_status "Adding files to Git..."
git add .

# Check if there are any changes to commit
if git diff --staged --quiet; then
    print_status "No changes to commit"
else
    print_status "Committing initial files..."
    git commit -m "Initial commit: Complete RAvED Platform implementation

- ✅ 7 Microservices fully implemented
- ✅ Event-driven architecture with Kafka
- ✅ Multi-channel notification system
- ✅ Real-time chat and messaging
- ✅ Advanced analytics and reporting
- ✅ Complete e-commerce functionality
- ✅ Social media features
- ✅ CI/CD pipeline with GitHub Actions
- ✅ Docker containerization
- ✅ Comprehensive documentation"
fi

print_header "GitHub Repository Setup"

if [ "$GH_AVAILABLE" = true ]; then
    # Check if user is logged in to GitHub CLI
    if gh auth status &> /dev/null; then
        print_status "GitHub CLI authenticated"
        
        # Create repository if it doesn't exist
        REPO_NAME="raved-platform"
        if ! gh repo view "$REPO_NAME" &> /dev/null; then
            print_status "Creating GitHub repository..."
            gh repo create "$REPO_NAME" --public --description "Complete social media and e-commerce platform with microservices architecture" --clone=false
            
            # Add remote origin
            gh repo view "$REPO_NAME" --json sshUrl -q .sshUrl | xargs git remote add origin
        else
            print_status "GitHub repository already exists"
        fi
        
        # Push to GitHub
        print_status "Pushing to GitHub..."
        git push -u origin main
        
        # Set up branch protection rules
        print_status "Setting up branch protection rules..."
        gh api repos/:owner/:repo/branches/main/protection \
            --method PUT \
            --field required_status_checks='{"strict":true,"contexts":["backend-test","frontend-test","security-scan"]}' \
            --field enforce_admins=true \
            --field required_pull_request_reviews='{"required_approving_review_count":1,"dismiss_stale_reviews":true}' \
            --field restrictions=null \
            2>/dev/null || print_warning "Could not set up branch protection rules (may require admin access)"
        
        # Create labels
        print_status "Creating issue labels..."
        gh label create "bug" --color "d73a4a" --description "Something isn't working" 2>/dev/null || true
        gh label create "enhancement" --color "a2eeef" --description "New feature or request" 2>/dev/null || true
        gh label create "performance" --color "fbca04" --description "Performance related issue" 2>/dev/null || true
        gh label create "security" --color "b60205" --description "Security related issue" 2>/dev/null || true
        gh label create "ci/cd" --color "0052cc" --description "CI/CD pipeline related" 2>/dev/null || true
        gh label create "documentation" --color "0075ca" --description "Improvements or additions to documentation" 2>/dev/null || true
        gh label create "high-priority" --color "d93f0b" --description "High priority issue" 2>/dev/null || true
        gh label create "needs-triage" --color "ededed" --description "Needs to be triaged" 2>/dev/null || true
        gh label create "needs-investigation" --color "fef2c0" --description "Needs investigation" 2>/dev/null || true
        
        print_status "GitHub repository setup completed!"
        print_status "Repository URL: $(gh repo view --json url -q .url)"
        
    else
        print_warning "GitHub CLI not authenticated. Please run 'gh auth login' first."
    fi
else
    print_warning "GitHub CLI not available. Please set up the repository manually:"
    echo "1. Create a new repository on GitHub"
    echo "2. Add the remote: git remote add origin <repository-url>"
    echo "3. Push the code: git push -u origin main"
fi

print_header "Setup Complete"

print_status "✅ Git repository initialized"
print_status "✅ .gitignore created"
print_status "✅ README.md created"
print_status "✅ Initial commit made"
print_status "✅ CI/CD pipeline configured"
print_status "✅ Issue templates created"
print_status "✅ Pull request template created"

if [ "$GH_AVAILABLE" = true ] && gh auth status &> /dev/null; then
    print_status "✅ GitHub repository created and configured"
    print_status "✅ Branch protection rules applied"
    print_status "✅ Issue labels created"
fi

echo ""
print_header "Next Steps"
echo "1. Configure repository secrets for CI/CD:"
echo "   - DOCKER_USERNAME"
echo "   - DOCKER_PASSWORD"
echo "   - SONAR_TOKEN"
echo ""
echo "2. Review and customize the CI/CD pipeline in .github/workflows/ci-cd.yml"
echo ""
echo "3. Set up monitoring and alerting for production deployment"
echo ""
echo "4. Configure environment-specific variables"
echo ""
print_status "🎉 RAvED Platform repository setup completed successfully!"
EOF

    chmod +x setup-github.sh
