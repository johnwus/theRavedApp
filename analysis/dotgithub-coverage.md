.github Coverage, Gaps, and Remediation Plan

Scope
- This document reviews the repository’s `.github/` directory: CI/CD workflows, security/quality scans, release automation, issue templates, and governance files. It summarizes current behavior, identifies gaps/risks, and proposes concrete remediation tasks aligned with project standards.

Inventory
- Workflows: `.github/workflows/`
  - `server-ci.yml`
  - `docker-build-push.yml`
  - `client-ci.yml`
  - `infrastructure-ci.yml`
  - `infrastructure-lint.yml`
  - `infrastructure-validate.yml`
  - `qodana_code_quality.yml`
  - `performance-test.yml`
  - `security-scan.yml`
  - `services-unit-tests.yml`
  - `release.yml` (empty)
- Issue templates: `.github/ISSUE_TEMPLATE/`
  - `bug_report.md` (empty)
  - `feature_request.md` (unread in scan; treat as likely missing/incomplete)
  - `performance_issue.md` (empty)
- Governance:
  - `dependabot.yml` (empty)
  - `CODEOWNERS` (empty)

Current implementation details

server-ci.yml
- Triggers: push/PR to `main` and `develop` when `server/**` changes.
- Job `backend-test`:
  - Matrix: all backend services.
  - Services: Postgres 15, Redis 7 with health checks.
  - Steps: checkout, setup Temurin JDK 21, cache `~/.m2`, run `mvn test` per service, then `mvn compile`, publish test reports with `dorny/test-reporter`.
  - Integration tests: only for `user-service` using Testcontainers (profile `with-it`).
- Job `code-quality`:
  - SonarCloud analysis after full `mvn verify` in `server/`.
- Job `docker-build` (reusable workflow):
  - Runs only on `main` (`if: github.ref == 'refs/heads/main'`). Builds and pushes images per service.
- Jobs `deploy-staging`, `deploy-production`:
  - Placeholders only (`echo`). Staging job depends on `docker-build` but is limited to `develop` branch, creating an unmet dependency (see Gaps).

docker-build-push.yml (reusable)
- Inputs: `service` (directory under `server/`).
- Secrets: `DOCKER_USERNAME`, `DOCKER_PASSWORD`.
- Steps: checkout, set up Buildx, Docker Hub login, build and push with tags `latest` and `${{ github.sha }}`, cache via GHA.

client-ci.yml
- Triggers on client changes for push/PR to `main`/`develop`.
- Node 20, yarn install with lockfile, lint, test (`--ci`), and type-check.

infrastructure-ci.yml
- Placeholder infra checks. Prints Terraform version if folder exists; announces Helm lint intent but does not run helm without setup. No actual validation of Terraform/Helm content.

infrastructure-lint.yml
- Sets up Helm v3.14.3 and runs `helm lint` on `infrastructure/helm` charts. Uses a hard-coded list of subcharts to iterate.

infrastructure-validate.yml
- Sets up Helm, Helmfile, and `yq`. Renders `helmfile` templates for `dev`, `staging`, `prod`. Also toggles provisioning flags for Kafka/Elasticsearch/RabbitMQ via `yq` and re-renders. Greps rendered output for `WEBSOCKET_ALLOWED_ORIGINS` keys for staging/prod.

qodana_code_quality.yml
- Runs JetBrains Qodana on PRs and `main` pushes. Writes PR comments and annotations, uses cache, does not upload results as job artifact.

performance-test.yml
- Manual and `main`-push triggers. Runs `scripts/testing/performance-tests.sh` after checkout. No artifacts, thresholds, or schedules configured beyond triggers.

security-scan.yml
- Runs Trivy filesystem scan on the repo for push/PR to `main`/`develop`. Uploads SARIF to GitHub Security. No image scanning or dependency (Maven/npm) specific scans.

services-unit-tests.yml
- PR-scoped workflow that checks a subset of server services (social, realtime, content, analytics, notification). Sequential `mvn test` per service. Duplicates coverage with `server-ci.yml` to some extent.

release.yml
- Empty (no release automation).

ISSUE_TEMPLATE
- `bug_report.md` and `performance_issue.md` are empty; `feature_request.md` appears missing or empty.

Governance
- `dependabot.yml` is empty; `CODEOWNERS` is empty.

Gaps and risks
- Staging deploy job never runs: it has `needs: [docker-build]` but `docker-build` only runs on `main` while staging deploy only runs on `develop`.
- Deploy jobs are placeholders with no actual deployment logic or environment protections.
- Infra CI is non-enforcing: does not run `terraform fmt/validate/plan` nor real `helm lint`/`template` in `infrastructure-ci.yml`.
- Helm lint iterates a hard-coded chart list; high risk of drift when charts change.
- Duplicate/overlapping unit test workflows (`server-ci.yml` matrix vs `services-unit-tests.yml`) may waste minutes and create confusion.
- No CodeQL/static analysis for security beyond Trivy FS scan.
- No image scanning post-build (Trivy/Grype) in CI for pushed images.
- Dependabot not configured; no automated dependency updates for Maven/npm/docker/actions.
- CODEOWNERS missing; branch protection cannot enforce proper ownership reviews.
- Issue templates missing; inconsistent issue quality and triage burden.
- Release automation missing; manual tagging/changelog risk, no provenance/signing.
- No concurrency/cancel-in-progress to reduce redundant CI runs.
- No artifact retention strategy for test reports, performance outputs, or SBOMs.

