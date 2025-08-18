# Master Infrastructure & Deployment Guide

This document is the single source of truth for completing and operating the infrastructure for TheRavedApp. It consolidates remaining tasks, options, workflows, and hands-on procedures.

Namespaces and environments
- dev: raved-dev
- staging: raved-staging
- prod: raved-prod

Key repos/paths
- Helmfile root: infrastructure/helm
- Per-service charts: infrastructure/helm/<service>
- Umbrella chart: infrastructure/helm/umbrella
- Raw K8s manifests: infrastructure/kubernetes
- CI workflows: .github/workflows
- Docs: docs/**

---
## 1) Infrastructure Completion Checklist

Current state (completed)
- Kubernetes manifests for all services
  - Deployments include envFrom (common + per-service ConfigMap + Secret), basic probes (/actuator/health), and resources
  - Supporting infra placeholders: Postgres, MongoDB, Redis, Kafka/RabbitMQ, ELK, Prometheus/Grafana/Jaeger, ingress
- Helm charts
  - Generic chart: raved-app
  - Per-service wrapper charts: api-gateway, user, content, social, realtime, ecommerce, notification, analytics, events, subscription, eureka, config
  - Umbrella chart composed of all services
  - Environment values: infrastructure/helm/env/{dev,staging,prod}
- Helmfile orchestration
  - Per-service releases using their charts, plus dependencies (Bitnami, Elastic)
  - Repos added for external-secrets and sealed-secrets
- CI workflows
  - infrastructure-validate.yml (helmfile template dev/staging/prod)
  - infrastructure-lint.yml (helm lint per-service + base chart)
- Credentials
  - Database username/password configured via Secrets: raved_admin / theRAVEDapp#123
- Secrets scaffolding options
  - Sealed Secrets and External Secrets runbooks and basic wiring added

Pending/next (to do)
- Decide readiness/liveness actuator endpoints per service
  - See docs/deployment/probes-and-healthchecks-todo.md
- Provide real cloud endpoints for staging/prod
  - See docs/deployment/cloud-endpoints-todo.md
- Choose secrets backend for staging/prod
  - Sealed Secrets (GitOps) or External Secrets (cloud secret stores)
- Expand env values for staging/prod
  - Fill DB, Redis, Kafka, ES, MongoDB, SMTP, Twilio
- Optional: policy/validation additions
  - kubeconform/kubeval, opa/gatekeeper, Snyk/Trivy IaC

---
## 2) Secrets Management Implementation Guide

Two supported paths. Use one (or mix):

A) Sealed Secrets (GitOps-friendly)
1. Install controller (once per cluster):
   - helm repo add sealed-secrets https://bitnami-labs.github.io/sealed-secrets
   - kubectl create namespace kube-system (if not exists)
   - helm upgrade --install sealed-secrets sealed-secrets/sealed-secrets -n kube-system
2. Create an unsealed Secret manifest locally (do not apply):
   - kubectl -n raved-staging create secret generic user-service-secrets \
     --from-literal=DATABASE_USERNAME=raved_admin \
     --from-literal=DATABASE_PASSWORD=theRAVEDapp#123 \
     --dry-run=client -o yaml > secret.yaml
3. Seal the secret using cluster’s controller key:
   - kubeseal -n raved-staging --format yaml < secret.yaml > infrastructure/kubernetes/secrets/sealed/user-service-sealed.yaml
4. Commit the sealed file and apply in cluster:
   - kubectl apply -f infrastructure/kubernetes/secrets/sealed/user-service-sealed.yaml
5. Repeat for other services/secrets; rotate by re-sealing with new values.

B) External Secrets Operator (ESO) (cloud secret stores)
1. Install ESO (staging/prod):
   - helm repo add external-secrets https://charts.external-secrets.io
   - kubectl create namespace external-secrets (if not exists)
   - helm upgrade --install external-secrets external-secrets/external-secrets \
     -n external-secrets --set installCRDs=true
   - Or via helmfile: infrastructure/helm/helmfile.yaml (installed by default when ENV != dev)
2. Create a SecretStore/ClusterSecretStore for your cloud (AWS/GCP/Azure/Vault):
   - See docs/security/external-secrets-runbook.md for examples
3. Create ExternalSecret resources per service
   - Example provided: infrastructure/kubernetes/secrets/external/user-service-db.externalsecret.yaml
4. ESO reconciles and creates native Kubernetes Secrets for the services

Notes
- Prefer Secrets (or sealed/external) over ConfigMaps for sensitive values
- Namespaces must match the target environment (e.g., raved-staging)

---
## 3) Cloud Endpoints Integration Plan

When real managed endpoints are available, update:
- Helm env values: infrastructure/helm/env/{staging,prod}/*-service.yaml
  - Example keys per service:
    - SQL services: *SERVICE*_DB_URL
    - Mongo services: MONGODB_URI, MONGODB_DATABASE or SOCIAL_SERVICE_MONGODB_*
    - Redis: REDIS_HOST, REDIS_PORT (6379 typical)
    - Kafka: KAFKA_BOOTSTRAP_SERVERS or KAFKA_SERVERS
    - Elasticsearch: ELASTICSEARCH_URIS (+ optional basic auth)
    - SMTP: MAIL_HOST, MAIL_PORT, MAIL_USERNAME, MAIL_PASSWORD, MAIL_FROM
    - Twilio: TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN, TWILIO_PHONE_NUMBER
- Kubernetes Secrets
  - Update or source via Sealed Secrets/External Secrets

Procedure
1. Collect endpoints and credentials per environment
2. Update env files under infrastructure/helm/env/staging and /prod
3. Update Secrets via chosen mechanism (sealed/external)
4. Render templates to validate:
   - cd infrastructure/helm; ENV=staging helmfile -e staging template
5. Deploy to staging for validation; promote to prod with the same keys/structure

Reference: docs/deployment/cloud-endpoints-todo.md

---
## 4) Deployment Procedures

A) Local dev cluster using the umbrella chart
- Pre-reqs: kubectl, Helm 3, optional helmfile; a cluster (kind/minikube)
- Option 1: Helmfile
  - cd infrastructure/helm
  - ENV=dev helmfile -e dev template
  - ENV=dev helmfile -e dev sync
- Option 2: Helm directly (umbrella)
  - cd infrastructure/helm/umbrella
  - helm dependency build
  - helm upgrade --install raved ./ --namespace raved-dev --create-namespace

B) Staging/Production deployments
- Helmfile driven (recommended):
  - cd infrastructure/helm
  - ENV=staging helmfile -e staging sync
  - ENV=prod helmfile -e prod sync
- Ensure secrets controllers are installed (ESO or Sealed Secrets)
- Ensure env files in infrastructure/helm/env/{staging,prod} are filled with correct endpoints

C) CI/CD integration points
- Build/test/publish images: existing workflows (docker-build-push reusable workflow)
- Infra checks on PRs:
  - Infrastructure Validate: helmfile template dev/staging/prod
  - Infrastructure Lint: helm lint base + per-service charts
- Optional future checks:
  - kubeconform/kubeval, policy engines, image scanning

D) Monitoring & validation steps
- After sync, verify core services:
  - kubectl -n raved-<env> get pods,svc,ingress
  - Check /actuator/health endpoints
  - Prom/Grafana/ELK/Jaeger UIs (if enabled) are reachable
- Logs:
  - kubectl -n raved-<env> logs deploy/<service> -f

---
## 5) Sample Manifests

A) Sealed Secret (template — replace encryptedData via kubeseal)
Path: infrastructure/kubernetes/secrets/sealed/user-service-sealed.sample.yaml
```yaml
apiVersion: bitnami.com/v1alpha1
kind: SealedSecret
metadata:
  name: user-service-secrets
  namespace: raved-staging
spec:
  encryptedData:
    DATABASE_USERNAME: ENC[REPLACE_WITH_KUBESEAL_OUTPUT]
    DATABASE_PASSWORD: ENC[REPLACE_WITH_KUBESEAL_OUTPUT]
  template:
    metadata:
      name: user-service-secrets
      namespace: raved-staging
    type: Opaque
```

B) External Secrets (template)
ClusterSecretStore sample
Path: infrastructure/kubernetes/secrets/external/clustersecretstore.sample.yaml
```yaml
apiVersion: external-secrets.io/v1beta1
kind: ClusterSecretStore
metadata:
  name: aws-secrets
spec:
  provider:
    aws:
      service: SecretsManager
      region: <your-region>
      auth:
        jwt:
          serviceAccountRef:
            name: external-secrets
            namespace: external-secrets
```

ExternalSecret for user-service
Path: infrastructure/kubernetes/secrets/external/user-service-db.externalsecret.yaml
```yaml
apiVersion: external-secrets.io/v1beta1
kind: ExternalSecret
metadata:
  name: user-service-db
  namespace: raved-staging
spec:
  refreshInterval: 1h
  secretStoreRef:
    kind: ClusterSecretStore
    name: aws-secrets
  target:
    name: user-service-secrets
    creationPolicy: Owner
  data:
    - secretKey: DATABASE_USERNAME
      remoteRef:
        key: raved/user-service
        property: username
    - secretKey: DATABASE_PASSWORD
      remoteRef:
        key: raved/user-service
        property: password
```

---
## Appendix: Useful Commands

General
- kubectl -n raved-<env> get pods,svc,ingress,hpa
- kubectl -n raved-<env> logs deploy/<service> -f

Helm/Helmfile
- cd infrastructure/helm; ENV=dev helmfile -e dev template
- cd infrastructure/helm; ENV=staging helmfile -e staging sync

CI workflows
- .github/workflows/infrastructure-validate.yml
- .github/workflows/infrastructure-lint.yml

Actuator endpoints decision
- docs/deployment/probes-and-healthchecks-todo.md

Cloud endpoints inputs
- docs/deployment/cloud-endpoints-todo.md

Secrets runbooks
- docs/security/sealed-secrets-runbook.md
- docs/security/external-secrets-runbook.md

