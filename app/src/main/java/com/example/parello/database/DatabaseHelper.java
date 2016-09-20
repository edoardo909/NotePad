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

}
