package com.example.parello.notepad;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
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

    private DatabaseHandler handler;
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
        switch (item.getItemId()) {
            case R.id.add_note:
                emptyNotesFragment();
                return true;
            case R.id.save_note:
                saveNote(note);
                if(isLargeScreen())
                refreshTabletListFragment();
                return true;
            case R.id.delete_note:
                deleteNoteDialog().show();

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
        findViewById();
        if (!isLargeScreen()) {
            nota.setChecked(true);
            NotesFragment notesFragment = NotesFragment.getInstance(nota);
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
            NotesFragment notesFragment = NotesFragment.getInstance(nota);
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
        findViewById();
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
            resetTextFields();
            getFragmentManager().beginTransaction().remove(notesFragment);
            getFragmentManager().beginTransaction().add(R.id.note_fragment,notesFragment);
        }
        Toast.makeText(getApplicationContext(),
                "Add a new note",
                Toast.LENGTH_SHORT).show();
    }

    private void startHandler(){
        handler = new DatabaseHandler(getApplicationContext());
    }

    private void saveNote(NoteInfo nota){
            if (note.isChecked()) {
                getEditorContent(nota);
                try {
                    handler.update(nota);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "unable to update note: " + nota.getIdCode(),
                            Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(),
                        "updated Note: " + nota.getIdCode(),
                        Toast.LENGTH_LONG).show();
            } else {
                nota = new NoteInfo();
                getEditorContent(nota);
                try {
                    handler.save(nota);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "unable to save note: " + nota.getIdCode(),
                            Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(),
                        "saved new Note",
                        Toast.LENGTH_LONG).show();
            }


    }

    public void deleteNotes(){
        ListFragment listFragment = null;
        if(isLargeScreen()) {
            listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        }else{
            listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.main_content);
        }
        List<NoteInfo> notesChecked = listFragment.getNotesToDelete();
        List<NoteInfo> notesToDelete = new ArrayList<>();
        for(NoteInfo note : notesChecked){
            notesToDelete.add(note);
            if(note.isChecked())
                note.setChecked(false);
            handler.delete(note);
        }

    }

    public void deleteNote(NoteInfo nota) {
            handler.delete(nota);

        resetTextFields();
        Toast.makeText(getApplicationContext(),
                "deleted Note: " + nota.getIdCode(),
                Toast.LENGTH_SHORT).show();
    }

    private NoteInfo getEditorContent(NoteInfo nota){
        findViewById();
        nota.setTitle(titleEditor.getText().toString());
        nota.setBody(bodyEditor.getText().toString());
        return nota;
    }

    public Dialog deleteNoteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.delete_dialog)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if(isLargeScreen()) {
                            deleteNotes();
                        }else {
                            deleteNote(note);
                        }
                        if(isLargeScreen()) {
                            refreshTabletListFragment();
                            resetTextFields();
                            Log.i("refresh", "List refreshed");
                        }else {
                            refreshPhoneListFragment();
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

    private void refreshPhoneListFragment() {
        if(getFragmentManager().getBackStackEntryCount()>0){
            onBackPressed();
        }else{
            //TODO refresh del ListFragment
        }
    }

    private void findViewById(){
        titleEditor = (EditText)findViewById(R.id.editor_note_title);
        bodyEditor = (EditText)findViewById(R.id.note_body);
    }

   private void refreshTabletListFragment(){
       getFragmentManager()
               .beginTransaction()
               .add(R.id.main_content, new ListFragment()).addToBackStack(null)
               .commit();

   }

    private void resetTextFields(){
        if(titleEditor != null || bodyEditor != null) {
            titleEditor.setText("");
            bodyEditor.setText("");
        }
    }

//    private void uncheckAllNotes(){
//        List<NoteInfo> notes = handler.getAllNotes();
//        for(NoteInfo note: notes){
//            note.setChecked(false);
//        }
//    }
}
