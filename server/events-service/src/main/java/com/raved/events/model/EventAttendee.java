package com.raved.events.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "event_attendees")
@IdClass(EventAttendeeId.class)
public class EventAttendee {
    
    @Id
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AttendeeStatus status = AttendeeStatus.INTERESTED;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    public enum AttendeeStatus {
        INTERESTED, GOING, CHECKED_IN, CANCELLED
    }

    // Constructors
    public EventAttendee() {
        this.joinedAt = Instant.now();
    }

    public EventAttendee(Long eventId, Long userId, AttendeeStatus status) {
        this();
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
    }

    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AttendeeStatus getStatus() {
        return status;
    }

    public void setStatus(AttendeeStatus status) {
        this.status = status;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    @PrePersist
    public void prePersist() {
        this.joinedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "EventAttendee{" +
                "eventId=" + eventId +
                ", userId=" + userId +
                ", status=" + status +
                ", joinedAt=" + joinedAt +
                '}';
    }
}

// Composite primary key class
class EventAttendeeId implements java.io.Serializable {
    private Long eventId;
    private Long userId;

    public EventAttendeeId() {}

    public EventAttendeeId(Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventAttendeeId that = (EventAttendeeId) o;
        return Objects.equals(eventId, that.eventId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId);
    }
}


