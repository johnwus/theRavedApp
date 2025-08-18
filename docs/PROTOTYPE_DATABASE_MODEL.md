# RAvED Prototype Database Model (Tailored from Web Prototype)

This document defines a microservice-aligned relational schema based on the features and fields observed in the `DOCTYPE.html` prototype. It follows the existing architecture pattern (service-per-database, API-only cross-service access) and uses PostgreSQL syntax and indexing conventions.

Notes
- Currency defaults to `GHS` (Ghana Cedi).
- Phone numbers intended in E.164; validations occur at service layer.
- Timestamps are UTC.
- Where prototype shows counters (likes, saves, views), denormalized counters are included for performance.

## 1. USER SERVICE DATABASE (user_service_db)

### Universities
```sql
CREATE TABLE universities (
  id              BIGSERIAL PRIMARY KEY,
  name            VARCHAR(255) NOT NULL UNIQUE,
  code            VARCHAR(10)  NOT NULL UNIQUE,
  country         VARCHAR(100) NOT NULL,
  city            VARCHAR(100) NOT NULL,
  domain_suffix   VARCHAR(50), -- e.g., "@student.ug.edu.gh"
  logo_url        TEXT,
  is_active       BOOLEAN DEFAULT true,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Faculties
```sql
CREATE TABLE faculties (
  id              BIGSERIAL PRIMARY KEY,
  university_id   BIGINT NOT NULL REFERENCES universities(id),
  name            VARCHAR(255) NOT NULL,
  code            VARCHAR(20)  NOT NULL,
  description     TEXT,
  color_code      VARCHAR(7),  -- hex for UI theming
  is_active       BOOLEAN DEFAULT true,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(university_id, code)
);
```

### Faculty Stats (prototype counters: members, posts, events)
```sql
CREATE TABLE faculty_stats (
  faculty_id     BIGINT PRIMARY KEY REFERENCES faculties(id) ON DELETE CASCADE,
  members_count  INTEGER DEFAULT 0,
  posts_count    INTEGER DEFAULT 0,
  events_count   INTEGER DEFAULT 0,
  updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

-- Note: The prototype UI does not expose Departments explicitly; omitted to mirror prototype.

### Users
```sql
CREATE TABLE users (
  id                   BIGSERIAL PRIMARY KEY,
  student_id           VARCHAR(50)  NOT NULL UNIQUE,
  email                VARCHAR(255) NOT NULL UNIQUE,
  username             VARCHAR(50)  NOT NULL UNIQUE,
  password_hash        VARCHAR(255) NOT NULL,

  -- Personal
  first_name           VARCHAR(100) NOT NULL,
  last_name            VARCHAR(100) NOT NULL,
  display_name         VARCHAR(100),
  bio                  TEXT,
  date_of_birth        DATE,
  gender               VARCHAR(20),
  phone_number         VARCHAR(20),

  -- Academic
  university_id        BIGINT NOT NULL REFERENCES universities(id),
  faculty_id           BIGINT NOT NULL REFERENCES faculties(id),
  -- department_id omitted to match prototype scope
  academic_year        INTEGER,
  graduation_year      INTEGER,

  -- Profile
  profile_picture_url  TEXT,
  cover_photo_url      TEXT,
  location             VARCHAR(255),
  website_url          TEXT,

  -- Privacy flags (from prototype settings)
  is_profile_public                  BOOLEAN DEFAULT true,
  allow_messages_from_strangers      BOOLEAN DEFAULT true,
  show_email                         BOOLEAN DEFAULT false,
  show_phone                         BOOLEAN DEFAULT false,

  -- System
  email_verified       BOOLEAN DEFAULT false,
  student_id_verified  BOOLEAN DEFAULT false,
  seller_verified      BOOLEAN DEFAULT false,
  is_active            BOOLEAN DEFAULT true,
  last_login_at        TIMESTAMP,
  created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### User Settings (themes, language, preferences)
```sql
CREATE TABLE user_settings (
  user_id            BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  dark_mode          BOOLEAN DEFAULT false,
  theme_name         VARCHAR(50) DEFAULT 'default', -- 'rose','emerald','ocean','sunset','galaxy', etc.
  language_code      VARCHAR(10) DEFAULT 'en',
  date_format        VARCHAR(20) DEFAULT 'relative', -- 'relative','dd/MM/yyyy','MM/dd/yyyy'
  currency           VARCHAR(3)  DEFAULT 'GHS',

  -- Privacy & interaction toggles
  private_account            BOOLEAN DEFAULT false,
  show_activity_status       BOOLEAN DEFAULT true,
  read_receipts              BOOLEAN DEFAULT true,
  allow_downloads            BOOLEAN DEFAULT true,
  allow_story_sharing        BOOLEAN DEFAULT true,

  -- Data & ads
  analytics_enabled          BOOLEAN DEFAULT true,
  personalized_ads_enabled   BOOLEAN DEFAULT true,

  -- Profile customization
  avatar_style               JSONB,     -- e.g., { border: 'gradient', color: '#...' }

  updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Student Verifications
```sql
CREATE TABLE student_verifications (
  id                      BIGSERIAL PRIMARY KEY,
  user_id                 BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  student_id_document_url TEXT NOT NULL,
  verification_status     VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
  verified_by             BIGINT REFERENCES users(id),
  verification_notes      TEXT,
  submitted_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  reviewed_at             TIMESTAMP,
  expires_at              TIMESTAMP,
  created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### User Sessions
```sql
CREATE TABLE user_sessions (
  id               BIGSERIAL PRIMARY KEY,
  user_id          BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  session_token    VARCHAR(255) NOT NULL UNIQUE,
  device_info      JSONB,
  ip_address       INET,
  user_agent       TEXT,
  is_active        BOOLEAN DEFAULT true,
  expires_at       TIMESTAMP NOT NULL,
  created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Verification Codes (email/phone during registration)
```sql
CREATE TABLE verification_codes (
  id            BIGSERIAL PRIMARY KEY,
  user_id       BIGINT, -- nullable until user exists
  channel       VARCHAR(20) NOT NULL, -- EMAIL, PHONE
  destination   VARCHAR(255) NOT NULL, -- email or phone
  purpose       VARCHAR(30) NOT NULL, -- REGISTER, VERIFY_EMAIL, VERIFY_PHONE, RESET_PASSWORD
  code_hash     VARCHAR(255) NOT NULL, -- store hashed code
  attempts      INTEGER DEFAULT 0,
  max_attempts  INTEGER DEFAULT 5,
  expires_at    TIMESTAMP NOT NULL,
  consumed_at   TIMESTAMP,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_verification_dest    ON verification_codes(destination);
CREATE INDEX idx_verification_purpose ON verification_codes(purpose);
```

### Blocked Users
```sql
CREATE TABLE blocked_users (
  user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  blocked_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, blocked_id)
);
```

---

## 2. CONTENT SERVICE DATABASE (content_service_db)

### Posts (includes outfits/general/events-as-posts if needed)
```sql
CREATE TABLE posts (
  id              BIGSERIAL PRIMARY KEY,
  user_id         BIGINT NOT NULL, -- ref users
  content         TEXT NOT NULL,

  post_type       VARCHAR(20) DEFAULT 'OUTFIT', -- OUTFIT, GENERAL, POLL, EVENT
  visibility      VARCHAR(20) DEFAULT 'PUBLIC', -- PUBLIC, FACULTY_ONLY, FOLLOWERS_ONLY, CONNECTIONS_ONLY, PRIVATE
  publish_status  VARCHAR(12) DEFAULT 'PUBLISHED', -- DRAFT, SCHEDULED, PUBLISHED, ARCHIVED
  scheduled_at    TIMESTAMP,
  faculty_id      BIGINT, -- ref faculties

  -- Denormalized metrics
  likes_count     INTEGER DEFAULT 0,
  comments_count  INTEGER DEFAULT 0,
  shares_count    INTEGER DEFAULT 0,
  views_count     INTEGER DEFAULT 0,
  saves_count     INTEGER DEFAULT 0,

  -- Moderation
  is_flagged      BOOLEAN DEFAULT false,
  moderation_status VARCHAR(20) DEFAULT 'APPROVED',
  flagged_reason  TEXT,

  -- System
  is_deleted      BOOLEAN DEFAULT false,
  is_featured     BOOLEAN DEFAULT false,
  featured_until  TIMESTAMP,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_posts_user_id       ON posts(user_id);
CREATE INDEX idx_posts_faculty_id    ON posts(faculty_id);
CREATE INDEX idx_posts_created_at    ON posts(created_at);
CREATE INDEX idx_posts_featured      ON posts(is_featured, featured_until);
CREATE INDEX idx_posts_visibility    ON posts(visibility);
```

### Media Files
```sql
CREATE TABLE media_files (
  id             BIGSERIAL PRIMARY KEY,
  post_id        BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
  file_url       TEXT NOT NULL,
  thumbnail_url  TEXT,
  file_type      VARCHAR(20) NOT NULL, -- IMAGE, VIDEO, AUDIO
  file_size      BIGINT,
  width          INTEGER,
  height         INTEGER,
  duration       INTEGER,
  file_format    VARCHAR(20),
  display_order  INTEGER DEFAULT 0,
  alt_text       TEXT,
  is_primary     BOOLEAN DEFAULT false,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_media_files_post_id ON media_files(post_id);
```

### Post Tags
```sql
CREATE TABLE post_tags (
  id         BIGSERIAL PRIMARY KEY,
  post_id    BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
  tag_name   VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(post_id, tag_name)
);
CREATE INDEX idx_post_tags_name   ON post_tags(tag_name);
CREATE INDEX idx_post_tags_post   ON post_tags(post_id);
```

### Post Mentions
```sql
CREATE TABLE post_mentions (
  id                 BIGSERIAL PRIMARY KEY,
  post_id            BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
  mentioned_user_id  BIGINT NOT NULL, -- ref users
  mention_type       VARCHAR(20) DEFAULT 'USER', -- USER, FACULTY
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(post_id, mentioned_user_id)
);
CREATE INDEX idx_post_mentions_user ON post_mentions(mentioned_user_id);
```

### Stories (prototype: image/video/text templates, expire)
```sql
CREATE TABLE stories (
  id             BIGSERIAL PRIMARY KEY,
  user_id        BIGINT NOT NULL, -- ref users
  story_type     VARCHAR(20) NOT NULL, -- IMAGE, VIDEO, TEXT
  media_url      TEXT,               -- null for TEXT
  thumbnail_url  TEXT,
  text_overlay   TEXT,
  template_key   VARCHAR(50),        -- e.g., 'ootd','mood','study','event'
  background     JSONB,              -- gradients/colors used for TEXT
  posted_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at     TIMESTAMP NOT NULL  -- typically +24h
);
CREATE INDEX idx_stories_user   ON stories(user_id);
CREATE INDEX idx_stories_expiry ON stories(expires_at);
```

### Story Views
```sql
CREATE TABLE story_views (
  story_id   BIGINT NOT NULL REFERENCES stories(id) ON DELETE CASCADE,
  viewer_id  BIGINT NOT NULL, -- ref users
  viewed_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (story_id, viewer_id)
);
```

---

## 3. SOCIAL SERVICE DATABASE (social_service_db)

### Likes (generic targets)
```sql
CREATE TABLE likes (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL, -- ref users
  target_id    BIGINT NOT NULL, -- post_id, comment_id, product_id
  target_type  VARCHAR(20) NOT NULL, -- POST, COMMENT, PRODUCT
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(user_id, target_id, target_type)
);
CREATE INDEX idx_likes_target ON likes(target_id, target_type);
CREATE INDEX idx_likes_user   ON likes(user_id);
```

### Comments
```sql
CREATE TABLE comments (
  id                 BIGSERIAL PRIMARY KEY,
  post_id            BIGINT NOT NULL, -- ref posts
  user_id            BIGINT NOT NULL, -- ref users
  parent_comment_id  BIGINT REFERENCES comments(id),
  content            TEXT NOT NULL,
  likes_count        INTEGER DEFAULT 0,
  replies_count      INTEGER DEFAULT 0,
  is_flagged         BOOLEAN DEFAULT false,
  moderation_status  VARCHAR(20) DEFAULT 'APPROVED',
  is_deleted         BOOLEAN DEFAULT false,
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_comments_post_id  ON comments(post_id);
CREATE INDEX idx_comments_user_id  ON comments(user_id);
CREATE INDEX idx_comments_parent   ON comments(parent_comment_id);
CREATE INDEX idx_comments_created  ON comments(created_at);
```

### Follows
```sql
CREATE TABLE follows (
  id            BIGSERIAL PRIMARY KEY,
  follower_id   BIGINT NOT NULL, -- ref users
  following_id  BIGINT NOT NULL, -- ref users
  follow_type   VARCHAR(20) DEFAULT 'USER', -- USER, FACULTY
  is_mutual     BOOLEAN DEFAULT false,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(follower_id, following_id, follow_type)
);
CREATE INDEX idx_follows_follower  ON follows(follower_id);
CREATE INDEX idx_follows_following ON follows(following_id);
```

### User Connections (prototype: accept/decline/block)
```sql
CREATE TABLE user_connections (
  id                 BIGSERIAL PRIMARY KEY,
  requester_id       BIGINT NOT NULL, -- ref users
  addressee_id       BIGINT NOT NULL, -- ref users
  connection_status  VARCHAR(20) DEFAULT 'PENDING', -- PENDING, ACCEPTED, BLOCKED
  connection_type    VARCHAR(20) DEFAULT 'FRIEND',  -- FRIEND, STUDY_BUDDY, ROOMMATE
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(requester_id, addressee_id)
);
CREATE INDEX idx_connections_requester ON user_connections(requester_id);
CREATE INDEX idx_connections_addressee ON user_connections(addressee_id);
```

### Saved Posts
```sql
CREATE TABLE saved_posts (
  user_id   BIGINT NOT NULL, -- ref users
  post_id   BIGINT NOT NULL, -- ref posts
  saved_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, post_id)
);
```

### Activities (feed of actions)
```sql
CREATE TABLE activities (
  id              BIGSERIAL PRIMARY KEY,
  user_id         BIGINT NOT NULL,     -- actor
  target_user_id  BIGINT,              -- optional
  activity_type   VARCHAR(50) NOT NULL, -- LIKE_POST, COMMENT_POST, FOLLOW_USER, etc.
  target_id       BIGINT,
  target_type     VARCHAR(20),         -- POST, COMMENT, USER, PRODUCT, ORDER
  metadata        JSONB,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_activities_user       ON activities(user_id);
CREATE INDEX idx_activities_targetuser ON activities(target_user_id);
CREATE INDEX idx_activities_type       ON activities(activity_type);
CREATE INDEX idx_activities_created    ON activities(created_at);
```

### Content Reports (abuse/report flows)
```sql
CREATE TABLE content_reports (
  id            BIGSERIAL PRIMARY KEY,
  reporter_id   BIGINT NOT NULL, -- ref users
  target_id     BIGINT NOT NULL, -- post_id, comment_id, product_id
  target_type   VARCHAR(20) NOT NULL, -- POST, COMMENT, PRODUCT
  reason        TEXT,
  metadata      JSONB,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_reports_target   ON content_reports(target_id, target_type);
CREATE INDEX idx_reports_reporter ON content_reports(reporter_id);
```
### Rankings / Leaderboards (prototype podium, points)
```sql
CREATE TABLE leaderboard_seasons (
  id            BIGSERIAL PRIMARY KEY,
  name          VARCHAR(100) NOT NULL,
  season_type   VARCHAR(20) DEFAULT 'WEEK', -- WEEK, MONTH, ALL
  starts_at     TIMESTAMP NOT NULL,
  ends_at       TIMESTAMP NOT NULL,
  is_active     BOOLEAN DEFAULT true,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE leaderboard_scores (
  id            BIGSERIAL PRIMARY KEY,
  season_id     BIGINT NOT NULL REFERENCES leaderboard_seasons(id) ON DELETE CASCADE,
  user_id       BIGINT NOT NULL, -- ref users
  score         INTEGER DEFAULT 0,
  rank          INTEGER,
  updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(season_id, user_id)
);
CREATE INDEX idx_leaderboard_season ON leaderboard_scores(season_id, score DESC);
```

---

## 4. REALTIME SERVICE DATABASE (realtime_service_db)

### Chat Rooms
```sql
CREATE TABLE chat_rooms (
  id             BIGSERIAL PRIMARY KEY,
  room_type      VARCHAR(20) NOT NULL, -- DIRECT, GROUP, FACULTY_GROUP
  name           VARCHAR(255),
  description    TEXT,
  avatar_url     TEXT,
  created_by     BIGINT NOT NULL, -- ref users
  faculty_id     BIGINT,          -- ref faculties (optional)
  is_active      BOOLEAN DEFAULT true,
  last_message_at TIMESTAMP,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_chat_rooms_type         ON chat_rooms(room_type);
CREATE INDEX idx_chat_rooms_last_message ON chat_rooms(last_message_at);
```

### Chat Room Members
```sql
CREATE TABLE chat_room_members (
  id          BIGSERIAL PRIMARY KEY,
  room_id     BIGINT NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
  user_id     BIGINT NOT NULL, -- ref users
  role        VARCHAR(20) DEFAULT 'MEMBER', -- ADMIN, MODERATOR, MEMBER
  joined_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_read_at TIMESTAMP,
  is_muted    BOOLEAN DEFAULT false,
  is_active   BOOLEAN DEFAULT true,
  UNIQUE(room_id, user_id)
);
CREATE INDEX idx_chat_members_room ON chat_room_members(room_id);
CREATE INDEX idx_chat_members_user ON chat_room_members(user_id);
```

### Messages
```sql
CREATE TABLE messages (
  id                   BIGSERIAL PRIMARY KEY,
  room_id              BIGINT NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
  sender_id            BIGINT NOT NULL, -- ref users
  message_type         VARCHAR(20) DEFAULT 'TEXT', -- TEXT, IMAGE, VIDEO, AUDIO, FILE, SYSTEM
  content              TEXT,
  media_url            TEXT,
  media_metadata       JSONB,
  reply_to_message_id  BIGINT REFERENCES messages(id),
  is_edited            BOOLEAN DEFAULT false,
  is_deleted           BOOLEAN DEFAULT false,
  edited_at            TIMESTAMP,
  created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_messages_room_id  ON messages(room_id);
CREATE INDEX idx_messages_sender   ON messages(sender_id);
CREATE INDEX idx_messages_created  ON messages(created_at);
```

### Message Reactions
```sql
CREATE TABLE message_reactions (
  id           BIGSERIAL PRIMARY KEY,
  message_id   BIGINT NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
  user_id      BIGINT NOT NULL, -- ref users
  reaction_type VARCHAR(20) NOT NULL, -- LIKE, LOVE, LAUGH, etc.
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(message_id, user_id, reaction_type)
);
CREATE INDEX idx_message_reactions_message ON message_reactions(message_id);
CREATE INDEX idx_message_reactions_user    ON message_reactions(user_id);
```

---

## 5. ECOMMERCE SERVICE DATABASE (ecommerce_service_db)

### Product Categories
```sql
CREATE TABLE product_categories (
  id                 BIGSERIAL PRIMARY KEY,
  name               VARCHAR(255) NOT NULL UNIQUE,
  description        TEXT,
  parent_category_id BIGINT REFERENCES product_categories(id),
  icon_url           TEXT,
  is_active          BOOLEAN DEFAULT true,
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Products (prototype: size, color, brand, condition, views/likes/saves)
```sql
CREATE TABLE products (
  id               BIGSERIAL PRIMARY KEY,
  seller_id        BIGINT NOT NULL, -- ref users
  post_id          BIGINT,          -- optional link to outfit post

  title            VARCHAR(255) NOT NULL,
  description      TEXT NOT NULL,
  category_id      BIGINT REFERENCES product_categories(id),
  brand            VARCHAR(100),
  size             VARCHAR(20),
  color            VARCHAR(50),
  condition        VARCHAR(20) DEFAULT 'GOOD', -- NEW, LIKE_NEW, GOOD, FAIR

  price            DECIMAL(10,2) NOT NULL,
  original_price   DECIMAL(10,2),
  currency         VARCHAR(3) DEFAULT 'GHS',
  is_negotiable    BOOLEAN DEFAULT false,

  -- Sales & discounts (prototype shows sale badges/discount toggles)
  is_on_sale       BOOLEAN DEFAULT false,
  sale_price       DECIMAL(10,2),
  discount_percent INTEGER,
  sale_starts_at   TIMESTAMP,
  sale_ends_at     TIMESTAMP,

  quantity         INTEGER DEFAULT 1,
  is_available     BOOLEAN DEFAULT true,

  views_count      INTEGER DEFAULT 0,
  likes_count      INTEGER DEFAULT 0,
  saves_count      INTEGER DEFAULT 0,

  ships_from       VARCHAR(255),
  processing_days  INTEGER DEFAULT 1,
  accepted_payment_methods JSONB, -- e.g., ["MOBILE_MONEY","CASH","BANK_TRANSFER","CARD"]
  meetup_location  JSONB,         -- name/address/geo for cash meetups
  contact_phone    VARCHAR(20),   -- seller contact for this listing

  is_featured      BOOLEAN DEFAULT false,
  featured_until   TIMESTAMP,
  is_active        BOOLEAN DEFAULT true,
  created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_products_seller     ON products(seller_id);
CREATE INDEX idx_products_category   ON products(category_id);
CREATE INDEX idx_products_price      ON products(price);
CREATE INDEX idx_products_featured   ON products(is_featured, featured_until);
CREATE INDEX idx_products_created    ON products(created_at);
```

### Product Images
```sql
CREATE TABLE product_images (
  id             BIGSERIAL PRIMARY KEY,
  product_id     BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  image_url      TEXT NOT NULL,
  thumbnail_url  TEXT,
  alt_text       TEXT,
  display_order  INTEGER DEFAULT 0,
  is_primary     BOOLEAN DEFAULT false,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_product_images_product ON product_images(product_id);
```

### Saved Products (bookmarks)
```sql
CREATE TABLE saved_products (
  user_id    BIGINT NOT NULL, -- ref users
  product_id BIGINT NOT NULL, -- ref products
  saved_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, product_id)
);
```

### Recently Viewed Products (for recommendations)
```sql
CREATE TABLE recently_viewed_products (
  user_id    BIGINT NOT NULL, -- ref users
  product_id BIGINT NOT NULL, -- ref products
  viewed_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, product_id)
);
```

### Carts
```sql
CREATE TABLE carts (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL UNIQUE, -- one active cart per user
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cart_items (
  id           BIGSERIAL PRIMARY KEY,
  cart_id      BIGINT NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
  product_id   BIGINT NOT NULL REFERENCES products(id),
  size         VARCHAR(20) DEFAULT 'M',
  color        VARCHAR(50) DEFAULT 'Black',
  quantity     INTEGER NOT NULL DEFAULT 1,
  unit_price   DECIMAL(10,2) NOT NULL,
  seller_id    BIGINT NOT NULL, -- duplicate for convenience during checkout splits
  UNIQUE(cart_id, product_id, size, color)
);
CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
```

### Orders
```sql
CREATE TABLE orders (
  id                 BIGSERIAL PRIMARY KEY,
  order_number       VARCHAR(50) NOT NULL UNIQUE,
  buyer_id           BIGINT NOT NULL, -- ref users
  seller_id          BIGINT NOT NULL, -- ref users (single-seller order)

  status             VARCHAR(20) DEFAULT 'PENDING', -- PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, REFUNDED
  payment_status     VARCHAR(20) DEFAULT 'PENDING', -- PENDING, PAID, FAILED, REFUNDED

  subtotal           DECIMAL(10,2) NOT NULL,
  shipping_cost      DECIMAL(10,2) DEFAULT 0,
  tax_amount         DECIMAL(10,2) DEFAULT 0,
  total_amount       DECIMAL(10,2) NOT NULL,
  currency           VARCHAR(3) DEFAULT 'GHS',

  shipping_address   JSONB NOT NULL,
  billing_address    JSONB,
  delivery_method    VARCHAR(20) DEFAULT 'DELIVERY', -- DELIVERY, PICKUP
  pickup_location    JSONB, -- name/address/geo for pickups
  selected_payment_method VARCHAR(50), -- as chosen at checkout
  tracking_number    VARCHAR(100),
  estimated_delivery DATE,

  ordered_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  confirmed_at       TIMESTAMP,
  shipped_at         TIMESTAMP,
  delivered_at       TIMESTAMP,
  cancelled_at       TIMESTAMP,

  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_orders_buyer   ON orders(buyer_id);
CREATE INDEX idx_orders_seller  ON orders(seller_id);
CREATE INDEX idx_orders_status  ON orders(status);
CREATE INDEX idx_orders_created ON orders(ordered_at);
```

### Order Items
```sql
CREATE TABLE order_items (
  id                BIGSERIAL PRIMARY KEY,
  order_id          BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  product_id        BIGINT NOT NULL REFERENCES products(id),
  quantity          INTEGER NOT NULL DEFAULT 1,
  unit_price        DECIMAL(10,2) NOT NULL,
  total_price       DECIMAL(10,2) NOT NULL,
  size              VARCHAR(20),
  color             VARCHAR(50),
  product_snapshot  JSONB, -- product details at purchase time
  created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_order_items_order ON order_items(order_id);
```

### Payments (prototype: momo, card, bank, cash)
```sql
CREATE TABLE payments (
  id                BIGSERIAL PRIMARY KEY,
  order_id          BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  payment_method    VARCHAR(50) NOT NULL, -- MOBILE_MONEY, CARD, BANK_TRANSFER, CASH
  payment_provider  VARCHAR(50),          -- e.g., HUBTEL, PAYSTACK, FLUTTERWAVE, STRIPE
  transaction_id    VARCHAR(255) UNIQUE,

  amount            DECIMAL(10,2) NOT NULL,
  currency          VARCHAR(3) DEFAULT 'GHS',
  status            VARCHAR(20) DEFAULT 'PENDING', -- PENDING, SUCCESS, FAILED, CANCELLED, REFUNDED

  provider_response JSONB,
  failure_reason    TEXT,

  -- Mobile money specifics
  momo_phone        VARCHAR(20),
  momo_network      VARCHAR(20), -- MTN, VODAFONE, AIRTELTIGO

  initiated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at      TIMESTAMP,
  failed_at         TIMESTAMP
);
CREATE INDEX idx_payments_order   ON payments(order_id);
CREATE INDEX idx_payments_status  ON payments(status);
```

### Seller Stats (dashboard: items, sales, rating)
```sql
CREATE TABLE seller_stats (
  seller_id        BIGINT PRIMARY KEY, -- ref users
  total_items      INTEGER DEFAULT 0,
  total_sales      DECIMAL(10,2) DEFAULT 0,
  items_sold       INTEGER DEFAULT 0,
  rating           DECIMAL(3,2) DEFAULT 0,
  updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 6. EVENTS SERVICE DATABASE (events_service_db)

While events could be modeled as `posts` with type `EVENT`, the prototype shows distinct fields. A dedicated service simplifies indexing and integrations.

### Events
```sql
CREATE TABLE events (
  id             BIGSERIAL PRIMARY KEY,
  organizer_id   BIGINT NOT NULL, -- ref users
  title          VARCHAR(255) NOT NULL,
  description    TEXT,
  image_url      TEXT,
  org_avatar_url TEXT,
  location_name  VARCHAR(255),
  location_geo   JSONB,       -- lat/lng or place_id
  starts_at      TIMESTAMP NOT NULL,
  ends_at        TIMESTAMP,
  category       VARCHAR(50),
  audience       VARCHAR(20) DEFAULT 'ALL', -- ALL, FACULTY_ONLY, INVITE, UNDERGRADUATE, GRADUATE, ALUMNI, PUBLIC
  price_amount   DECIMAL(10,2) DEFAULT 0,
  currency       VARCHAR(3) DEFAULT 'GHS',
  capacity       INTEGER,
  is_featured    BOOLEAN DEFAULT false,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_events_time    ON events(starts_at);
CREATE INDEX idx_events_org     ON events(organizer_id);
CREATE INDEX idx_events_category ON events(category);
```

### Event Attendees
```sql
CREATE TABLE event_attendees (
  event_id     BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  user_id      BIGINT NOT NULL, -- ref users
  status       VARCHAR(20) DEFAULT 'INTERESTED', -- INTERESTED, GOING, CHECKED_IN, CANCELLED
  joined_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (event_id, user_id)
);
```

---

## 7. NOTIFICATION SERVICE DATABASE (notification_service_db)

### Notification Templates
```sql
CREATE TABLE notification_templates (
  id                BIGSERIAL PRIMARY KEY,
  template_name     VARCHAR(100) NOT NULL UNIQUE,
  template_type     VARCHAR(20) NOT NULL, -- PUSH, EMAIL, SMS
  subject_template  TEXT,
  body_template     TEXT NOT NULL,
  variables         JSONB,
  is_active         BOOLEAN DEFAULT true,
  created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Notifications
```sql
CREATE TABLE notifications (
  id                BIGSERIAL PRIMARY KEY,
  user_id           BIGINT NOT NULL, -- ref users
  notification_type VARCHAR(50) NOT NULL, -- LIKE, COMMENT, FOLLOW, ORDER_UPDATE, MESSAGE, etc.
  title             VARCHAR(255) NOT NULL,
  body              TEXT NOT NULL,
  data              JSONB,
  action_url        TEXT,
  image_url         TEXT,
  is_read           BOOLEAN DEFAULT false,
  is_sent           BOOLEAN DEFAULT false,
  delivery_status   VARCHAR(20) DEFAULT 'PENDING',
  scheduled_at      TIMESTAMP,
  sent_at           TIMESTAMP,
  read_at           TIMESTAMP,
  expires_at        TIMESTAMP,
  created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_notifications_user  ON notifications(user_id);
CREATE INDEX idx_notifications_type  ON notifications(notification_type);
CREATE INDEX idx_notifications_read  ON notifications(is_read);
CREATE INDEX idx_notifications_time  ON notifications(created_at);
```

### Device Tokens
```sql
CREATE TABLE device_tokens (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL, -- ref users
  token        VARCHAR(500) NOT NULL,
  platform     VARCHAR(20) NOT NULL, -- IOS, ANDROID, WEB
  device_info  JSONB,
  is_active    BOOLEAN DEFAULT true,
  last_used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(user_id, token)
);
CREATE INDEX idx_device_tokens_user   ON device_tokens(user_id);
CREATE INDEX idx_device_tokens_active ON device_tokens(is_active);
```

---

## 8. ANALYTICS SERVICE DATABASE (analytics_service_db)

### Analytics Events
```sql
CREATE TABLE analytics_events (
  id             BIGSERIAL PRIMARY KEY,
  user_id        BIGINT, -- nullable for anonymous
  session_id     VARCHAR(255),
  event_name     VARCHAR(100) NOT NULL,
  event_category VARCHAR(50),
  properties     JSONB,
  user_properties JSONB,
  platform       VARCHAR(20), -- IOS, ANDROID, WEB
  app_version    VARCHAR(20),
  device_info    JSONB,
  location_info  JSONB,
  event_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  server_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_analytics_user  ON analytics_events(user_id);
CREATE INDEX idx_analytics_name  ON analytics_events(event_name);
CREATE INDEX idx_analytics_cat   ON analytics_events(event_category);
CREATE INDEX idx_analytics_time  ON analytics_events(event_time);
```

### User Metrics (denormalized KPIs)
```sql
CREATE TABLE user_metrics (
  id                      BIGSERIAL PRIMARY KEY,
  user_id                 BIGINT NOT NULL UNIQUE, -- ref users
  posts_count             INTEGER DEFAULT 0,
  likes_given             INTEGER DEFAULT 0,
  likes_received          INTEGER DEFAULT 0,
  comments_given          INTEGER DEFAULT 0,
  comments_received       INTEGER DEFAULT 0,
  followers_count         INTEGER DEFAULT 0,
  following_count         INTEGER DEFAULT 0,
  connections_count       INTEGER DEFAULT 0,
  products_sold           INTEGER DEFAULT 0,
  products_bought         INTEGER DEFAULT 0,
  total_sales_amount      DECIMAL(10,2) DEFAULT 0,
  total_purchases_amount  DECIMAL(10,2) DEFAULT 0,
  login_streak            INTEGER DEFAULT 0,
  total_sessions          INTEGER DEFAULT 0,
  total_time_spent        BIGINT  DEFAULT 0,
  engagement_score        DECIMAL(5,2) DEFAULT 0,
  influence_score         DECIMAL(5,2) DEFAULT 0,
  last_calculated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_user_metrics_engagement ON user_metrics(engagement_score);
CREATE INDEX idx_user_metrics_influence  ON user_metrics(influence_score);
```

### Content Metrics (posts/products)
```sql
CREATE TABLE content_metrics (
  id                   BIGSERIAL PRIMARY KEY,
  content_id           BIGINT NOT NULL, -- post_id, product_id
  content_type         VARCHAR(20) NOT NULL, -- POST, PRODUCT
  views_count          INTEGER DEFAULT 0,
  likes_count          INTEGER DEFAULT 0,
  comments_count       INTEGER DEFAULT 0,
  shares_count         INTEGER DEFAULT 0,
  saves_count          INTEGER DEFAULT 0,
  engagement_rate      DECIMAL(5,2) DEFAULT 0,
  viral_score          DECIMAL(5,2) DEFAULT 0,
  reach                INTEGER DEFAULT 0,
  impressions          INTEGER DEFAULT 0,
  conversion_rate      DECIMAL(5,2),
  revenue_generated    DECIMAL(10,2),
  peak_engagement_time TIMESTAMP,
  metrics_date         DATE DEFAULT CURRENT_DATE,
  last_calculated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(content_id, content_type, metrics_date)
);
CREATE INDEX idx_content_metrics_type       ON content_metrics(content_type);
CREATE INDEX idx_content_metrics_date       ON content_metrics(metrics_date);
CREATE INDEX idx_content_metrics_engagement ON content_metrics(engagement_rate);
```

---

## 9. SUBSCRIPTION SERVICE DATABASE (subscription_service_db)

### Plans (prototype shows premium upsell and trial)
```sql
CREATE TABLE subscription_plans (
  id             BIGSERIAL PRIMARY KEY,
  plan_code      VARCHAR(50) NOT NULL UNIQUE,
  name           VARCHAR(100) NOT NULL,
  description    TEXT,
  price_amount   DECIMAL(10,2) NOT NULL,
  currency       VARCHAR(3) DEFAULT 'GHS',
  billing_cycle  VARCHAR(20) DEFAULT 'MONTHLY', -- MONTHLY, YEARLY
  features       JSONB, -- list of feature flags/limits
  is_active      BOOLEAN DEFAULT true,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### User Subscriptions
```sql
CREATE TABLE user_subscriptions (
  id               BIGSERIAL PRIMARY KEY,
  user_id          BIGINT NOT NULL, -- ref users
  plan_id          BIGINT NOT NULL REFERENCES subscription_plans(id),
  status           VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, TRIALING, CANCELED, EXPIRED
  trial_end_at     TIMESTAMP,
  current_period_start TIMESTAMP,
  current_period_end   TIMESTAMP,
  cancel_at_period_end BOOLEAN DEFAULT false,
  created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(user_id)
);
CREATE INDEX idx_user_subscriptions_user ON user_subscriptions(user_id);
```

---

## 10. Search & Indexing Strategy

- Full-text search indexes on `users(first_name,last_name,username)`, `posts(content)`, `products(title,description)`.
- Composite indexes for feeds: `(faculty_id, created_at, visibility)` filtered where `is_deleted=false`.
- Leaderboard: `(season_id, score DESC)`.
- Messages: `(room_id, created_at DESC)`.

---

## 11. Data Validation & Constraints (Service Layer)

- Sanitize user-generated content (posts, comments, stories, product descriptions) to prevent XSS.
- Enforce E.164 phone format; Ghana validations for mobile money.
- Prices use DECIMAL with currency `GHS` by default; avoid floating math in services.
- File uploads validated for type/size; store metadata in `media_files`/`product_images`.
- Story expiry enforced on read and via background cleanup.
- Order totals recalculated server-side; do not trust client cart totals.

---

## 12. Cross-Service References

As with the existing architecture, services store foreign keys to external services as numeric IDs and resolve details through service APIs (e.g., content → user, ecommerce → user, social → content). Caching and denormalization (metrics tables) are leveraged for performance.


