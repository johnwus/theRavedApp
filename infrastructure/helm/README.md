# Raved Helm Deployment

This Helm setup deploys all services using a single reusable chart. A helmfile orchestrates per-environment deployments and namespace creation.

## Structure
- Chart: `raved-app/`
- Env values: `env/{dev,staging,prod}/*.yaml`
- Helmfile: `helmfile.yaml` (uses ENV variable)

## Usage
Set the environment and apply:

```bash
# Windows PowerShell
$env:ENV = "dev"; helmfile -f infrastructure/helm/helmfile.yaml apply

# Linux/macOS
ENV=dev helmfile -f infrastructure/helm/helmfile.yaml apply
```

Available ENV values: `dev`, `staging`, `prod`.

Override images, env vars, and ingress in the env values files. HPA is enabled when `autoscaling.enabled: true`.
