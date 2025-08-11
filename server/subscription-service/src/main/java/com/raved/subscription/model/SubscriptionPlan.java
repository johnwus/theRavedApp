package com.raved.subscription.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "plan_code", nullable = false, unique = true, length = 50)
  private String planCode;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(columnDefinition = "text")
  private String description;

  @Column(name = "price_amount", nullable = false)
  private BigDecimal priceAmount;

  @Column(length = 3)
  private String currency = "GHS";

  @Column(name = "billing_cycle", length = 20)
  private String billingCycle = "MONTHLY"; // MONTHLY, YEARLY

  @Column(columnDefinition = "jsonb")
  private String features;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @Column(name = "created_at")
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  private Instant updatedAt = Instant.now();

  // Constructors
  public SubscriptionPlan() {
  }

  public SubscriptionPlan(String planCode, String name, String description, BigDecimal priceAmount) {
    this.planCode = planCode;
    this.name = name;
    this.description = description;
    this.priceAmount = priceAmount;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  // Convenience method for services that expect getPlanId()
  public Long getPlanId() {
    return this.id;
  }

  public void setPlanId(Long planId) {
    this.id = planId;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPlanCode() {
    return planCode;
  }

  public void setPlanCode(String planCode) {
    this.planCode = planCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPriceAmount() {
    return priceAmount;
  }

  public void setPriceAmount(BigDecimal priceAmount) {
    this.priceAmount = priceAmount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getBillingCycle() {
    return billingCycle;
  }

  public void setBillingCycle(String billingCycle) {
    this.billingCycle = billingCycle;
  }

  public String getFeatures() {
    return features;
  }

  public void setFeatures(String features) {
    this.features = features;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  // Lifecycle methods
  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = Instant.now();
  }
}
