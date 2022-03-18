package com.example.covider.database;

import android.content.Context;

import com.example.covider.database.building.BuildingManager;
import com.example.covider.database.checkin.CheckinManager;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.notification.NotificationManager;
import com.example.covider.database.report.ReportManager;
import com.example.covider.database.risk.RiskManager;
import com.example.covider.database.user.UserManager;

public class ManagerFactory {
    private static CourseManager courseManagerInstance = null;
    private static EnrollmentManager enrollmentManagerInstance = null;
    private static NotificationManager notificationManagerInstance = null;
    private static UserManager userManagerInstance = null;
    private static CheckinManager checkinManagerInstance = null;
    private static BuildingManager buildingManagerInstance = null;
    private static ReportManager reportManagerInstance = null;
    private static RiskManager riskManagerInstance = null;


    private static boolean initialized = false;

    // By doing this, all the default constructor and the default information would be plugged in to the com.example.covider.database
    // no need to plug in context when want to getting the instances
    public static void initialize(Context context){
        if(!initialized){
            courseManagerInstance = CourseManager.getInstance(context);
            enrollmentManagerInstance = EnrollmentManager.getInstance(context);
            notificationManagerInstance = NotificationManager.getInstance(context);
            userManagerInstance = UserManager.getInstance(context);
            checkinManagerInstance = CheckinManager.getInstance(context);
            buildingManagerInstance = BuildingManager.getInstance(context);
            reportManagerInstance = ReportManager.getInstance(context);
            riskManagerInstance = RiskManager.getInstance(context);
        }
    }

    public static CourseManager getCourseManagerInstance() {
        return courseManagerInstance;
    }

    public static EnrollmentManager getEnrollmentManagerInstance() {
        return enrollmentManagerInstance;
    }

    public static NotificationManager getNotificationManagerInstance() {
        return notificationManagerInstance;
    }

    public static UserManager getUserManagerInstance() {
        return userManagerInstance;
    }

    public static CheckinManager getCheckinManagerInstance() {
        return checkinManagerInstance;
    }

    public static BuildingManager getBuildingManagerInstance() {
        return buildingManagerInstance;
    }

    public static ReportManager getReportManagerInstance() {
        return reportManagerInstance;
    }

    public static RiskManager getRiskManagerInstance() {
        return riskManagerInstance;
    }
}
