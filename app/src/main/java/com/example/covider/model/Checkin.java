package com.example.covider.model;

import java.util.Objects;

public class Checkin {
    private long id;
    private long userId;
    private long buildingId;
    private long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checkin checkin = (Checkin) o;
        return getId() == checkin.getId()
                && getUserId() == checkin.getUserId()
                && getBuildingId() == checkin.getBuildingId()
                && getTimestamp() == checkin.getTimestamp();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getBuildingId(), getTimestamp());
    }

    public Checkin(long id, int userId, int buildingId, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.buildingId = buildingId;
        this.timestamp = timestamp;
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

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
