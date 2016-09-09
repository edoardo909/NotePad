package com.example.parello.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.parello.adapter.ListAdapter;
import com.example.parello.database.DatabaseHelper;
import com.example.parello.database.NotesInfoDBDAO;
import com.example.parello.database.DatabaseHandler;
import com.example.parello.notepad.ListActivity;
import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.NoteSelectedListener;
import com.example.parello.notepad.R;

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
    private DatabaseHandler handler;

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

    private List<NoteInfo> displayListView(View view) {
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        listView = (ListView) view.findViewById(R.id.listaDiNote);

        List<NoteInfo> notesList = handler.getAllNotes();

        if (notesList.isEmpty()) {
        notesList.add(new NoteInfo("database"));
        notesList.add(new NoteInfo("vuoto"));
        notesList.add(new NoteInfo("quindi"));
        notesList.add(new NoteInfo("testo di prova"));
        }
        NoteInfo[] noteArray = notesList.toArray(new NoteInfo[notesList.size()]);
        //create an ArrayAdaptar from the String Array
        ListAdapter dataAdapter = new ListAdapter(noteArray, getActivity());
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

        return notesList;
    }


}
