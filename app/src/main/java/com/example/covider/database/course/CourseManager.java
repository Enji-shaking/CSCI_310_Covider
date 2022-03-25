package com.example.covider.database.course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.notification.NotificationManager;
import com.example.covider.model.course.Course;
import com.example.covider.model.user.Student;
import com.example.covider.model.user.User;

public class CourseManager extends DatabaseHandler {
    private static final String TABLE_NAME = "course";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    public static final String KEY_BUILDING_ID = "building_id";
    public static final String KEY_IS_ONLINE = "is_online";

    private static CourseManager instance = null;

    public static CourseManager getInstance(Context context){
        if(instance == null){
            instance = new CourseManager(context);
        }
        return instance;
    }

    private CourseManager(Context context) {
        super(context);
        if(!isTableExist(TABLE_NAME)){
            String SQL_CREATE_Query = "CREATE TABLE " + TABLE_NAME +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT, " +
                    KEY_BUILDING_ID + " INTEGER, " +
                    KEY_IS_ONLINE + " INTEGER " +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
            createDefaultCourses();
        }
    }

    // Warning: this will not modify the id inside since no insertion happened.
    // Have to insert the created one immediately otherwise will get two course with the same id
    public Course generateNewCourse(String name, long courseBuildingId){
        return new Course(getNextId(TABLE_NAME), name, courseBuildingId, 0);
    }


    public long addOrUpdateCourse(Course course){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, course.getId());
        values.put(KEY_NAME, course.getName());
        values.put(KEY_BUILDING_ID, course.getBuilding());
        values.put(KEY_IS_ONLINE, course.getIsOnline());
        return db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public long addCourse(String courseName, long courseBuildingId, int isOnline){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, courseName);
        values.put(KEY_BUILDING_ID, courseBuildingId);
        values.put(KEY_IS_ONLINE, isOnline);
        return db.insert(TABLE_NAME, null, values);
    }

    public long addCourse(String courseName, long courseBuildingId){
        return addCourse(courseName, courseBuildingId, 0);
    }





    // code to get the single contact
    public Course getCourse(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME, KEY_BUILDING_ID, KEY_IS_ONLINE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Course course = new Course(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getLong(2),
                cursor.getInt(3)
        );
        // return course
        cursor.close();
        return course;
    }

    private void createDefaultCourses(){
        // should have course id of 1,2,3
        addCourse("MarkCourse", 1, 0);
        addCourse("EnjiCourse", 1, 0);
        addCourse("ZSNCourse", 1, 0);
        addCourse("DummyCourse", 3, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // No need to do anything
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//
//        // Create tables again
//        onCreate(db);
    }

    public ArrayList<Long> notifyOnline(long profId, long courseId){
        // TODO: verify if the professor is teaching the course, or he is actually a professor
        ManagerFactory.initialize(getContext());
        NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
        EnrollmentManager em = ManagerFactory.getEnrollmentManagerInstance();
        ArrayList<Student> list = em.getStudentsEnrollingIn(courseId);
        ArrayList<Long> l = new ArrayList<>();
        Course c = getCourse(courseId);
        for(Student s: list){
            l.add(nm.addNotification(profId, s.getId(), "Course " + c.getName() + " is now online"));
        }
        return l;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getKeyName() {
        return KEY_NAME;
    }
}
