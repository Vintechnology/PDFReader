package com.example.user.pdfreader.pdf_manager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.user.pdfreader.InteractiveImageView;
import com.example.user.pdfreader.R;

/**
 * Created by user on 5/6/2017.
 */

public class BookViewPager extends ViewPager {
    private int pageCount;
    public BookViewPager(Context context) {
        super(context);
    }

    public BookViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        pageCount=adapter.getCount();
    }

    public void nextPage(){
        if(getCurrentItem()<pageCount-1)
        setCurrentItem(getCurrentItem()+1,true);
    }

    public void previousPage(){
        if(getCurrentItem()>0){
            setCurrentItem(getCurrentItem()-1,true);
        }
    }

    public void toPage(int realPagePosition){
        int indexPagePosition=realPagePosition-1;
        if(indexPagePosition>=0 && indexPagePosition<pageCount){
            setCurrentItem(indexPagePosition,false);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View currentView=findViewWithTag(BookPageAdapter.TAG+getCurrentItem());
        InteractiveImageView currentImageView = (InteractiveImageView) currentView.findViewById(R.id.img_view);
        return !currentImageView.isDragable() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
    public int getPageCount(){
        return pageCount;
    }
}
