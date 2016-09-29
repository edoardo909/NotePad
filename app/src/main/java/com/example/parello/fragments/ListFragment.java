package com.example.parello.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.parello.adapter.ListAdapter;
import com.example.parello.database.DatabaseHandler;
import com.example.parello.notepad.ListActivity;
import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.NoteSelectedListener;
import com.example.parello.notepad.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Parello on 06/09/2016.
 */
public class ListFragment extends Fragment {

    NoteSelectedListener mListener;
    private ListView mListView;
    private ListAdapter mDataAdapter;
    private DatabaseHandler mDatabase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHandler(getActivity());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);

        final List<NoteInfo> notesList = getNoteInfoData();

        NoteInfo[] noteArray = notesList.toArray(new NoteInfo[notesList.size()]);
        //create an ArrayAdaptar from the String Array
        mDataAdapter = new ListAdapter(noteArray, getActivity());
        mListView = (ListView) view.findViewById(R.id.listaDiNote);
        // Assign adapter to ListView
        mListView.setAdapter(mDataAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                NoteInfo nota = (NoteInfo) mListView.getAdapter().getItem(position);
                mListener.noteSelected(nota);


            }
        });


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

    private List<NoteInfo> getNoteInfoData() {
        List<NoteInfo> notesList = mDatabase.getAllNotes();

        if (notesList.isEmpty()) {
            notesList.add(new NoteInfo("database"));
            notesList.add(new NoteInfo("vuoto"));
            notesList.add(new NoteInfo("quindi"));
            notesList.add(new NoteInfo("testo di prova"));
        }
        return notesList;
    }

    public void refreshList() {
        List<NoteInfo> notesList = mDatabase.getAllNotes();
        NoteInfo[] noteArray = notesList.toArray(new NoteInfo[notesList.size()]);
        mDataAdapter.setData(noteArray);
        mDataAdapter.notifyDataSetChanged();
    }

   public List<NoteInfo> getNotesToDelete(){
       List<NoteInfo> notesChecked = Arrays.asList(mDataAdapter.getData());
       List<NoteInfo> notesToDelete = new ArrayList<>();
       for(NoteInfo note : notesChecked){
           if (note.isChecked()){
               notesToDelete.add(note);
           }
       }
       return notesToDelete;
   }



}
