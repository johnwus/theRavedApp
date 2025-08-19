## E-commerce Service — Gaps Analysis and Remediation Plan

Compared `server/ecommerce-service` with `docs/services/ecommerce-service.md` across controllers, services, models, repos, mappers, configs, and migrations.

### 1) API Coverage vs Docs

- **Product catalog filtering incomplete**
  - Controller only implements basic price range and then falls back to `getProductsBySeller(null, ...)` with a TODO; category/condition/size/faculty filters missing.
    ```69:77:server/ecommerce-service/src/main/java/com/raved/ecommerce/controller/ProductController.java
    // TODO: Implement comprehensive filtering with category, condition, etc.
    products = productService.getProductsBySeller(null, pageable);
    ```
  - Remediation: Implement a specification-based filter (category, subcategory, condition, size, faculty, sort) and update service/repo accordingly.

- **Recommendations are stubbed**
  - Recommendations endpoint returns trending products with a TODO.
  - Remediation: Implement personalized recommendations (recent views, similar tags/brand, collaborative signals) and back it with a service/feature flags.

- **Authentication placeholders**
  - Multiple controllers use `Long userId = 1L` placeholders for JWT extraction.
    ```161:164:server/ecommerce-service/src/main/java/com/raved/ecommerce/controller/CartController.java
    Long userId = 1L; // Placeholder - should extract from JWT token
    ```
  - Remediation: Integrate security context/JWT parser to extract userId; ensure authorization checks (buyer/seller) on order/status endpoints.

### 2) Models/DTOs vs Docs

- Product model/DTOs not fully reviewed here, but docs require rich fields (shippingOptions, images arrays, engagement counters). Verify consistency of `Product`, `ProductImage`, mappers and add missing fields or computed views.

- Order endpoints match docs broadly; ensure `delivery_address` JSONB and status enums align with migrations.

- Payment endpoints present; verify mobile money provider fields and verification logic.

### 3) Repositories/Queries

- Ensure support for advanced product search (text search by name+description, tag filters) and trending logic (views/likes/bookmarks, recent). Add indexes as per docs if missing.

### 4) Migrations vs Entities

- Migrations exist V1–V14 with products, images, orders, order_items, payments, carts, saved/recently_viewed, seller_stats, and alter scripts. Validate entity fields against:
  - Engagement counters on products (view_count, like_count, bookmark_count) — V12 adds counters; service should update counters.
  - Cart totals fields (unit_price, total_price) — V8 defines; service must compute accurately.
  - Payments extra fields (provider, provider_transaction_id, payment_details JSONB) — V14 adds; ensure model maps and controller/service fill them.
  - Remediation: Cross-verify each JPA entity to Flyway columns; add missing columns or entity fields and update mappers.

### 5) Payments & Gateways

- Verification endpoint returns stub `verified: true`. No actual gateway verification.
  - Remediation: Implement gateway integration (mobile money providers, Stripe if applicable) in `PaymentServiceImpl`, with retry/backoff and idempotency; add signature/OTP verification.

- Rate limiting for payment attempts not implemented (docs: 5/min per order).
  - Remediation: Add Redis-backed limiter keyed by order ID.

### 6) Rate Limiting (global)

- Not present for product creation, cart ops, order creation, and search.
  - Remediation: Introduce a shared limiter utility; enforce per-user limits per docs, configurable in YAML.

### 7) Security & Authorization

- Controllers have `@PreAuthorize` but lack ownership checks (e.g., only buyer sees own orders; only seller updates their orders or features products).
  - Remediation: Implement method-level checks using order/product ownership, or AOP interceptors.

### 8) Events & Integrations

- Kafka publisher class exists (`EcommerceEventPublisher`), ensure it publishes on product created/sold, order created/updated, payment completed; add listeners for user/subscription/content events per docs.

### 9) Search & Discovery

- Search endpoint proxies to `productService.searchProducts`; confirm implementation uses indices and supports filters; add trending endpoint logic (recent popularity) if missing.

### 10) Seller Dashboard

- Docs specify seller dashboard endpoints/analytics; not present in controllers. Seller products/orders endpoints also absent.
  - Remediation: Add `/api/v1/seller/dashboard`, `/seller/products`, `/seller/orders`, `/seller/analytics` with aggregation queries against orders/products and `seller_stats` table.

### 11) Config & Profiles

- Profiles use `spring.profiles` property in blocks; prefer `spring.config.activate.on-profile` for Spring Boot 3+ consistency.
  - Remediation: Align profile activation style.

### 12) Validation

- Add validators for product name/description length, price constraints, Ghana phone formats, and order total consistency.

---

## Remediation Plan

1) Implement comprehensive product filtering and search; add repository specs and indices.
2) Replace JWT placeholders with real authentication extraction; add ownership authorization checks.
3) Implement personalized recommendations service.
4) Finish payment verification with providers and add rate limiting.
5) Add Redis-based rate limits for product creation, cart ops, order creation, and search.
6) Ensure entities align with Flyway migrations; add fields/migrations as needed (payments V14, products counters V12, cart item fields V13).
7) Add seller dashboard and seller-specific endpoints.
8) Expand Kafka events and add consumers for external updates.
9) Add validation constraints and request validators.
10) Tests: add integration tests for filters, orders, payments, and rate limits.








