package com.example.covider.database.risk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.checkin.CheckinManager;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.report.ReportManager;
import com.example.covider.model.checkin.Checkin;
import com.example.covider.model.report.BuildingRiskReport;
import com.example.covider.model.report.CourseRiskReport;
import com.example.covider.model.report.UserDailyReport;
import com.example.covider.model.user.Student;
import com.example.covider.model.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RiskManager extends DatabaseHandler {

    private static RiskManager instance = null;
    private static EnrollmentManager enrollmentManager = null;
    private static CheckinManager checkinManager = null;
    private static ReportManager reportManager = null;
    private static CourseManager courseManager = null;

    private static final long MILLIS_PER_DAY = 86400000L;

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Nothing needs to be done
    }

    public static RiskManager getInstance(Context context){
        ManagerFactory.initialize(context);
        if (enrollmentManager == null){
            enrollmentManager = ManagerFactory.getEnrollmentManagerInstance();
        }

        if (checkinManager == null){
            checkinManager = ManagerFactory.getCheckinManagerInstance();
        }

        if (reportManager == null){
            reportManager = ManagerFactory.getReportManagerInstance();
        }

        if (courseManager == null){
            courseManager = ManagerFactory.getCourseManagerInstance();
        }

        if (instance == null){
            instance = new RiskManager(context);
        }
        return instance;
    }

    // consider move to mode
    public enum UserRisk{
        LOW_RISK,
        HIGH_RISK,
        POSITIVE
    }

    private RiskManager(Context context){
        super(context);
        // No need to create table here
    }

    public UserRisk getUserRisk(long userId){
        UserDailyReport userDailyReport = reportManager.getUserMostRecentReport(userId);
        // if a user does not have any previous report, they are considered Positive
        if (userDailyReport == null){
            return UserRisk.POSITIVE;
        }
        if (userDailyReport.getIsPositive() == 1){
            return UserRisk.POSITIVE;
        }
        if (userDailyReport.getNote() != null && !userDailyReport.getNote().isEmpty()){
            return UserRisk.HIGH_RISK;
        }
        return UserRisk.LOW_RISK;
    }

    //hard coded to 2 days
    public BuildingRiskReport getReportForBuilding(long buildingId){
        return getReportForBuilding(buildingId, 3 * MILLIS_PER_DAY);
    }

    public BuildingRiskReport getReportForBuilding(long buildingId, long spanTimeMillis){
        long spanStartTime = System.currentTimeMillis() - spanTimeMillis;
        long spanEndTime = System.currentTimeMillis();

        BuildingRiskReport buildingRiskReport = new BuildingRiskReport("Last 2 Days", spanStartTime, spanEndTime);

        ArrayList<Checkin> checkins = checkinManager.getBuildingCheckinsInTimeSpan(buildingId, spanStartTime, spanEndTime);
        Set<Long> userIdSet = new HashSet<>();
        for (Checkin c : checkins) {
            Long aLong = Long.valueOf(c.getUserId());
            userIdSet.add(aLong);
        }

        for (Long userid : userIdSet){
            UserRisk risk = getUserRisk(userid);
            if (risk == UserRisk.POSITIVE) {
                buildingRiskReport.numPositiveVisitors += 1;
            } else if ( risk == UserRisk.HIGH_RISK){
                buildingRiskReport.numHighRiskVisitors += 1;
            } else if ( risk == UserRisk.LOW_RISK){
                buildingRiskReport.numLowRiskVisitors += 1;
            } else {
                System.out.println("Something went wrong in RiskManager.getReportForBuilding");
            }
        }

        return buildingRiskReport;
    }

    public CourseRiskReport getReportForCourse(long courseId){
        CourseRiskReport courseRiskReport = new CourseRiskReport();

        for (Student s : enrollmentManager.getStudentsEnrollingIn(courseId)) {
            UserRisk risk = getUserRisk(s.getId());

            if (risk == UserRisk.POSITIVE) {
                courseRiskReport.positiveStudents += 1;
            } else if ( risk == UserRisk.HIGH_RISK){
                courseRiskReport.highRiskStudents += 1;
            } else if ( risk == UserRisk.LOW_RISK){
                courseRiskReport.lowRiskStudents += 1;
            } else {
                courseRiskReport.lowRiskStudents -= 100;
            }

        }

        long buildingId = courseManager.getCourse(courseId).getBuilding();
        courseRiskReport.buildingRiskReport = getReportForBuilding(buildingId);

        return courseRiskReport;
    }




}
