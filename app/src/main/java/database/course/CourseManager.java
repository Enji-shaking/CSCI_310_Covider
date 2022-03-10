package database.course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import database.DatabaseHandler;
import database.notification.NotificationManager;
import model.course.Course;
import model.user.User;

public class CourseManager extends DatabaseHandler {
    private static final String TABLE_NAME = "course";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String TABLE_ENROLLMENT_NAME = "enrollment";
    private static final String KEY_ENROLLMENT_ID = "id";
    private static final String KEY_ENROLLMENT_USER_ID = "userId";
    private static final String KEY_ENROLLMENT_COURSE_ID = "courseId";

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
        if(!isTableExist(TABLE_ENROLLMENT_NAME)){
            String SQL_CREATE_Query = "CREATE TABLE " + TABLE_ENROLLMENT_NAME +
                    " (" +
                    KEY_ENROLLMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_ENROLLMENT_USER_ID + " INTEGER, " +
                    KEY_ENROLLMENT_COURSE_ID + " INTEGER " +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
            createDefaultEnrollment();
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
    Course getCourse(int id) {
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


    // 注意，这里是偷懒的作品，不单独写一个enrollment manager了
    // 3 个default user 都和course1 有联系
    private void createDefaultEnrollment(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENROLLMENT_USER_ID, 1);
        values.put(KEY_ENROLLMENT_COURSE_ID, 1);
        db.insert(TABLE_ENROLLMENT_NAME, null, values);
        values.clear();
        values.put(KEY_ENROLLMENT_USER_ID, 2);
        values.put(KEY_ENROLLMENT_COURSE_ID, 1);
        db.insert(TABLE_ENROLLMENT_NAME, null, values);
        values.clear();
        values.put(KEY_ENROLLMENT_USER_ID, 3);
        values.put(KEY_ENROLLMENT_COURSE_ID, 1);
        db.insert(TABLE_ENROLLMENT_NAME, null, values);
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

    // 妈的，我还是需要变成一个enrollment manager，先这样，明天再说
    public ArrayList<Long> notifyOnline(int profId, int courseId){
//
        NotificationManager nm = NotificationManager.getInstance(getContext());
        ArrayList<Long> l = new ArrayList<>();
        l.add(nm.addNotification(profId, 1,  "Course online"));
        l.add(nm.addNotification(profId, 3,  "Course online"));
        return l;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

}
