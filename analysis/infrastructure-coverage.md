## Raved Infrastructure — Full Coverage Guide

This document explains everything a newcomer needs to understand, operate, and extend the `infrastructure/` folder. It covers what each piece is, how it is implemented, conventions used across environments, day-2 operations, and why specific choices were made.

### Who this is for
- Engineers onboarding to platform/devops
- Backend developers deploying new services
- SREs operating environments

### How to use this guide
- Start with the overview and directory tour
- Use the environment and workflow sections when deploying or debugging
- Refer to checklists and runbooks during changes or incidents

---

## High-level overview

- **Scope**: Build, package, provision, deploy, configure, and operate the platform outside of application business logic.
- **Core components**:
  - Containers built from `infrastructure/docker/`
  - Kubernetes manifests in `infrastructure/kubernetes/` and Helm charts in `infrastructure/helm/`
  - Cloud resources defined in `infrastructure/terraform/`
  - Operational automation in `infrastructure/scripts/`
  - Human documentation and standards in `infrastructure/docs/`
  - Cross-service database guidance in `infrastructure/database-structure-guide.md`
  - Setup milestones and verification notes in `infrastructure/SETUP_COMPLETE.md`

Why this structure? It enforces separation of concerns: app code lives in `server/`, while everything required to run the app in any environment lives in `infrastructure/` so it can be versioned, reviewed, and promoted through the same pipelines.

---

## Directory tour

- `infrastructure/SETUP_COMPLETE.md`
  - A checklist or milestone log indicating what’s been bootstrapped, verified, or is pending. Use it to understand environment readiness.

- `infrastructure/database-structure-guide.md`
  - Shared database patterns across services: naming, migrations, indexing, partitioning, retention, and consistency rules. Align new schemas to this before shipping.

- `infrastructure/docs/`
  - Reference documentation: architecture diagrams, environment maps (dev/staging/prod), security posture, backup/restore, DR strategy, CI/CD release processes, RBAC conventions.

- `infrastructure/docker/`
  - Dockerfiles and (optionally) Compose files used for local builds and CI image generation. Patterns include multi-stage builds, minimal base images, non-root users, and healthcheck scripts.

- `infrastructure/kubernetes/`
  - Raw Kubernetes YAMLs. Expect Deployments, Services, Ingress/IngressRoutes, ConfigMaps, Secrets references, HPAs, PodDisruptionBudgets, Jobs/CronJobs, and possibly Kustomize overlays per environment.

- `infrastructure/helm/`
  - Helm charts and `values-<env>.yaml` files. Use this when you prefer templated, versioned releases with environment-specific configuration injected via values.

- `infrastructure/scripts/`
  - Automation: cluster bootstrap, applying manifests/charts, seeding data, running migrations, rotating credentials, verifying health and dependencies. Intended to be idempotent and environment-aware.

- `infrastructure/terraform/`
  - Infrastructure as Code. Common modules: VPC/networking, Kubernetes cluster (EKS/GKE/AKS), databases (Postgres/RDS), caches (Redis), object storage (S3/GCS), messaging (Kafka/RabbitMQ), DNS/IAM, secrets, and monitoring stacks. Environment stacks live under `envs/` with separate state.

---

## Environments and configuration strategy

Environments typically include dev, staging, and prod. Each environment has:

- **Docker image tags**: immutable builds (e.g., `service:1.2.3`), promoted across envs.
- **Kubernetes config**: either separate directories (e.g., `kubernetes/dev/`) or Helm values (e.g., `helm/values-dev.yaml`).
- **External resources**: provisioned via `terraform/envs/dev|staging|prod` with isolated state and credentials.
- **Secrets**: managed via the environment’s secret manager or encrypted GitOps artifacts (SOPS/SealedSecrets). Never commit raw secrets.
- **Feature flags and toggles**: enabled per environment through ConfigMaps/values.

Config layering rule of thumb:
1. Defaults in charts/manifests
2. Environment overrides in values/overlays
3. Last-mile runtime overrides via Secrets/ConfigMaps

---

## Docker strategy (`infrastructure/docker/`)

Goals: reproducible, minimal, secure images.

Recommended patterns:
- Multi-stage builds: separate build and runtime layers
- Non-root users, read-only filesystem, drop capabilities where possible
- Healthcheck scripts for app-level readiness
- Cache-friendly layering: deps first, source last
- Explicit versions for OS and runtime

