package com.example.parello.notepad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.parello.database.DatabaseHandler;
import com.example.parello.database.DatabaseHelper;
import com.example.parello.fragments.ListFragment;
import com.example.parello.fragments.NotesFragment;

public class ListActivity extends AppCompatActivity implements NoteSelectedListener {

    private boolean notePage = false;
    private DatabaseHandler handler;
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
               // handler.save();
                return true;
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

    private void deleteNote(NoteInfo nota) {
        NotesFragment notesFragment = NotesFragment.getInstance(nota);
        notesFragment.getArguments().clear();
    }
//    private void saveNote(NoteInfo nota){
//        NotesFragment notesFragment = NotesFragment.setInstance();
//
//    }

}
