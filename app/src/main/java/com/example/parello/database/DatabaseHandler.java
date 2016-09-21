package com.example.parello.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.parello.notepad.NoteInfo;

import java.util.ArrayList;

/**
 * Created by Parello on 08/09/2016.
 */
public class DatabaseHandler extends NotesInfoDBDAO {
    private static final String WHERE_ID_EQUALS = DatabaseHelper.KEY_ROWID
            + " = ?";

    public DatabaseHandler(Context context) {
        super(context);
    }

    public long save(NoteInfo nota) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.KEY_TITLE, nota.getTitle());
        initialValues.put(DatabaseHelper.KEY_BODY,nota.getBody());

        return database.insert(DatabaseHelper.DATABASE_TABLE, null, initialValues);
    }

//    public NoteInfo getNote(NoteInfo nota){
//        NoteInfo noteInfo = null;
//        String sql = "SELECT * FROM " + DatabaseHelper.DATABASE_TABLE
//                + " WHERE " + DatabaseHelper.KEY_ROWID + " =?";
//        database.beginTransaction();
//        Cursor cursor = database.rawQuery(sql, new String[] { nota + "" });
//
//        if (cursor.moveToNext()) {
//            noteInfo = new NoteInfo();
//            noteInfo.setIdCode(cursor.getInt(0));
//            noteInfo.setBody(cursor.getString(1));
//        }
//        return noteInfo;
//    }

    public ArrayList<NoteInfo> getAllNotes() {
        ArrayList<NoteInfo> notesInfos = new ArrayList<>();
       // String sql = "SELECT * FROM " + DatabaseHelper.DATABASE_TABLE+"=?";
        //Cursor cursor = database.rawQuery(sql, new String[] { "" });
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE,
                new String[] { DatabaseHelper.KEY_ROWID,
                        DatabaseHelper.KEY_TITLE,
                        DatabaseHelper.KEY_BODY,}, null, null, null,
                null, null);
        while (cursor.moveToNext()) {
            NoteInfo nota = new NoteInfo();
            nota.setIdCode(cursor.getInt(0));
            nota.setTitle(cursor.getString(1));
            nota.setBody(cursor.getString(2));
            notesInfos.add(nota);
        }
        cursor.close();
        return notesInfos;
    }

    public long update(NoteInfo nota) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_ROWID,nota.getIdCode());
        values.put(DatabaseHelper.KEY_TITLE, nota.getTitle());
        values.put(DatabaseHelper.KEY_BODY, nota.getBody());

//        long result = database.update(DatabaseHelper.DATABASE_TABLE, values,
//                WHERE_ID_EQUALS,
//                new String[] { String.valueOf(nota.getIdCode()) });

        return database.updateWithOnConflict(DatabaseHelper.DATABASE_TABLE,
                values,
                WHERE_ID_EQUALS,
                new String[]{ String.valueOf(nota.getIdCode()) },0);
    }

    public int delete(NoteInfo nota) {
        return database.delete(DatabaseHelper.DATABASE_TABLE, WHERE_ID_EQUALS,
                new String[] { nota.getIdCode() + "" });
    }

}
