package database.course;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import database.course.CourseManager;
import database.notification.Notification;
import database.notification.NotificationManager;
import model.course.Course;
import model.user.User;

public class CourseManagerTest {
    CourseManager um;
    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        um = CourseManager.getInstance(instrumentationContext);
    }

    @Test
    public void testDefaultUsers(){
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
        NotificationManager nm = NotificationManager.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext());
        try{
            l = um.notifyOnline(2, 1);
            assertEquals(l.size(), 2);
            Notification not1 = nm.getNotification(l.get(0));
            Notification not2 = nm.getNotification(l.get(1));
            assertEquals(not1.getFrom(), 2);
            assertEquals(not1.getTo(), 1);
            assertEquals(not2.getFrom(), 2);
            assertEquals(not2.getTo(), 3);
        }finally {
            if(l!=null){
                for(long i: l){
                    nm.deleteNotification(i);
                }
            }
        }
    }
}