Remediation plan (prioritized)
1) Fix staging deploy gating and implement deploy steps
   - Option A: Enable `docker-build` on `develop` branch as well; keep deploy-staging depending on it.
   - Option B: In staging job, build images inline (or use `:develop-<sha>` tags) without depending on main-only job.
   - Implement Helmfile-based deploy with environment `staging` and required approvals.

2) Add Dependabot configuration
   - Ecosystems: `maven` for `/server`, `npm` for `/client`, `docker` for Dockerfiles, `github-actions` for workflows.

3) Add CODEOWNERS and enforce via branch protection
   - Map service directories to responsible owners/teams to require review.

4) Strengthen infra validation in CI
   - In `infrastructure-ci.yml`, set up Terraform with OIDC and run `fmt -check`, `init -backend=false`, `validate` per module. For Helm, run `helm lint` and `helm template` (or reuse `infrastructure-validate.yml`). Add `kubeconform` against rendered manifests.

5) Consolidate test workflows
   - Remove or repurpose `services-unit-tests.yml` to avoid duplication with `server-ci.yml` matrix. Alternatively, scope it to PR paths only and skip matrix duplication.

6) Add image scanning and SBOM
   - After docker build/push, run Trivy image scan and upload SARIF. Generate SBOM (Syft) and attach as artifact.

7) Add CodeQL (optional but recommended)
   - Enable GitHub CodeQL for JavaScript/TypeScript and Java.

8) Improve performance tests
   - Add scheduled (cron) nightly runs, publish artifacts and trend, and fail on threshold regression.

9) Implement release automation
   - Trigger on tag push. Build images with version tag, generate changelog (Conventional Commits), create GitHub Release, and optionally deploy to production with required environment approvals.

10) Workflow hygiene
   - Add `concurrency` groups with `cancel-in-progress` for PR/branch.
   - Fail fast when mandatory secrets are missing (clear messages).
   - Discover/iterate chart dirs dynamically instead of hard-coded list.

Proposed content snippets (for reference)

Dependabot (`.github/dependabot.yml`)
```yaml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/server"
    schedule: { interval: "weekly" }
  - package-ecosystem: "npm"
    directory: "/client"
    schedule: { interval: "weekly" }
  - package-ecosystem: "docker"
    directory: "/"
    schedule: { interval: "weekly" }
  - package-ecosystem: "github-actions"
    directory: "/"
    schedule: { interval: "weekly" }
```

CODEOWNERS (`.github/CODEOWNERS`)
```txt
# Default owners
* @raved/platform-maintainers

# Backend services
/server/user-service/ @raved/backend-team
/server/social-service/ @raved/backend-team
/server/ecommerce-service/ @raved/backend-team
/server/content-service/ @raved/backend-team
/server/notification-service/ @raved/backend-team
/server/analytics-service/ @raved/data-team
/server/realtime-service/ @raved/realtime-team
/server/events-service/ @raved/events-team
/server/subscription-service/ @raved/backend-team

# Client
/client/ @raved/frontend-team

# Infrastructure
/infrastructure/ @raved/infra-team
.github/ @raved/devex-team
```

Issue templates (minimum)
- `bug_report.md`: title, environment, steps to reproduce, expected vs actual, logs, screenshots, regression flag, severity.
- `feature_request.md`: problem statement, proposal, alternatives, scope, acceptance criteria, rollout plan.
- `performance_issue.md`: workload description, baseline vs observed metrics, SLOs affected, repro script, hardware/limits, suspected components.

Actionable tasks
- server-ci.yml
  - Fix staging deploy dependency or branch condition and implement Helmfile deploy for `staging` and `production` with environment protections.
  - Add artifact uploads for surefire reports and coverage (if any) across services.
  - Consider adding more integration tests (Testcontainers) for other services gated by profiles.
- docker-build-push.yml
  - Add SBOM generation and Trivy image scan job post-build; optionally Cosign signing and provenance.
- client-ci.yml
  - If e2e present, separate e2e job; upload test artifacts.
- infrastructure-ci.yml
  - Implement Terraform/Helm actual checks as described.
- infrastructure-lint.yml
  - Replace hard-coded chart list with dynamic discovery.
- infrastructure-validate.yml
  - Add `kubeconform` validation of rendered manifests against cluster schemas.
- qodana_code_quality.yml
  - Optionally upload SARIF to code scanning; configure project roots.
- performance-test.yml
  - Add cron schedule and artifact publishing; add thresholds.
- security-scan.yml
  - Add dependency scans (OWASP Dependency-Check or Maven/Gradle audit, npm audit) and image scans.
- services-unit-tests.yml
  - Consolidate or restrict to path filters to avoid duplication.
- release.yml
  - Implement tag-based release pipeline.
- ISSUE_TEMPLATE/*.md
  - Populate with structured templates.
- dependabot.yml, CODEOWNERS
  - Populate and enable.

Notes
- Align all changes with existing CI/CD and infrastructure patterns already documented in `infrastructure/`. Prefer reusable workflows for Java and Node to DRY repeated steps.






