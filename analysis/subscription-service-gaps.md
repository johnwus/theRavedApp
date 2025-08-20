## Subscription Service — Gaps Analysis and Remediation Plan

Compared `server/subscription-service` with `docs/services/subscription-service.md` across controllers, services, models, repositories, mappers, configs, and migrations.

### 1) API Coverage vs Docs

- Controllers present: `SubscriptionPlanController`, `UserSubscriptionController`, `PaymentController` (process subscription payment). Verify paths match `/api/v1/subscriptions/...` per docs; add missing endpoints:
  - Subscribe (`POST /subscriptions/subscribe`), current subscription, cancel/reactivate, feature listing and feature check, billing history/upcoming/retry, trial start/status. If any are missing or under different paths, add/align.

### 2) Data Models & Migrations vs Docs

- Migrations only include `subscription_plans` and `user_subscriptions`. Docs require additional tables: `subscriptions` (detailed), `billing_history`, and `feature_usage` with comprehensive fields.
  - Remediation: Add migrations for `subscriptions`, `billing_history`, and `feature_usage` per docs schema, with indexes and constraints.

- `SubscriptionPlan` entity should include JSONB for features/limits, popularity, display order, active flag; validate alignment with V1 migration.

- `UserSubscription` likely minimal; align to docs `subscriptions` schema: status, periods, trial, next billing, cancellation flags/reasons, provider ids, payment method/provider, amount/currency.

### 3) Services & Business Logic

- Implement subscription lifecycle: subscribe (with optional trial), activate on payment confirmation, handle cancel/reactivate, compute next billing date, and manage grace period and past_due.
- Feature access control: compute available features and limits from plan + usage; implement fast `hasAccess` check with Redis caching and fallback to DB; add `feature_usage` write path.
- Billing: generate invoices (numbers/URLs), persist `billing_history`, support retry/backoff per config; integrate with Stripe/mobile money providers.
- Trial management: enforce `max-trials-per-user`, set trial period based on plan or defaults, expose status.

### 4) Payment Integration

- `PaymentController` and `PaymentServiceImpl` exist; verify they create provider intents, handle webhooks/confirmations, and update `subscriptions` and `billing_history`. Add idempotency keys and retry logic.

### 5) Controllers & DTOs

- Ensure DTOs match docs payloads (subscribe with `planId`, `paymentMethod`, `paymentDetails`, `startTrial`).
- Add responses including subscription snapshot (periods, nextBillingDate, features) and payment intent when applicable.

### 6) Configuration

- YAML should include provider keys (Stripe/webhooks), subscription policy (trial defaults, retry attempts/delays, grace period), and feature limits. Add Redis config if using caching.

### 7) Validation & Security

- Validate plan existence, user eligibility for trial, payment method support, and transitions (e.g., cannot reactivate active). Add method-level security and ownership checks.

### 8) Events & Analytics

- Publish events on subscription created/updated/cancelled, billing paid/failed; consume analytics if needed. Update Analytics Service with revenue and churn metrics.

### 9) Tests

- Add tests for subscribe/cancel/reactivate flows, feature check, billing retry, and trial rules. Include controller contract tests and migration/entity alignment tests.

---

## Remediation Plan

1) Add missing DB tables (`subscriptions`, `billing_history`, `feature_usage`) and align entities.
2) Implement full subscription lifecycle endpoints and services per docs.
3) Integrate payment providers with intents, webhooks, idempotency, and retries.
4) Implement feature access checks and usage tracking with caching.
5) Implement billing history/upcoming/retry and invoicing.
6) Enforce trial policy and add trial endpoints.
7) Add validations, security, and tests.








