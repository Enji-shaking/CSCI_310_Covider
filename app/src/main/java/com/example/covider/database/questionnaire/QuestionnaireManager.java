package com.example.covider.database.questionnaire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.model.notification.Notification;
import com.example.covider.model.questionnaire.Questionnaire;

import java.util.ArrayList;


public class QuestionnaireManager extends DatabaseHandler {
    private static final String TABLE_NAME = "questionnaire";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_BUILDING_ID = "buildingId";
    private static final String KEY_MASK = "mask";
    private static final String KEY_SANITIZER = "sanitizer";
    private static final String KEY_DISTANCE = "social_distance";
    private static final String KEY_SYMPTOMS = "symptoms";

    public static QuestionnaireManager instance = null;

    public static QuestionnaireManager getInstance(Context context){
        if(instance == null){
            instance = new QuestionnaireManager(context);
        }
        return instance;
    }

    private QuestionnaireManager(Context context) {
        super(context);
        if(!isTableExist(TABLE_NAME)){
            String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_USER_ID + " INTEGER, " +
                    KEY_BUILDING_ID + " TEXT, " +
                    KEY_MASK + " BOOLEAN, " +
                    KEY_SANITIZER + " BOOLEAN, " +
                    KEY_DISTANCE + " BOOLEAN, " +
                    KEY_SYMPTOMS + " BOOLEAN " +
                ");";

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public int addOrUpdateQuestionnaire(Questionnaire q){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, q.getId());
        values.put(KEY_USER_ID, q.getUserId());
        values.put(KEY_BUILDING_ID, q.getBuildingId());
        values.put(KEY_DISTANCE, q.isDistance());
        values.put(KEY_MASK, q.isMasks());
        values.put(KEY_SANITIZER, q.isSanitizer());
        values.put(KEY_SYMPTOMS, q.isSymptoms());
        return (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long addQuestionnaire(long userId, long buildingId, boolean masks, boolean sanitizer, boolean distance, boolean symptoms) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_BUILDING_ID, buildingId);
        values.put(KEY_DISTANCE, distance);
        values.put(KEY_MASK, masks);
        values.put(KEY_SANITIZER, sanitizer);
        values.put(KEY_SYMPTOMS, symptoms);
        return db.insert(TABLE_NAME, null, values);
    }

    public void deleteQuestionnaire(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{Long.toString(id)});
    }

    public ArrayList<Questionnaire> getQuestionnaireByBuildingId(long buildingId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Questionnaire> list = new ArrayList<Questionnaire>();
        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_BUILDING_ID, KEY_MASK,KEY_SANITIZER,KEY_DISTANCE,KEY_SYMPTOMS },
                KEY_BUILDING_ID + "=?",
                new String[] { String.valueOf(buildingId) }, null, null, null, null)){
            if(!cursor.moveToFirst()){
                return list;
            }
            do{
                list.add(new Questionnaire(
                        cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getLong(2),
                        cursor.getInt(3) > 0,
                        cursor.getInt(4) > 0,
                        cursor.getInt(5) > 0,
                        cursor.getInt(6) > 0
                ));
            }
            while (cursor.moveToNext());
        }
        return list;
    }
}
