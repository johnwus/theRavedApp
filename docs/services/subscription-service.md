# Subscription Service Documentation

## 1. Service Overview

### Primary Purpose
The Subscription Service manages premium membership subscriptions, billing cycles, payment processing, and feature access control for TheRavedApp's premium features.

### Core Responsibilities
- **Subscription Management**: Premium membership lifecycle management
- **Billing & Payments**: Recurring payment processing and billing cycles
- **Feature Access Control**: Premium feature authorization and gating
- **Trial Management**: Free trial periods and conversion tracking
- **Revenue Analytics**: Subscription revenue and churn analysis
- **Plan Management**: Subscription tiers and pricing management

### Service Boundaries
- **Owns**: Subscriptions, billing records, payment history, feature access
- **Does NOT Own**: User profiles, payment methods, business transactions
- **Interfaces With**: User Service for authentication, E-commerce Service for payments, Analytics Service for metrics

### Business Domain Ownership
- Premium subscription lifecycle and billing
- Feature access control and authorization
- Revenue tracking and subscription analytics
- Trial and conversion management

### Performance Requirements
- **Subscription Queries**: < 200ms for feature access checks
- **Payment Processing**: < 10s for subscription payments
- **Feature Authorization**: < 100ms for premium feature checks
- **Billing Operations**: Process 1,000+ subscriptions daily
- **Trial Conversions**: < 1s for trial activation

## 2. API Specification

### Subscription Management

#### GET /api/v1/subscriptions/plans
**Purpose**: Get available subscription plans
**Response**:
```json
{
  "success": true,
  "plans": [
    {
      "id": "premium_monthly",
      "name": "Premium Monthly",
      "description": "Access to all premium features with monthly billing",
      "price": 15.00,
      "currency": "GHS",
      "billingCycle": "monthly",
      "trialDays": 7,
      "features": [
        "premium_themes",
        "advanced_analytics",
        "seller_dashboard",
        "priority_support",
        "ad_free_experience",
        "unlimited_posts"
      ],
      "popular": true
    },
    {
      "id": "premium_yearly",
      "name": "Premium Yearly",
      "description": "Access to all premium features with yearly billing (2 months free)",
      "price": 150.00,
      "currency": "GHS",
      "billingCycle": "yearly",
      "trialDays": 14,
      "savings": 30.00,
      "features": [
        "premium_themes",
        "advanced_analytics",
        "seller_dashboard",
        "priority_support",
        "ad_free_experience",
        "unlimited_posts",
        "exclusive_events"
      ]
    }
  ]
}
```

#### POST /api/v1/subscriptions/subscribe
**Purpose**: Subscribe to premium plan
```json
{
  "planId": "premium_monthly",
  "paymentMethod": "mobile_money",
  "paymentDetails": {
    "phoneNumber": "0241234567",
    "provider": "mtn"
  },
  "startTrial": true
}
```

**Response**:
```json
{
  "success": true,
  "subscription": {
    "id": "sub_123",
    "userId": "user_456",
    "planId": "premium_monthly",
    "status": "trial",
    "currentPeriod": {
      "start": "2024-08-14T10:30:00Z",
      "end": "2024-08-21T10:30:00Z"
    },
    "trialEnd": "2024-08-21T10:30:00Z",
    "nextBillingDate": "2024-08-21T10:30:00Z",
    "amount": 15.00,
    "currency": "GHS",
    "features": [
      "premium_themes",
      "advanced_analytics",
      "seller_dashboard",
      "priority_support",
      "ad_free_experience",
      "unlimited_posts"
    ],
    "createdAt": "2024-08-14T10:30:00Z"
  },
  "paymentIntent": {
    "id": "pi_789",
    "status": "requires_confirmation",
    "clientSecret": "pi_789_secret_abc123"
  }
}
```

#### GET /api/v1/subscriptions/current
**Purpose**: Get user's current subscription
**Response**:
```json
{
  "success": true,
  "subscription": {
    "id": "sub_123",
    "userId": "user_456",
    "plan": {
      "id": "premium_monthly",
      "name": "Premium Monthly",
      "price": 15.00,
      "currency": "GHS"
    },
    "status": "active",
    "currentPeriod": {
      "start": "2024-08-14T10:30:00Z",
      "end": "2024-09-14T10:30:00Z"
    },
    "nextBillingDate": "2024-09-14T10:30:00Z",
    "cancelAtPeriodEnd": false,
    "features": [
      "premium_themes",
      "advanced_analytics",
      "seller_dashboard",
      "priority_support",
      "ad_free_experience",
      "unlimited_posts"
    ],
    "usage": {
      "postsThisMonth": 45,
      "analyticsViews": 123,
      "themeChanges": 3
    },
    "createdAt": "2024-08-14T10:30:00Z",
    "lastPayment": {
      "amount": 15.00,
      "date": "2024-08-14T10:30:00Z",
      "status": "completed"
    }
  }
}
```

