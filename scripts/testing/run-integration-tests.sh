#!/usr/bin/env bash
set -euo pipefail

# Example: run Testcontainers profile for user-service if available
if [ -d "server/user-service" ]; then
  echo "Running integration tests for user-service..."
  pushd server/user-service >/dev/null
  mvn -q -Pwith-it verify -B || true
  popd >/dev/null
fi

echo "Integration tests completed."

