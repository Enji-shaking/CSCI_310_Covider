package model.user;

import android.app.Activity;

import java.util.ArrayList;

import database.ManagerFactory;
import database.course.CourseManager;
import database.enrollment.EnrollmentManager;
import database.notification.NotificationManager;
import model.course.Course;

public class Professor extends User{

    public Professor(long id, String name, String password) {
        super(id, name, password, 0);
    }

    public ArrayList<Long> notifyOnline(){
        // TODO: find a correct context for below (or not?)
        // TODO: ManagerFactory.initialize(context..?)
        ArrayList<Long> notifications = new ArrayList<>();
        NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
        EnrollmentManager em = ManagerFactory.getEnrollmentManagerInstance();
        CourseManager cm = ManagerFactory.getCourseManagerInstance();
        ArrayList<Course> list = em.getCoursesTaughtBy(getId());
        for(Course c: list){
            notifications.addAll(notifyOnline(c.getId()));
        }
        return notifications;
    }
    public ArrayList<Long> notifyOnline(long courseId){
        // TODO: find a correct context for below (or not?)
        // TODO: ManagerFactory.initialize(context..?)
        ArrayList<Long> notifications = new ArrayList<>();
        CourseManager cm = ManagerFactory.getCourseManagerInstance();
        return cm.notifyOnline(getId(), courseId);
    }
}
