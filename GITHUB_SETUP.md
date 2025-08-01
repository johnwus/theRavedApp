# 🚀 GitHub Repository Setup Guide

This guide will help you set up the GitHub repository for TheRavedApp with continuous integration and deployment.

## 📋 Prerequisites

- Git configured with your credentials ✅ (Already done)
- GitHub account: `johnwus` ✅
- Email: `johnowusuyn@gmail.com` ✅

## 🔧 Option 1: Automated Setup (Recommended)

### Install GitHub CLI

**Windows:**
```powershell
winget install --id GitHub.cli
```

**macOS:**
```bash
brew install gh
```

**Linux:**
```bash
# Ubuntu/Debian
curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update
sudo apt install gh
```

### Run Setup Script

**Windows (PowerShell):**
```powershell
.\scripts\setup\setup-github-repo.ps1
```

**macOS/Linux:**
```bash
chmod +x scripts/setup/setup-github-repo.sh
./scripts/setup/setup-github-repo.sh
```

## 🔧 Option 2: Manual Setup

### 1. Create GitHub Repository

1. Go to [GitHub](https://github.com/new)
2. Repository name: `theRavedApp`
3. Description: `Complete full-stack social commerce platform for university students with microservices architecture`
4. Set to **Public** (or Private if preferred)
5. **Don't** initialize with README (we already have one)
6. Click **Create repository**

### 2. Connect Local Repository

```bash
# Add remote origin
git remote add origin https://github.com/johnwus/theRavedApp.git

# Push main branch
git branch -M main
git push -u origin main

# Create and push develop branch
git checkout -b develop
git push -u origin develop
git checkout main
```

### 3. Set Up Branch Protection

1. Go to **Settings** → **Branches**
2. Add rule for `main` branch:
   - ✅ Require a pull request before merging
   - ✅ Require approvals (1)
   - ✅ Dismiss stale PR approvals when new commits are pushed
   - ✅ Require status checks to pass before merging
   - ✅ Require branches to be up to date before merging
   - Add status checks: `Client CI/CD`, `Server CI/CD`
   - ✅ Restrict pushes that create files larger than 100MB

3. Add rule for `develop` branch:
   - ✅ Require a pull request before merging
   - ✅ Require approvals (1)
   - ✅ Require status checks to pass before merging

### 4. Configure Repository Settings

1. Go to **Settings** → **General**
2. Features:
   - ✅ Issues
   - ✅ Wiki
   - ✅ Projects
3. Pull Requests:
   - ✅ Allow squash merging
   - ❌ Allow merge commits
   - ✅ Allow rebase merging
   - ✅ Automatically delete head branches

### 5. Set Up Repository Secrets

Go to **Settings** → **Secrets and variables** → **Actions**

Add the following secrets:

| Secret Name | Description | Required For |
|-------------|-------------|--------------|
| `DOCKER_USERNAME` | Your Docker Hub username | Docker image publishing |
| `DOCKER_PASSWORD` | Your Docker Hub password/token | Docker image publishing |
| `EXPO_TOKEN` | Your Expo access token | Mobile app builds |
| `DATABASE_URL` | Production database URL | Production deployment |
| `REDIS_URL` | Production Redis URL | Production deployment |
| `JWT_SECRET` | JWT signing secret (generate random) | Authentication |
| `STRIPE_SECRET_KEY` | Stripe secret key | Payment processing |
| `PAYPAL_CLIENT_SECRET` | PayPal client secret | Payment processing |

### 6. Add Repository Topics

Go to **Settings** → **General** → **Topics**

Add these topics:
- `social-commerce`
- `microservices`
- `react-native`
- `spring-boot`
- `university`
- `java`
- `typescript`
- `docker`
- `kubernetes`

## 🔄 Continuous Integration Features

### ✅ Automated Workflows

1. **Client CI/CD** (`client-ci.yml`)
   - Runs on changes to `client/**`
   - Node.js testing, linting, type checking
   - Expo builds for production
   - Code coverage reporting

2. **Server CI/CD** (`server-ci.yml`)
   - Runs on changes to `server/**`
   - Java testing with PostgreSQL and Redis
   - Security scanning with OWASP
   - Docker image building and publishing
   - Maven artifact generation

3. **Main CI/CD Pipeline** (`main-ci.yml`)
   - Orchestrates all workflows
   - Path-based change detection
   - Staging and production deployments
   - Performance testing
   - Security scanning

4. **Infrastructure CI** (`infrastructure-ci.yml`)
   - Terraform validation
   - Kubernetes manifest validation
   - Docker compose testing

5. **Security Scanning** (`security-scan.yml`)
   - Dependency vulnerability scanning
   - Code security analysis
   - SARIF report generation

6. **Performance Testing** (`performance-test.yml`)
   - Load testing
   - Performance regression detection

### ✅ Branch Strategy

- **`main`** - Production-ready code
- **`develop`** - Integration branch for features
- **`feature/*`** - Feature development branches
- **`hotfix/*`** - Critical production fixes

### ✅ Deployment Strategy

- **Pull Request** → Run tests and security scans
- **Push to `develop`** → Deploy to staging environment
- **Push to `main`** → Deploy to production environment

## 🎯 Next Steps

1. **Set up development environment:**
   ```bash
   # Install dependencies
   ./scripts/setup/setup-development.sh
   ```

2. **Start development:**
   ```bash
   # Create feature branch
   git checkout develop
   git checkout -b feature/authentication-system
   
   # Make changes and commit
   git add .
   git commit -m "feat: implement JWT authentication"
   git push -u origin feature/authentication-system
   
   # Create pull request on GitHub
   ```

3. **Monitor CI/CD:**
   - Check [Actions tab](https://github.com/johnwus/theRavedApp/actions) for build status
   - Review security scan results
   - Monitor deployment status

## 🆘 Troubleshooting

### Common Issues

1. **GitHub CLI not authenticated:**
   ```bash
   gh auth login
   ```

2. **Remote already exists:**
   ```bash
   git remote set-url origin https://github.com/johnwus/theRavedApp.git
   ```

3. **Permission denied:**
   - Check if you have write access to the repository
   - Verify your GitHub credentials

4. **CI/CD failures:**
   - Check the Actions tab for detailed error logs
   - Ensure all required secrets are set
   - Verify branch protection rules

## 📞 Support

If you encounter any issues:
1. Check the [GitHub Actions documentation](https://docs.github.com/en/actions)
2. Review the workflow files in `.github/workflows/`
3. Check repository settings and secrets
4. Ensure all prerequisites are met

---

**Happy coding! 🚀**
