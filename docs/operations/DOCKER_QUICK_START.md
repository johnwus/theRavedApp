# 🐳 RAvED Docker Quick Start Guide

This file was relocated from the repository root to follow the documented project structure.

- Start infra only: `docker compose up -d postgres redis rabbitmq`
- Start discovery: `docker compose up -d eureka-server config-server`
- Start API Gateway: `docker compose up -d api-gateway`
- Start services: `docker compose up -d user-service content-service social-service`
- Health: `curl http://localhost:8080/actuator/health`

