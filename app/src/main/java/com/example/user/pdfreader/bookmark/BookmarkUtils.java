package com.example.user.pdfreader.bookmark;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.pdfreader.R;
import com.example.user.pdfreader.pdf_manager.BookViewPager;

import java.util.ArrayList;

/**
 * Created by user on 6/4/2017.
 */

public class BookmarkUtils {
    public static void showAllBookmark(final Context context,final BookmarkDatabaseManager db, final int currentPage, final BookViewPager pager,final String fileName){
        ArrayList<Bookmark> array=db.getAllBookmark(fileName);
        final BookmarkArrayAdapter adapter=new BookmarkArrayAdapter(context, array);
        new AlertDialog.Builder(context).setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int page=adapter.getItem(which).getPage();
                pager.toPage(page);
            }
        }).setPositiveButton("BOOKMARK HERE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAddBookmarkDialog(db,currentPage,adapter,context,fileName);
            }
        }).setNegativeButton("CANCEL",null).setTitle("Bookmark Menu").show();
    }

    public static void showAddBookmarkDialog(final BookmarkDatabaseManager db, final int currentPage,final BookmarkArrayAdapter adapter, Context context,final String fileName){
        View layout= LayoutInflater.from(context).inflate(R.layout.add_bookmark_dialog_layout,null);
        final EditText note=(EditText)layout.findViewById(R.id.bookmark_note);
        // TODO: 6/5/2017 get user text and set page + add bookmark
        TextView page= (TextView)layout.findViewById(R.id.bookmark_page);
        TextView file=(TextView)layout.findViewById(R.id.bookmark_file);
        page.setText(currentPage);
        file.setText(fileName);
        new AlertDialog.Builder(context).setView(layout).setTitle("Add New Bookmark").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bookmark newBookmark= db.addBookmark(currentPage,note.getText().toString(),fileName);
                adapter.add(newBookmark);
            }
        }).show();

    }
}
