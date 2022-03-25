package com.example.covider.database.checkin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.model.building.Building;
import com.example.covider.model.checkin.Checkin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.CheckedInputStream;

public class CheckinManager extends DatabaseHandler {

    private static final String TABLE_NAME = "checkin";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_BUILDING_ID = "building_id";
    private static final String KEY_TIME = "timestamp";

    private static CheckinManager instance = null;
    private static final long MILLIS_PER_DAY = 86400000L;

    public static CheckinManager getInstance(Context context){
        if(instance == null){
            instance = new CheckinManager(context);
        }
        return instance;
    }

    // take a checkin object and plug it
    public long addOrUpdateCheckin(Checkin checkin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, checkin.getId());
        values.put(KEY_USER_ID, checkin.getUserId());
        values.put(KEY_BUILDING_ID, checkin.getBuildingId());
        values.put(KEY_TIME, checkin.getTimestamp());
        return db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // add a new checkin
    public long addCheckin(int userId, int buildingId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_BUILDING_ID, buildingId);
        values.put(KEY_TIME, System.currentTimeMillis());
        return db.insert(TABLE_NAME, null, values);
    }

    private void create_default_checkin(){
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
                    KEY_USER_ID + " INTEGER, " +
                    KEY_BUILDING_ID + " INTEGER, " +
                    KEY_TIME + " INTEGER " +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
            create_default_checkin();
        }
//        addCheckin(1, 111);
    }

    // with given timestamp
    public Checkin generateNewCheckin(int userId, int buildingId, long timestamp){
        return new Checkin(getNextId(TABLE_NAME), userId, buildingId, timestamp);
    }

    // with current timestamp
    public Checkin generateNewCheckin(int userId, int buildingId){
        return new Checkin(getNextId(TABLE_NAME), userId, buildingId, System.currentTimeMillis());
    }

    // code to get the single contact
    Checkin getCheckin(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_BUILDING_ID, KEY_TIME}, KEY_ID + "=?",
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

    public ArrayList<Checkin> getBuildingCheckinsInTimeSpan(long buildingId, long startTime, long endTime){
        ArrayList<Checkin> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_BUILDING_ID, KEY_TIME}, KEY_BUILDING_ID + "=? AND " + KEY_TIME + " BETWEEN ? AND ? ",
                new String[] { String.valueOf(buildingId), String.valueOf(startTime), String.valueOf(endTime) }, null, null, null, null)){
            if(!cursor.moveToFirst()){
                return list;
            }
            /*
            {
               id=1
               name=SAL
            }
            */
            do{
                list.add(new Checkin(
                        cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getLong(2),
                        cursor.getLong(3)
                ));
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<Checkin> getUserCheckinsInTimeSpan(long userId, long startTime, long endTime){
        ArrayList<Checkin> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_BUILDING_ID, KEY_TIME}, KEY_USER_ID + "=? AND " + KEY_TIME + " BETWEEN ? AND ? ",
                new String[] { String.valueOf(userId), String.valueOf(startTime), String.valueOf(endTime) }, null, null, null, null)){
            if(!cursor.moveToFirst()){
                return list;
            }
            /*
            {
               id=1
               name=SAL
            }
            */
            do{
                list.add(new Checkin(
                        cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getLong(2),
                        cursor.getLong(3)
                ));
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Nothing needs to be done
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    /*
    Return List<buildingId> of user's recent visit
     */
    public ArrayList<Long> getFrequentVisit(long userId){
        return getFrequentVisit(userId, 3);
    }

    /*
    Return List<buildingId> of user's recent visit

        "SELECT building_id , COUNT(*) AS visits " +
        "FROM checkin " +
        "WHERE user_id = 999 " +
        "GROUP BY user_id " +
        "ORDER BY visits DESC " +
        "LIMIT 1"
     */
    public ArrayList<Long> getFrequentVisit(long userId, int topk){
        ArrayList<Long> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT building_id " +
                "FROM " + CheckinManager.getTableName() + " " +
                "WHERE user_id = ? " +
                "GROUP BY user_id " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT ?";
        try(
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(topk)});
        ){
            if(!cursor.moveToFirst()){
                return list;
            }
            /*
            {
                building_id = 111;
            }
            */
            do {
                list.add(cursor.getLong(0));
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    // 3 days default
    public ArrayList<Long> getCloseContact(long userId) {
        return getCloseContact(userId,System.currentTimeMillis(),System.currentTimeMillis() - 3 * MILLIS_PER_DAY);
    }


    public ArrayList<Long> getCloseContact(long userId, long startTime, long endTime){
        ArrayList<Checkin> buildingCheckins = getUserCheckinsInTimeSpan(userId, startTime, endTime);
        Set<Long> closeContactUserIds = new HashSet<Long>();
        for (Checkin checkin : buildingCheckins){
            Long buildingId = checkin.getBuildingId();
            ArrayList<Checkin> userCheckinsInBuilding = getBuildingCheckinsInTimeSpan(buildingId, startTime, endTime);
            for (Checkin c : userCheckinsInBuilding){
                closeContactUserIds.add(c.getUserId());
            }
        }

        closeContactUserIds.remove(userId);

        ArrayList<Long> res = new ArrayList<Long>(closeContactUserIds);
        return res;
    }



}
