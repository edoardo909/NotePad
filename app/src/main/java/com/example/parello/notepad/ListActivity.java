package com.example.parello.notepad;

import android.app.Fragment;
<<<<<<< Updated upstream
import android.os.Bundle;
=======
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
>>>>>>> Stashed changes
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parello.adapter.ListAdapter;
import com.example.parello.database.DatabaseHandler;
import com.example.parello.fragments.ListFragment;
import com.example.parello.fragments.NotesFragment;

import java.util.List;


public class ListActivity extends AppCompatActivity implements NoteSelectedListener {

    private DatabaseHandler handler;
    private EditText bodyEditor;
    private EditText titleEditor;
    NoteInfo note = null;

    private boolean isLargeScreen() {
<<<<<<< Updated upstream
        Fragment listFragment = getFragmentManager().findFragmentById(R.id.displayList);
=======
        Fragment listFragment = getFragmentManager().findFragmentById(R.id.list_fragment);
>>>>>>> Stashed changes
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
               // note.setSelected(false);
                emptyNotesFragment();
                return true;
            case R.id.save_note:
                saveNote(note);
<<<<<<< Updated upstream
                return true;
            case R.id.delete_note:
                deleteNote(note);
=======
                if(isLargeScreen())
                refreshFragment();
                return true;
            case R.id.delete_note:
                deleteNoteDialog().show();
                if(isLargeScreen())
                refreshFragment();
>>>>>>> Stashed changes
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
            note.setSelected(false);
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
<<<<<<< Updated upstream
                 getFragmentManager().findFragmentById(R.id.displayNote);
=======
            getFragmentManager().findFragmentById(R.id.list_fragment);
            nota.setSelected(true);
            NotesFragment notesFragment = NotesFragment.getInstance(nota);
            note = notesFragment.getArguments().getParcelable("nota");

//
//            getFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.note_fragment, notesFragment)
//                    .commit();

            Toast.makeText(getApplicationContext(),
                    "Clicked on Note: " + nota.getIdCode() +
                            " is " + nota.isSelected(),
                    Toast.LENGTH_SHORT).show();
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
                getFragmentManager().findFragmentById(R.id.displayNote);
=======
            getFragmentManager().findFragmentById(R.id.list_fragment);
            NotesFragment notesFragment = NotesFragment.newInstance();
            note = new NoteInfo();
//            getFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.note_fragment,notesFragment)
//                    .commit();

>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
            getEditorContent(nota);
            handler.save(nota);
            Toast.makeText(getApplicationContext(),
                    "Saved Note: " + nota.getIdCode(),
                    Toast.LENGTH_SHORT).show();
        }
=======
            if (note.isSelected()) {
                getEditorContent(nota);
                handler.update(nota);
                Toast.makeText(getApplicationContext(),
                        "updated Note: " + nota.getIdCode(),
                        Toast.LENGTH_LONG).show();
            } else {
                getEditorContent(nota);
                if(isLargeScreen()){

                    getFragmentManager()
                            .beginTransaction()
                            .detach(getFragmentManager().findFragmentById(R.id.list_fragment))
                            .attach(getFragmentManager().findFragmentById(R.id.list_fragment))
                            .commit();
                    ListFragment listFragment = ListFragment.getInstance(nota);

                    getFragmentManager().beginTransaction().replace(R.id.list_fragment,listFragment).addToBackStack(null).commit();
                }
                handler.save(nota);
                Toast.makeText(getApplicationContext(),
                        "saved new Note",
                        Toast.LENGTH_LONG).show();
            }


>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
=======
   private void refreshFragment(){
       ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
       List<NoteInfo> notesList = handler.getAllNotes();

       NoteInfo[] noteArray = notesList.toArray(new NoteInfo[notesList.size()]);
       ListAdapter adapter = new ListAdapter(noteArray, this);
       adapter.notifyDataSetChanged();
       getFragmentManager()
               .beginTransaction().attach(listFragment).detach(listFragment).commit();

//               .replace(R.id.list_fragment, listFragment)
//               .addToBackStack(null)
//               .commit();
   }
>>>>>>> Stashed changes
}
