package com.example.parello.notepad;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.fragments.ListFragment;
import com.example.parello.fragments.NotesFragment;


public class ListActivity extends AppCompatActivity implements NoteSelectedListener {

    private DatabaseHandler handler;
    private EditText bodyEditor;
    private EditText titleEditor;
    NoteInfo note = null;

    private boolean isLargeScreen() {
//        String size = getSizeName(getApplicationContext());
 //       return size == "xlarge" ? false : true;
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
                refreshFragment();
                return true;
            case R.id.delete_note:
                deleteNoteDialog().show();
                refreshFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startHandler();
        if (savedInstanceState != null) {
            return;
        }
        setContentView(R.layout.main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (!isLargeScreen()) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, new ListFragment())
                    .commit();
        }else{
            getFragmentManager().findFragmentById(R.id.list_fragment);
        }

    }

    @Override
    public void onBackPressed() {
        if ( getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
            note.setSelected(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void noteSelected(NoteInfo nota) {
        if (!isLargeScreen()) {
            nota.setSelected(true);
            NotesFragment notesFragment = NotesFragment.getInstance(nota);
            note = notesFragment.getArguments().getParcelable("nota");
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, notesFragment)
                    .addToBackStack(null)
                    .commit();

            Toast.makeText(getApplicationContext(),
                                    "Clicked on Note: " + nota.getIdCode() +
                                            " is " + nota.isSelected(),
                                    Toast.LENGTH_SHORT).show();
        } else {
                 getFragmentManager().findFragmentById(R.id.list_fragment);
            nota.setSelected(true);
            NotesFragment notesFragment = NotesFragment.getInstance(nota);
            note = notesFragment.getArguments().getParcelable("nota");
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_fragment, notesFragment)
                    .addToBackStack(null)
                    .commit();

            Toast.makeText(getApplicationContext(),
                    "Clicked on Note: " + nota.getIdCode() +
                            " is " + nota.isSelected(),
                    Toast.LENGTH_SHORT).show();

        }
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
            NotesFragment notesFragment = NotesFragment.newInstance();
            note = new NoteInfo();
            getFragmentManager().beginTransaction().replace(R.id.note_fragment,notesFragment).addToBackStack(null).commit();
                getFragmentManager().findFragmentById(R.id.list_fragment);
        }
    }

    private void startHandler(){
        handler = new DatabaseHandler(getApplicationContext());
    }

    private void saveNote(NoteInfo nota){
        titleEditor = (EditText)findViewById(R.id.editor_note_title);
        bodyEditor = (EditText)findViewById(R.id.note_body);

            if (note.isSelected()) {
                getEditorContent(nota);
                handler.update(nota);
                Toast.makeText(getApplicationContext(),
                        "updated Note: " + nota.getIdCode(),
                        Toast.LENGTH_LONG).show();
            } else {

                getFragmentManager()
                        .beginTransaction()
                        .detach(getFragmentManager().findFragmentById(R.id.list_fragment))
                        .attach(getFragmentManager().findFragmentById(R.id.list_fragment))
                        .commit();

                getEditorContent(nota);
                handler.save(nota);
                Toast.makeText(getApplicationContext(),
                        "saved new Note",
                        Toast.LENGTH_LONG).show();
            }


    }

    public void deleteNote(NoteInfo nota) {
        handler.delete(nota);
        Toast.makeText(getApplicationContext(),
                "deleted Note: " + nota.getIdCode(),
                Toast.LENGTH_SHORT).show();
    }

    private NoteInfo getEditorContent(NoteInfo nota){
        nota.setTitle(titleEditor.getText().toString());
        nota.setBody(bodyEditor.getText().toString());
        return nota;
    }

    public Dialog deleteNoteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.delete_dialog)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteNote(note);
                        onBackPressed();
                    }
                })
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return alertDialogBuilder.create();

    }


   private void refreshFragment(){
       ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
       getFragmentManager()
               .beginTransaction()
               .replace(R.id.list_fragment, listFragment)
               .addToBackStack(null)
               .commit();
   }
}
