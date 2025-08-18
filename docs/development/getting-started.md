# Getting Started

Documentation for TheRavedApp

## Overview

This document provides information about the system.

## Scripts and Workflows
- Testing scripts are under `scripts/testing/`:
  - `run-unit-tests.sh`, `run-integration-tests.sh`, `run-e2e-tests.sh`, `performance-tests.sh`
- Monitoring scripts are under `scripts/monitoring/`:
  - `health-check.sh`, `logs.sh`, `metrics.sh`
- CI workflows are split under `.github/workflows/`:
  - `client-ci.yml`, `server-ci.yml`, `security-scan.yml`, `infrastructure-ci.yml`, `performance-test.yml`, `release.yml`

Run `npm run test`, `npm run test:integration`, and `npm run test:e2e` to invoke the testing scripts.
