package com.example.user.pdfreader.pdf_manager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by user on 4/23/2017.
 */

public class PDFDisplayer {
    private boolean isClosed;
    private PdfRenderer renderer;
    private File pdfDocument;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor mFileDescriptor;
    public PDFDisplayer(File pdfDocument){
        isClosed=true;
        this.pdfDocument=pdfDocument;
        openRenderer();

    }

    public Bitmap updateView(int position){
        position=Math.min(renderer.getPageCount()-1,Math.max(position,0));
        if(currentPage!=null) currentPage.close();

        currentPage= renderer.openPage(position);
        Bitmap mBitmap= Bitmap.createBitmap(currentPage.getWidth(),currentPage.getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.WHITE);
        currentPage.render(mBitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        return mBitmap;
    }

    public void closeRenderer(){
        if(!isClosed){
            currentPage.close();
            renderer.close();
            try{
                mFileDescriptor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            isClosed=true;
            Log.d("PdfDisplayer","Renderer Closed");
        }
    }

    public void openRenderer(){
        if(isClosed){
            try{
                mFileDescriptor= ParcelFileDescriptor.open(pdfDocument,ParcelFileDescriptor.MODE_READ_ONLY);
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                renderer=new PdfRenderer(mFileDescriptor);
            }catch(Exception e){
                e.printStackTrace();
            }
            isClosed=false;
            Log.d("PdfDisplayer","Renderer Opened");
        }
    }



    public int getPageCount(){
        return renderer.getPageCount();
    }

}
