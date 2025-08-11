package com.raved.subscription.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_subscriptions")
public class UserSubscription {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "plan_id", nullable = false)
  private Long planId;

  @Column(length = 20)
  private String status = "ACTIVE"; // ACTIVE, TRIALING, CANCELED, EXPIRED

  @Column(name = "trial_end_at")
  private Instant trialEndAt;

  @Column(name = "current_period_start")
  private Instant currentPeriodStart;

  @Column(name = "current_period_end")
  private Instant currentPeriodEnd;

  @Column(name = "cancel_at_period_end")
  private Boolean cancelAtPeriodEnd = false;

  @Column(name = "created_at")
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  private Instant updatedAt = Instant.now();

  // Relationships
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_id", insertable = false, updatable = false)
  private SubscriptionPlan plan;

  // Constructors
  public UserSubscription() {}

  public UserSubscription(Long userId, Long planId) {
    this.userId = userId;
    this.planId = planId;
    this.status = "ACTIVE";
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  // Getters and Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public Long getPlanId() { return planId; }
  public void setPlanId(Long planId) { this.planId = planId; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public Instant getTrialEndAt() { return trialEndAt; }
  public void setTrialEndAt(Instant trialEndAt) { this.trialEndAt = trialEndAt; }

  public Instant getCurrentPeriodStart() { return currentPeriodStart; }
  public void setCurrentPeriodStart(Instant currentPeriodStart) { this.currentPeriodStart = currentPeriodStart; }

  public Instant getCurrentPeriodEnd() { return currentPeriodEnd; }
  public void setCurrentPeriodEnd(Instant currentPeriodEnd) { this.currentPeriodEnd = currentPeriodEnd; }

  public Boolean getCancelAtPeriodEnd() { return cancelAtPeriodEnd; }
  public void setCancelAtPeriodEnd(Boolean cancelAtPeriodEnd) { this.cancelAtPeriodEnd = cancelAtPeriodEnd; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

  public SubscriptionPlan getPlan() { return plan; }
  public void setPlan(SubscriptionPlan plan) { this.plan = plan; }

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

  // Business logic methods
  public boolean isActive() {
    return "ACTIVE".equals(this.status) || "TRIALING".equals(this.status);
  }

  public boolean isTrial() {
    return "TRIALING".equals(this.status);
  }

  public boolean isExpired() {
    return "EXPIRED".equals(this.status);
  }

  public boolean isCanceled() {
    return "CANCELED".equals(this.status);
  }

  public void activate() {
    this.status = "ACTIVE";
    this.updatedAt = Instant.now();
  }

  public void cancel() {
    this.status = "CANCELED";
    this.updatedAt = Instant.now();
  }

  public void expire() {
    this.status = "EXPIRED";
    this.updatedAt = Instant.now();
  }
}


