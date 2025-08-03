#!/bin/bash

# Raved Development Setup Script
echo "🚀 Setting up Raved development environment..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 18+ first."
    exit 1
fi

# Check if Yarn is installed
if ! command -v yarn &> /dev/null; then
    echo "❌ Yarn is not installed. Installing Yarn..."
    npm install -g yarn
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17+ first."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven first."
    exit 1
fi

echo "✅ All prerequisites are installed!"

# Setup client dependencies
echo "📱 Setting up client dependencies..."
cd client
yarn install
cd ..

# Setup server dependencies
echo "🖥️  Setting up server dependencies..."
cd server
mvn clean install -DskipTests
cd ..

# Create environment files
echo "⚙️  Creating environment files..."
if [ ! -f .env.development ]; then
    cp .env.example .env.development
    echo "📝 Created .env.development - please update with your configuration"
fi

# Start infrastructure services
echo "🐳 Starting infrastructure services..."
docker-compose -f infrastructure/docker/development/docker-compose.yml up -d

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30

# Run database migrations
echo "🗄️  Running database migrations..."
./scripts/database/migrate.sh

echo "🎉 Development environment setup complete!"
echo ""
echo "Next steps:"
echo "1. Update .env.development with your configuration"
echo "2. Start the client: cd client && yarn start"
echo "3. Start the services: yarn dev:services"
echo "4. Access the app at http://localhost:8080"
echo ""
echo "Useful commands:"
echo "- yarn dev: Start all services"
echo "- yarn logs: View service logs"
echo "- yarn health: Check service health"