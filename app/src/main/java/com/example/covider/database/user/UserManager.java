package com.example.covider.database.user;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.covider.database.DatabaseHandler;
import com.example.covider.model.user.User;

public class UserManager extends DatabaseHandler {
    private static final String TABLE_NAME = "user";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASS = "pass";
    private static final String KEY_IS_STU = "isStudent";

    private static UserManager instance = null;

    public static UserManager getInstance(Context context){
        if(instance == null){
            instance = new UserManager(context);
        }
        return instance;
    }

    private UserManager(Context context) {
        super(context);
        if(!isTableExist(TABLE_NAME)){
            String SQL_CREATE_Query = "CREATE TABLE " + TABLE_NAME +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT, " +
                    KEY_PASS + " TEXT, " +
                    KEY_IS_STU + " INTEGER" +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
//            createDefaultUsers();
        }
    }

    // Warning: this will not modify the id inside since no insertion happened.
    // Have to insert the created one immediately otherwise will get two notifications with the same id
    public User generateNewUser(String name, String password, int is_student){
        return new User(getNextId(TABLE_NAME), name, password, is_student);
    }


    public int addOrUpdateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASS, user.getPassword());
        values.put(KEY_IS_STU, user.getIsStudent());
        return (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public int addUser(String name, String password, int is_student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_PASS, password);
        values.put(KEY_IS_STU, is_student);
        return (int) db.insert(TABLE_NAME, null, values);
    }



    // code to get the single contact
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME, KEY_PASS, KEY_IS_STU }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst()){
            return null;
        }


        User user = new User(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3)
        );
        // return user
        cursor.close();
        return user;
    }

    // code to get the single contact
    // TODO: write Test
    public User getUserByName(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME, KEY_PASS, KEY_IS_STU }, KEY_NAME + "=?",
                new String[] { userName }, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst()){
            return null;
        }


        User user = new User(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3)
        );
        // return user
        cursor.close();
        return user;
    }

    private void createDefaultUsers(){
        // should have user id of 1,2,3
        addUser("Mark", "password", 1);
        addUser("Enji", "password", 1);
        addUser("ZSN", "password", 0);
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

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getKeyName() {
        return KEY_NAME;
    }

    public static String getKeyPass() {
        return KEY_PASS;
    }

    public static String getKeyIsStu() {
        return KEY_IS_STU;
    }
}
