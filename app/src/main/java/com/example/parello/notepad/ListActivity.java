package com.example.parello.notepad;

import android.app.Fragment;
import android.os.Bundle;
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
                saveNote(note);
                return true;
            case R.id.delete_note:
                deleteNote(note);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        startHandler();


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
                                    Toast.LENGTH_LONG).show();
        } else {
                 getFragmentManager().findFragmentById(R.id.displayNote);

        }
    }


    public void emptyNotesFragment(){
        if (!isLargeScreen()) {
            NotesFragment notesFragment =  NotesFragment.newInstance();

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, notesFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
                getFragmentManager().findFragmentById(R.id.displayNote);
        }
    }

    private void startHandler(){
        handler = new DatabaseHandler(getApplicationContext());
    }

    private void saveNote(NoteInfo nota){

        titleEditor = (EditText)findViewById(R.id.editor_note_title);
        bodyEditor = (EditText)findViewById(R.id.note_body);
        if(nota.isSelected()) {
            getEditorContent(nota);
            handler.update(nota);
            Toast.makeText(getApplicationContext(),
                    "updated Note: " + nota.getIdCode(),
                    Toast.LENGTH_LONG).show();
        }else {

            getEditorContent(nota);
            handler.save(nota);
            Toast.makeText(getApplicationContext(),
                    "Saved Note: " + nota.getIdCode(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote(NoteInfo nota) {
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


//    private void getInstance(NoteInfo nota){
//        if (!isLargeScreen()) {
//            NotesFragment notesFragment =  NotesFragment.setInstance(nota);
//            getFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_content, notesFragment)
//                    .addToBackStack(null)
//                    .commit();
//        } else {
//            getFragmentManager().findFragmentById(R.id.displayNote);
//        }
//    }

}
