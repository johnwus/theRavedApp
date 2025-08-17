# Sealed Secrets Runbook

Install controller (once per cluster):
- kubectl create namespace kube-system (if not exists)
- helm repo add sealed-secrets https://bitnami-labs.github.io/sealed-secrets
- helm upgrade --install sealed-secrets sealed-secrets/sealed-secrets -n kube-system

Seal a secret:
1. Create a Secret manifest (do not apply):
   kubectl -n raved-staging create secret generic user-service-secrets \
     --from-literal=DATABASE_USERNAME=raved_admin \
     --from-literal=DATABASE_PASSWORD=theRAVEDapp#123 \
     --dry-run=client -o yaml > secret.yaml
2. kubeseal -n raved-staging --format yaml < secret.yaml > infrastructure/kubernetes/secrets/sealed/user-service-sealed.yaml
3. Commit the sealed file, then apply:
   kubectl apply -f infrastructure/kubernetes/secrets/sealed/user-service-sealed.yaml

Rotate:
- Re-run the steps with updated values. Sealed secrets are cluster/namespace specific.

