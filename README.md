# Raved - Student Fashion Social Platform

A modern social platform designed specifically for students to share fashion content, connect with peers, and discover trends within their academic community.

## 🚀 Features

- **Student Verification**: Secure authentication through institutional integration
- **Faculty-Specific Feeds**: Content organized by academic departments
- **Real-time Chat**: Instant messaging and notifications
- **Social Interactions**: Like, comment, follow, and share content
- **E-commerce Integration**: Buy and sell fashion items
- **Analytics Dashboard**: Track engagement and trending content
- **Mobile-First Design**: Optimized for mobile devices with React Native

## 🏗️ Architecture

Raved is built using a modern microservices architecture:

### Frontend
- **React Native** with Expo Development Builds
- **Redux Toolkit** for state management
- **RTK Query** for API integration
- **NativeWind** (Tailwind CSS for React Native)
- **Socket.io** for real-time features

### Backend
- **Spring Boot 3.x** microservices
- **Spring Cloud** for service discovery and configuration
- **PostgreSQL** for primary data storage
- **Redis** for caching and sessions
- **Apache Kafka** for event streaming
- **RabbitMQ** for message queuing

### Infrastructure
- **Docker** for containerization
- **Kubernetes** for orchestration
- **Terraform** for infrastructure as code
- **GitHub Actions** for CI/CD

## 📋 Prerequisites

- **Node.js** 18+ and **Yarn** 3.6+
- **Java** 17+ and **Maven** 3.8+
- **Docker** and **Docker Compose**
- **Git**

## 🛠️ Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/raved-app.git
   cd raved-app
   ```

2. **Run the setup script**
   ```bash
   chmod +x scripts/setup/setup-development.sh
   ./scripts/setup/setup-development.sh
   ```

3. **Start the development environment**
   ```bash
   yarn dev
   ```

4. **Start the mobile app**
   ```bash
   cd client
   yarn start
   ```

## 📱 Mobile Development

### iOS
```bash
cd client
yarn ios
```

### Android
```bash
cd client
yarn android
```

### Building for Production
```bash
cd client
yarn build:all
```

## 🖥️ Server Development

### Start all services
```bash
yarn dev:services
```

### Build services
```bash
cd server
mvn clean install
```

### Run individual service
```bash
cd server/user-service
mvn spring-boot:run
```

## 🧪 Testing

### Run all tests
```bash
yarn test
```

### Client tests
```bash
cd client
yarn test
```

### Server tests
```bash
cd server
mvn test
```

### Integration tests
```bash
yarn test:integration
```

## 📊 Monitoring

Access monitoring dashboards:
- **API Gateway**: http://localhost:8080/actuator
- **Eureka Dashboard**: http://localhost:8761
- **RabbitMQ Management**: http://localhost:15672
- **Database**: localhost:5432

## 🚀 Deployment

### Development
```bash
yarn deploy:dev
```

### Staging
```bash
yarn deploy:staging
```

### Production
```bash
yarn deploy:prod
```

## 📁 Project Structure

```
raved-app/
├── client/                 # React Native frontend
├── server/                 # Spring Boot microservices
├── infrastructure/         # Docker, Kubernetes, Terraform
├── docs/                   # Documentation
├── scripts/                # Build and deployment scripts
└── .github/                # CI/CD workflows
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [docs/](docs/)
- **Issues**: [GitHub Issues](https://github.com/your-org/raved-app/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-org/raved-app/discussions)

## 🎯 Roadmap

- [ ] Advanced content filtering
- [ ] AI-powered recommendations
- [ ] Video content support
- [ ] Campus event integration
- [ ] Virtual fashion shows
- [ ] Marketplace expansion

---

Made with ❤️ by the Raved Team