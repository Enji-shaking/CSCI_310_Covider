package com.example.covider.model.enrollment;

public class Enrollment {
    private long id;
    private long userId;
    private long courseId;
    private int is_student;

    public Enrollment(long id, long userId, long courseId, int is_student) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.is_student = is_student;
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

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getIs_student() {
        return is_student;
    }

    public void setIs_student(int is_student) {
        this.is_student = is_student;
    }
}
