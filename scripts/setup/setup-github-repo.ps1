# ===================================
# GitHub Repository Setup Script (PowerShell)
# ===================================

param(
    [string]$Username = "johnwus",
    [string]$RepoName = "theRavedApp",
    [string]$Description = "Complete full-stack social commerce platform for university students with microservices architecture"
)

# Colors for output
$Red = "Red"
$Green = "Green"
$Yellow = "Yellow"
$Blue = "Blue"

Write-Host "🚀 Setting up GitHub repository for TheRavedApp" -ForegroundColor $Blue
Write-Host "==================================================" -ForegroundColor $Blue

# Check if GitHub CLI is installed
try {
    $ghVersion = gh --version
    Write-Host "✅ GitHub CLI found: $($ghVersion[0])" -ForegroundColor $Green
} catch {
    Write-Host "⚠️  GitHub CLI not found. Please install it first:" -ForegroundColor $Yellow
    Write-Host "   Windows: winget install --id GitHub.cli" -ForegroundColor $Yellow
    Write-Host "   Or download from: https://github.com/cli/cli/releases" -ForegroundColor $Yellow
    Write-Host ""
    Write-Host "📋 Manual setup instructions:" -ForegroundColor $Blue
    Write-Host "1. Go to https://github.com/new"
    Write-Host "2. Repository name: $RepoName"
    Write-Host "3. Description: $Description"
    Write-Host "4. Set to Public (or Private if preferred)"
    Write-Host "5. Don't initialize with README (we already have one)"
    Write-Host "6. Create repository"
    Write-Host "7. Run the following commands:"
    Write-Host ""
    Write-Host "   git remote add origin https://github.com/$Username/$RepoName.git" -ForegroundColor $Yellow
    Write-Host "   git branch -M main" -ForegroundColor $Yellow
    Write-Host "   git push -u origin main" -ForegroundColor $Yellow
    Write-Host ""
    exit 1
}

# Check if user is logged in to GitHub CLI
try {
    gh auth status 2>$null
    Write-Host "✅ GitHub CLI authenticated" -ForegroundColor $Green
} catch {
    Write-Host "🔐 Please login to GitHub CLI first:" -ForegroundColor $Yellow
    Write-Host "   gh auth login" -ForegroundColor $Yellow
    exit 1
}

# Create the repository
Write-Host "📦 Creating GitHub repository..." -ForegroundColor $Blue

try {
    gh repo create "$Username/$RepoName" --description $Description --public --clone=false --confirm
    Write-Host "✅ Repository created successfully" -ForegroundColor $Green
} catch {
    Write-Host "❌ Failed to create repository. It might already exist." -ForegroundColor $Red
    Write-Host "🔄 Continuing with existing repository..." -ForegroundColor $Yellow
}

# Add remote origin
Write-Host "🔗 Adding remote origin..." -ForegroundColor $Blue
try {
    git remote add origin "https://github.com/$Username/$RepoName.git" 2>$null
} catch {
    Write-Host "⚠️  Remote origin already exists. Updating..." -ForegroundColor $Yellow
    git remote set-url origin "https://github.com/$Username/$RepoName.git"
}

# Create and push main branch
Write-Host "📤 Pushing to main branch..." -ForegroundColor $Blue
git branch -M main
git push -u origin main

# Create develop branch
Write-Host "🌿 Creating develop branch..." -ForegroundColor $Blue
git checkout -b develop
git push -u origin develop
git checkout main

# Set up branch protection rules
Write-Host "🛡️  Setting up branch protection rules..." -ForegroundColor $Blue

try {
    # Protect main branch
    $mainProtection = @{
        required_status_checks = @{
            strict = $true
            contexts = @("Client CI/CD", "Server CI/CD")
        }
        enforce_admins = $true
        required_pull_request_reviews = @{
            required_approving_review_count = 1
            dismiss_stale_reviews = $true
        }
        restrictions = $null
        allow_force_pushes = $false
        allow_deletions = $false
    }
    
    gh api "repos/$Username/$RepoName/branches/main/protection" --method PUT --input - <<< ($mainProtection | ConvertTo-Json -Depth 10)
    
    Write-Host "✅ Branch protection rules configured" -ForegroundColor $Green
} catch {
    Write-Host "⚠️  Could not set up branch protection rules automatically" -ForegroundColor $Yellow
    Write-Host "Please set them up manually in the GitHub repository settings" -ForegroundColor $Yellow
}

