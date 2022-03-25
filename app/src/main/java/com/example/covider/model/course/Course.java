package com.example.covider.model.course;

import java.util.Objects;

public class Course {
    private long id;
    private String name;
    private long building;
    private int isOnline; // 1 if is Online, 0 if In Person, 2 to be defined

    public Course(long id, String name, long building) {
        this.id = id;
        this.name = name;
        this.building = building;
        this.isOnline = 0;
    }

    public Course(long id, String name, long building, int isOnline) {
        this.id = id;
        this.name = name;
        this.building = building;
        this.isOnline = isOnline;
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

    public long getBuilding() {
        return building;
    }

    public void setBuilding(long building) {
        this.building = building;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id && building == course.building && isOnline == course.isOnline && Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, building, isOnline);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", building=" + building +
                ", isOnline=" + isOnline +
                '}';
    }


}
