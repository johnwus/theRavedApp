# E-commerce Service Documentation

## 1. Service Overview

### Primary Purpose
The E-commerce Service manages the student-to-student fashion marketplace in TheRavedApp, handling product listings, shopping cart operations, order processing, payment integration, and seller dashboard functionality.

### Core Responsibilities
- **Product Catalog**: Fashion item listings with detailed specifications
- **Shopping Cart**: Cart management with size/color variants
- **Order Processing**: Complete order lifecycle from creation to fulfillment
- **Payment Integration**: Multiple payment methods (Mobile Money, Bank Transfer, Cash)
- **Seller Dashboard**: Premium seller tools and analytics
- **Inventory Management**: Stock tracking and availability
- **Transaction Security**: Secure payment processing and fraud prevention

### Service Boundaries
- **Owns**: Products, orders, payments, transactions, seller profiles, inventory
- **Does NOT Own**: User profiles, content posts, social interactions
- **Interfaces With**: User Service for authentication, Content Service for product posts, Subscription Service for premium features

### Business Domain Ownership
- Marketplace operations and product management
- Order fulfillment and transaction processing
- Payment gateway integration
- Seller performance and analytics

### Performance Requirements
- **Product Search**: < 300ms response time
- **Cart Operations**: < 200ms for add/remove/update
- **Checkout Process**: < 2s for order creation
- **Payment Processing**: < 10s for payment confirmation
- **Concurrent Transactions**: Support 1,000+ simultaneous orders

## 2. API Specification

### Product Management

#### GET /api/v1/products
**Purpose**: Get product catalog with filtering and pagination
**Query Parameters**:
- `category`: Product category filter
- `minPrice`, `maxPrice`: Price range filter
- `size`: Size filter
- `condition`: Condition filter
- `faculty`: Seller faculty filter
- `sort`: Sort by (price, date, popularity)
- `page`, `size`: Pagination

**Response**:
```json
{
  "success": true,
  "products": [
    {
      "id": "prod_123",
      "name": "Vintage Denim Jacket",
      "description": "Classic vintage denim jacket in excellent condition",
      "price": 45.00,
      "originalPrice": 60.00,
      "currency": "GHS",
      "category": "clothing",
      "subcategory": "jackets",
      "condition": "excellent",
      "size": "M",
      "color": "Blue",
      "brand": "Levi's",
      "images": [
        "https://cdn.raved.app/products/prod_123_1.jpg",
        "https://cdn.raved.app/products/prod_123_2.jpg"
      ],
      "thumbnailUrl": "https://cdn.raved.app/products/prod_123_thumb.jpg",
      "seller": {
        "id": "user_456",
        "username": "fashionista_gh",
        "name": "Sarah Miller",
        "avatar": "https://cdn.raved.app/avatars/user_456.jpg",
        "faculty": "Arts",
        "rating": 4.8,
        "totalSales": 23,
        "verified": true
      },
      "availability": {
        "inStock": true,
        "quantity": 1,
        "reserved": false
      },
      "engagement": {
        "views": 156,
        "likes": 24,
        "bookmarks": 8
      },
      "createdAt": "2024-08-10T14:30:00Z",
      "updatedAt": "2024-08-12T09:15:00Z"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 450,
    "totalPages": 23,
    "hasNext": true
  },
  "filters": {
    "categories": ["clothing", "accessories", "shoes"],
    "priceRange": { "min": 5.00, "max": 200.00 },
    "sizes": ["XS", "S", "M", "L", "XL"],
    "conditions": ["new", "excellent", "good", "fair"]
  }
}
```

