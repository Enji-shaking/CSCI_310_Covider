package com.example.covider.database.notification;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.model.enrollment.Enrollment;
import com.example.covider.model.notification.Notification;

public class NotificationManagerTest {
    NotificationManager nm;
//    boolean setUpDone = false;
    @Before
    public void setUp() {
//        if(!setUpDone){
            Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Config.Change_Test();
//            instrumentationContext.deleteDatabase(Config.DATABASE_NAME);
            ManagerFactory.initialize(instrumentationContext);
            nm = ManagerFactory.getNotificationManagerInstance();
//            setUpDone = true;
//        }
    }

    @Test
    public void testAddAndDeleteNotification(){
        Notification notification = nm.generateNewNotification(999, 888, 0, "Test");
        nm.addOrUpdateNotification(notification);
        Notification got = nm.getNotification(notification.getId());
        assertEquals(notification, got);
        nm.deleteNotification(notification.getId());
        got = nm.getNotification(notification.getId());
        assertNull(got);
    }

    @Test
    public void retrieveNotificationForUser(){
        long id1 = nm.addNotification(9999, 8888,  "Test");
        long id2 = nm.addNotification(9999, 8888,  "Test");
        long id3 = nm.addNotification(9999, 8888,  "Test");
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

    @After
    public void clean(){
        Config.Change_Normal();
    }
}