Example build command:
```bash
docker build -t raved/<service>:<version> -f infrastructure/docker/<service>/Dockerfile .
```

Tips:
- Use `.dockerignore` to keep contexts small
- Prefer distroless/alpine when compatible
- Scan images (e.g., Trivy) as part of CI

---

## Kubernetes manifests (`infrastructure/kubernetes/`)

What you’ll see:
- Deployments with probes, resources, HPA targets
- Services (ClusterIP) and external access via Ingress
- ConfigMaps and Secret refs mounted or injected as env vars
- HPAs and PodDisruptionBudgets for resilience
- Jobs for migrations and CronJobs for periodic tasks

Apply manifests (dev example):
```bash
kubectl apply -f infrastructure/kubernetes/dev/
kubectl rollout status deploy/<workload>
```

Common conventions:
- Labels/annotations align with CI/CD and monitoring
- Liveness probes light-weight; readiness probes reflect dependency readiness
- Resource requests/limits set based on profiling and SLOs

---

## Helm charts (`infrastructure/helm/`)

Why Helm: versioned, parameterized releases with values per environment.

Typical layout:
- `Chart.yaml` — name, version, dependencies
- `values.yaml` — sane defaults
- `values-<env>.yaml` — environment overrides (replicas, resources, domains, secrets references)
- `templates/` — Deployment, Service, Ingress, HPA, ConfigMap, Secret manifests

Release example:
```bash
helm upgrade --install raved-<svc> infrastructure/helm/<svc> \
  -f infrastructure/helm/<svc>/values.yaml \
  -f infrastructure/helm/<svc>/values-dev.yaml \
  --namespace raved-dev --create-namespace
```

Best practices:
- Do not commit plain secrets; reference external secret providers
- Template annotations for metrics/logging/trace auto-discovery
- Use `helm diff` in CI before applying

---

## Terraform (`infrastructure/terraform/`)

Purpose: declaratively provision cloud resources, versioned and peer-reviewed.

Common structure:
- `modules/` — reusable building blocks (vpc, cluster, database, cache, storage, dns, secrets, observability)
- `envs/<env>/` — stacks that compose modules for each environment
- Remote state (S3/GCS + DynamoDB/locks) to support teams and prevent drift

Workflow:
```bash
cd infrastructure/terraform/envs/dev
terraform init
terraform plan -out tfplan
terraform apply tfplan
```

Guidelines:
- Keep providers pinned; run `terraform fmt` and `validate` in CI
- Prefer modules for repeatability; document inputs/outputs
- Tag all resources for cost, ownership, and cleanup

---

## Secrets and configuration

Principles:
- Never commit secrets; use a secret manager or encrypted GitOps artifacts
- Separate config (non-sensitive) from secrets (sensitive)
- Rotate credentials and use short-lived access where possible

Implementation options seen in repos like this:
- Kubernetes Secrets sourced from a cloud secret manager
- SealedSecrets/SOPS with encrypted manifests in Git
- External Secrets Operator referencing cloud secret stores

Runtime patterns:
- Inject secrets via env vars; mount config via ConfigMaps
- Keep secret names stable; rotate values without redeploying apps when supported

---

## Databases and migrations

Use `infrastructure/database-structure-guide.md` to standardize:
- Schema naming, column conventions, timestamp and timezone rules
- Primary/foreign keys and cascade rules
- Indexing patterns (b-tree vs GIN/GIN_trgm), partitioning when needed
- Migration tooling (Flyway/Liquibase) and migration ordering (V1__*.sql ...)
- Data retention and archiving strategy

Operational patterns:
- Run migrations as a Kubernetes Job or initContainer
- Back up data periodically; test restores regularly
- Add read replicas and connection pooling for scalability

---

## Messaging, caching, and storage

- Messaging (Kafka/RabbitMQ): provision via Terraform, configure topics/queues, DLQs, retention, and idempotency keys. Expose connection URLs as secrets.
- Caching (Redis): provision with HA parameters, define eviction policy, set up TLS/auth, and follow key naming conventions.
- Object storage (S3/GCS): buckets per environment, lifecycle policies, KMS encryption, signed URLs for uploads/downloads.

Kubernetes integration:
- Connection settings passed via Secrets
- Liveness/readiness checks validate dependencies when feasible