#### POST /api/v1/products
**Purpose**: Create new product listing (seller only)
```json
{
  "name": "Vintage Denim Jacket",
  "description": "Classic vintage denim jacket in excellent condition. Perfect for casual campus wear.",
  "price": 45.00,
  "originalPrice": 60.00,
  "category": "clothing",
  "subcategory": "jackets",
  "condition": "excellent",
  "size": "M",
  "color": "Blue",
  "brand": "Levi's",
  "material": "100% Cotton",
  "tags": ["vintage", "denim", "casual"],
  "images": ["image1.jpg", "image2.jpg"],
  "shippingOptions": {
    "pickup": true,
    "delivery": true,
    "deliveryFee": 5.00
  }
}
```

#### GET /api/v1/products/{productId}
**Purpose**: Get detailed product information

#### PUT /api/v1/products/{productId}
**Purpose**: Update product (seller only)

#### DELETE /api/v1/products/{productId}
**Purpose**: Delete product (seller only)

### Shopping Cart Management

#### GET /api/v1/cart
**Purpose**: Get user's shopping cart
**Response**:
```json
{
  "success": true,
  "cart": {
    "id": "cart_789",
    "userId": "user_123",
    "items": [
      {
        "id": "cart_item_1",
        "productId": "prod_123",
        "product": {
          "id": "prod_123",
          "name": "Vintage Denim Jacket",
          "price": 45.00,
          "images": ["https://cdn.raved.app/products/prod_123_1.jpg"],
          "seller": {
            "id": "user_456",
            "name": "Sarah Miller"
          }
        },
        "quantity": 1,
        "size": "M",
        "color": "Blue",
        "unitPrice": 45.00,
        "totalPrice": 45.00,
        "addedAt": "2024-08-14T10:30:00Z"
      }
    ],
    "summary": {
      "itemCount": 1,
      "subtotal": 45.00,
      "deliveryFee": 5.00,
      "total": 50.00,
      "currency": "GHS"
    },
    "updatedAt": "2024-08-14T10:30:00Z"
  }
}
```

#### POST /api/v1/cart/items
**Purpose**: Add item to cart
```json
{
  "productId": "prod_123",
  "quantity": 1,
  "size": "M",
  "color": "Blue"
}
```

#### PUT /api/v1/cart/items/{itemId}
**Purpose**: Update cart item quantity

#### DELETE /api/v1/cart/items/{itemId}
**Purpose**: Remove item from cart

#### DELETE /api/v1/cart
**Purpose**: Clear entire cart

### Order Management

#### POST /api/v1/orders
**Purpose**: Create order from cart
```json
{
  "deliveryMethod": "hostel",
  "deliveryAddress": {
    "hostelName": "Akuafo Hall",
    "roomNumber": "A204",
    "additionalInfo": "Ground floor, near the entrance"
  },
  "paymentMethod": "mobile_money",
  "paymentDetails": {
    "phoneNumber": "0241234567",
    "provider": "mtn"
  },
  "buyerPhone": "0241234567",
  "specialInstructions": "Please call when you arrive"
}
```

**Response**:
```json
{
  "success": true,
  "order": {
    "id": "order_456",
    "orderNumber": "RVD-2024-001234",
    "userId": "user_123",
    "status": "pending_payment",
    "items": [
      {
        "productId": "prod_123",
        "productName": "Vintage Denim Jacket",
        "sellerId": "user_456",
        "sellerName": "Sarah Miller",
        "quantity": 1,
        "size": "M",
        "color": "Blue",
        "unitPrice": 45.00,
        "totalPrice": 45.00
      }
    ],
    "summary": {
      "subtotal": 45.00,
      "deliveryFee": 5.00,
      "total": 50.00,
      "currency": "GHS"
    },
    "delivery": {
      "method": "hostel",
      "address": {
        "hostelName": "Akuafo Hall",
        "roomNumber": "A204",
        "additionalInfo": "Ground floor, near the entrance"
      },
      "estimatedDelivery": "2024-08-15T16:00:00Z"
    },
    "payment": {
      "method": "mobile_money",
      "status": "pending",
      "amount": 50.00,
      "currency": "GHS"
    },
    "createdAt": "2024-08-14T11:00:00Z"
  }
}
```

#### GET /api/v1/orders
**Purpose**: Get user's order history

