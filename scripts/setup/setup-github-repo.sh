#!/bin/bash

# ===================================
# GitHub Repository Setup Script
# ===================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Repository details
GITHUB_USERNAME="johnwus"
REPO_NAME="theRavedApp"
REPO_DESCRIPTION="Complete full-stack social commerce platform for university students with microservices architecture"

echo -e "${BLUE}🚀 Setting up GitHub repository for TheRavedApp${NC}"
echo "=================================================="

# Check if GitHub CLI is installed
if ! command -v gh &> /dev/null; then
    echo -e "${YELLOW}⚠️  GitHub CLI not found. Please install it first:${NC}"
    echo "   Windows: winget install --id GitHub.cli"
    echo "   macOS: brew install gh"
    echo "   Linux: https://github.com/cli/cli/blob/trunk/docs/install_linux.md"
    echo ""
    echo -e "${BLUE}📋 Manual setup instructions:${NC}"
    echo "1. Go to https://github.com/new"
    echo "2. Repository name: ${REPO_NAME}"
    echo "3. Description: ${REPO_DESCRIPTION}"
    echo "4. Set to Public (or Private if preferred)"
    echo "5. Don't initialize with README (we already have one)"
    echo "6. Create repository"
    echo "7. Run the following commands:"
    echo ""
    echo "   git remote add origin https://github.com/${GITHUB_USERNAME}/${REPO_NAME}.git"
    echo "   git branch -M main"
    echo "   git push -u origin main"
    echo ""
    exit 1
fi

# Check if user is logged in to GitHub CLI
if ! gh auth status &> /dev/null; then
    echo -e "${YELLOW}🔐 Please login to GitHub CLI first:${NC}"
    echo "   gh auth login"
    exit 1
fi

echo -e "${GREEN}✅ GitHub CLI found and authenticated${NC}"

# Create the repository
echo -e "${BLUE}📦 Creating GitHub repository...${NC}"

gh repo create "${GITHUB_USERNAME}/${REPO_NAME}" \
    --description "${REPO_DESCRIPTION}" \
    --public \
    --clone=false \
    --confirm

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Repository created successfully${NC}"
else
    echo -e "${RED}❌ Failed to create repository. It might already exist.${NC}"
    echo -e "${YELLOW}🔄 Continuing with existing repository...${NC}"
fi

# Add remote origin
echo -e "${BLUE}🔗 Adding remote origin...${NC}"
git remote add origin "https://github.com/${GITHUB_USERNAME}/${REPO_NAME}.git" 2>/dev/null || {
    echo -e "${YELLOW}⚠️  Remote origin already exists. Updating...${NC}"
    git remote set-url origin "https://github.com/${GITHUB_USERNAME}/${REPO_NAME}.git"
}

# Create and push main branch
echo -e "${BLUE}📤 Pushing to main branch...${NC}"
git branch -M main
git push -u origin main

# Create develop branch
echo -e "${BLUE}🌿 Creating develop branch...${NC}"
git checkout -b develop
git push -u origin develop
git checkout main

# Set up branch protection rules
echo -e "${BLUE}🛡️  Setting up branch protection rules...${NC}"

# Protect main branch
gh api repos/${GITHUB_USERNAME}/${REPO_NAME}/branches/main/protection \
    --method PUT \
    --field required_status_checks='{"strict":true,"contexts":["Client CI/CD","Server CI/CD"]}' \
    --field enforce_admins=true \
    --field required_pull_request_reviews='{"required_approving_review_count":1,"dismiss_stale_reviews":true}' \
    --field restrictions=null \
    --field allow_force_pushes=false \
    --field allow_deletions=false

# Protect develop branch
gh api repos/${GITHUB_USERNAME}/${REPO_NAME}/branches/develop/protection \
    --method PUT \
    --field required_status_checks='{"strict":true,"contexts":["Client CI/CD","Server CI/CD"]}' \
    --field enforce_admins=false \
    --field required_pull_request_reviews='{"required_approving_review_count":1}' \
    --field restrictions=null \
    --field allow_force_pushes=false \
    --field allow_deletions=false

echo -e "${GREEN}✅ Branch protection rules configured${NC}"

# Set up repository settings
echo -e "${BLUE}⚙️  Configuring repository settings...${NC}"

# Enable issues, wiki, projects
gh api repos/${GITHUB_USERNAME}/${REPO_NAME} \
    --method PATCH \
    --field has_issues=true \
    --field has_wiki=true \
    --field has_projects=true \
    --field allow_squash_merge=true \
    --field allow_merge_commit=false \
    --field allow_rebase_merge=true \
    --field delete_branch_on_merge=true

# Add repository topics
gh api repos/${GITHUB_USERNAME}/${REPO_NAME}/topics \
    --method PUT \
    --field names='["social-commerce","microservices","react-native","spring-boot","university","java","typescript","docker","kubernetes"]'

echo -e "${GREEN}✅ Repository settings configured${NC}"

# Create repository secrets (you'll need to set these manually)
echo -e "${YELLOW}🔐 Please set up the following repository secrets manually:${NC}"
echo "   Go to: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}/settings/secrets/actions"
echo ""
echo "   Required secrets:"
echo "   - DOCKER_USERNAME: Your Docker Hub username"
echo "   - DOCKER_PASSWORD: Your Docker Hub password/token"
echo "   - EXPO_TOKEN: Your Expo access token"
echo "   - DATABASE_URL: Production database URL"
echo "   - REDIS_URL: Production Redis URL"
echo "   - JWT_SECRET: JWT signing secret"
echo "   - STRIPE_SECRET_KEY: Stripe secret key"
echo "   - PAYPAL_CLIENT_SECRET: PayPal client secret"
echo ""

# Create initial issues
echo -e "${BLUE}📋 Creating initial project issues...${NC}"

gh issue create \
    --title "🚀 Project Setup and Initial Configuration" \
    --body "Set up development environment, configure databases, and prepare for development." \
    --label "setup,high-priority"

gh issue create \
    --title "🔐 Implement Authentication System" \
    --body "Complete JWT authentication, user registration, and login functionality." \
    --label "authentication,backend"

gh issue create \
    --title "📱 Mobile App UI Implementation" \
    --body "Implement core mobile app screens and navigation." \
    --label "frontend,mobile"

gh issue create \
    --title "🛒 E-commerce Features" \
    --body "Implement product catalog, shopping cart, and payment processing." \
    --label "ecommerce,backend"

gh issue create \
    --title "💬 Real-time Chat System" \
    --body "Implement WebSocket-based real-time messaging." \
    --label "realtime,backend"

echo -e "${GREEN}✅ Initial issues created${NC}"

# Create project board
echo -e "${BLUE}📊 Creating project board...${NC}"

gh project create \
    --title "TheRavedApp Development" \
    --body "Main project board for tracking development progress"

echo -e "${GREEN}✅ Project board created${NC}"

echo ""
echo -e "${GREEN}🎉 GitHub repository setup complete!${NC}"
echo "=================================================="
echo -e "${BLUE}📍 Repository URL: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}${NC}"
echo -e "${BLUE}🔧 Actions: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}/actions${NC}"
echo -e "${BLUE}📋 Issues: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}/issues${NC}"
echo -e "${BLUE}📊 Projects: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}/projects${NC}"
echo ""
echo -e "${YELLOW}📝 Next steps:${NC}"
echo "1. Set up repository secrets (see above)"
echo "2. Configure deployment environments"
echo "3. Set up monitoring and logging"
echo "4. Start development on feature branches"
echo ""
echo -e "${GREEN}Happy coding! 🚀${NC}"
