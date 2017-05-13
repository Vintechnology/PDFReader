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
    private int currentPagePosition;
    private int pageCount;
    public BookViewPager(Context context) {
        super(context);
        shareContructor();
    }

    public BookViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        shareContructor();
    }

    private void shareContructor(){
        currentPagePosition=0;
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPagePosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        pageCount=adapter.getCount();
    }

    public void nextPage(){
        if(currentPagePosition<pageCount-1)
        setCurrentItem(currentPagePosition+1,true);
    }

    public void previousPage(){
        if(currentPagePosition>0){
            setCurrentItem(currentPagePosition-1,true);
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
}