# Set up repository settings
Write-Host "⚙️  Configuring repository settings..." -ForegroundColor $Blue

try {
    gh api "repos/$Username/$RepoName" --method PATCH --field has_issues=true --field has_wiki=true --field has_projects=true --field allow_squash_merge=true --field allow_merge_commit=false --field allow_rebase_merge=true --field delete_branch_on_merge=true
    
    # Add repository topics
    $topics = @("social-commerce", "microservices", "react-native", "spring-boot", "university", "java", "typescript", "docker", "kubernetes")
    gh api "repos/$Username/$RepoName/topics" --method PUT --field names=$topics
    
    Write-Host "✅ Repository settings configured" -ForegroundColor $Green
} catch {
    Write-Host "⚠️  Could not configure all repository settings" -ForegroundColor $Yellow
}

# Display manual setup instructions for secrets
Write-Host "🔐 Please set up the following repository secrets manually:" -ForegroundColor $Yellow
Write-Host "   Go to: https://github.com/$Username/$RepoName/settings/secrets/actions"
Write-Host ""
Write-Host "   Required secrets:"
Write-Host "   - DOCKER_USERNAME: Your Docker Hub username"
Write-Host "   - DOCKER_PASSWORD: Your Docker Hub password/token"
Write-Host "   - EXPO_TOKEN: Your Expo access token"
Write-Host "   - DATABASE_URL: Production database URL"
Write-Host "   - REDIS_URL: Production Redis URL"
Write-Host "   - JWT_SECRET: JWT signing secret"
Write-Host "   - STRIPE_SECRET_KEY: Stripe secret key"
Write-Host "   - PAYPAL_CLIENT_SECRET: PayPal client secret"
Write-Host ""

# Create initial issues
Write-Host "📋 Creating initial project issues..." -ForegroundColor $Blue

try {
    gh issue create --title "🚀 Project Setup and Initial Configuration" --body "Set up development environment, configure databases, and prepare for development." --label "setup,high-priority"
    gh issue create --title "🔐 Implement Authentication System" --body "Complete JWT authentication, user registration, and login functionality." --label "authentication,backend"
    gh issue create --title "📱 Mobile App UI Implementation" --body "Implement core mobile app screens and navigation." --label "frontend,mobile"
    gh issue create --title "🛒 E-commerce Features" --body "Implement product catalog, shopping cart, and payment processing." --label "ecommerce,backend"
    gh issue create --title "💬 Real-time Chat System" --body "Implement WebSocket-based real-time messaging." --label "realtime,backend"
    
    Write-Host "✅ Initial issues created" -ForegroundColor $Green
} catch {
    Write-Host "⚠️  Could not create all issues automatically" -ForegroundColor $Yellow
}

Write-Host ""
Write-Host "🎉 GitHub repository setup complete!" -ForegroundColor $Green
Write-Host "==================================================" -ForegroundColor $Green
Write-Host "📍 Repository URL: https://github.com/$Username/$RepoName" -ForegroundColor $Blue
Write-Host "🔧 Actions: https://github.com/$Username/$RepoName/actions" -ForegroundColor $Blue
Write-Host "📋 Issues: https://github.com/$Username/$RepoName/issues" -ForegroundColor $Blue
Write-Host "📊 Projects: https://github.com/$Username/$RepoName/projects" -ForegroundColor $Blue
Write-Host ""
Write-Host "📝 Next steps:" -ForegroundColor $Yellow
Write-Host "1. Set up repository secrets (see above)"
Write-Host "2. Configure deployment environments"
Write-Host "3. Set up monitoring and logging"
Write-Host "4. Start development on feature branches"
Write-Host ""
Write-Host "Happy coding! 🚀" -ForegroundColor $Green