---

## Networking and ingress

- Ingress controllers (e.g., NGINX/Traefik) configured via Helm
- DNS and certificates automated (ExternalDNS + cert-manager)
- Zero-trust principles: network policies to restrict east-west traffic
- Service mesh optional for mTLS and richer telemetry

Checklist:
- Map hostnames to Ingress rules per environment
- Ensure TLS termination and HSTS
- Rate limiting at edge for public endpoints

---

## Observability (logs, metrics, traces)

- Logs: centralized via DaemonSets/sidecars (e.g., Fluent Bit) with indexing and retention
- Metrics: Prometheus scraping + Grafana dashboards; app exposes `/metrics`
- Tracing: OpenTelemetry SDKs export to a collector backend

Operational practices:
- SLOs and alerting rules versioned alongside manifests
- Use labels for workload to dashboard mapping
- Include runbooks linked from alerts

---

## CI/CD integration and release flow

Expected pipeline stages:
1. Build and test application
2. Build and scan Docker images; push to registry
3. Terraform plan and (on approval) apply for infra changes
4. Helm/Kubernetes deploy with environment values
5. Run DB migrations (Job) and smoke tests

Controls:
- Promotion via immutable images
- `helm diff` and `terraform plan` artifacts attached to PRs
- Rollback plans: Helm rollback, blue/green or canary deployments

---

## Runbooks and playbooks (examples)

Deployment (Helm):
```bash
helm upgrade --install raved-app infrastructure/helm/raved-app \
  -f infrastructure/helm/values.yaml \
  -f infrastructure/helm/values-staging.yaml
kubectl rollout status deploy/raved-app -n raved-staging
```

Rolling back a release:
```bash
helm history raved-app -n raved-staging
helm rollback raved-app <REVISION> -n raved-staging
```

Terraform changes:
```bash
cd infrastructure/terraform/envs/staging
terraform init -upgrade
terraform plan -var-file=staging.tfvars -out plan.out
terraform apply plan.out
```

Secret rotation via External Secrets (conceptual):
1. Update value in secret manager
2. Wait for controller sync to refresh K8s Secret
3. Verify pods pick up without restart or roll them

---

## Security and compliance

- Principles: least privilege, encrypted at rest/in transit, auditable changes
- IAM: per-service roles; no shared long-lived credentials
- Image security: signed and scanned
- Network: ingress whitelisting, network policies, WAF for public endpoints
- Secrets: centralized, rotated, and access logged

Periodic tasks:
- Access review, key/secret rotation, vulnerability scans, dependency updates

---

## Disaster recovery and backups

- RPO/RTO targets defined in `docs/`
- Automated backups for databases and object storage; regular restoration tests
- Immutable backups where supported; cross-region replication for critical data
- DR runbook: order of service restoration, DNS failover, config restore

---

## Cost management and scalability

- Tagging for cost attribution
- Autoscaling via HPAs; cluster/node autoscaler configured via Terraform/Helm
- Resource requests/limits tuned; right-size instances and storage
- Storage lifecycle policies for cold data

---

## Onboarding checklist

- [ ] Read `infrastructure/SETUP_COMPLETE.md` for environment status
- [ ] Review `infrastructure/docs/` for architecture and standards
- [ ] Install required CLIs (kubectl, helm, terraform, docker)
- [ ] Authenticate to cloud and container registry
- [ ] Verify k8s context and namespace, then list workloads
- [ ] Run a dry-run Helm diff and Terraform plan for a no-op change
- [ ] Deploy to dev and validate healthchecks and dashboards

---

## Troubleshooting quick reference

- Pods failing readiness: check dependent services (DB/cache/message broker), secrets mounted, and config endpoints
- CrashLoopBackOff: view logs, check env vars, validate image tag
- 5xx at ingress: check service endpoints, probe failures, upstream timeouts
- Terraform drift: re-run plan, import externally changed resources if necessary
- Certificate issues: verify DNS, ACME challenges, cert-manager logs

---

## Directory reference cheat sheet

- `docs/`: Architecture diagrams, runbooks, standards
- `docker/`: Dockerfiles, healthchecks, scripts
- `kubernetes/`: Raw manifests, possibly with overlays per env
- `helm/`: Chart templates, values per env, release metadata
- `scripts/`: Automation for setup, deploy, migrate, rotate, verify
- `terraform/`: Modules and environment stacks, remote state config
- `database-structure-guide.md`: Cross-service DB practices
- `SETUP_COMPLETE.md`: Environment readiness notes/checklist

