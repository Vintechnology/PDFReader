package com.example.user.pdfreader.pdf_manager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.pdfreader.InteractiveImageView;
import com.example.user.pdfreader.R;
import com.example.user.pdfreader.pdf_manager.PDFDisplayer;

import java.io.File;
import java.util.Stack;

/**
 * Created by user on 5/5/2017.
 */

public class BookPageAdapter extends PagerAdapter {
    private Context mContext;
    private PDFDisplayer mDisplayer;
    private Stack<View> recycleViewStack;
    private View.OnTouchListener childListener;
    public BookPageAdapter(Context context,File fileToRead){
        this.mContext=context;
        this.mDisplayer=new PDFDisplayer(fileToRead);
        this.recycleViewStack= new Stack<>();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mDisplayer.currentPagePosition=position;
        View returnView;
        if(recycleViewStack.isEmpty()){
            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            returnView=inflater.inflate(R.layout.page_item,container,false);
        }else{
            returnView=recycleViewStack.pop();
        }
        InteractiveImageView imageView=(InteractiveImageView) returnView.findViewById(R.id.img_view);
        imageView.setImageBitmap(mDisplayer.updateView());
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((InteractiveImageView)v).panListener.onTouch(v,event);
                childListener.onTouch(v,event);
                return true;
            }
        });
        container.addView(returnView);
        return returnView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View recycleView=(View)object;
        container.removeView(recycleView);
        recycleViewStack.push(recycleView);
    }

    @Override
    public int getCount() {
        return mDisplayer.getPageCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public void closeRenderer(){
        mDisplayer.closeRenderer();
    }

    public void setChildOnTouchListener(View.OnTouchListener listener){
        childListener=listener;
    }

}
