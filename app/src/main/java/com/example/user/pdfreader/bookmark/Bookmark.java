package com.example.user.pdfreader.bookmark;

/**
 * Created by user on 6/5/2017.
 */

public class Bookmark {
    private int page;
    private String note;
    private int id;
    public Bookmark(int page, String note,int position){
        this.page=page;
        this.note=note;
        this.id=position;
    }

    public int getPage() {
        return page;
    }

    public String getNote() {
        return note;
    }

    public int getId() {
        return id;
    }
}
