package com.raved.notification.dto.response;

import java.util.Map;

/**
 * Statistics for notification delivery
 */
public class DeliveryStats {

    private long totalSent;
    private long totalDelivered;
    private long totalFailed;
    private double deliveryRate;
    private double failureRate;
    private Map<String, Long> deliveryByChannel;

    // Constructors
    public DeliveryStats() {}

    public DeliveryStats(long totalSent, long totalDelivered, long totalFailed, 
                        Map<String, Long> deliveryByChannel) {
        this.totalSent = totalSent;
        this.totalDelivered = totalDelivered;
        this.totalFailed = totalFailed;
        this.deliveryByChannel = deliveryByChannel;
        
        // Calculate rates
        if (totalSent > 0) {
            this.deliveryRate = (double) totalDelivered / totalSent * 100;
            this.failureRate = (double) totalFailed / totalSent * 100;
        } else {
            this.deliveryRate = 0;
            this.failureRate = 0;
        }
    }

    // Getters and Setters
    public long getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(long totalSent) {
        this.totalSent = totalSent;
    }

    public long getTotalDelivered() {
        return totalDelivered;
    }

    public void setTotalDelivered(long totalDelivered) {
        this.totalDelivered = totalDelivered;
    }

    public long getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(long totalFailed) {
        this.totalFailed = totalFailed;
    }

    public double getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(double deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(double failureRate) {
        this.failureRate = failureRate;
    }

    public Map<String, Long> getDeliveryByChannel() {
        return deliveryByChannel;
    }

    public void setDeliveryByChannel(Map<String, Long> deliveryByChannel) {
        this.deliveryByChannel = deliveryByChannel;
    }
}
