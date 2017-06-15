package com.example.user.pdfreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 5/16/2017.
 */

public class BookListArrayAdapter extends ArrayAdapter<File> {
    private ArrayList<File> fileList;

    public BookListArrayAdapter(Context context, ArrayList<File> fileList){
        super(context,0);
        this.fileList=fileList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View returnView=convertView;
        if(returnView==null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            returnView = inflater.inflate(R.layout.array_item, parent, false);
        }
        TextView nameView=(TextView)returnView.findViewById(R.id.book_name);
        File displayedFile=fileList.get(position);
        nameView.setText(displayedFile.getName());
        return returnView;
    }

    @Override
    public int getCount() {
        return fileList.size();
    }
}
