package com.example.covider.database.checkin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.model.checkin.Checkin;

public class CheckinManager extends DatabaseHandler {

    private static final String TABLE_NAME = "checkin";
    private static final String KEY_ID = "id";
    private static final String KEY_USER = "user_id";
    private static final String KEY_BUILDING = "building_id";
    private static final String KEY_TIME = "timestamp";

    private static CheckinManager instance = null;

    public static CheckinManager getInstance(Context context){
        if(instance == null){
            instance = new CheckinManager(context);
        }
        return instance;
    }

    public long addOrUpdateCheckin(Checkin checkin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, checkin.getId());
        values.put(KEY_USER, checkin.getUserId());
        values.put(KEY_BUILDING, checkin.getBuildingId());
        values.put(KEY_TIME, checkin.getTimestamp());
        return db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long addCheckin(int userId, int buildingId, long timestamp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER, userId);
        values.put(KEY_BUILDING, buildingId);
        values.put(KEY_TIME, timestamp);
        return db.insert(TABLE_NAME, null, values);
    }

    private void create_default_checkin(){
//        Checkin c = generateNewCheckin(999, 111, getEpochTime());
        // The time of March 9 around 11 pm
        long id = addOrUpdateCheckin(new Checkin(10, 999, 111, 1646896886094L));
        System.out.println(id);
    }

    private CheckinManager(Context context) {
        super(context);
        if(!isTableExist(TABLE_NAME)){
            String SQL_CREATE_Query = "CREATE TABLE " + TABLE_NAME +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_USER + " INTEGER, " +
                    KEY_BUILDING + " INTEGER, " +
                    KEY_TIME + " INTEGER " +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
            create_default_checkin();
        }
    }

    public Checkin generateNewCheckin(int userId, int buildingId, long timestamp){
        return new Checkin(getNextId(TABLE_NAME), userId, buildingId, timestamp);
    }

    // code to get the single contact
    Checkin getCheckin(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER, KEY_BUILDING, KEY_TIME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null)){
            if(!cursor.moveToFirst()){
                return null;
            }
            return new Checkin(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getLong(3)
            );
        }
    }

    public void deleteCheckin(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{Long.toString(id)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Nothing needs to be done
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

}
