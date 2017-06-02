package com.example.user.pdfreader;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by user on 4/29/2017.
 */

public class InteractiveImageView extends android.support.v7.widget.AppCompatImageView {
    protected  Matrix matrix;
    private static float globalScale=1f;
    public OnTouchListener panListener=new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);

            PointF curr = new PointF(event.getX(), event.getY());

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    last.set(curr);

                    start.set(last);

                    mode = DRAG;

                    break;

                case MotionEvent.ACTION_MOVE:

                    if (mode == DRAG) {

                        float deltaX = curr.x - last.x;

                        float deltaY = curr.y - last.y;

                        float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);

                        float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);

                        matrix.postTranslate(fixTransX, fixTransY);

                        fixTrans();

                        last.set(curr.x, curr.y);

                    }

                    break;

                case MotionEvent.ACTION_UP:

                    mode = NONE;

                    int xDiff = (int) Math.abs(curr.x - start.x);

                    int yDiff = (int) Math.abs(curr.y - start.y);

                    if (xDiff < CLICK && yDiff < CLICK)

                        performClick();

                    break;

                case MotionEvent.ACTION_POINTER_UP:

                    mode = NONE;

                    break;

            }

            setImageMatrix(matrix);

            invalidate();

            return true; // indicate event was handled

        }

    };
    // We can be in one of these 3 states
    public static final int NONE = 0;
    public static final int DRAG = 1;
    public static final int ZOOM = 2;

    private int mode = NONE;

    // Remember some things for zooming
    private PointF last = new PointF();
    private PointF start = new PointF();
    private float minScale = 1f;
    private float maxScale = 3f;
    private float[] m;
    private int viewWidth, viewHeight;

    private static final int CLICK = 3;

    protected float saveScale = 1f;

    protected float origWidth, origHeight;

    private int oldMeasuredWidth, oldMeasuredHeight;

    private ScaleGestureDetector mScaleDetector;

    public InteractiveImageView(Context context) {
        super(context);
        sharedConstructing();

    }

    public InteractiveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing();
    }

    private void sharedConstructing() {

        super.setClickable(true);

        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        matrix = new Matrix();

        m = new float[9];
        setOnTouchListener(panListener);
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(matrix);

    }

    public void setMaxZoom(float x) {

        maxScale = x;

    }

    public void setMinZoon(float x){
        minScale=x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            mode = ZOOM;

            return true;

        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float mScaleFactor = detector.getScaleFactor();

            scaleImage(mScaleFactor,detector.getFocusX(),detector.getFocusY());
            globalScale=saveScale;
            Log.d("scale",""+saveScale);
            Log.d("scale factor",""+mScaleFactor);
            return true;

        }

    }

    void fixTrans() {

        matrix.getValues(m);

        float transX = m[Matrix.MTRANS_X];

        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);

        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);
        if (fixTransX != 0 || fixTransY != 0)

            matrix.postTranslate(fixTransX, fixTransY);

    }



    float getFixTrans(float trans, float viewSize, float contentSize) {

        float minTrans, maxTrans;

        if (contentSize <= viewSize) {

            minTrans = 0;

            maxTrans = viewSize - contentSize;

        } else {

            minTrans = viewSize - contentSize;

            maxTrans = 0;

        }

        if (trans < minTrans)

            return -trans + minTrans;

        if (trans > maxTrans)

            return -trans + maxTrans;

        return 0;

    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {

        if (contentSize <= viewSize) {

            return 0;

        }

        return delta;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("OnMeasure","Called");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);

        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        //
        // Rescales image on rotation
        //
        if (oldMeasuredWidth == viewWidth && oldMeasuredHeight == viewHeight

                || viewWidth == 0 || viewHeight == 0)

            return;

        oldMeasuredHeight = viewHeight;

        oldMeasuredWidth = viewWidth;

        fitImageToView();

    }

    public boolean isDraggable(){
        if(viewWidth >= origWidth * saveScale)return false;
        return true;
    }

    public float getMatrixValue(int value){
        float[] matrixValue=new float[9];
        matrix.getValues(matrixValue);
        return matrixValue[value];
    }

    public boolean atImageEnd(){//in scale mode to account
        float currXPos=getMatrixValue(Matrix.MTRANS_X);
        float maxOffset=viewWidth-origWidth*saveScale;
        return currXPos<=maxOffset;
    }
    public boolean atImageStart(){//in scale mode to account
        float currXPos=getMatrixValue(Matrix.MTRANS_X);
        float minOffset=0;
        return currXPos>=minOffset;
    }

    public void scaleImage(float mScaleFactor,float focusX,float focusY){
        float origScale = saveScale;

        saveScale *= mScaleFactor;

        if (saveScale > maxScale) {

            saveScale = maxScale;

            mScaleFactor = maxScale / origScale;

        } else if (saveScale < minScale) {

            saveScale = minScale;

            mScaleFactor = minScale / origScale;

        }

        if (origWidth * saveScale < viewWidth || origHeight * saveScale < viewHeight){
            matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            //Center image after scale
            float redundantYSpace=(viewHeight-origHeight*saveScale)/2f;
            float ySpace=getMatrixValue(Matrix.MTRANS_Y);
            redundantYSpace-=ySpace;
            matrix.postTranslate(-getMatrixValue(Matrix.MTRANS_X),redundantYSpace);
        }

        else

            matrix.postScale(mScaleFactor, mScaleFactor, focusX, focusY);

        fixTrans();
    }

    public void syncMatrix(){
        Log.d("Sync Matrix","Called");
        scaleImage(globalScale/saveScale,0,0);
        setImageMatrix(matrix);
    }

    public void fitImageToView(){
        if (saveScale == minScale) {

        //Fit to screen.

        Drawable drawable = getDrawable();

        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)

            return;

        int bmWidth = drawable.getIntrinsicWidth();

        int bmHeight = drawable.getIntrinsicHeight();

        Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

        float scaleX =  (float)viewWidth / (float) bmWidth;

        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                matrix.setScale(scaleX,scaleX);
                origWidth=viewWidth;
                origHeight=bmHeight*scaleX;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                matrix.setScale(scaleX, scaleX);

                // Center the image

                float redundantYSpace = (float) viewHeight - (scaleX * (float) bmHeight);

                redundantYSpace /= (float) 2;


                matrix.postTranslate(0, redundantYSpace);

                origWidth = viewWidth;

                origHeight = viewHeight - 2 * redundantYSpace;
                break;
        }

        minScale=1f;
        saveScale=minScale;

        setImageMatrix(matrix);

    }

        fixTrans();}

}