#### PUT /api/v1/subscriptions/cancel
**Purpose**: Cancel subscription
```json
{
  "cancelAtPeriodEnd": true,
  "reason": "too_expensive",
  "feedback": "Great service but too costly for a student"
}
```

#### PUT /api/v1/subscriptions/reactivate
**Purpose**: Reactivate cancelled subscription

### Feature Access Control

#### GET /api/v1/subscriptions/features
**Purpose**: Get user's available features
**Response**:
```json
{
  "success": true,
  "features": {
    "premium_themes": {
      "available": true,
      "limit": null,
      "used": 3
    },
    "advanced_analytics": {
      "available": true,
      "limit": null,
      "used": 123
    },
    "seller_dashboard": {
      "available": true,
      "limit": null,
      "used": 1
    },
    "unlimited_posts": {
      "available": true,
      "limit": null,
      "used": 45
    },
    "priority_support": {
      "available": true,
      "limit": null,
      "used": 0
    }
  },
  "subscription": {
    "status": "active",
    "plan": "premium_monthly",
    "expiresAt": "2024-09-14T10:30:00Z"
  }
}
```

#### POST /api/v1/subscriptions/features/check
**Purpose**: Check if user has access to specific feature
```json
{
  "feature": "seller_dashboard"
}
```

**Response**:
```json
{
  "success": true,
  "hasAccess": true,
  "feature": "seller_dashboard",
  "subscription": {
    "status": "active",
    "expiresAt": "2024-09-14T10:30:00Z"
  }
}
```

### Billing & Payment History

#### GET /api/v1/subscriptions/billing/history
**Purpose**: Get billing history
**Response**:
```json
{
  "success": true,
  "billingHistory": [
    {
      "id": "bill_123",
      "subscriptionId": "sub_123",
      "amount": 15.00,
      "currency": "GHS",
      "status": "paid",
      "billingDate": "2024-08-14T10:30:00Z",
      "paidDate": "2024-08-14T10:32:00Z",
      "paymentMethod": "mobile_money",
      "invoice": {
        "number": "INV-2024-001234",
        "downloadUrl": "https://cdn.raved.app/invoices/INV-2024-001234.pdf"
      },
      "period": {
        "start": "2024-08-14T10:30:00Z",
        "end": "2024-09-14T10:30:00Z"
      }
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "hasNext": false
  }
}
```

#### GET /api/v1/subscriptions/billing/upcoming
**Purpose**: Get upcoming billing information

#### POST /api/v1/subscriptions/billing/retry
**Purpose**: Retry failed payment

### Trial Management

#### POST /api/v1/subscriptions/trial/start
**Purpose**: Start free trial
```json
{
  "planId": "premium_monthly"
}
```

#### GET /api/v1/subscriptions/trial/status
**Purpose**: Get trial status
**Response**:
```json
{
  "success": true,
  "trial": {
    "isActive": true,
    "planId": "premium_monthly",
    "startDate": "2024-08-14T10:30:00Z",
    "endDate": "2024-08-21T10:30:00Z",
    "daysRemaining": 7,
    "features": [
      "premium_themes",
      "advanced_analytics",
      "seller_dashboard"
    ],
    "usage": {
      "postsCreated": 12,
      "analyticsViews": 45,
      "themeChanges": 2
    }
  }
}
```

## 3. Data Models & Database Schema

### Database Choice: PostgreSQL
**Rationale**: Subscription data requires ACID compliance for billing integrity, complex queries for revenue analytics, and strong consistency for payment processing.

### Core Tables

#### subscription_plans
```sql
CREATE TABLE subscription_plans (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',
    billing_cycle VARCHAR(20) NOT NULL, -- 'monthly', 'yearly'
    trial_days INTEGER DEFAULT 0,
    
    -- Features
    features JSONB NOT NULL DEFAULT '[]',
    feature_limits JSONB DEFAULT '{}',
    
    -- Display
    is_popular BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_billing_cycle CHECK (billing_cycle IN ('monthly', 'yearly')),
    CONSTRAINT valid_price CHECK (price >= 0)
);

CREATE INDEX idx_plans_active ON subscription_plans(is_active, display_order);
```

