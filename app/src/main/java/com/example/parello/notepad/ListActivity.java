package com.example.parello.notepad;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
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
                if(isLargeScreen())
                refreshTabletListFragment();
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
                "Clicked on Note: " + nota.getIdCode() +
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
                        deleteNote(note);
                    }
                })
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return alertDialogBuilder.create();

    }

    private void findViewById(){
        titleEditor = (EditText)findViewById(R.id.editor_note_title);
        bodyEditor = (EditText)findViewById(R.id.note_body);
    }

   private void refreshTabletListFragment(){
       ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
       listFragment.refreshList();
   }

    private void resetTextFields(){
        titleEditor.setText("");
        bodyEditor.setText("");
    }
}
