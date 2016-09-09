package com.example.parello.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;

import com.example.parello.notepad.NoteInfo;

import java.util.ArrayList;

/**
 * Created by Parello on 08/09/2016.
 */
public class DatabaseHandler extends NotesInfoDBDAO implements DatabaseInterface {
    private static final String WHERE_ID_EQUALS = DatabaseHelper.KEY_ROWID
            + " =?";

    public DatabaseHandler(Context context) {
        super(context);
    }

    public long save(NoteInfo noteInfo) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.KEY_TITLE, noteInfo.getTitle());
        initialValues.put(DatabaseHelper.KEY_BODY,noteInfo.getBody());

        return database.insert(DatabaseHelper.DATABASE_TABLE, null, initialValues);
    }

    public NoteInfo getNote(long id){
        NoteInfo noteInfo = null;
        String sql = "SELECT * FROM " + DatabaseHelper.DATABASE_TABLE
                + " WHERE " + DatabaseHelper.KEY_ROWID + " = ?";
        database.beginTransaction();
        Cursor cursor = database.rawQuery(sql, new String[] { id + "" });

        if (cursor.moveToNext()) {
            noteInfo = new NoteInfo();
            noteInfo.setIdCode(cursor.getInt(0));
            noteInfo.setBody(cursor.getString(1));
        }
        return noteInfo;
    }

    public ArrayList<NoteInfo> getAllNotes() {
        ArrayList<NoteInfo> notesInfos = new ArrayList<NoteInfo>();
        String sql = "SELECT * FROM " + DatabaseHelper.DATABASE_TABLE+"=?";
        //Cursor cursor = database.rawQuery(sql, new String[] { "" });
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE,
                new String[] { DatabaseHelper.KEY_ROWID,
                        DatabaseHelper.KEY_TITLE,
                        DatabaseHelper.KEY_BODY,}, null, null, null,
                null, null);
        while (cursor.moveToNext()) {
            NoteInfo noteInfo = new NoteInfo();
            noteInfo.setIdCode(cursor.getInt(0));
            noteInfo.setTitle(cursor.getString(1));
            noteInfo.setBody(cursor.getString(2));
            notesInfos.add(noteInfo);
        }
        return notesInfos;
    }



    public long update(NoteInfo noteInfo) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_TITLE, noteInfo.getTitle());
        values.put(DatabaseHelper.KEY_BODY, noteInfo.getBody());

        long result = database.update(DatabaseHelper.DATABASE_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(noteInfo.getIdCode()) });
        return result;

    }

    public int delete(NoteInfo noteInfo) {
        return database.delete(DatabaseHelper.DATABASE_TABLE,null/* WHERE_ID_EQUALS*/,
                new String[] { noteInfo.getIdCode() + "" });
    }
}
