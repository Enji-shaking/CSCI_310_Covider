package com.example.covider.database.enrollment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.course.Course;
import com.example.covider.model.enrollment.Enrollment;
import com.example.covider.model.user.Professor;
import com.example.covider.model.user.Student;
import com.example.covider.model.user.User;

public class EnrollmentManager extends DatabaseHandler {
    private static final String TABLE_NAME = "enrollment";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_COURSE_ID = "courseId";
    private static final String KEY_IS_STU = "isStudent";

    private static EnrollmentManager instance = null;

    public static EnrollmentManager getInstance(Context context){
        if(instance == null){
            instance = new EnrollmentManager(context);
        }
        return instance;
    }

    public EnrollmentManager(Context context) {
        super(context);
        if(!isTableExist(TABLE_NAME)){
            String SQL_CREATE_Query = "CREATE TABLE " + TABLE_NAME +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_USER_ID + " INTEGER, " +
                    KEY_COURSE_ID + " INTEGER, " +
                    KEY_IS_STU + " INTEGER" +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
            createDefaultEnrollment();
        }
    }

    public void addEnrollment(long userId, long courseId, int isStudent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_COURSE_ID, courseId);
        values.put(KEY_IS_STU, isStudent);
        db.insert(TABLE_NAME, null, values);
    }

    public int addOrUpdateEnrollment(Enrollment enrollment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, enrollment.getId());
        values.put(KEY_USER_ID, enrollment.getUserId());
        values.put(KEY_COURSE_ID, enrollment.getCourseId());
        values.put(KEY_IS_STU, enrollment.getIs_student());
        return (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteEnrollment(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{Long.toString(id)});
    }

    // 3 ???default user ??????course1 ?????????
    private void createDefaultEnrollment(){

    }

    public ArrayList<Student> getStudentsEnrollingIn(long courseId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Student> list = new ArrayList<>();

        //  eg: SELECT u.id, u.name, u.pass, u.isStudent
        //      FROM enrollment INNER JOIN user AS u
        //          ON enrollment.courseId = u.id
        //      WHERE enrollment.isStudent = 1 AND enrollment.userId = ?
        String MY_QUERY = "SELECT " + "u." + UserManager.getKeyId() + ", u." + UserManager.getKeyName() +
                ", u." + UserManager.getKeyPass() + ", u." + UserManager.getKeyIsStu() +
                " FROM " +
                TABLE_NAME + " INNER JOIN " + UserManager.getTableName()  + " u ON " +
                TABLE_NAME + "." + KEY_USER_ID + " = " + "u." + UserManager.getKeyId() +
                " WHERE " + TABLE_NAME + "." + KEY_IS_STU + "=? AND " + TABLE_NAME + "." + KEY_COURSE_ID + "=?";
        try(
                Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(1), String.valueOf(courseId)})
        )
            {
//            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            if(!cursor.moveToFirst()){
                return list;
            }
            /*
            {
               id=1
               name=Mark
               pass=password
               isStudent=1
            }
            */
            do{
                list.add(new Student(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                ));
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public Professor getTheInstructorFor(long courseId){
        SQLiteDatabase db = this.getReadableDatabase();
        String MY_QUERY = "SELECT " + "u." + UserManager.getKeyId() + ", u." + UserManager.getKeyName() +
                ", u." + UserManager.getKeyPass() + ", u." + UserManager.getKeyIsStu() +
                " FROM " +
                TABLE_NAME + " INNER JOIN " + UserManager.getTableName()  + " u ON " +
                TABLE_NAME + "." + KEY_USER_ID + " = " + "u." + UserManager.getKeyId() +
                " WHERE " + TABLE_NAME + "." + KEY_IS_STU + "=? AND " + TABLE_NAME + "." + KEY_COURSE_ID + "=?";
        try(
                Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(0), String.valueOf(courseId)})
        )
        {
            if(!cursor.moveToFirst()){
                return null;
            }
            /*
            {
               id=3
               name=ZSN
               pass=password
               isStudent=0
            }
            */
            return (new Professor(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2)
            ));
        }
    }


    public ArrayList<Course> getCoursesTakenBy(long userId){
        ArrayList<Course> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        //  eg: SELECT c.id, c.name, c.building_id, c.is_online
        //      FROM enrollment INNER JOIN course AS c
        //          ON enrollment.courseId = c.id
        //      WHERE enrollment.isStudent = 1 AND enrollment.userId = ?
        String MY_QUERY = "SELECT " + "c." + CourseManager.getKeyId() + ", c." + CourseManager.getKeyName() + ", c." + CourseManager.KEY_BUILDING_ID + ", c." + CourseManager.KEY_IS_ONLINE+
                " FROM " +
                TABLE_NAME + " INNER JOIN " + CourseManager.getTableName()  + " c ON " +
                TABLE_NAME + "." + KEY_COURSE_ID + " = " + "c." + CourseManager.getKeyId() +
                " WHERE " + TABLE_NAME + "." + KEY_IS_STU + "=? AND " + TABLE_NAME + "." + KEY_USER_ID + "=?";
        try(
                Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(1), String.valueOf(userId)})
        ){
            if(!cursor.moveToFirst()){
                return list;
            }
            /*
            {
               id=1
               name=MarkCourse
               building_id = 1
            }
            */
            do{
                list.add(new Course(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getLong(2),
                        cursor.getInt(3)
                ));
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<Course> getCoursesTaughtBy(long userId){
        ArrayList<Course> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //  eg: SELECT c.id, c.name
        //      FROM enrollment INNER JOIN course AS c
        //          ON enrollment.courseId = c.id
        //      WHERE enrollment.isStudent = 0 AND enrollment.userId = ?
        String MY_QUERY = "SELECT " + "c." + CourseManager.getKeyId() + ", c." + CourseManager.getKeyName() + ", c." + CourseManager.KEY_BUILDING_ID+
                " FROM " +
                TABLE_NAME + " INNER JOIN " + CourseManager.getTableName()  + " c ON " +
                TABLE_NAME + "." + KEY_COURSE_ID + " = " + "c." + CourseManager.getKeyId() +
                " WHERE " + TABLE_NAME + "." + KEY_IS_STU + "=? AND " + TABLE_NAME + "." + KEY_USER_ID + "=?";
        try(
                Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(0), String.valueOf(userId)})
        ){
            if(!cursor.moveToFirst()){
                return list;
            }
            /*
            {
               id=1
               name=MarkCourse
               building_id = 1
            }
            */
            do{
                list.add(new Course(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getLong(2)
                ));
            }
            while (cursor.moveToNext());
        }
        return list;
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
}
