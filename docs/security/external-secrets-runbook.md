# External Secrets Operator Runbook

Install ESO (staging/prod):
- kubectl create namespace external-secrets (if not exists)
- helm repo add external-secrets https://charts.external-secrets.io
- helm upgrade --install external-secrets external-secrets/external-secrets -n external-secrets --set installCRDs=true

Configure secret store (example using AWS Secrets Manager):

<example>
apiVersion: external-secrets.io/v1beta1
kind: ClusterSecretStore
metadata:
  name: aws-secrets
spec:
  provider:
    aws:
      service: SecretsManager
      region: eu-west-1
      auth:
        jwt:
          serviceAccountRef:
            name: external-secrets
            namespace: external-secrets
</example>

Create an ExternalSecret:

<example>
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
</example>

Notes:
- Ensure the service account for ESO has permissions to read secrets from the external store.
- For GCP/Azure/Vault, replace the provider section accordingly.

