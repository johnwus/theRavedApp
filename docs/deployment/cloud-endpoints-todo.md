# Cloud Endpoints — TODO

Goal: Populate staging/prod values with real managed service endpoints when available.

Pending inputs
- PostgreSQL service URLs for each DB-backed service
- Kafka bootstrap servers
- Redis host/port
- Elasticsearch URIs
- MongoDB cloud connection strings
- SMTP service (host/port/user)
- Twilio (account SID/auth token/phone)

Where to set
- Helmfile env values: infrastructure/helm/env/{staging,prod}/*-service.yaml
- Per-service ConfigMaps/Secrets in Kubernetes: infrastructure/kubernetes/configmaps/*, infrastructure/kubernetes/secrets/*
- Optionally in Sealed Secrets or External Secrets Operator

Action items
- Provide the real endpoints/credentials
- Replace placeholder in-cluster service names/credentials
- Add CI validation for helmfile with environment-specific values if needed

Notes
- Keep secrets out of ConfigMaps; store sensitive data in Kubernetes Secrets (or sealed/external)
- Revisit resources/HPAs in staging/prod once real usage is observed

