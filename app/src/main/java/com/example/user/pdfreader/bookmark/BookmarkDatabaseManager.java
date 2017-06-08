package com.example.user.pdfreader.bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by user on 6/4/2017.
 */

public class BookmarkDatabaseManager extends SQLiteOpenHelper {// TODO: 6/6/2017 change to use SQL
    private static final int VERSION=1;
    private static final String DATABASE_NAME="ReadingActivity_BookmarkDatabase.db";
    private static final String TABLE_NAME="AllBookmark";
    private static final String BOOKMARK_PAGE_KEY="bookmark_page";
    private static final String BOOKMARK_NOTE_KEY="bookmark_note";
    private static final String BOOKMARK_FILE_KEY="bookmark_file";
    private static final String ID_KEY="id";
    public BookmarkDatabaseManager(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    public Bookmark addBookmark(int page, String note,String fileName){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(BOOKMARK_PAGE_KEY,page);
        values.put(BOOKMARK_NOTE_KEY,note);
        values.put(BOOKMARK_FILE_KEY,fileName);
        int id=(int)db.insert(TABLE_NAME,null,values);
        return new Bookmark(page,note,id);
    }

    public ArrayList<Bookmark> getAllBookmark(String fileName){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_NAME,new String[]{ID_KEY,BOOKMARK_PAGE_KEY,BOOKMARK_NOTE_KEY},BOOKMARK_FILE_KEY+"=?"
                            ,new String[]{fileName},null,null,BOOKMARK_PAGE_KEY+" ASC");
        ArrayList<Bookmark> array=new ArrayList<>();
        if(c.moveToFirst()){
            do{
                array.add(new Bookmark(c.getInt(1),c.getString(2),c.getInt(0)));
            }while(c.moveToNext());
        }
        c.close();
        return array;
    }

    /* TODO: 6/5/2017 create delete bookmark function */
    public void deleteBookmark(Bookmark deleteBookmark){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_NAME,ID_KEY+"=?",new String[]{String.valueOf(deleteBookmark.getId())});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createScript="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+ID_KEY+" INTEGER PRIMARY KEY, "+BOOKMARK_FILE_KEY+" TEXT, "
                            +BOOKMARK_PAGE_KEY+" INTEGER, "+BOOKMARK_NOTE_KEY+" TEXT)";
        db.execSQL(createScript);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
