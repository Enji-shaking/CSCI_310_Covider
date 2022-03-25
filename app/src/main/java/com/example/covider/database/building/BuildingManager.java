package com.example.covider.database.building;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.database.checkin.CheckinManager;
import com.example.covider.model.building.Building;
import com.example.covider.model.course.Course;

import java.util.ArrayList;

public class BuildingManager extends DatabaseHandler {
    private static final String TABLE_NAME = "building";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "building_name";

    public static BuildingManager instance = null;

    public static BuildingManager getInstance(Context context){
        if(instance == null){
            instance = new BuildingManager(context);
        }
        return instance;
    }

    private BuildingManager(Context context) {
        super(context);
        if(!isTableExist("building")){
            String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_NAME + " TEXT" +
                        ");";

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(sql);
//            create_default_building();
        }
//        addCheckin(1, 111);
    }

    public int addOrUpdateBuilding(Building building){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, building.getId());
        values.put(KEY_NAME, building.getName());
        return (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int addBuilding(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        return (int) db.insert(TABLE_NAME, null, values);
    }


    private void create_default_building(){
//        addBuilding("SA"); // will be overwritten by the next line
//        addOrUpdateBuilding(new Building(1,"SAL"));
//        addOrUpdateBuilding(new Building(2,"KAP"));
//        addOrUpdateBuilding(new Building(4,"LVL"));
    }

    public Building getBuildingByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT id, name FROM building WHERE building.name = ?;
        Cursor cursor = db.query(
                TABLE_NAME, new String[] {KEY_ID, KEY_NAME},
                KEY_NAME + "=?", new String[] { name },
                null, null, null);
        if (cursor == null || !cursor.moveToFirst()){
            return null;
        }

        Building building = new Building(
                cursor.getLong(0),
                cursor.getString(1)
        );

        cursor.close();
        return building;
    }

    public Building getBuildingById(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT id, name FROM building WHERE building.name = ?;
        Cursor cursor = db.query(
                TABLE_NAME, new String[] {KEY_ID, KEY_NAME},
                KEY_ID + "=?", new String[] { String.valueOf(id) },
                null, null, null);
        if (cursor == null || !cursor.moveToFirst()){
            return null;
        }

        Building building = new Building(
                cursor.getLong(0),
                cursor.getString(1)
        );

        cursor.close();
        return building;
    }

    public ArrayList<Building> getBuildingList(){
        ArrayList<Building> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + KEY_ID + ", " + KEY_NAME + " FROM " + TABLE_NAME + ";";
        try(
            Cursor cursor = db.rawQuery(sql, new String[] {});
        ){
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
                list.add(new Building(
                        cursor.getLong(0),
                        cursor.getString(1)
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
        // do nothing
    }
}