---

## FAQ

- How do I add a new service?
  1) Create `infrastructure/docker/<service>/Dockerfile`
  2) Add K8s manifests or a Helm chart under `kubernetes/` or `helm/<service>/`
  3) Provision dependencies via Terraform (e.g., database, cache)
  4) Add env-specific values and secrets references
  5) Update scripts/docs and CI to include the service

- Where do secrets live?
  Managed in a secret manager and referenced by K8s via controllers or encrypted manifests. Do not commit plaintext secrets.

- How are DB migrations run?
  As a Kubernetes Job (or initContainer) invoked during deploys; logs surface in CI and cluster events.

- How do we roll back?
  Helm rollback for workloads; Terraform requires explicit revert (never blindly `destroy`). For data, follow restore runbooks.

---

## Glossary

- IaC: Infrastructure as Code
- HPA: Horizontal Pod Autoscaler
- PDB: Pod Disruption Budget
- DR: Disaster Recovery
- RBAC: Role-Based Access Control
- SLO: Service Level Objective

---

## Appendices

### A) Command cheat sheets

Kubernetes:
```bash
kubectl get pods -n <ns>
kubectl logs deploy/<name> -n <ns>
kubectl describe ingress/<name> -n <ns>
```

Helm:
```bash
helm ls -n <ns>
helm history <release> -n <ns>
helm diff upgrade <release> <chart> -f values.yaml -f values-<env>.yaml
```

Terraform:
```bash
terraform fmt -recursive
terraform validate
terraform plan -out plan.out
terraform apply plan.out
```

Docker:
```bash
docker build -t raved/<service>:<tag> -f infrastructure/docker/<service>/Dockerfile .
docker run --rm -p 8080:8080 raved/<service>:<tag>
```

### B) Change management checklist

- [ ] Helm: bump app version, review `helm diff`, apply to dev first
- [ ] Terraform: review `plan`, ensure state is locked, apply in non-prod first
- [ ] Secrets: rotate if touching credentials; validate roll-forward plan
- [ ] Observability: confirm dashboards and alerts updated
- [ ] Run smoke tests and verify rollouts

### C) Security checklist

- [ ] Images scanned and signed
- [ ] Least-privilege IAM roles
- [ ] TLS everywhere; valid certs
- [ ] No plaintext secrets in repo
- [ ] Network policies in place for sensitive services

---

This guide provides a comprehensive map to the `infrastructure/` folder, its conventions, and operational practices. For deeper, environment-specific details and exact manifests, see `infrastructure/docs/`, the Helm values in `infrastructure/helm/`, the raw YAML in `infrastructure/kubernetes/`, and Terraform stacks in `infrastructure/terraform/envs/`.

---

## Environment-specific examples (dev, staging, prod)

### Helm values patterns

Base defaults (`values.yaml`):
```yaml
replicaCount: 2
image:
  repository: registry.example.com/raved/<service>
  tag: "1.0.0"
  pullPolicy: IfNotPresent
resources:
  requests:
    cpu: "200m"
    memory: "256Mi"
  limits:
    cpu: "500m"
    memory: "512Mi"
env:
  LOG_LEVEL: info
service:
  type: ClusterIP
ingress:
  enabled: true
  className: nginx
  hosts:
    - host: <service>.example.com
      paths:
        - path: /
          pathType: Prefix
```

Dev overrides (`values-dev.yaml`):
```yaml
replicaCount: 1
image:
  tag: "1.0.0-dev+<git-sha>"
env:
  LOG_LEVEL: debug
  FEATURE_FLAG_EXPERIMENTAL: "true"
ingress:
  hosts:
    - host: <service>.dev.example.com
resources:
  requests:
    cpu: "100m"
    memory: "128Mi"
  limits:
    cpu: "300m"
    memory: "256Mi"
```

Staging overrides (`values-staging.yaml`):
```yaml
replicaCount: 2
image:
  tag: "1.0.0-rc"
env:
  LOG_LEVEL: info
ingress:
  hosts:
    - host: <service>.staging.example.com
resources:
  requests:
    cpu: "250m"
    memory: "384Mi"
  limits:
    cpu: "750m"
    memory: "768Mi"
```

