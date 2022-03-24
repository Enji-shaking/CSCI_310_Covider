package com.example.covider.database.checkin;

import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.covider.model.checkin.Checkin;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class CheckinManagerTest extends TestCase {
    CheckinManager cm;

    @Before
    public void setUp() {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        cm = CheckinManager.getInstance(instrumentationContext);
    }

    @Test
    public void testDefaultCheckin(){
        Checkin c = cm.getCheckin(10);
        assertEquals(c.getId(), 10);
        assertEquals(c.getUserId(), 999);
        assertEquals(c.getBuildingId(), 111);
        assertEquals(c.getTimestamp(), 1646896886094L);
    }

    @Test
    public void testDeletion(){
        Checkin checkin = cm.generateNewCheckin(999, 111, 555555);
        cm.addOrUpdateCheckin(checkin);
        cm.deleteCheckin(checkin.getId());
        Checkin got = cm.getCheckin(checkin.getId());
        assertNull(got);
    }

    @Test
    public void testAddNotification(){
        Checkin checkin = cm.generateNewCheckin(999, 888, 5555555);
        cm.addOrUpdateCheckin(checkin);
        Checkin got = cm.getCheckin(checkin.getId());
        assertEquals(checkin, got);
        cm.deleteCheckin(checkin.getId());
    }

    @Test
    public void testGetId(){
        assertNotEquals(cm.getNextId(CheckinManager.getTableName()), -1);
        System.out.println(cm.getNextId(CheckinManager.getTableName()));
    }

    @Test
    public void testGetBuildingCheckinsInTimeSpan(){
        Checkin checkin = cm.generateNewCheckin(999, 888, 100000001L);
        cm.addOrUpdateCheckin(checkin);
        Checkin checkin2 = cm.generateNewCheckin(996, 888, 100000003L);
        cm.addOrUpdateCheckin(checkin2);

        ArrayList<Checkin> list =  cm.getBuildingCheckinsInTimeSpan(888, 100000000L, 100000004L);
        assertEquals(2,list.size());

        ArrayList<Checkin> listOne =  cm.getBuildingCheckinsInTimeSpan(888, 100000000L, 100000001L);
        assertEquals(1,listOne.size());

        ArrayList<Checkin> listEmpty =  cm.getBuildingCheckinsInTimeSpan(888, 0L, 1L);
        assertEquals(0, listEmpty.size());

    }

    @Test
    public void testGetUserCheckinsInTimeSpan(){
        Checkin checkin1 = cm.generateNewCheckin(1006, 777, 100000002L);
        cm.addOrUpdateCheckin(checkin1);
        Checkin checkin2 = cm.generateNewCheckin(1006, 888, 100000003L);
        cm.addOrUpdateCheckin(checkin2);

        ArrayList<Checkin> list =  cm.getUserCheckinsInTimeSpan(1006, 100000000L, 100000004L);
        assertEquals(2,list.size());

        ArrayList<Checkin> listOne =  cm.getUserCheckinsInTimeSpan(1006, 100000000L, 100000002L); //Inclusive
        assertEquals(1,listOne.size());

        ArrayList<Checkin> listEmpty =  cm.getUserCheckinsInTimeSpan(1006, 0L, 1L);
        assertEquals(0, listEmpty.size());

    }

    @Test
    public void testGetFrequentVisit(){
        ArrayList<Long> buildingIds = cm.getFrequentVisit(999);
        assertEquals(1,buildingIds.size());
        assertEquals(Long.valueOf(111),buildingIds.get(0));

        ArrayList<Long> buildingIds2 = cm.getFrequentVisit(999,0);
        assertEquals(0,buildingIds2.size());
    }


    @Test
    public void testGetCloseContact(){ // TODO: ADD DUMMY DATA
        ArrayList<Long> closeContacts = cm.getCloseContact(999);
        assertEquals(0,closeContacts.size());
    }


}