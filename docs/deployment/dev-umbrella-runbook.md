# Dev Umbrella Deployment Runbook

This describes how to render and (optionally) install the umbrella chart for a local dev cluster.

Prereqs
- kubectl configured to your dev cluster (kind/minikube)
- Helm 3 installed
- Optional: Helmfile if using helmfile workflows

Option A: Helmfile
- cd infrastructure/helm
- Set environment variable: ENV=dev
- Render: helmfile -e dev template
- Install: helmfile -e dev sync

Option B: Helm directly (umbrella)
- cd infrastructure/helm/umbrella
- helm dependency build
- Install to raved-dev namespace:
  helm upgrade --install raved ./ --namespace raved-dev --create-namespace

Notes
- The umbrella includes per-service charts that wrap the generic raved-app chart.
- For dev, dependencies like Postgres/Redis/Kafka/ES may be provided via Bitnami charts in helmfile.
- Secrets in this repo are examples; replace with Sealed/External Secrets for real environments.