Prod overrides (`values-prod.yaml`):
```yaml
replicaCount: 4
image:
  tag: "1.0.0"
env:
  LOG_LEVEL: warn
ingress:
  hosts:
    - host: <service>.example.com
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"
  limits:
    cpu: "1500m"
    memory: "1536Mi"
hpa:
  enabled: true
  minReplicas: 4
  maxReplicas: 12
  targetCPUUtilizationPercentage: 65
```

Release commands:
```bash
helm upgrade --install <svc> infrastructure/helm/<svc> -f values.yaml -f values-dev.yaml -n dev
helm upgrade --install <svc> infrastructure/helm/<svc> -f values.yaml -f values-staging.yaml -n staging
helm upgrade --install <svc> infrastructure/helm/<svc> -f values.yaml -f values-prod.yaml -n prod
```

### Ingress hostnames by environment
```yaml
ingress:
  hosts:
    - host: api.dev.example.com   # dev
    - host: api.staging.example.com # staging
    - host: api.example.com       # prod
```

### Kubernetes Deployment snippet with probes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: <svc>
spec:
  replicas: 2
  selector:
    matchLabels: { app: <svc> }
  template:
    metadata:
      labels: { app: <svc> }
    spec:
      containers:
        - name: <svc>
          image: registry.example.com/raved/<svc>:1.0.0
          ports: [{ containerPort: 8080 }]
          readinessProbe:
            httpGet: { path: /actuator/health/readiness, port: 8080 }
            periodSeconds: 5
            failureThreshold: 6
          livenessProbe:
            httpGet: { path: /actuator/health/liveness, port: 8080 }
            periodSeconds: 10
          resources:
            requests: { cpu: "200m", memory: "256Mi" }
            limits:   { cpu: "500m", memory: "512Mi" }
```

---

## Terraform module skeletons

### Example: Postgres module

`modules/postgres/main.tf`:
```hcl
variable "name" {}
variable "instance_class" { default = "db.t3.medium" }
variable "engine_version" { default = "15" }
variable "storage_gb" { default = 50 }

resource "aws_db_instance" "this" {
  identifier              = var.name
  engine                  = "postgres"
  engine_version          = var.engine_version
  instance_class          = var.instance_class
  allocated_storage       = var.storage_gb
  db_subnet_group_name    = var.db_subnet_group
  vpc_security_group_ids  = var.sg_ids
  username                = var.username
  password                = var.password
  skip_final_snapshot     = true
  backup_retention_period = 7
  deletion_protection     = true
}

output "endpoint" { value = aws_db_instance.this.address }
```

`envs/dev/postgres.tf`:
```hcl
module "postgres_main" {
  source          = "../../modules/postgres"
  name            = "raved-dev-postgres"
  instance_class  = "db.t3.small"
  storage_gb      = 20
  db_subnet_group = module.vpc.db_subnet_group
  sg_ids          = [aws_security_group.db.id]
  username        = var.db_username
  password        = var.db_password
}

output "postgres_endpoint" { value = module.postgres_main.endpoint }
```

### Example: Redis module

`modules/redis/main.tf`:
```hcl
resource "aws_elasticache_replication_group" "this" {
  replication_group_id          = var.name
  engine                        = "redis"
  engine_version                = "7.0"
  node_type                     = var.node_type
  number_cache_clusters         = var.replicas
  automatic_failover_enabled    = true
  security_group_ids            = var.sg_ids
  subnet_group_name             = var.subnet_group
  at_rest_encryption_enabled    = true
  transit_encryption_enabled    = true
}

output "primary_endpoint" { value = aws_elasticache_replication_group.this.primary_endpoint_address }
```

---

## External secrets pattern (Kubernetes)

Using External Secrets Operator with cloud secret store:
```yaml
apiVersion: external-secrets.io/v1beta1
kind: ExternalSecret
metadata:
  name: <svc>-secrets
spec:
  refreshInterval: 1h
  secretStoreRef:
    name: aws-secrets-manager
    kind: ClusterSecretStore
  target:
    name: <svc>-secret
    creationPolicy: Owner
  data:
    - secretKey: DATABASE_URL
      remoteRef:
        key: /raved/<env>/<svc>/DATABASE_URL
    - secretKey: JWT_PUBLIC_KEY
      remoteRef:
        key: /raved/<env>/<svc>/JWT_PUBLIC_KEY
