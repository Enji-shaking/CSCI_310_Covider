package com.example.covider.database.risk;

import android.content.Context;


import static org.junit.Assert.*;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.covider.database.ManagerFactory;
import com.example.covider.database.checkin.CheckinManager;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.report.ReportManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.course.Course;
import com.example.covider.model.enrollment.Enrollment;
import com.example.covider.model.report.BuildingRiskReport;
import com.example.covider.model.report.CourseRiskReport;
import com.example.covider.model.report.UserDailyReport;
import com.example.covider.model.user.User;

import org.junit.Before;
import org.junit.Test;

public class RiskManagerTest {
    /*

    RiskManager riskManager;
    EnrollmentManager enrollmentManager;
    CheckinManager checkinManager;
    ReportManager reportManager;
    CourseManager courseManager;
    UserManager userManager;

    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        ManagerFactory.initialize(instrumentationContext);
        riskManager = ManagerFactory.getRiskManagerInstance();
        reportManager = ManagerFactory.getReportManagerInstance();
        checkinManager = ManagerFactory.getCheckinManagerInstance();
        enrollmentManager = ManagerFactory.getEnrollmentManagerInstance();
        courseManager = ManagerFactory.getCourseManagerInstance();
        userManager = ManagerFactory.getUserManagerInstance();

        reportManager.addOrUpdateReport(new UserDailyReport(10009, 9, 1, "Stay Positive", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(10010, 10, 0, "Fever", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(10011, 11, 0, "", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(10012, 12, 1, "", System.currentTimeMillis()));

        checkinManager.addCheckin(9, 99);
        checkinManager.addCheckin(10, 99);
        checkinManager.addCheckin(11, 99);

        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1009, 9, 101, 1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1010, 10,101,1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1011, 11,101,1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1012, 12, 101,0));

        courseManager.addOrUpdateCourse(new Course(101,"Course For Testing", 99, 0));

        userManager.addOrUpdateUser(new User(9,"RiskTester1", "12345678", 1));
        userManager.addOrUpdateUser(new User(10,"RiskTester2", "12345678", 1));
        userManager.addOrUpdateUser(new User(11,"RiskTester3", "12345678", 1));
        userManager.addOrUpdateUser(new User(12,"RiskTester4", "12345678", 0));
    }

    @Test
    public void testGetUserRisk(){
        RiskManager.UserRisk expectedPositive = RiskManager.UserRisk.POSITIVE;
        RiskManager.UserRisk positive = riskManager.getUserRisk(9);
        assertEquals(expectedPositive,positive);
    }

    @Test
    public void testGetReportForBuilding(){
        BuildingRiskReport buildingRiskReport = riskManager.getReportForBuilding(99);
        assertEquals(3,buildingRiskReport.getNumVisitors());
        assertEquals(1,buildingRiskReport.numHighRiskVisitors);
        assertEquals((1.0+0.3+0.0)/3.0,buildingRiskReport.getRiskIndex(),0.01);
    }

    @Test
    public void testGetReportForCourse(){
        CourseRiskReport courseRiskReport = riskManager.getReportForCourse(101);
//        assertEquals("#",courseRiskReport.toString());
        assertEquals(1,courseRiskReport.highRiskStudents);

        // Testing the inner class of CourseRiskReport (which is BuildingRiskReport)
        BuildingRiskReport buildingRiskReport = courseRiskReport.buildingRiskReport;
        assertNotNull(buildingRiskReport);
//        assertEquals(3,buildingRiskReport.getNumVisitors());
//        assertEquals(1,buildingRiskReport.numHighRiskVisitors);
//        assertEquals((1.0+0.3+0.0)/3.0,buildingRiskReport.getRiskIndex(),0.01);
    }


    */


}
