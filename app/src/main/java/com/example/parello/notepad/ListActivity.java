package com.example.parello.notepad;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.fragments.ListFragment;
import com.example.parello.fragments.NotesFragment;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity implements NoteSelectedListener {

    private DatabaseHandler mDatabase;
    private EditText bodyEditor;
    private EditText titleEditor;
    private NoteInfo note = null;

    private boolean isLargeScreen() {
        Fragment listFragment = getFragmentManager().findFragmentById(R.id.list_fragment);
        return listFragment == null ? false : true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NotesFragment notesFragment = (NotesFragment)getFragmentManager().findFragmentById(R.id.note_fragment);
        ListFragment listFragment = (ListFragment)getFragmentManager().findFragmentById(R.id.list_fragment);
        switch (item.getItemId()) {
            case R.id.add_note:
                emptyNotesFragment();
                return true;
            case R.id.save_note:
                notesFragment.saveNote(note);
                if(isLargeScreen())
                listFragment.refreshTabletListFragment();
                return true;
            case R.id.delete_note:
                if (!isLargeScreen()) {
                    deleteNotesSmallScreenDialog().show();
                }else{
                    deleteNoteDialog().show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startHandler();
        setContentView(R.layout.main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(savedInstanceState == null) {
            if (!isLargeScreen()) {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.main_content, new ListFragment())
                        .commit();
            } else {
                getFragmentManager().findFragmentById(R.id.list_fragment);
                emptyNotesFragment();
            }

        }
    }


    @Override
    public void onBackPressed() {
        if ( getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
            note.setChecked(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void noteSelected(NoteInfo nota) {
        NotesFragment notesFragment = NotesFragment.getInstance(nota);
        titleEditor = (EditText)findViewById(R.id.editor_note_title);
        bodyEditor = (EditText)findViewById(R.id.note_body);
        if (!isLargeScreen()) {
            nota.setChecked(true);
            //NotesFragment notesFragment = NotesFragment.getInstance(nota);
            note = notesFragment.getArguments().getParcelable("nota");
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, notesFragment)
                    .addToBackStack(null)
                    .commit();

            Toast.makeText(getApplicationContext(),
                                    "Clicked on Note: " + nota.getIdCode() +
                                            " is " + nota.isChecked(),
                                    Toast.LENGTH_SHORT).show();
        } else {
                 getFragmentManager().findFragmentById(R.id.list_fragment);
            nota.setChecked(true);

            note = notesFragment.getArguments().getParcelable("nota");
            titleEditor.setText(note.getTitle().toString());
            bodyEditor.setText(note.getBody().toString());
            getFragmentManager().beginTransaction().remove(notesFragment);
            getFragmentManager().beginTransaction().add(R.id.note_fragment,notesFragment);

            Toast.makeText(getApplicationContext(),
                    "Clicked on Note: " + nota.getIdCode() +
                            " is " + nota.isChecked(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void noteChecked(NoteInfo nota){
        titleEditor = (EditText)findViewById(R.id.editor_note_title);
        bodyEditor = (EditText)findViewById(R.id.note_body);
        getFragmentManager().findFragmentById(R.id.list_fragment);
        nota.setChecked(true);
        NotesFragment notesFragment = NotesFragment.getInstance(nota);

        note = notesFragment.getArguments().getParcelable("nota");
        Toast.makeText(getApplicationContext(),
                "checked on Note: " + nota.getIdCode() +
                        " is " + nota.isChecked(),
                Toast.LENGTH_SHORT).show();
    }

    public void emptyNotesFragment(){
        if (!isLargeScreen()) {
            NotesFragment notesFragment =  NotesFragment.newInstance();
            note = new NoteInfo();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, notesFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getFragmentManager().findFragmentById(R.id.list_fragment);
            NotesFragment notesFragment = NotesFragment.newInstance();
            note = new NoteInfo();
            notesFragment.resetTextFields();
            getFragmentManager().beginTransaction().remove(notesFragment);
            getFragmentManager().beginTransaction().add(R.id.note_fragment,notesFragment);
        }
        Toast.makeText(getApplicationContext(),
                "Add a new note",
                Toast.LENGTH_SHORT).show();
    }

    private void startHandler(){
        mDatabase = new DatabaseHandler(getApplicationContext());
    }


    public Dialog deleteNoteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        final NotesFragment notesFragment = (NotesFragment) getFragmentManager().findFragmentById(R.id.note_fragment);
        alertDialogBuilder.setMessage(R.string.delete_dialog)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if(isLargeScreen()) {
                            notesFragment.deleteNotes();
                            listFragment.refreshTabletListFragment();
                            notesFragment.resetTextFields();
                            Log.i("refresh", "List refreshed");
                        }
                    }
                })
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return alertDialogBuilder.create();

    }




    private Dialog deleteNotesSmallScreenDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_content);
        final NotesFragment notesFragment = (NotesFragment)fragment;
        final ListFragment listFragment = (ListFragment)fragment;
            alertDialogBuilder.setMessage(R.string.delete_dialog)
                    .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            notesFragment.deleteNote(note);
                            listFragment.refreshPhoneListFragment();
                        }
                    })
                    .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            return alertDialogBuilder.create();


    }



}
