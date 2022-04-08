package com.example.covider.database.checkin;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.model.checkin.Checkin;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class CheckinManagerTest {
    CheckinManager cm;
    @Before
    public void setUp() {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Config.Change_Test();
        ManagerFactory.initialize(instrumentationContext);
        cm = ManagerFactory.getCheckinManagerInstance();
    }

//    @Test
//    public void once(){
//        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        Config.Change_Test();
//        instrumentationContext.deleteDatabase(Config.DATABASE_NAME);
//    }

    @Test
    public void testDefaultCheckin(){
        cm.addOrUpdateCheckin(new Checkin(10, 999, 111, 1646896886094L));
        Checkin c = cm.getCheckin(10);
        Assert.assertEquals(c.getId(), 10);
        Assert.assertEquals(c.getUserId(), 999);
        Assert.assertEquals(c.getBuildingId(), 111);
        Assert.assertEquals(c.getTimestamp(), 1646896886094L);
        cm.deleteCheckin(10);
    }

    @Test
    public void testDeletion(){
        Checkin checkin = cm.generateNewCheckin(999, 111, 555555);
        cm.addOrUpdateCheckin(checkin);
        cm.deleteCheckin(checkin.getId());
        Checkin got = cm.getCheckin(checkin.getId());
        Assert.assertNull(got);
    }

//    @Test
//    public void testGetId(){
//        assertNotEquals(cm.getNextId(CheckinManager.getTableName()), -1);
////        System.out.println(cm.getNextId(CheckinManager.getTableName()));
//    }

    @Test
    public void testGetBuildingCheckinsInTimeSpan(){
        Checkin checkin = cm.generateNewCheckin(999, 888, 100000001L);
        cm.addOrUpdateCheckin(checkin);
        Checkin checkin2 = cm.generateNewCheckin(996, 888, 100000003L);
        cm.addOrUpdateCheckin(checkin2);

        ArrayList<Checkin> list =  cm.getBuildingCheckinsInTimeSpan(888, 100000000L, 100000004L);
        Assert.assertEquals(2, list.size());

        ArrayList<Checkin> listOne =  cm.getBuildingCheckinsInTimeSpan(888, 100000000L, 100000001L);
        Assert.assertEquals(1, listOne.size());

        ArrayList<Checkin> listEmpty =  cm.getBuildingCheckinsInTimeSpan(888, 0L, 1L);
        Assert.assertEquals(0, listEmpty.size());

        cm.deleteCheckin(checkin.getId());
        cm.deleteCheckin(checkin2.getId());
    }

    @Test
    public void testGetUserCheckinsInTimeSpan(){
        Checkin checkin1 = cm.generateNewCheckin(1006, 777, 100000002L);
        cm.addOrUpdateCheckin(checkin1);
        Checkin checkin2 = cm.generateNewCheckin(1006, 888, 100000003L);
        cm.addOrUpdateCheckin(checkin2);

        ArrayList<Checkin> list =  cm.getUserCheckinsInTimeSpan(1006, 100000000L, 100000004L);
        Assert.assertEquals(2, list.size());

        ArrayList<Checkin> listOne =  cm.getUserCheckinsInTimeSpan(1006, 100000000L, 100000002L); //Inclusive
        Assert.assertEquals(1, listOne.size());

        ArrayList<Checkin> listEmpty =  cm.getUserCheckinsInTimeSpan(1006, 0L, 1L);
        Assert.assertEquals(0, listEmpty.size());

        cm.deleteCheckin(checkin1.getId());
        cm.deleteCheckin(checkin2.getId());
    }

    @Test
    public void testGetFrequentVisit(){
        cm.addOrUpdateCheckin(new Checkin(10, 999, 111, 1646896886094L));
        ArrayList<Long> buildingIds = cm.getFrequentVisit(999);
        Assert.assertEquals(1, buildingIds.size());
        Assert.assertEquals(Long.valueOf(111), buildingIds.get(0));

        ArrayList<Long> buildingIds2 = cm.getFrequentVisit(999,0);
        Assert.assertEquals(0, buildingIds2.size());
    }


    @Test
    public void testGetCloseContact(){ // TODO: ADD DUMMY DATA
        cm.addOrUpdateCheckin(new Checkin(11, 998, 111, System.currentTimeMillis()-10));
        cm.addOrUpdateCheckin(new Checkin(10, 999, 111, System.currentTimeMillis()));
        ArrayList<Long> closeContacts = cm.getCloseContact(999);
        Assert.assertEquals(1, closeContacts.size());
        Assert.assertEquals(998L, (long)closeContacts.get(0));
        cm.deleteCheckin(10);
        cm.deleteCheckin(11);
    }

    @After
    public void clean(){
        Config.Change_Normal();
    }

}