```

---

## Promotion workflow (dev → staging → prod)

1. Merge to main triggers build and image push
2. Helm deploy to dev with `values-dev.yaml`
3. Run smoke tests and integration tests
4. Promote same image tag to staging with `values-staging.yaml`; run load/regression tests
5. Manual approval; deploy to prod with `values-prod.yaml`
6. Post-deploy checks: dashboards green, error budget within SLO, no alert storms

Rollback guidelines:
- Use `helm rollback` to revert workloads
- Avoid Terraform rollbacks unless necessary; prefer forward fixes
- For data, follow restore runbook and announce read-only windows if needed

---

## Sample edge rate limiting (Ingress annotation)
```yaml
metadata:
  annotations:
    nginx.ingress.kubernetes.io/limit-rps: "5"
    nginx.ingress.kubernetes.io/limit-burst-multiplier: "3"
```

---

## Cloud provider–tailored examples (AWS)

### Terraform backend and providers
```hcl
terraform {
  backend "s3" {
    bucket         = "raved-terraform-state"
    key            = "envs/dev/infra.tfstate"
    region         = "us-east-1"
    dynamodb_table = "raved-terraform-locks"
    encrypt        = true
  }
  required_providers {
    aws = { source = "hashicorp/aws", version = "~> 5.0" }
  }
}

provider "aws" {
  region = var.aws_region
}
```

### EKS cluster (module usage sketch)
```hcl
module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  name   = "raved-dev"
  cidr   = "10.0.0.0/16"
  azs    = ["us-east-1a", "us-east-1b", "us-east-1c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]
}

module "eks" {
  source          = "terraform-aws-modules/eks/aws"
  cluster_name    = "raved-dev-eks"
  cluster_version = "1.29"
  subnet_ids      = module.vpc.private_subnets
  vpc_id          = module.vpc.vpc_id

  eks_managed_node_groups = {
    default = {
      desired_size = 2
      max_size     = 5
      min_size     = 2
      instance_types = ["m6i.large"]
    }
  }
}
```

### IRSA (IAM Roles for Service Accounts) example
```hcl
module "irsa_external_secrets" {
  source                           = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  role_name                        = "raved-dev-external-secrets"
  attach_external_secrets_policy   = true
  oidc_providers = {
    main = {
      provider_arn               = module.eks.oidc_provider_arn
      namespace_service_accounts = ["raved-dev:external-secrets"]
    }
  }
}
```

### Route53 + cert-manager (DNS-01)
```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-dns
spec:
  acme:
    email: ops@example.com
    server: https://acme-v02.api.letsencrypt.org/directory
    privateKeySecretRef:
      name: letsencrypt-dns
    solvers:
      - dns01:
          route53:
            region: us-east-1
            hostedZoneID: Z1234567890ABC
```

### S3 bucket for media with lifecycle
```hcl
resource "aws_s3_bucket" "media" {
  bucket = "raved-dev-media"
  lifecycle_rule {
    id      = "expire-objects"
    enabled = true
    noncurrent_version_expiration { days = 30 }
    expiration { days = 180 }
  }
}
```

### External Secrets (AWS Secrets Manager store)
```yaml
apiVersion: external-secrets.io/v1beta1
kind: ClusterSecretStore
metadata:
  name: aws-secrets-manager
spec:
  provider:
    aws:
      service: SecretsManager
      region: us-east-1
      auth:
        jwt:
          serviceAccountRef:
            name: external-secrets
            namespace: raved-dev
```

---

## Helmfile (multi-release orchestration)

`helmfile.yaml` example:
```yaml
repositories:
  - name: bitnami
    url: https://charts.bitnami.com/bitnami

environments:
  dev:
    values: [values-dev.yaml]
  staging:
    values: [values-staging.yaml]
  prod:
    values: [values-prod.yaml]

releases:
  - name: user-service
    namespace: raved-{{ .Environment.Name }}
    chart: ./infrastructure/helm/user-service
    values:
      - ./infrastructure/helm/user-service/values.yaml
      - ./infrastructure/helm/user-service/values-{{ .Environment.Name }}.yaml

  - name: content-service
    namespace: raved-{{ .Environment.Name }}
    chart: ./infrastructure/helm/content-service
    values:
      - ./infrastructure/helm/content-service/values.yaml
      - ./infrastructure/helm/content-service/values-{{ .Environment.Name }}.yaml
