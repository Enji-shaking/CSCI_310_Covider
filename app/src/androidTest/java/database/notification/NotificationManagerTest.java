package database.notification;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import database.user.UserManager;

public class NotificationManagerTest {
    NotificationManager nm;
    @Before
    public void setUp() {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        nm = NotificationManager.getInstance(instrumentationContext);
    }

    @Test
    public void testDefaultMessage(){
//        System.out.println(Notification.getId_inc());
        Notification notification = nm.getNotification(10);
        assertEquals(notification.getFrom(), 999);
        assertEquals(notification.getTo(), 888);
        assertEquals(notification.getRead(), 0);
        assertEquals(notification.getMessage(), "Hi Enji");
    }

    @Test
    public void testDeletion(){
        Notification notification = nm.generateNewNotification(999, 888, 0, "Test");
        nm.addOrUpdateNotification(notification);
        nm.deleteNotification(notification.getId());
        Notification got = nm.getNotification(notification.getId());
        assertNull(got);
    }

    @Test
    public void testAddNotification(){
        Notification notification = nm.generateNewNotification(999, 888, 0, "Test");
        nm.addOrUpdateNotification(notification);
        Notification got = nm.getNotification(notification.getId());
        assertEquals(notification, got);
        nm.deleteNotification(notification.getId());
    }

    @Test
    public void retrieveNotificationForUser(){
//        Notification notification = nm.generateNewNotification(9999, 8888, 0, "Test");
        int id1 = nm.addNotification(9999, 8888, 0, "Test");
//        Notification notification2 = nm.generateNewNotification(9999, 8888, 0, "Test");
        int id2 = nm.addNotification(9999, 8888, 0, "Test");
//        Notification notification3 = nm.generateNewNotification(9999, 8888, 0, "Test");
        int id3 = nm.addNotification(9999, 8888, 0, "Test");
//        System.out.println(notification.getId() + notification2.getId() + notification3.getId());
//        Notification t = nm.getNotification(notification.getId());
        try{
            ArrayList<Notification> got = nm.getNotificationFor(8888);
            assertEquals(3, got.size());
            for(Notification n: got){
                assertEquals(n.getFrom(), 9999);
                assertEquals(n.getTo(), 8888);
                assertEquals(n.getRead(), 0);
                assertEquals(n.getMessage(), "Test");
            }
        } finally {
            nm.deleteNotification(id1);
            nm.deleteNotification(id2);
            nm.deleteNotification(id3);
        }
    }
    @Test
    public void testGetId(){
        assertNotEquals(nm.getNextId(nm.getTableName()), -1);
    }
}