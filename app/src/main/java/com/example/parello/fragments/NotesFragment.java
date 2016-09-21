package com.example.parello.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.R;

/**
 * Created by Parello on 06/09/2016.
 */
public class NotesFragment extends Fragment {

    private static EditText bodyEditor;
    private static EditText titleEditor;
    DatabaseHandler handler;

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
        handler = new DatabaseHandler(getActivity());
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
            if(getArguments().getParcelable("nota") != null) {
                NoteInfo nota = getArguments().getParcelable("nota");
            if (nota == null) {
                setNote(nota);
            } else {
                titleEditor.setText(nota.getTitle());
                bodyEditor.setText(nota.getBody());
             }
            }
        return layout;
    }

    public void setNote(NoteInfo note){
        note = new NoteInfo();
        note.setTitle(bodyEditor.getText().toString());
        note.setBody(bodyEditor.getText().toString());
    }

}