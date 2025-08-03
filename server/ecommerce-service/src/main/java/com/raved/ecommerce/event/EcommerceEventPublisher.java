package com.raved.ecommerce.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Publisher for ecommerce events to Kafka
 */
@Component
public class EcommerceEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EcommerceEventPublisher.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.topics.ecommerce-events:ecommerce-events}")
    private String ecommerceEventsTopic;

    /**
     * Publish order placed event
     */
    public void publishOrderPlacedEvent(Long userId, Long orderId, String orderNumber, BigDecimal totalAmount, Map<String, Object> orderDetails) {
        logger.info("Publishing order placed event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>(orderDetails);
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        eventData.put("totalAmount", totalAmount);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "ORDER_PLACED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish order confirmed event
     */
    public void publishOrderConfirmedEvent(Long userId, Long orderId, String orderNumber, String paymentMethodId) {
        logger.info("Publishing order confirmed event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        eventData.put("paymentMethodId", paymentMethodId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "ORDER_CONFIRMED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish order shipped event
     */
    public void publishOrderShippedEvent(Long userId, Long orderId, String orderNumber, String trackingNumber, String carrier) {
        logger.info("Publishing order shipped event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        eventData.put("trackingNumber", trackingNumber);
        eventData.put("carrier", carrier);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "ORDER_SHIPPED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish order delivered event
     */
    public void publishOrderDeliveredEvent(Long userId, Long orderId, String orderNumber) {
        logger.info("Publishing order delivered event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "ORDER_DELIVERED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish order cancelled event
     */
    public void publishOrderCancelledEvent(Long userId, Long orderId, String orderNumber, String reason) {
        logger.info("Publishing order cancelled event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        eventData.put("reason", reason);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "ORDER_CANCELLED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish payment success event
     */
    public void publishPaymentSuccessEvent(Long userId, Long orderId, String orderNumber, BigDecimal amount, String paymentMethodId) {
        logger.info("Publishing payment success event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        eventData.put("amount", amount);
        eventData.put("paymentMethodId", paymentMethodId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "PAYMENT_SUCCESS");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish payment failed event
     */
    public void publishPaymentFailedEvent(Long userId, Long orderId, String orderNumber, BigDecimal amount, String reason) {
        logger.info("Publishing payment failed event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        eventData.put("amount", amount);
        eventData.put("reason", reason);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "PAYMENT_FAILED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish refund processed event
     */
    public void publishRefundProcessedEvent(Long userId, Long orderId, String orderNumber, BigDecimal refundAmount) {
        logger.info("Publishing refund processed event: order {} by user {}", orderNumber, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", orderId);
        eventData.put("orderNumber", orderNumber);
        eventData.put("refundAmount", refundAmount);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "REFUND_PROCESSED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish product viewed event
     */
    public void publishProductViewedEvent(Long userId, Long productId, String productName, Long sellerId) {
        logger.info("Publishing product viewed event: product {} by user {}", productId, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("productId", productId);
        eventData.put("productName", productName);
        eventData.put("sellerId", sellerId);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "PRODUCT_VIEWED");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish product added to cart event
     */
    public void publishProductAddedToCartEvent(Long userId, Long productId, String productName, int quantity, BigDecimal price) {
        logger.info("Publishing product added to cart event: product {} by user {}", productId, userId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("productId", productId);
        eventData.put("productName", productName);
        eventData.put("quantity", quantity);
        eventData.put("price", price);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", userId);
        event.put("ecommerceEventType", "PRODUCT_ADDED_TO_CART");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, userId.toString());
    }

    /**
     * Publish low stock alert event
     */
    public void publishLowStockAlertEvent(Long sellerId, Long productId, String productName, int currentStock, int threshold) {
        logger.info("Publishing low stock alert event: product {} for seller {}", productId, sellerId);
        
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("productId", productId);
        eventData.put("productName", productName);
        eventData.put("currentStock", currentStock);
        eventData.put("threshold", threshold);
        
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ECOMMERCE");
        event.put("userId", sellerId);
        event.put("ecommerceEventType", "LOW_STOCK_ALERT");
        event.put("eventData", eventData);
        event.put("timestamp", System.currentTimeMillis());
        
        publishEvent(event, sellerId.toString());
    }

    private void publishEvent(Map<String, Object> event, String key) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ecommerceEventsTopic, key, eventJson);
            logger.debug("Ecommerce event published successfully: {}", event.get("ecommerceEventType"));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize ecommerce event: {}", event.get("ecommerceEventType"), e);
        } catch (Exception e) {
            logger.error("Failed to publish ecommerce event: {}", event.get("ecommerceEventType"), e);
        }
    }
}
