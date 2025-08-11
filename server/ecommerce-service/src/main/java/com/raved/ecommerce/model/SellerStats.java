package com.raved.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SellerStats entity for TheRavedApp
 * Represents statistics for sellers in the ecommerce system.
 * Based on the seller_stats table schema from migration V10.
 */
@Entity
@Table(name = "seller_stats")
public class SellerStats {
    
    @Id
    @Column(name = "seller_id")
    private Long sellerId;
    
    @Column(name = "total_items", columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalItems = 0;
    
    @Column(name = "total_sales", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal totalSales = BigDecimal.ZERO;
    
    @Column(name = "items_sold", columnDefinition = "INTEGER DEFAULT 0")
    private Integer itemsSold = 0;
    
    @Column(name = "rating", precision = 3, scale = 2, columnDefinition = "DECIMAL(3,2) DEFAULT 0")
    private BigDecimal rating = BigDecimal.ZERO;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public SellerStats() {
    }
    
    public SellerStats(Long sellerId) {
        this.sellerId = sellerId;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Lifecycle methods
    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Business logic methods
    public void incrementTotalItems() {
        this.totalItems++;
    }
    
    public void decrementTotalItems() {
        if (this.totalItems > 0) {
            this.totalItems--;
        }
    }
    
    public void addSale(BigDecimal amount) {
        this.totalSales = this.totalSales.add(amount);
        this.itemsSold++;
    }
    
    public void updateRating(BigDecimal newRating) {
        this.rating = newRating;
    }
    
    public BigDecimal getAverageRating() {
        return this.itemsSold > 0 ? this.rating : BigDecimal.ZERO;
    }
    
    // Getters and setters
    public Long getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    
    public Integer getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
    
    public BigDecimal getTotalSales() {
        return totalSales;
    }
    
    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }
    
    public Integer getItemsSold() {
        return itemsSold;
    }
    
    public void setItemsSold(Integer itemsSold) {
        this.itemsSold = itemsSold;
    }
    
    public BigDecimal getRating() {
        return rating;
    }
    
    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "SellerStats{" +
                "sellerId=" + sellerId +
                ", totalItems=" + totalItems +
                ", totalSales=" + totalSales +
                ", itemsSold=" + itemsSold +
                ", rating=" + rating +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
