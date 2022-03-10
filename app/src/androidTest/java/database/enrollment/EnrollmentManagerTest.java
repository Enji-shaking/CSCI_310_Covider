package database.enrollment;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import database.ManagerFactory;
import model.course.Course;
import model.user.Professor;
import model.user.Student;

public class EnrollmentManagerTest {
    EnrollmentManager em;
    @Before
    public void setUp(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ManagerFactory.initialize(instrumentationContext);
        em = ManagerFactory.getEnrollmentManagerInstance();
    }

    @Test
    public void testGetStudentsEnrollingIn(){
        ArrayList<Student> l =  em.getStudentsEnrollingIn(1);
        assertEquals(l.size(), 2);
    }

    @Test
    public void testGetTheInstructorFor(){
        Professor p = em.getTheInstructorFor(1);
        assertEquals(p.getId(), 3);
        assertEquals(p.getName(), "ZSN");
        assertEquals(p.getPassword(), "password");
        assertEquals(p.getIsStudent(), 0);
    }

    @Test
    public void testGetCoursesTakenBy(){
        ArrayList<Course> l =  em.getCoursesTakenBy(1);
        assertEquals(l.size(), 2);
        assertTrue(l.get(0).getName().equals("MarkCourse") || l.get(1).getName().equals("MarkCourse"));
        assertTrue(l.get(0).getName().equals("EnjiCourse") || l.get(1).getName().equals("EnjiCourse"));
    }

    @Test
    public void testGetCoursesTaughtBy(){
        ArrayList<Course> l =  em.getCoursesTaughtBy(3);
        assertEquals(l.size(), 2);
        assertTrue(l.get(0).getName().equals("MarkCourse") || l.get(1).getName().equals("MarkCourse"));
        assertTrue(l.get(0).getName().equals("EnjiCourse") || l.get(1).getName().equals("EnjiCourse"));
    }
}