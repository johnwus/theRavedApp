# Raved Project Makefile

.PHONY: help setup dev build test clean deploy

# Default target
help:
	@echo "Raved Project Commands:"
	@echo "  setup     - Setup development environment"
	@echo "  dev       - Start development environment"
	@echo "  build     - Build all services"
	@echo "  test      - Run all tests"
	@echo "  clean     - Clean build artifacts"
	@echo "  deploy    - Deploy to staging"
	@echo ""
	@echo "Client Commands:"
	@echo "  client-install  - Install client dependencies"
	@echo "  client-start    - Start client development server"
	@echo "  client-test     - Run client tests"
	@echo "  client-build    - Build client for production"
	@echo ""
	@echo "Server Commands:"
	@echo "  server-install  - Install server dependencies"
	@echo "  server-start    - Start all server services"
	@echo "  server-test     - Run server tests"
	@echo "  server-build    - Build all server services"

# Setup development environment
setup:
	@echo "🚀 Setting up Raved development environment..."
	chmod +x scripts/setup/setup-development.sh
	./scripts/setup/setup-development.sh

# Start development environment
dev:
	@echo "🏃 Starting development environment..."
	docker-compose up -d

# Build all services
build: client-build server-build

# Run all tests
test: client-test server-test

# Clean build artifacts
clean:
	@echo "🧹 Cleaning build artifacts..."
	cd client && rm -rf node_modules dist .expo
	cd server && mvn clean
	docker system prune -f

# Deploy to staging
deploy:
	@echo "🚀 Deploying to staging..."
	./scripts/deploy/deploy-staging.sh

# Client commands
client-install:
	@echo "📱 Installing client dependencies..."
	cd client && yarn install

client-start:
	@echo "📱 Starting client development server..."
	cd client && yarn start

client-test:
	@echo "🧪 Running client tests..."
	cd client && yarn test

client-build:
	@echo "🏗️ Building client for production..."
	cd client && yarn build:all

# Server commands
server-install:
	@echo "🖥️ Installing server dependencies..."
	cd server && mvn clean install -DskipTests

server-start:
	@echo "🖥️ Starting server services..."
	docker-compose -f docker-compose.yml up -d

server-test:
	@echo "🧪 Running server tests..."
	cd server && mvn test

server-build:
	@echo "🏗️ Building server services..."
	cd server && mvn clean package

# Infrastructure commands
infra-up:
	@echo "🏗️ Starting infrastructure services..."
	docker-compose -f infrastructure/docker/development/docker-compose.yml up -d

infra-down:
	@echo "🛑 Stopping infrastructure services..."
	docker-compose -f infrastructure/docker/development/docker-compose.yml down

# Database commands
db-migrate:
	@echo "🗄️ Running database migrations..."
	./scripts/database/migrate.sh

db-seed:
	@echo "🌱 Seeding database..."
	./scripts/database/seed.sh

db-backup:
	@echo "💾 Backing up database..."
	./scripts/database/backup.sh

# Monitoring commands
logs:
	@echo "📋 Viewing service logs..."
	./scripts/monitoring/logs.sh

health:
	@echo "🏥 Checking service health..."
	./scripts/monitoring/health-check.sh

# Development utilities
lint:
	@echo "🔍 Running linters..."
	cd client && yarn lint
	# Add server linting if needed

format:
	@echo "✨ Formatting code..."
	cd client && yarn prettier --write .
	# Add server formatting if needed