#### GET /api/v1/orders/{orderId}
**Purpose**: Get specific order details

#### PUT /api/v1/orders/{orderId}/status
**Purpose**: Update order status (seller/admin only)

### Payment Processing

#### POST /api/v1/payments/process
**Purpose**: Process payment for order
```json
{
  "orderId": "order_456",
  "paymentMethod": "mobile_money",
  "paymentDetails": {
    "phoneNumber": "0241234567",
    "provider": "mtn",
    "transactionId": "MM_TXN_789"
  }
}
```

#### GET /api/v1/payments/{paymentId}/status
**Purpose**: Check payment status

#### POST /api/v1/payments/{paymentId}/verify
**Purpose**: Verify payment completion

### Seller Dashboard (Premium Feature)

#### GET /api/v1/seller/dashboard
**Purpose**: Get seller dashboard data
**Response**:
```json
{
  "success": true,
  "dashboard": {
    "summary": {
      "totalProducts": 15,
      "activeProducts": 12,
      "totalSales": 23,
      "totalRevenue": 1250.00,
      "averageRating": 4.8,
      "responseRate": 95
    },
    "recentOrders": [
      {
        "id": "order_789",
        "orderNumber": "RVD-2024-001235",
        "buyerName": "Alex Johnson",
        "productName": "Vintage Denim Jacket",
        "amount": 50.00,
        "status": "completed",
        "createdAt": "2024-08-13T14:30:00Z"
      }
    ],
    "topProducts": [
      {
        "id": "prod_123",
        "name": "Vintage Denim Jacket",
        "views": 156,
        "likes": 24,
        "sales": 3
      }
    ],
    "analytics": {
      "viewsThisWeek": 245,
      "salesThisWeek": 3,
      "revenueThisWeek": 135.00
    }
  }
}
```

#### GET /api/v1/seller/products
**Purpose**: Get seller's product listings

#### GET /api/v1/seller/orders
**Purpose**: Get orders for seller's products

#### GET /api/v1/seller/analytics
**Purpose**: Get detailed seller analytics

### Search & Discovery

#### GET /api/v1/products/search
**Purpose**: Search products with advanced filters

#### GET /api/v1/products/trending
**Purpose**: Get trending products

#### GET /api/v1/products/recommendations
**Purpose**: Get personalized product recommendations

### Rate Limiting
- **Product Creation**: 20 products per day per seller
- **Cart Operations**: 100 requests per minute per user
- **Order Creation**: 10 orders per hour per user
- **Search**: 100 requests per minute per user
- **Payment Processing**: 5 attempts per minute per order

## 3. Data Models & Database Schema

### Database Choice: PostgreSQL
**Rationale**: E-commerce requires ACID compliance for financial transactions, complex queries for inventory management, and strong consistency for order processing and payment handling.

### Core Tables

#### products
```sql
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seller_id UUID NOT NULL, -- Reference to User Service
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'GHS',
    category VARCHAR(100) NOT NULL,
    subcategory VARCHAR(100),
    condition VARCHAR(50) NOT NULL,
    size VARCHAR(20),
    color VARCHAR(50),
    brand VARCHAR(100),
    material VARCHAR(100),
    tags TEXT[], -- Array of tags
    
    -- Media
    images TEXT[] NOT NULL, -- Array of image URLs
    thumbnail_url TEXT,
    
    -- Availability
    in_stock BOOLEAN DEFAULT TRUE,
    quantity INTEGER DEFAULT 1,
    reserved_quantity INTEGER DEFAULT 0,
    
    -- Engagement metrics
    view_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    bookmark_count INTEGER DEFAULT 0,
    
    -- Shipping
    pickup_available BOOLEAN DEFAULT TRUE,
    delivery_available BOOLEAN DEFAULT TRUE,
    delivery_fee DECIMAL(8,2) DEFAULT 0,
    
    -- Status
    status VARCHAR(50) DEFAULT 'active', -- active, sold, inactive, deleted
    moderation_status VARCHAR(50) DEFAULT 'approved',
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    sold_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT valid_price CHECK (price > 0),
    CONSTRAINT valid_condition CHECK (condition IN ('new', 'excellent', 'good', 'fair')),
    CONSTRAINT valid_status CHECK (status IN ('active', 'sold', 'inactive', 'deleted'))
);

CREATE INDEX idx_products_seller_id ON products(seller_id);
CREATE INDEX idx_products_category ON products(category, subcategory);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_condition ON products(condition);
CREATE INDEX idx_products_status ON products(status, created_at);
CREATE INDEX idx_products_tags ON products USING GIN(tags);
CREATE INDEX idx_products_search ON products USING GIN(to_tsvector('english', name || ' ' || description));
```

