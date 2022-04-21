package com.example.covider.model.building;

import java.util.Objects;

public class Building {

    private long id;
    private String name;
    private String requirement;
    private static final String defaultRequirement = "There's no special requirement for the building, but please be aware";

    public Building(long id, String name) {
        this.id = id;
        this.name = name;
        this.requirement = defaultRequirement;
    }

    public Building(long id, String name, String req) {
        this.id = id;
        this.name = name;
        this.requirement = req;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequirement() {
        return requirement;
    }

    public static String getDefaultRequirement() {
        return defaultRequirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return id == building.id && name.equals(building.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }



}
