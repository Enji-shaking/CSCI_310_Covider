package com.example.covider.model.report;

public class BuildingRiskReport {
    public String description;
    public long spanStartTime;
    public long spanEndTime;
    public long numLowRiskVisitors; // number of Low Risk Visitors in Time Span <spanStartTime, spanEndTime>
    public long numHighRiskVisitors;
    public long numPositiveVisitors;


    public BuildingRiskReport() {
    }

    public BuildingRiskReport(String description, long spanStartTime, long spanEndTime) {
        this.description = description;
        this.spanStartTime = spanStartTime;
        this.spanEndTime = spanEndTime;
    }

    @Override
    public String toString() {
        return "BuildingRiskReport{" +
                "description='" + description + '\'' +
                ", spanStartTime=" + spanStartTime +
                ", spanEndTime=" + spanEndTime +
                ", numVisitors=" + getNumVisitors() +
                ", numLowRiskVisitors=" + numLowRiskVisitors +
                ", numHighRiskVisitors=" + numHighRiskVisitors +
                ", numPositiveVisitors=" + numPositiveVisitors +
                '}';
    }

    public long getNumVisitors(){
        return numLowRiskVisitors + numHighRiskVisitors + numPositiveVisitors;
    }
    public double getRiskIndex(){
        return (1.0 * numPositiveVisitors + 0.3 * numHighRiskVisitors) / getNumVisitors();
    };
}
