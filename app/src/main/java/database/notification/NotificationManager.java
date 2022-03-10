package database.notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import database.DatabaseHandler;

public class NotificationManager extends DatabaseHandler {
    private static final String TABLE_NAME = "notification";
    private static final String KEY_ID = "id"; // user id
    private static final String KEY_FROM = "from_id"; // user id
    private static final String KEY_TO = "to_id"; // user id
    private static final String KEY_READ = "read"; //
    private static final String KEY_MSG = "message";

    private static NotificationManager instance = null;

    public static NotificationManager getInstance(Context context){
        if(instance == null){
            instance = new NotificationManager(context);
        }
        return instance;
    }

    private NotificationManager(Context context) {
        super(context);
        if(!isTableExist(TABLE_NAME)){
            String SQL_CREATE_Query = "CREATE TABLE " + TABLE_NAME +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_FROM + " INTEGER, " +
                    KEY_TO + " INTEGER, " +
                    KEY_READ + " INTEGER, " +
                    KEY_MSG + " TEXT" +
                    ")";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(SQL_CREATE_Query);
            create_default_notification();
        }
    }


    private void create_default_notification(){
        Notification notification1 = new Notification(10, 999L, 888L, 0, "Hi Enji");
        addOrUpdateNotification(notification1);
    }

    // Warning: this will not modify the id inside since no insertion happened.
    // Have to insert the created one immediately otherwise will get two notifications with the same id
    public Notification generateNewNotification(long from, long to, int read, String message){
        return new Notification(getNextId(TABLE_NAME), from, to, read, message);
    }

    public long addOrUpdateNotification(Notification notification){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, notification.getId());
        values.put(KEY_FROM, notification.getFrom());
        values.put(KEY_TO, notification.getTo());
        values.put(KEY_READ, notification.getRead());
        values.put(KEY_MSG, notification.getMessage());
        return db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long addNotification(int from, int to, String message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FROM, from);
        values.put(KEY_TO, to);
        values.put(KEY_READ, 0);
        values.put(KEY_MSG, message);
        return db.insert(TABLE_NAME, null, values);
    }

    // code to get the single contact
    public Notification getNotification(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_FROM,
                        KEY_TO, KEY_READ, KEY_MSG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null)){
            if(!cursor.moveToFirst()){
                return null;
            }
            return new Notification(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getLong(2),
                    cursor.getInt(3),
                    cursor.getString(4)
            );
        }
    }

    public ArrayList<Notification> getNotificationFor(long user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Notification> list = new ArrayList<Notification>();
        try(Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_FROM,
                        KEY_TO, KEY_READ, KEY_MSG }, KEY_TO + "=?",
                new String[] { String.valueOf(user_id) }, null, null, null, null)){
            if(!cursor.moveToFirst()){
                return list;
            }
            do{
                list.add(new Notification(
                        cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getLong(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                ));
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public void deleteNotification(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{Long.toString(id)});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        System.out.println("on create " + db);
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
}
