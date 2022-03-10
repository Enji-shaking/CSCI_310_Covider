package database.checkin;

import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;


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
}