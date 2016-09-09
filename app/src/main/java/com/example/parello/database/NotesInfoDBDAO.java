package com.example.parello.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Parello on 08/09/2016.
 */
public class NotesInfoDBDAO {

    protected SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    public Context mContext;

    public NotesInfoDBDAO(Context context) {
        this.mContext = context;
        dbHelper = DatabaseHelper.getHelper(mContext);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DatabaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }
}
