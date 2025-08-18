#!/usr/bin/env bash
set -euo pipefail

echo "Tailing logs for docker compose services (if running)..."
docker compose logs -f --tail=200 || true

