package com.example.parello.adapter;

import com.example.parello.notepad.NoteInfo;
import com.example.parello.notepad.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parello on 08/09/2016.
 */
public class ListAdapter extends BaseAdapter {

    NoteInfo[] data;
    Context context;
    LayoutInflater layoutInflater;


    public ListAdapter(NoteInfo[] data, Context context) {
        super();
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        NoteInfo nota = data[position];
        ViewHolder holder = null;

        if (layout == null) {
            layout = layoutInflater.inflate(R.layout.note_list_row, null);

            holder = new ViewHolder();

            holder.checkBox = (CheckBox) layout.findViewById(R.id.check_note);
            holder.title = (TextView) layout.findViewById(R.id.note_title);

            layout.setTag(holder);
        } else {
            holder = (ViewHolder) layout.getTag();
        }
        holder.title.setText(nota.getTitle());

        return layout;

    }

    static class ViewHolder {
        TextView title;
        CheckBox checkBox;
    }
}
