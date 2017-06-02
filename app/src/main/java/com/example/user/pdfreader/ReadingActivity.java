package com.example.user.pdfreader;

import android.annotation.SuppressLint;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.user.pdfreader.pdf_manager.BookPageAdapter;
import com.example.user.pdfreader.pdf_manager.BookViewPager;

import java.io.File;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReadingActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private BookViewPager mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private TextView mPageCounterBox;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private AlphaAnimation fadeAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("OnCreate","DEBUG");
        setContentView(R.layout.activity_reading);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (BookViewPager) findViewById(R.id.fullscreen_content);
        mPageCounterBox= (TextView)findViewById(R.id.page_counter_box);

        File fileToRead=null;
        if(savedInstanceState==null){
            Bundle bundle= getIntent().getExtras();
            if(bundle!=null){
                fileToRead=(File) bundle.get(MainActivity.FILE_TO_READ);
            }
        }else{
            fileToRead=(File)savedInstanceState.get(MainActivity.FILE_TO_READ);
        }
        setTitle(fileToRead.getName());
        mContentView.setAdapter(new BookPageAdapter(this,fileToRead));

        final GestureDetectorCompat detector=new GestureDetectorCompat(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                toggle();
                pageCounterToggle();
                Log.d("Gesture","SingleTap");
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
        // Set up the user interaction to manually show or hide the system UI.
        View.OnTouchListener toggleListener=new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        };// TODO: 5/4/2017 Rmember to set this listener on child item
        BookPageAdapter adapter=(BookPageAdapter) mContentView.getAdapter();
        adapter.setChildOnTouchListener(toggleListener);
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        View.OnClickListener buttonListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewId=v.getId();
                switch(viewId){
                    case R.id.next_button:// TODO: 5/5/2017 sthg here
                        mContentView.nextPage();
                        break;
                    case R.id.previous_button:
                        mContentView.previousPage();
                        break;
                }
            }
        };
        View nextButton=findViewById(R.id.next_button);
        nextButton.setOnTouchListener(mDelayHideTouchListener);
        nextButton.setOnClickListener(buttonListener);
        View previousButton=findViewById(R.id.previous_button);
        previousButton.setOnTouchListener(mDelayHideTouchListener);
        previousButton.setOnClickListener(buttonListener);

        mPageCounterBox.setText(mContentView.getCurrentItem()+1+"/"+mContentView.getPageCount());

        mContentView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View currentView=mContentView.findViewWithTag(BookPageAdapter.TAG+position);
                InteractiveImageView currentImageView = (InteractiveImageView) currentView.findViewById(R.id.img_view);
                currentImageView.syncMatrix();
                mPageCounterBox.setText(mContentView.getCurrentItem()+1+"/"+mContentView.getPageCount());
                pageCounterToggle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fadeAnimate=new AlphaAnimation(mPageCounterBox.getAlpha(),0.0f);
        fadeAnimate.setDuration(UI_ANIMATION_DELAY);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavUtils.navigateUpFromSameTask(this);
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
        pageCounterToggle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BookPageAdapter adapter=(BookPageAdapter)mContentView.getAdapter();
        adapter.closeRenderer();
        Log.d("ReadingActivity","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        BookPageAdapter adapter=(BookPageAdapter)mContentView.getAdapter();
        adapter.openRenderer();
        Log.d("ReadingActivity","onRestart");
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
            delayedHide(AUTO_HIDE_DELAY_MILLIS);
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void pageCounterToggle(){
        switch(mPageCounterBox.getVisibility()){
            case View.VISIBLE:
                mHideHandler.removeCallbacks(hideCounter);
                mHideHandler.removeCallbacks(fadeAnimateRunnable);
                mHideHandler.postDelayed(fadeAnimateRunnable,AUTO_HIDE_DELAY_MILLIS-UI_ANIMATION_DELAY);
                mHideHandler.postDelayed(hideCounter,AUTO_HIDE_DELAY_MILLIS);
                break;
            case View.GONE:
                mHideHandler.post(showCounter);
                mHideHandler.postDelayed(fadeAnimateRunnable,AUTO_HIDE_DELAY_MILLIS-UI_ANIMATION_DELAY);
                mHideHandler.postDelayed(hideCounter,AUTO_HIDE_DELAY_MILLIS);
                break;
        }
    }
    private Runnable hideCounter=new Runnable() {
        @Override
        public void run() {
            mPageCounterBox.setVisibility(View.GONE);
        }
    };
    private Runnable showCounter=new Runnable() {
        @Override
        public void run() {
            mPageCounterBox.setVisibility(View.VISIBLE);
        }
    };
    private Runnable fadeAnimateRunnable=new Runnable() {
        @Override
        public void run() {
            mPageCounterBox.startAnimation(fadeAnimate);
        }
    };
}