#### shopping_carts
```sql
CREATE TABLE shopping_carts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE, -- One cart per user
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_carts_user_id ON shopping_carts(user_id);
```

#### cart_items
```sql
CREATE TABLE cart_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_id UUID REFERENCES shopping_carts(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    size VARCHAR(20),
    color VARCHAR(50),
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    CONSTRAINT valid_quantity CHECK (quantity > 0),
    CONSTRAINT valid_unit_price CHECK (unit_price > 0),
    UNIQUE(cart_id, product_id, size, color)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product_id ON cart_items(product_id);
```

#### orders
```sql
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_number VARCHAR(50) UNIQUE NOT NULL,
    buyer_id UUID NOT NULL, -- Reference to User Service
    status VARCHAR(50) NOT NULL DEFAULT 'pending_payment',

    -- Totals
    subtotal DECIMAL(10,2) NOT NULL,
    delivery_fee DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',

    -- Delivery information
    delivery_method VARCHAR(50) NOT NULL, -- 'pickup', 'hostel', 'campus'
    delivery_address JSONB,
    buyer_phone VARCHAR(20) NOT NULL,
    special_instructions TEXT,
    estimated_delivery TIMESTAMP WITH TIME ZONE,
    delivered_at TIMESTAMP WITH TIME ZONE,

    -- Payment information
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(50) DEFAULT 'pending',
    payment_id UUID,

    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    CONSTRAINT valid_status CHECK (status IN ('pending_payment', 'paid', 'processing', 'shipped', 'delivered', 'cancelled', 'refunded')),
    CONSTRAINT valid_delivery_method CHECK (delivery_method IN ('pickup', 'hostel', 'campus')),
    CONSTRAINT valid_payment_method CHECK (payment_method IN ('mobile_money', 'bank_transfer', 'cash', 'card')),
    CONSTRAINT valid_payment_status CHECK (payment_status IN ('pending', 'processing', 'completed', 'failed', 'refunded'))
);

CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_order_number ON orders(order_number);
```

#### order_items
```sql
CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id),
    seller_id UUID NOT NULL, -- Reference to User Service

    -- Product details (snapshot at time of order)
    product_name VARCHAR(255) NOT NULL,
    product_description TEXT,
    product_image_url TEXT,

    -- Order item details
    quantity INTEGER NOT NULL DEFAULT 1,
    size VARCHAR(20),
    color VARCHAR(50),
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,

    -- Item status
    status VARCHAR(50) DEFAULT 'pending', -- pending, confirmed, shipped, delivered, cancelled
    seller_notes TEXT,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    CONSTRAINT valid_quantity CHECK (quantity > 0),
    CONSTRAINT valid_unit_price CHECK (unit_price > 0),
    CONSTRAINT valid_item_status CHECK (status IN ('pending', 'confirmed', 'shipped', 'delivered', 'cancelled'))
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_seller_id ON order_items(seller_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
```

