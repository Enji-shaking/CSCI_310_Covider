package database.course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import database.DatabaseHandler;
import database.ManagerFactory;
import database.enrollment.EnrollmentManager;
import database.notification.NotificationManager;
import model.course.Course;
import model.user.Student;
import model.user.User;

public class CourseManager extends DatabaseHandler {
    private static final String TABLE_NAME = "course";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

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
                    KEY_NAME + " TEXT " +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
            createDefaultCourses();
        }
    }

    // Warning: this will not modify the id inside since no insertion happened.
    // Have to insert the created one immediately otherwise will get two notifications with the same id
    public Course generateNewCourse(String name){
        return new Course(getNextId(TABLE_NAME), name);
    }


    public long addOrUpdateCourse(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        return db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public long addCourse(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        return db.insert(TABLE_NAME, null, values);
    }



    // code to get the single contact
    Course getCourse(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Course course = new Course(
                cursor.getLong(0),
                cursor.getString(1)
        );
        // return course
        cursor.close();
        return course;
    }

    private void createDefaultCourses(){
        // should have course id of 1,2,3
        addCourse("MarkCourse");
        addCourse("EnjiCourse");
        addCourse("ZSNCourse");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
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
