package database.course;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import database.ManagerFactory;
import model.notification.Notification;
import database.notification.NotificationManager;
import model.course.Course;

public class CourseManagerTest {
    CourseManager um;
    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        ManagerFactory.initialize(instrumentationContext);
        um = ManagerFactory.getCourseManagerInstance();
    }

    @Test
    public void testDefaultCourse(){
        Course course = um.getCourse(1);
        assertEquals(course.getName(), "MarkCourse");
        Course course2 = um.getCourse(2);
        assertEquals(course2.getName(), "EnjiCourse");
        Course course3 = um.getCourse(3);
        assertEquals(course3.getName(), "ZSNCourse");
    }

    @Test
    public void testNotifyOnline(){
        ArrayList<Long> l = null;
//        updated: no need to do below
//        NotificationManager nm = NotificationManager.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext());
        NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
        try{
            l = um.notifyOnline(3, 1);
            assertEquals(l.size(), 2);
            Notification not1 = nm.getNotification(l.get(0));
            Notification not2 = nm.getNotification(l.get(1));
            assertEquals(not1.getFrom(), 3);
            assertEquals(not1.getTo(), 1);
            assertEquals(not2.getFrom(), 3);
            assertEquals(not2.getTo(), 2);
        }finally {
            if(l!=null){
                for(long i: l){
                    nm.deleteNotification(i);
                }
            }
        }
    }
}