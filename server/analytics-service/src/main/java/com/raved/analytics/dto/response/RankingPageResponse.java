package com.raved.analytics.dto.response;

import com.raved.analytics.model.RankingSnapshot;

import java.time.LocalDate;
import java.util.List;

public class RankingPageResponse {
    private String snapshotType;
    private String metric;
    private String period;
    private LocalDate date;
    private String category;
    private int page;
    private int size;
    private int totalItems;
    private List<RankingSnapshot.Item> items;

    public String getSnapshotType() { return snapshotType; }
    public void setSnapshotType(String snapshotType) { this.snapshotType = snapshotType; }
    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public List<RankingSnapshot.Item> getItems() { return items; }
    public void setItems(List<RankingSnapshot.Item> items) { this.items = items; }
}