```

Commands:
```bash
helmfile -e dev apply
helmfile -e staging apply
helmfile -e prod apply
```

---

## Kustomize overlays

`kubernetes/base/deployment.yaml`:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 2
  template:
    spec:
      containers:
        - name: user-service
          image: registry.example.com/raved/user-service:1.0.0
```

`kubernetes/overlays/dev/kustomization.yaml`:
```yaml
resources:
  - ../../base/deployment.yaml
patches:
  - target: { kind: Deployment, name: user-service }
    patch: |
      - op: replace
        path: /spec/replicas
        value: 1
      - op: replace
        path: /spec/template/spec/containers/0/image
        value: registry.example.com/raved/user-service:1.0.0-dev
```

Apply:
```bash
kubectl apply -k kubernetes/overlays/dev
```

---

## CI/CD snippets (GitHub Actions)

### Build and push image
```yaml
name: build-and-push
on: [push]
jobs:
  docker:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: docker/setup-buildx-action@v3
      - uses: docker/login-action@v3
        with:
          registry: ${{ secrets.REGISTRY_HOST }}
          username: ${{ secrets.REGISTRY_USER }}
          password: ${{ secrets.REGISTRY_PASS }}
      - name: Build
        run: |
          docker build -t ${{ secrets.REGISTRY_HOST }}/raved/user-service:${{ github.sha }} -f infrastructure/docker/user-service/Dockerfile .
      - name: Push
        run: |
          docker push ${{ secrets.REGISTRY_HOST }}/raved/user-service:${{ github.sha }}
```

### Helm deploy (with kubeconfig secret)
```yaml
name: deploy-dev
on:
  workflow_dispatch:
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Setup kubectl
        uses: azure/setup-kubectl@v4
      - name: Kubeconfig
        run: |
          mkdir -p ~/.kube
          echo "${KUBECONFIG_CONTENT}" > ~/.kube/config
        env:
          KUBECONFIG_CONTENT: ${{ secrets.KUBECONFIG_DEV_BASE64 }}
      - name: Helm upgrade
        run: |
          helm upgrade --install user-service infrastructure/helm/user-service \
            -f infrastructure/helm/user-service/values.yaml \
            -f infrastructure/helm/user-service/values-dev.yaml \
            --set image.tag=${{ github.sha }} \
            -n raved-dev --create-namespace
```

### Terraform with OIDC (AWS)
```yaml
name: terraform-dev
on:
  pull_request:
  workflow_dispatch:
permissions:
  id-token: write
  contents: read
jobs:
  tf:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: arn:aws:iam::123456789012:role/raved-terraform-gha
          aws-region: us-east-1
      - name: Terraform Init/Plan
        run: |
          cd infrastructure/terraform/envs/dev
          terraform init -input=false
          terraform plan -input=false -out plan.out
      - name: Terraform Apply (manual)
        if: github.event_name == 'workflow_dispatch'
        run: |
          cd infrastructure/terraform/envs/dev
          terraform apply -input=false plan.out
```

### Image scanning (Trivy)
```yaml
      - uses: aquasecurity/trivy-action@0.20.0
        with:
          image-ref: ${{ secrets.REGISTRY_HOST }}/raved/user-service:${{ github.sha }}
          format: table
          exit-code: '1'
          ignore-unfixed: true
```

---

## Per-file implementation logic (what each file actually does)

### Top-level guides
- `infrastructure/SETUP_COMPLETE.md`
  - Operational report and quickstart. Enumerates running services, connection strings, and helper scripts. Used as a post-setup verification artifact.
- `infrastructure/database-structure-guide.md`
  - Prescribes DB layout per service (Postgres and Mongo). Provides verification queries and troubleshooting steps. Guides Flyway/Liquibase alignment without containing migrations.

### `docs/provisioning`
- `elasticsearch-indices.md`
  - Defines index templates and creation steps using ES HTTP API. Establishes shard/replica counts and mappings for fields used by content/analytics services.
- `kafka-topics.md`
  - Declares expected topics per service and two ways to provision them: Strimzi `KafkaTopic` CRDs or CLI. Partitions/replication are set to small defaults (3/1) for dev.
