#!/usr/bin/env bash
set -euo pipefail

# Simple health checks against common services
curl -fsS http://localhost:8080/actuator/health || true
curl -fsS http://localhost:8761 || true
curl -fsS http://localhost:8888/actuator/health || true

echo "Health checks executed."

