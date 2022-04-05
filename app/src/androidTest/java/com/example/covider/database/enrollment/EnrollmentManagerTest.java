package com.example.covider.database.enrollment;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.course.Course;
import com.example.covider.model.enrollment.Enrollment;
import com.example.covider.model.user.Professor;
import com.example.covider.model.user.Student;
import com.example.covider.model.user.User;

public class EnrollmentManagerTest {
    EnrollmentManager em;
    @Before
    public void setUp(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Config.Change_Test();
        ManagerFactory.initialize(instrumentationContext);
        em = ManagerFactory.getEnrollmentManagerInstance();
    }

    @Test
    public void testGetCoursesTakenBy(){
        UserManager um = ManagerFactory.getUserManagerInstance();
        CourseManager cm = ManagerFactory.getCourseManagerInstance();

        cm.addOrUpdateCourse(new Course(1, "dummy", 999));
        um.addOrUpdateUser(new User(1, "dummy1", "", 1));
        um.addOrUpdateUser(new User(2, "dummy1", "", 1));
//        um.addOrUpdateUser(new User(3, "dummy1", "", 0));

        em.addOrUpdateEnrollment(new Enrollment(1, 1, 1, 1));
        em.addOrUpdateEnrollment(new Enrollment(2, 2, 1, 1));
//        em.addOrUpdateEnrollment(new Enrollment(3, 3, 1, 0));

        ArrayList<Student> l =  em.getStudentsEnrollingIn(1);
        assertEquals(l.size(), 2);

        em.deleteEnrollment(1);
        em.deleteEnrollment(2);
        um.deleteUser(1);
        um.deleteUser(2);
        cm.deleteCourse(1);

    }

    @Test
    public void testGetTheInstructorFor(){
        UserManager um = ManagerFactory.getUserManagerInstance();
        CourseManager cm = ManagerFactory.getCourseManagerInstance();

        cm.addOrUpdateCourse(new Course(1, "dummy", 999));

        um.addOrUpdateUser(new User(3, "dummy1", "", 0));

        em.addOrUpdateEnrollment(new Enrollment(3, 3, 1, 0));

        Professor p =  em.getTheInstructorFor(1);
        assertEquals("dummy1",p.getName());
        assertEquals("",p.getPassword());

        em.deleteEnrollment(3);
        um.deleteUser(3);
        cm.deleteCourse(1);
    }

    @After
    public void clean(){
        Config.Change_Normal();
    }
}