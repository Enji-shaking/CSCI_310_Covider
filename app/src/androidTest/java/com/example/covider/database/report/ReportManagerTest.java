package com.example.covider.database.report;

import android.content.Context;


import static org.junit.Assert.*;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.covider.database.ManagerFactory;
import com.example.covider.model.report.UserDailyReport;

import org.junit.Before;
import org.junit.Test;

public class ReportManagerTest {
    /*
    ReportManager reportManager;

    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        ManagerFactory.initialize(instrumentationContext);
        reportManager = ManagerFactory.getReportManagerInstance();
        reportManager.create_default_report();
    }

    @Test
    public void testDefaultReports(){
        UserDailyReport userOneExpected = new UserDailyReport(10003, 1, 1, "Positive", 1646896887002L);
        UserDailyReport userOneById = reportManager.getReport(userOneExpected.getId());
        assertEquals(userOneExpected.getId(), userOneById.getId());
        assertEquals(userOneExpected.getUserId(),userOneById.getUserId());
        assertEquals(userOneExpected.getTimestamp(),userOneById.getTimestamp());
        assertEquals(userOneExpected.getIsPositive(),userOneById.getIsPositive());

        UserDailyReport userOneMostRecent = reportManager.getUserMostRecentReport(userOneExpected.getUserId());
        assertEquals(userOneExpected.getId(), userOneMostRecent.getId());
        assertEquals(userOneExpected.getUserId(),userOneMostRecent.getUserId());
        assertEquals(userOneExpected.getTimestamp(),userOneMostRecent.getTimestamp());
        assertEquals(userOneExpected.getIsPositive(),userOneMostRecent.getIsPositive());
    }
    */
}
