# Development Onboarding

This guide helps new contributors get productive quickly with TheRavedApp infrastructure.

## Prerequisites
- Git, Node.js, Java 21, Docker
- kubectl, Helm 3, optional helmfile
- A local Kubernetes cluster (kind/minikube)

## Quick Start (Dev cluster)
1. Start your cluster (kind/minikube)
2. Render infra to validate:
   - cd infrastructure/helm
   - ENV=dev helmfile -e dev template
3. Deploy services and dependencies:
   - ENV=dev helmfile -e dev sync
4. Confirm:
   - kubectl -n raved-dev get pods,svc,ingress

## Secrets in Dev
- This repo uses basic Kubernetes Secrets for dev convenience
- See docs/security/secrets-management.md for production-ready options

## Where to change config
- Helmfile root: infrastructure/helm/helmfile.yaml
- Per-service charts: infrastructure/helm/<service>
- Env values: infrastructure/helm/env/{dev,staging,prod}
- Raw K8s manifests: infrastructure/kubernetes

## Useful docs
- Master infra guide: docs/deployment/master-infra-deployment-guide.md
- Probes decision: docs/deployment/probes-and-healthchecks-todo.md
- Cloud endpoints TODO: docs/deployment/cloud-endpoints-todo.md
- Sealed/External Secrets runbooks: docs/security/*.md