#### payments
```sql
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',
    status VARCHAR(50) DEFAULT 'pending',

    -- Payment provider details
    provider VARCHAR(50), -- 'mtn', 'vodafone', 'airtel', 'bank'
    provider_transaction_id VARCHAR(255),
    provider_reference VARCHAR(255),

    -- Payment details
    payment_details JSONB, -- Store method-specific details

    -- Processing information
    processed_at TIMESTAMP WITH TIME ZONE,
    failure_reason TEXT,
    retry_count INTEGER DEFAULT 0,

    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    CONSTRAINT valid_amount CHECK (amount > 0),
    CONSTRAINT valid_status CHECK (status IN ('pending', 'processing', 'completed', 'failed', 'cancelled', 'refunded'))
);

CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_provider_txn ON payments(provider_transaction_id);
```

#### seller_profiles
```sql
CREATE TABLE seller_profiles (
    user_id UUID PRIMARY KEY, -- Reference to User Service
    business_name VARCHAR(255),
    description TEXT,

    -- Performance metrics
    total_sales INTEGER DEFAULT 0,
    total_revenue DECIMAL(12,2) DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0,
    total_reviews INTEGER DEFAULT 0,
    response_rate DECIMAL(5,2) DEFAULT 0, -- Percentage

    -- Settings
    auto_accept_orders BOOLEAN DEFAULT FALSE,
    vacation_mode BOOLEAN DEFAULT FALSE,
    vacation_message TEXT,

    -- Verification
    verified BOOLEAN DEFAULT FALSE,
    verification_date TIMESTAMP WITH TIME ZONE,

    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_seller_profiles_verified ON seller_profiles(verified);
CREATE INDEX idx_seller_profiles_rating ON seller_profiles(average_rating);
```

### Data Validation Rules
- **Product Price**: Must be positive, maximum 2 decimal places
- **Product Name**: 3-255 characters
- **Product Description**: Maximum 2000 characters
- **Order Total**: Must match sum of item prices plus delivery fee
- **Payment Amount**: Must match order total exactly
- **Phone Numbers**: Ghana format validation

### Migration Strategy
```sql
-- V1__Initial_ecommerce_schema.sql
-- V2__Add_shopping_cart_system.sql
-- V3__Add_order_management.sql
-- V4__Add_payment_processing.sql
-- V5__Add_seller_profiles.sql
-- V6__Add_advanced_search_indexes.sql
```

## 4. Service Discovery & Communication

### Eureka Configuration
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    instance-id: ${spring.application.name}:${server.port}
    prefer-ip-address: true

spring:
  application:
    name: ecommerce-service
```

### Inter-Service Communication

#### Synchronous REST Calls
```java
// User Service - Validate users and get profiles
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{userId}/profile")
    UserProfile getUserProfile(@PathVariable String userId);

    @GetMapping("/api/v1/users/{userId}/permissions")
    UserPermissions getUserPermissions(@PathVariable String userId);
}

// Subscription Service - Check premium features
@FeignClient(name = "subscription-service")
public interface SubscriptionServiceClient {
    @GetMapping("/api/v1/subscriptions/{userId}/features")
    SubscriptionFeatures getUserFeatures(@PathVariable String userId);
}

// Notification Service - Send order notifications
@FeignClient(name = "notification-service")
public interface NotificationServiceClient {
    @PostMapping("/api/v1/notifications/order")
    void sendOrderNotification(@RequestBody OrderNotificationRequest request);
}
```

#### Asynchronous Messaging (Kafka)
```yaml
# Published Topics
- ecommerce.product.created: New product listed
- ecommerce.product.sold: Product sold
- ecommerce.order.created: New order placed
- ecommerce.order.updated: Order status changed
- ecommerce.payment.completed: Payment processed successfully

# Consumed Topics
- user.updated: Update seller profile information
- subscription.updated: Update premium feature access
- content.post.created: Link content posts to products
```

### Circuit Breaker Configuration
```yaml
resilience4j:
  circuitbreaker:
    instances:
      user-service:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
      payment-gateway:
        failure-rate-threshold: 30
        wait-duration-in-open-state: 60s
```
```
