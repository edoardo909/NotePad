package com.example.parello.notepad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.database.DatabaseHelper;
import com.example.parello.fragments.ListFragment;
import com.example.parello.fragments.NotesFragment;

public class ListActivity extends AppCompatActivity implements NoteSelectedListener {

    private boolean notePage = false;
    private DatabaseHandler handler;
    private CheckBox checkBox;
    private EditText bodyEditor;
    private EditText titleEditor;
    NoteInfo note = new NoteInfo();
    private boolean isLargeScreen() {
        Fragment listFragment = getFragmentManager().findFragmentById(R.id.displayList);
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
                note.describeContents();
                saveNote(note);
                return true;
            case R.id.delete_note:
                deleteNote(note);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DatabaseHelper helper = new DatabaseHelper(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        if (!isLargeScreen()) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, new ListFragment())
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        if ( getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void noteSelected(NoteInfo nota) {
        if (!isLargeScreen()) {
            NotesFragment notesFragment = NotesFragment.getInstance(nota);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, notesFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            NotesFragment notesFragment = (NotesFragment)
                    getFragmentManager().findFragmentById(R.id.displayNote);

        }
    }


    public void emptyNotesFragment(){
        if (!isLargeScreen()) {
            NotesFragment notesFragment =  NotesFragment.setInstance();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, notesFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            NotesFragment notesFragment = (NotesFragment)
                    getFragmentManager().findFragmentById(R.id.displayNote);
        }
    }

    private void startHandler(){
        handler = new DatabaseHandler(getApplicationContext());
    }

    private void saveNote(NoteInfo nota){
        startHandler();
        titleEditor = (EditText)findViewById(R.id.note_title);
        bodyEditor = (EditText)findViewById(R.id.note_body);
        note.setTitle(titleEditor.getText().toString());
        note.setBody(bodyEditor.getText().toString());
        handler.save(nota);
    }

    private void deleteNote(NoteInfo nota) {
        startHandler();
        NotesFragment notesFragment = NotesFragment.getInstance(nota);
        nota.getIdCode();
        handler.delete(nota);
    }
}
