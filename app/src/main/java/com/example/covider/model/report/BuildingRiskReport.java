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
                ", riskIndex=" + getRiskIndex() +
                '}';
    }

    public long getNumVisitors(){
        return numLowRiskVisitors + numHighRiskVisitors + numPositiveVisitors;
    }
    public double getRiskIndex(){
        if(getNumVisitors() == 0){
            return 0;
        }
        return (1.0 * numPositiveVisitors + 0.3 * numHighRiskVisitors) / getNumVisitors();
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSpanStartTime() {
        return spanStartTime;
    }

    public void setSpanStartTime(long spanStartTime) {
        this.spanStartTime = spanStartTime;
    }

    public long getSpanEndTime() {
        return spanEndTime;
    }

    public void setSpanEndTime(long spanEndTime) {
        this.spanEndTime = spanEndTime;
    }

    public long getNumLowRiskVisitors() {
        return numLowRiskVisitors;
    }

    public void setNumLowRiskVisitors(long numLowRiskVisitors) {
        this.numLowRiskVisitors = numLowRiskVisitors;
    }

    public long getNumHighRiskVisitors() {
        return numHighRiskVisitors;
    }

    public void setNumHighRiskVisitors(long numHighRiskVisitors) {
        this.numHighRiskVisitors = numHighRiskVisitors;
    }

    public long getNumPositiveVisitors() {
        return numPositiveVisitors;
    }

    public void setNumPositiveVisitors(long numPositiveVisitors) {
        this.numPositiveVisitors = numPositiveVisitors;
    }
}
