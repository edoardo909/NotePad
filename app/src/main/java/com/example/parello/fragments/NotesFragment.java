package com.example.parello.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
<<<<<<< Updated upstream
import android.os.Parcelable;
=======
import android.support.v7.app.AlertDialog;
>>>>>>> Stashed changes
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.R;

/**
 * Created by Parello on 06/09/2016.
 */
public class NotesFragment extends Fragment {

<<<<<<< Updated upstream
    private static EditText bodyEditor;
    private static EditText titleEditor;
    DatabaseHandler handler;
=======
    private EditText bodyEditor;
    private EditText titleEditor;
    private DatabaseHandler mDatabase;
    private NoteInfo nota = null;
>>>>>>> Stashed changes

   public static NotesFragment getInstance(NoteInfo nota) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putParcelable("nota", nota);
        fragment.setArguments(args);
        return fragment;
    }
//    public static NotesFragment setInstance(NoteInfo nota){
//        NotesFragment fragment = new NotesFragment();
//        Bundle args = getInstance(nota).getArguments();
//        nota.setTitle(titleEditor.getText().toString());
//        nota.setBody(bodyEditor.getText().toString());
//        args.putParcelable("nota", nota);
//        fragment.setArguments(args);
//        return fragment;
//
//    }

    public static NotesFragment newInstance(){
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        NoteInfo note = new NoteInfo();
        note.setTitle("no title");
        note.setBody("nota di prova");
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
<<<<<<< Updated upstream
            if(getArguments().getParcelable("nota") != null) {
                NoteInfo nota = getArguments().getParcelable("nota");
            if (nota == null) {
=======

        try {
            nota = getArguments().getParcelable("nota");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nota == null) {
>>>>>>> Stashed changes
                setNote(nota);
            } else {
                titleEditor.setText(nota.getTitle());
                bodyEditor.setText(nota.getBody());
             }
            }
        return layout;
    }

<<<<<<< Updated upstream
    public void setNote(NoteInfo note){
        note = new NoteInfo();
        note.setTitle(bodyEditor.getText().toString());
        note.setBody(bodyEditor.getText().toString());
=======
    public void setNote(NoteInfo nota){
        nota = new NoteInfo();
        nota.setTitle(titleEditor.getText().toString());
        nota.setBody(bodyEditor.getText().toString());
    }

    public void saveNote(NoteInfo nota) {

        if (nota.isChecked()) {
            getEditorContent(nota);
            try {
                mDatabase.update(nota);
            } catch (Exception e) {
                e.printStackTrace();
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
            }
            Toast.makeText(getActivity(),
                    "saved new Note",
                    Toast.LENGTH_LONG).show();
        }
    }

    public NoteInfo getEditorContent(NoteInfo nota){
        nota.setTitle(titleEditor.getText().toString());
        nota.setBody(bodyEditor.getText().toString());
        return nota;
    }

    public void deleteNote(NoteInfo nota) {
        mDatabase.delete(nota);
        Toast.makeText(getActivity(),
                "deleted Note: " + nota.getIdCode(),
                Toast.LENGTH_SHORT).show();
    }

    public Dialog deleteNoteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.delete_dialog)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteNote(nota);

                    }
                })
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return alertDialogBuilder.create();
>>>>>>> Stashed changes

    }

}