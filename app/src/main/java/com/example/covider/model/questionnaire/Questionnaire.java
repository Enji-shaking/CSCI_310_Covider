package com.example.covider.model.questionnaire;

import java.util.Objects;

public class Questionnaire {
    private long id;
    private long userId;
    private long buildingId;
    // do most people wear mask?
    private boolean masks;
    // is there enough sanitizer?
    private boolean sanitizer;
    // do people keep social distance?
    private boolean distance;
    // are there any people with symptoms?
    private boolean symptoms;

    public Questionnaire(long id, long userId, long buildingId, boolean masks, boolean sanitizer, boolean distance, boolean symptoms) {
        this.id = id;
        this.userId = userId;
        this.buildingId = buildingId;
        this.masks = masks;
        this.sanitizer = sanitizer;
        this.distance = distance;
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", userId=" + userId +
                ", buildingId=" + buildingId +
                ", masks=" + masks +
                ", sanitizer=" + sanitizer +
                ", distance=" + distance +
                ", symptoms=" + symptoms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;
        return getId() == that.getId() && getUserId() == that.getUserId() && buildingId == that.buildingId && isMasks() == that.isMasks() && isSanitizer() == that.isSanitizer() && isDistance() == that.isDistance() && isSymptoms() == that.isSymptoms();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), buildingId, isMasks(), isSanitizer(), isDistance(), isSymptoms());
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

    public boolean isMasks() {
        return masks;
    }

    public void setMasks(boolean masks) {
        this.masks = masks;
    }

    public boolean isSanitizer() {
        return sanitizer;
    }

    public void setSanitizer(boolean sanitizer) {
        this.sanitizer = sanitizer;
    }

    public boolean isDistance() {
        return distance;
    }

    public void setDistance(boolean distance) {
        this.distance = distance;
    }

    public boolean isSymptoms() {
        return symptoms;
    }

    public void setSymptoms(boolean symptoms) {
        this.symptoms = symptoms;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }
}
