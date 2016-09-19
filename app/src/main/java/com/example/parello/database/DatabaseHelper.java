package com.example.parello.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Parello on 07/09/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    public static final String DATABASE_NAME = "data";
    public static final String DATABASE_TABLE = "notes";
    public static final int DATABASE_VERSION = 2;

    private static final String TAG = "NotesDbAdapter";
    private static DatabaseHelper mDbHelper;

    public static final String DATABASE_CREATE ="CREATE TABLE "+DATABASE_TABLE+"(" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, " + KEY_BODY + " TEXT)";




//    "create table notes (_id integer primary key autoincrement, "
//                    + "title text not null, body text not null);";

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (mDbHelper == null)
            mDbHelper = new DatabaseHelper(context);
        return mDbHelper;
    }
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

//    public long createNote(String title, String body, String date) {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_TITLE, title);
//        initialValues.put(KEY_BODY, body);
//
//        return mDb.insert(DATABASE_TABLE, null, initialValues);
//    }
//
//    public boolean deleteNote(long rowId) {
//
//        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
//    }
//
//    public Cursor fetchAllNotes() {
//
//        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
//                KEY_BODY}, null, null, null, null, null);
//    }
//
//    public Cursor fetchNote(long rowId) throws SQLException {
//
//        Cursor mCursor =
//
//                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
//                                KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
//                        null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//    }
//
//    public boolean updateNote(long rowId, String title, String body,String date) {
//        ContentValues args = new ContentValues();
//        args.put(KEY_TITLE, title);
//        args.put(KEY_BODY, body);
//
//        //One more parameter is added for data
//        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//    }


}
