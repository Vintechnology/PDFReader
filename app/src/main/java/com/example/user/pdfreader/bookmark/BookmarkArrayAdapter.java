package com.example.user.pdfreader.bookmark;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.pdfreader.R;

import java.util.ArrayList;

/**
 * Created by user on 6/4/2017.
 */

public class BookmarkArrayAdapter extends ArrayAdapter<Bookmark> {
    public BookmarkArrayAdapter (Context context,ArrayList<Bookmark> array){
        super(context,0,array);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.array_bookmark_item,parent,false);
        }
        Bookmark currentBookmark= getItem(position);
        TextView pageView=(TextView) convertView.findViewById(R.id.array_page);
        TextView noteView=(TextView)convertView.findViewById(R.id.array_note);
        pageView.setText(getContext().getResources().getString(R.string.bookmark_page_text)+currentBookmark.getPage());
        noteView.setText(getContext().getResources().getString(R.string.bookmark_note_text)+currentBookmark.getNote());
        return convertView;
    }
}
