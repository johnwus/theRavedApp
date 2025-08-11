package com.raved.ecommerce.model;

/**
 * Order status enum for TheRavedApp
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED,
    REFUND_REQUESTED,
    PARTIALLY_REFUNDED
}