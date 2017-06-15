package com.example.user.pdfreader.pdf_manager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.user.pdfreader.InteractiveImageView;
import com.example.user.pdfreader.R;

/**
 * Created by user on 5/6/2017.
 */

public class BookViewPager extends ViewPager {
    private int pageCount;
    private float startX;
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

    public boolean toPage(int realPagePosition){
        int indexPagePosition=realPagePosition-1;
        if(indexPagePosition>=0 && indexPagePosition<pageCount){
            setCurrentItem(indexPagePosition,false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View currentView=findViewWithTag(BookPageAdapter.TAG+getCurrentItem());
        InteractiveImageView currentImageView = (InteractiveImageView) currentView.findViewById(R.id.img_view);
        return !currentImageView.isDraggable() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
    public int getPageCount(){
        return pageCount;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }
}
