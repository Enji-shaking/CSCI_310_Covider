package com.example.covider.model.report;

import java.util.Objects;

public class UserDailyReport {
    private long id;
    private long userId;
    private long isPositive;
    private String note;
    private long timestamp;

    public UserDailyReport(long id, long userId, long isPositive, String note, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.isPositive = isPositive;
        this.note = note;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDailyReport userDailyReport = (UserDailyReport) o;
        return id == userDailyReport.id && userId == userDailyReport.userId && isPositive == userDailyReport.isPositive && timestamp == userDailyReport.timestamp && Objects.equals(note, userDailyReport.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, isPositive, note, timestamp);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userId=" + userId +
                ", isPositive=" + isPositive +
                ", note='" + note + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(long isPositive) {
        this.isPositive = isPositive;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
