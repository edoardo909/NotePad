package com.example.parello.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
<<<<<<< Updated upstream
import android.widget.CheckBox;
import android.widget.CompoundButton;
=======
import android.widget.EditText;
>>>>>>> Stashed changes
import android.widget.ListView;
import android.widget.Toast;

import com.example.parello.adapter.ListAdapter;
import com.example.parello.database.DatabaseHelper;
import com.example.parello.database.NotesInfoDBDAO;
import com.example.parello.database.DatabaseHandler;
import com.example.parello.notepad.ListActivity;
import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.NoteSelectedListener;
import com.example.parello.notepad.R;
import com.example.parello.notepad.ListActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parello on 06/09/2016.
 */
public class ListFragment extends Fragment {

    NoteSelectedListener mListener;
    private NotesInfoDBDAO notesDAO;
    private ListView listView;
    private NoteInfo noteInfo;
    private DatabaseHelper db;
    private CheckBox checkBox;
    private DatabaseHandler handler;
    private EditText bodyEditor;
    private EditText titleEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new DatabaseHandler(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);
        displayListView(view);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoteSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoteSelectedListener");
        }
    }

    public static ListFragment getInstance(NoteInfo nota) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelable("nota", nota);
        fragment.setArguments(args);
        return fragment;
    }

    private List<NoteInfo> displayListView(View view) {
        listView = (ListView) view.findViewById(R.id.listaDiNote);

        final List<NoteInfo> notesList = handler.getAllNotes();

        if (notesList.isEmpty()) {
        notesList.add(new NoteInfo("database"));
        notesList.add(new NoteInfo("vuoto"));
        notesList.add(new NoteInfo("quindi"));
        notesList.add(new NoteInfo("testo di prova"));
        }
        NoteInfo[] noteArray = notesList.toArray(new NoteInfo[notesList.size()]);
        //List<NoteInfo> noteList = new ArrayList<>();
        //create an ArrayAdaptar from the String Array
        final ListAdapter dataAdapter = new ListAdapter(noteArray, getActivity());
        final ListView listView = (ListView) view.findViewById(R.id.listaDiNote);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                NoteInfo nota = (NoteInfo) listView.getAdapter().getItem(position);
                mListener.noteSelected(nota);

            }
        });

//        checkBox =(CheckBox) view.findViewById(R.id.check_note);
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        NoteInfo nota = (NoteInfo) checkBox
//                                .getTag();
//                        nota.setSelected(buttonView.isChecked());
//
//                        for(int i=0; i>notesList.size();i++){
//                            NoteInfo note = notesList.get(i);
//                        }
//                        if(nota.isSelected()){
//                            Toast.makeText(getActivity(),
//                                    "Clicked on Checkbox: " + nota.getIdCode() +
//                                            " is " + nota.isSelected(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

        return notesList;
    }



}
