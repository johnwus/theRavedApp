package com.raved.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Product Entity for TheRavedApp
 *
 * Represents products in the ecommerce system.
 * Based on the products table schema from migration V2.
 */
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_seller", columnList = "seller_id"),
        @Index(name = "idx_products_category", columnList = "category_id"),
        @Index(name = "idx_products_price", columnList = "price"),
        @Index(name = "idx_products_featured", columnList = "is_featured, featured_until"),
        @Index(name = "idx_products_created_at", columnList = "created_at"),
        @Index(name = "idx_products_available", columnList = "is_available"),
        @Index(name = "idx_products_condition", columnList = "condition")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId; // Reference to user service

    @Column(name = "post_id")
    private Long postId; // Reference to content service (linked outfit post)

    @NotBlank(message = "Product title is required")
    @Size(max = 255, message = "Product title must not exceed 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Product description is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @Size(max = 20, message = "Size must not exceed 20 characters")
    private String size;

    @Size(max = 50, message = "Color must not exceed 50 characters")
    private String color;

    @Size(max = 20, message = "Condition must not exceed 20 characters")
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'NEW'")
    private String condition = "NEW"; // NEW, LIKE_NEW, GOOD, FAIR

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Size(max = 3, message = "Currency must not exceed 3 characters")
    @Column(columnDefinition = "VARCHAR(3) DEFAULT 'GHS'")
    private String currency = "GHS";

    @Column(name = "is_negotiable", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isNegotiable = false;

    @Column(name = "quantity", columnDefinition = "INTEGER DEFAULT 1")
    private Integer quantity = 1;

    @Column(name = "is_available", columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAvailable = true;

    @Column(name = "shipping_cost", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name = "ships_from")
    private String shipsFrom;

    @Column(name = "processing_days", columnDefinition = "INTEGER DEFAULT 1")
    private Integer processingDays = 1;

    @Column(name = "views_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer viewsCount = 0;

    @Column(name = "likes_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer likesCount = 0;

    @Column(name = "saves_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer savesCount = 0;

    @Column(name = "orders_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer ordersCount = 0;

    // V11 migration fields
    @Column(name = "accepted_payment_methods", columnDefinition = "JSONB")
    private String acceptedPaymentMethods;

    @Column(name = "meetup_location", columnDefinition = "JSONB")
    private String meetupLocation;

    @Column(name = "contact_phone", columnDefinition = "VARCHAR(20)")
    private String contactPhone;

    @Column(name = "is_featured", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isFeatured = false;

    @Column(name = "featured_until")
    private LocalDateTime featuredUntil;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    // Constructors
    public Product() {
    }

    public Product(Long sellerId, String title, String description, BigDecimal price) {
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getSellerUserId() {
        return sellerId; // Alias for backward compatibility
    }

    public void setSellerUserId(Long sellerUserId) {
        this.sellerId = sellerUserId; // Alias for backward compatibility
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUniversityId() {
        // This field was removed from the schema, return null for backward
        // compatibility
        return null;
    }

    public void setUniversityId(Long universityId) {
        // This field was removed from the schema, no-op for backward compatibility
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return title; // Alias for backward compatibility
    }

    public void setName(String name) {
        this.title = name; // Alias for backward compatibility
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsNegotiable() {
        return isNegotiable;
    }

    public void setIsNegotiable(Boolean isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStockQuantity() {
        return quantity; // Alias for backward compatibility
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.quantity = stockQuantity; // Alias for backward compatibility
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getShipsFrom() {
        return shipsFrom;
    }

    public void setShipsFrom(String shipsFrom) {
        this.shipsFrom = shipsFrom;
    }

    public Integer getProcessingDays() {
        return processingDays;
    }

    public void setProcessingDays(Integer processingDays) {
        this.processingDays = processingDays;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getSavesCount() {
        return savesCount;
    }

    public void setSavesCount(Integer savesCount) {
        this.savesCount = savesCount;
    }

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
    }

    public String getAcceptedPaymentMethods() {
        return acceptedPaymentMethods;
    }

    public void setAcceptedPaymentMethods(String acceptedPaymentMethods) {
        this.acceptedPaymentMethods = acceptedPaymentMethods;
    }

    public String getMeetupLocation() {
        return meetupLocation;
    }

    public void setMeetupLocation(String meetupLocation) {
        this.meetupLocation = meetupLocation;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public LocalDateTime getFeaturedUntil() {
        return featuredUntil;
    }

    public void setFeaturedUntil(LocalDateTime featuredUntil) {
        this.featuredUntil = featuredUntil;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setIsDeleted(boolean isDeleted) {
        // This field was removed from the schema, use isActive instead
        this.isActive = !isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public String getImageUrls() {
        if (images == null || images.isEmpty()) {
            return "";
        }
        return images.stream()
                .map(ProductImage::getImageUrl)
                .reduce("", (a, b) -> a + "," + b)
                .replaceFirst("^,", "");
    }

    public void setImageUrls(String imageUrls) {
        // This method is for DTO mapping, actual images are managed through the images
        // list
        // Implementation can be added if needed for bulk image URL setting
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // Lifecycle methods
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public void incrementViews() {
        this.viewsCount++;
    }

    public void incrementLikes() {
        this.likesCount++;
    }

    public void decrementLikes() {
        if (this.likesCount > 0) {
            this.likesCount--;
        }
    }

    public void decreaseStock(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
            if (this.quantity == 0) {
                this.isAvailable = false;
            }
        }
    }

    public void increaseStock(int quantity) {
        this.quantity += quantity;
        if (this.quantity > 0) {
            this.isAvailable = true;
        }
    }

    public boolean isInStock() {
        return this.quantity > 0 && this.isAvailable;
    }

    public boolean isActive() {
        return this.isActive && this.isAvailable;
    }

    public boolean isFeaturedAndValid() {
        return this.isFeatured &&
                (this.featuredUntil == null || this.featuredUntil.isAfter(LocalDateTime.now()));
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", sellerId=" + sellerId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
