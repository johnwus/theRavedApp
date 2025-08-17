#!/usr/bin/env bash
set -euo pipefail

echo "Metrics placeholder. Hit Prometheus endpoint if present."
curl -fsS http://localhost:9090/-/ready || true

