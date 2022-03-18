package com.example.covider.model.report;

public class CourseRiskReport {
    public long lowRiskStudents;
    public long highRiskStudents;
    public long positiveStudents;
    public BuildingRiskReport buildingRiskReport;

    public CourseRiskReport() {
    }

    @Override
    public String toString() {
        return "CourseRiskReport{" +
                "lowRiskStudents=" + lowRiskStudents +
                ", highRiskStudents=" + highRiskStudents +
                ", positiveStudents=" + positiveStudents +
                ", buildingRiskReport=" + buildingRiskReport +
                '}';
    }
}
