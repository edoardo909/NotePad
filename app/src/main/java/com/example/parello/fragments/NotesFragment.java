package com.example.parello.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parello on 06/09/2016.
 */
public class NotesFragment extends Fragment {

    private EditText bodyEditor;
    private EditText titleEditor;
    DatabaseHandler mDatabase;

   public static NotesFragment getInstance(NoteInfo nota) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putParcelable("nota", nota);
        fragment.setArguments(args);
        return fragment;
    }

    public static NotesFragment newInstance(){
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        NoteInfo note = new NoteInfo();
        note.setTitle("no title");
        note.setBody("");
        args.putParcelable("nota", note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHandler(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.editor_view, container, false);
        bodyEditor = (EditText) layout.findViewById(R.id.note_body);
        titleEditor = (EditText) layout.findViewById(R.id.editor_note_title);

        NoteInfo nota = null;
        try {
            nota = getArguments().getParcelable("nota");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nota == null) {
                setNote(nota);
            } else {
                titleEditor.setText(nota.getTitle());
                bodyEditor.setText(nota.getBody());
             }

        return layout;
    }

    public void setNote(NoteInfo note){
        note = new NoteInfo();
        note.setTitle(titleEditor.getText().toString());
        note.setBody(bodyEditor.getText().toString());
    }

    public void saveNote(NoteInfo nota){
        if (nota.isChecked()) {
            getEditorContent(nota);
            try {
                mDatabase.update(nota);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "unable to update note: " + nota.getIdCode(),
                        Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getActivity(),
                    "updated Note: " + nota.getIdCode(),
                    Toast.LENGTH_LONG).show();
        } else {
            nota = new NoteInfo();
            getEditorContent(nota);
            try {
                mDatabase.save(nota);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "unable to save note: " + nota.getIdCode(),
                        Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getActivity(),
                    "saved new Note",
                    Toast.LENGTH_LONG).show();
        }
    }

    private NoteInfo getEditorContent(NoteInfo nota){
        findViews();
        nota.setTitle(titleEditor.getText().toString());
        nota.setBody(bodyEditor.getText().toString());
        return nota;
    }

    public void findViews(){
        titleEditor = (EditText)getActivity().findViewById(R.id.editor_note_title);
        bodyEditor = (EditText)getActivity().findViewById(R.id.note_body);
    }

    public void resetTextFields(){
        if(titleEditor != null || bodyEditor != null) {
            titleEditor.setText("");
            bodyEditor.setText("");
        }
    }

    public boolean checkNoteFields() {
        //String title = titleEditor.getText().toString();
        String body = bodyEditor.getText().toString();
        if(!body.isEmpty())
            return true;
            else
            return false;
    }
}