package com.example.parello.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.notepad.ListActivity;
import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.R;

/**
 * Created by Parello on 06/09/2016.
 */
public class NotesFragment extends Fragment {

    private EditText editor;
    private NoteInfo note;

   public static NotesFragment getInstance(NoteInfo nota) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putParcelable("nota", nota);
        fragment.setArguments(args);
        return fragment;
    }

    public static NotesFragment setInstance(){
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        NoteInfo note = new NoteInfo();
        note.setBody("nota di prova");
        args.putParcelable("nota", note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
               //emptyNotesFragment();
                return true;
            case R.id.save_note:
                setInstance();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.editor_view, container, false);
        editor = (EditText) layout.findViewById(R.id.editText);

            if(getArguments().getParcelable("nota") != null) {
                NoteInfo nota = getArguments().getParcelable("nota");
            if (nota == null) {
                setNote(nota);
            } else {
                editor.setText(nota.getTitle());
             }
            }
        return layout;
    }

    public void setNote(NoteInfo note){
        note = new NoteInfo();
        note.setTitle(editor.getText().toString());
        note.setBody(editor.getText().toString());

    }
    
}