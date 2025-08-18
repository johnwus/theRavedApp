# Secrets Management Options

This repo currently uses standard Kubernetes Secrets for simplicity in dev. For staging/production, prefer one of:

- Bitnami Sealed Secrets (encrypts secrets into SealedSecret resources stored in git)
- External Secrets Operator (syncs from secret managers like AWS Secrets Manager, GCP Secret Manager, Vault)

## Option A: Sealed Secrets (GitOps friendly)

Prereqs:
- Install controller in the cluster (see upstream docs)
- Install kubeseal CLI locally

Workflow:
1. Create a Kubernetes Secret manifest (do not apply):
   kubectl create secret generic my-app --from-literal=DATABASE_PASSWORD=theRAVEDapp#123 --dry-run=client -o yaml > secret.yaml
2. Seal it:
   kubeseal --format yaml < secret.yaml > sealedsecret.yaml
3. Commit sealedsecret.yaml to this repo under infrastructure/kubernetes/secrets/sealed/
4. Apply sealedsecret.yaml; the controller will materialize a Secret at runtime.

Notes:
- Sealed secrets are namespace- and controller-key bound; re-seal if you change namespace/cluster.

## Option B: External Secrets Operator (cloud secret stores)

Use ESO to map roles and secret store references to Kubernetes Secrets.

Workflow (high level):
- Install ESO via Helm
- Configure SecretStore or ClusterSecretStore
- Create ExternalSecret resources referencing keys in your cloud secret manager
- ESO reconciles to create/update Kubernetes Secrets

## Recommendations
- Dev: Kubernetes Secrets (ok), or Sealed Secrets for parity
- Staging/Prod: External Secrets with your cloud secret manager; or Sealed Secrets if GitOps is preferred

See also:
- docs/deployment/cloud-endpoints-todo.md

