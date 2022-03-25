package com.example.covider.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.covider.config.Config;

public abstract class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private static SQLiteDatabase currentDB;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    protected DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        currentDB = db;
    }

    @Override
    public abstract void onUpgrade(SQLiteDatabase db, int i, int i1);


    @Override
    public SQLiteDatabase getReadableDatabase() {
        if(currentDB != null){
            return currentDB;
        }
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        if(currentDB != null){
            return currentDB;
        }
        return super.getWritableDatabase();
    }

    public boolean isTableExist(String tableName){
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'";
        try (Cursor cursor = getReadableDatabase().rawQuery(query, null)) {
            if (cursor != null) {
                return cursor.getCount() > 0;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    public long getNextId(String table_name){
//        printAutoIncrements();
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor cursor = db.query(
                "sqlite_sequence", new String[]{"seq"}, "name"+"=?", new String[]{table_name},
                null, null, null, null )){
            if(!cursor.moveToFirst()){
                System.out.println("Error getting key");
                return -1;
            }
            return cursor.getInt(0)+1;
        }
    }
}
