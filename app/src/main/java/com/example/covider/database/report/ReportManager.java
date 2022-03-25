package com.example.covider.database.report;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.model.report.UserDailyReport;

public class ReportManager extends DatabaseHandler {

    public static final String TABLE_NAME = "report";
    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_IS_POSITIVE = "is_positive";
    public static final String KEY_NOTE = "note";
    public static final String KEY_TIME = "timestamp";

    private static ReportManager instance = null;

    public static ReportManager getInstance(Context context){
        if(instance == null){
            instance = new ReportManager(context);
        }
        return instance;
    }


    private ReportManager(Context context) {
        super(context);
        if(!isTableExist(TABLE_NAME)){
            String SQL_CREATE_Query = "CREATE TABLE " + TABLE_NAME +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_USER_ID + " INTEGER, " +
                    KEY_IS_POSITIVE + " INTEGER, " +
                    KEY_NOTE + " TEXT, " +
                    KEY_TIME + " INTEGER " +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);

            create_default_report();
        }
    }

    // take a report object and plug it
    public long addOrUpdateReport(UserDailyReport userDailyReport){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, userDailyReport.getId());
        values.put(KEY_USER_ID, userDailyReport.getUserId());
        values.put(KEY_IS_POSITIVE, userDailyReport.getIsPositive());
        values.put(KEY_NOTE, userDailyReport.getNote());
        values.put(KEY_TIME, userDailyReport.getTimestamp());
        return db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // add a new report
    // if positive, isPositive = true,
    // if symptom, note not empty
    public long addReport(long userId, int isPositive, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_IS_POSITIVE, isPositive);
        values.put(KEY_NOTE, note);
        values.put(KEY_TIME, System.currentTimeMillis());
        return db.insert(TABLE_NAME, null, values);
    }

    public void create_default_report(){
        addOrUpdateReport(new UserDailyReport(10001,1, 0, "",1646896887000L));
        addOrUpdateReport(new UserDailyReport(10002,2, 0, "",1646896887001L));
        addOrUpdateReport(new UserDailyReport(10003,1, 1, "Positive",1646896887002L));
    }

    // code to get the single contact
    public UserDailyReport getReport(long reportId) {
        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_IS_POSITIVE, KEY_NOTE, KEY_TIME}, KEY_ID + "=?",
                new String[] { String.valueOf(reportId) }, null, null, null, null)){
            if(!cursor.moveToFirst()){
                return null;
            }
            return new UserDailyReport(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getLong(4)
            );
        }
    }

    public UserDailyReport getUserMostRecentReport(long userId){
        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_IS_POSITIVE, KEY_NOTE, KEY_TIME}, KEY_USER_ID + "=?",
                new String[] { String.valueOf(userId) }, null, null, KEY_TIME + " DESC", "1")){
            if(!cursor.moveToFirst()){
                return null;
            }
            return new UserDailyReport(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getLong(4)
            );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Nothing needs to be done
    }

}