#### subscriptions
```sql
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL, -- Reference to User Service
    plan_id VARCHAR(50) REFERENCES subscription_plans(id),
    
    -- Subscription status
    status VARCHAR(50) NOT NULL DEFAULT 'trial', -- 'trial', 'active', 'cancelled', 'expired', 'past_due'
    
    -- Billing periods
    current_period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    current_period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    trial_start TIMESTAMP WITH TIME ZONE,
    trial_end TIMESTAMP WITH TIME ZONE,
    
    -- Billing
    next_billing_date TIMESTAMP WITH TIME ZONE,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',
    
    -- Cancellation
    cancel_at_period_end BOOLEAN DEFAULT FALSE,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    cancellation_reason VARCHAR(100),
    cancellation_feedback TEXT,
    
    -- Payment
    payment_method VARCHAR(50),
    payment_provider VARCHAR(50),
    provider_subscription_id VARCHAR(255),
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_status CHECK (status IN ('trial', 'active', 'cancelled', 'expired', 'past_due')),
    CONSTRAINT valid_periods CHECK (current_period_end > current_period_start)
);

CREATE INDEX idx_subscriptions_user ON subscriptions(user_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
CREATE INDEX idx_subscriptions_billing_date ON subscriptions(next_billing_date);
CREATE INDEX idx_subscriptions_trial_end ON subscriptions(trial_end);
```

#### billing_history
```sql
CREATE TABLE billing_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subscription_id UUID REFERENCES subscriptions(id) ON DELETE CASCADE,
    user_id UUID NOT NULL, -- Reference to User Service
    
    -- Billing details
    invoice_number VARCHAR(100) UNIQUE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',
    
    -- Status
    status VARCHAR(50) NOT NULL DEFAULT 'pending', -- 'pending', 'paid', 'failed', 'refunded'
    
    -- Dates
    billing_date TIMESTAMP WITH TIME ZONE NOT NULL,
    due_date TIMESTAMP WITH TIME ZONE NOT NULL,
    paid_date TIMESTAMP WITH TIME ZONE,
    
    -- Payment details
    payment_method VARCHAR(50),
    payment_provider VARCHAR(50),
    provider_payment_id VARCHAR(255),
    
    -- Period covered
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    
    -- Invoice
    invoice_url TEXT,
    
    -- Failure details
    failure_reason TEXT,
    retry_count INTEGER DEFAULT 0,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    CONSTRAINT valid_status CHECK (status IN ('pending', 'paid', 'failed', 'refunded')),
    CONSTRAINT valid_amount CHECK (amount > 0)
);

CREATE INDEX idx_billing_subscription ON billing_history(subscription_id, billing_date);
CREATE INDEX idx_billing_user ON billing_history(user_id, billing_date);
CREATE INDEX idx_billing_status ON billing_history(status, due_date);
CREATE INDEX idx_billing_invoice ON billing_history(invoice_number);
```

#### feature_usage
```sql
CREATE TABLE feature_usage (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL, -- Reference to User Service
    subscription_id UUID REFERENCES subscriptions(id),
    feature_name VARCHAR(100) NOT NULL,
    
    -- Usage tracking
    usage_count INTEGER DEFAULT 0,
    usage_date DATE NOT NULL,
    
    -- Metadata
    metadata JSONB DEFAULT '{}',
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(user_id, feature_name, usage_date)
);

CREATE INDEX idx_feature_usage_user ON feature_usage(user_id, usage_date);
CREATE INDEX idx_feature_usage_subscription ON feature_usage(subscription_id, usage_date);
CREATE INDEX idx_feature_usage_feature ON feature_usage(feature_name, usage_date);
```

## 4. Technology Stack & Infrastructure

### Framework & Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>com.stripe</groupId>
        <artifactId>stripe-java</artifactId>
    </dependency>
</dependencies>
```

### Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/raved_subscription_db
    username: raved_admin
    password: theRAVEDapp#123

# Payment provider configuration
stripe:
  api-key: ${STRIPE_SECRET_KEY}
  webhook-secret: ${STRIPE_WEBHOOK_SECRET}

# Subscription configuration
subscription:
  trial:
    default-days: 7
    max-trials-per-user: 1
  
  billing:
    retry-attempts: 3
    retry-delay-days: [1, 3, 7]
    grace-period-days: 7
  
  features:
    premium_themes: { limit: null }
    advanced_analytics: { limit: null }
    seller_dashboard: { limit: 1 }
    unlimited_posts: { limit: null }
```

### Performance Optimizations
- **Feature Caching**: Redis for feature access checks
- **Billing Automation**: Scheduled jobs for recurring billing
- **Payment Retry**: Exponential backoff for failed payments
- **Usage Tracking**: Efficient daily aggregation of feature usage
