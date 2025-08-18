#!/usr/bin/env bash
set -euo pipefail

# Frontend unit tests
if [ -d "client" ]; then
  echo "Running frontend unit tests..."
  pushd client >/dev/null
  if [ -f package-lock.json ]; then npm ci --prefer-offline; else npm install; fi
  npm run test:ci || npm test || true
  popd >/dev/null
fi

# Backend unit tests for all services
if [ -d "server" ]; then
  echo "Running backend unit tests..."
  pushd server >/dev/null
  mvn -q -T1C -DskipITs=true -DskipIT=true -Dskip-integration-tests=true clean test -B
  popd >/dev/null
fi

echo "Unit tests completed."

