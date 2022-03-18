package com.example.covider.model.user;

import java.util.ArrayList;

import com.example.covider.database.ManagerFactory;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.notification.NotificationManager;
import com.example.covider.model.course.Course;

public class Professor extends User{

    public Professor(long id, String name, String password) {
        super(id, name, password, 0);
    }

    public ArrayList<Long> notifyOnline(){
        // TODO: find a correct context for below (or not?)
        // TODO: ManagerFactory.initialize(context..?)
        ArrayList<Long> notifications = new ArrayList<>();
        NotificationManager nm = ManagerFactory.getNotificationManagerInstance(); // TODO: Variable not used
        EnrollmentManager em = ManagerFactory.getEnrollmentManagerInstance();
        CourseManager cm = ManagerFactory.getCourseManagerInstance(); // TODO: Variable not used
        ArrayList<Course> list = em.getCoursesTaughtBy(getId());
        for(Course c: list){
            notifications.addAll(notifyOnline(c.getId()));
        }
        return notifications;
    }
    public ArrayList<Long> notifyOnline(long courseId){
        // TODO: find a correct context for below (or not?)
        // TODO: ManagerFactory.initialize(context..?)
        ArrayList<Long> notifications = new ArrayList<>(); // TODO: Variable not used
        CourseManager cm = ManagerFactory.getCourseManagerInstance();
        return cm.notifyOnline(getId(), courseId);
    }
}
