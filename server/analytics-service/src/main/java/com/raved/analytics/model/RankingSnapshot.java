package com.raved.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "rankings_snapshots")
@CompoundIndexes({
    @CompoundIndex(name = "idx_type_metric_date", def = "{'snapshotType':1,'metric':1,'period':1,'date':1}"),
    @CompoundIndex(name = "idx_category_date", def = "{'category':1,'period':1,'date':1}"),
    @CompoundIndex(name = "idx_entity_rank", def = "{'snapshotType':1,'metric':1,'period':1,'date':1,'items.entityId':1,'items.rank':1}")
})
public class RankingSnapshot {

    @Id
    private String id;

    @Indexed
    private String snapshotType; // USER, CONTENT

    @Indexed
    private String metric; // ENGAGEMENT, INFLUENCE, VIRALITY

    @Indexed
    private String period; // DAILY, WEEKLY, MONTHLY

    @Indexed
    private LocalDate date; // period start

    @Indexed
    private String category; // e.g., contentCategory or faculty (optional)

    private LocalDateTime computedAt;

    private List<Item> items;

    public static class Item {

        private String entityId; // userId or contentId
        private Integer rank;
        private BigDecimal score;
        private String contentType; // optional for content

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public BigDecimal getScore() {
            return score;
        }

        public void setScore(BigDecimal score) {
            this.score = score;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSnapshotType() {
        return snapshotType;
    }

    public void setSnapshotType(String snapshotType) {
        this.snapshotType = snapshotType;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getComputedAt() {
        return computedAt;
    }

    public void setComputedAt(LocalDateTime computedAt) {
        this.computedAt = computedAt;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