- `rabbitmq-topology.md`
  - Declares direct exchanges, durable queues, and bindings that match realtime-service routing keys. Provides a Kubernetes Job using `rabbitmq:3-management` to automate topology creation via `rabbitmqadmin`.

### Helm orchestration
- `helm/helmfile.yaml`
  - Central release driver. Pulls charts from local and remote repos. Uses `ENV` env var to select per-environment values. Each service release injects `workload` values via `readFile` + `fromYaml` of `helm/env/<env>/<svc>.yaml`. Installs infra dependencies (RabbitMQ, Kafka, Redis, PostgreSQL, monitoring, Elastic stack) and secrets controllers (External Secrets, Sealed Secrets) with environment-aware `installed` flags.
- `helm/<service>/Chart.yaml`, `values.yaml`
  - Defines a minimal per-service chart with default values. Intended to be parameterized entirely by the env files and reused across environments.
- `helm/env/<env>/*.yaml`
  - Per-release value files consumed by Helmfile into the `workload` key. Typically include image tag, replicas, resources, env, ingress hosts, HPA thresholds, and external dependency endpoints/secrets.
- `helm/raved-app/templates/*`
  - Opinionated base templates: `deployment.yaml` (uses `.Values.image`, probes, resources), `service.yaml` (ClusterIP), `ingress.yaml` (hosts + TLS), `hpa.yaml` (CPU/Memory autoscaling), `configmap.yaml` and `secret.yaml` (non-sensitive vs sensitive settings). `_helpers.tpl` centralizes naming labels/annotations.
- `helm/umbrella/templates/provisioning-*.yaml`
  - Bootstrap provisioning for dependencies (Kafka, Elasticsearch, RabbitMQ) as Jobs/CRDs so clusters become self-contained after Helm apply.

### Kubernetes manifests (raw YAMLs)
- `kubernetes/namespaces/*.yaml`
  - Creates dedicated namespaces per environment: `raved-dev`, `raved-staging`, `raved-prod`.
- `kubernetes/configmaps/*-config.yaml`
  - Plain ConfigMaps per service for non-sensitive config. Mounted as env or files into pods; safe to version.
- `kubernetes/secrets/*-secrets.yaml`
  - Per-service Secret placeholders for sensitive config. `external/` contains ExternalSecret examples to sync from a cloud secret store; `sealed/` shows SealedSecrets flow.
- `kubernetes/services/<svc>/deployment.yaml`
  - Deploys each microservice. Common logic: labels for selection and metrics, container image/tag, env from ConfigMaps/Secrets, readiness/liveness probes, resource requests/limits.
- `kubernetes/services/<svc>/service.yaml`
  - ClusterIP service targeting the deployment’s selector labels to expose port 8080 (or service-specific) internally.
- `kubernetes/services/<svc>/hpa.yaml`
  - HorizontalPodAutoscaler referencing the deployment; targets CPU (and optionally memory) utilization thresholds.
- `kubernetes/ingress/*`
  - `nginx-ingress-controller.yaml` installs/parametrizes the ingress controller. `cert-manager.yaml` configures certificate automation. `ssl-certificates.yaml` defines issuers/certificates per host.
- `kubernetes/databases/postgres/*`
  - StatefulSet with PVC (`pvc.yaml`) for durable storage, Service for cluster access, and ConfigMap for tuning. Similar patterns present for MongoDB and Redis directories.
- `kubernetes/messaging/*`
  - Kafka: StatefulSet for brokers + Service; configmap for broker configs. RabbitMQ: Deployment + Service; configmap for users/vhosts.
- `kubernetes/logging` and `kubernetes/monitoring`
  - EFK (Elasticsearch, Kibana, Logstash) manifests with proper services/statefulsets. Prometheus, Grafana, and Jaeger deployments and services for metrics/tracing.

### Implementation logic patterns across files
- Separation of concerns: charts/manifests don’t hardcode env specifics; Helmfile injects per-env values.
- Declarative provisioning: use CRDs (Strimzi), Jobs (rabbitmqadmin), and controllers (External Secrets, Sealed Secrets) to produce dependencies in-cluster.
- Security by design: secrets remain externalized; examples show the intended integration without embedding plaintext secrets in Git.
- Operability: probes, HPAs, labels/annotations, and monitoring/logging stacks are provided so services are observable and scalable by default.


