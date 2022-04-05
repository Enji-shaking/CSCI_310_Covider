package com.example.covider.database.course;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.enrollment.Enrollment;
import com.example.covider.model.notification.Notification;
import com.example.covider.database.notification.NotificationManager;
import com.example.covider.model.course.Course;
import com.example.covider.model.user.User;

public class CourseManagerTest {
    CourseManager cm;
    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Config.Change_Test();
        ManagerFactory.initialize(instrumentationContext);
        cm = ManagerFactory.getCourseManagerInstance();
    }

    @Test
    public void testAddAndDeleteCourse(){
        long id1 = cm.addCourse("MarkCourse", 1, 0);
        long id2 = cm.addCourse("EnjiCourse", 1, 0);
        long id3 = cm.addCourse("ZSNCourse", 1, 0);
        Course course = cm.getCourse(id1);
        assertEquals(course.getName(), "MarkCourse");
        Course course2 = cm.getCourse(id2);
        assertEquals(course2.getName(), "EnjiCourse");
        Course course3 = cm.getCourse(id3);
        assertEquals(course3.getName(), "ZSNCourse");
        cm.deleteCourse(id1);
        cm.deleteCourse(id2);
        cm.deleteCourse(id3);
        course = cm.getCourse(id1);
        assertNull(course);
        course = cm.getCourse(id2);
        assertNull(course);
        course = cm.getCourse(id3);
        assertNull(course);
    }

    @Test
public void testNotifyOnline(){
        ArrayList<Long> l = null;
        NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
        EnrollmentManager em = ManagerFactory.getEnrollmentManagerInstance();
        UserManager um = ManagerFactory.getUserManagerInstance();
        try{
            cm.addOrUpdateCourse(new Course(1, "dummy", 999));
            em.addOrUpdateEnrollment(new Enrollment(1, 1, 1, 1));
            em.addOrUpdateEnrollment(new Enrollment(2, 2, 1, 1));
            em.addOrUpdateEnrollment(new Enrollment(3, 3, 1, 0));
            um.addOrUpdateUser(new User(1, "dummy1", "", 1));
            um.addOrUpdateUser(new User(2, "dummy1", "", 1));
            um.addOrUpdateUser(new User(3, "dummy1", "", 0));
            l = cm.notifyOnline(3, 1);
            assertEquals(2,l.size());
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
            cm.deleteCourse(1);
            em.deleteEnrollment(1);
            em.deleteEnrollment(2);
            em.deleteEnrollment(3);
            um.deleteUser(1);
            um.deleteUser(2);
            um.deleteUser(3);
        }
    }
    @After
    public void clean(){
        Config.Change_Normal();
    }
}