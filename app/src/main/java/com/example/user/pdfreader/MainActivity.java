package com.example.user.pdfreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_TO_READ="FILE_TO_READ";
    private ListView listView;
    private ArrayList<File> fileList;
    private View emptyFileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileList=new ArrayList<>();
        listView=(ListView) findViewById(R.id.pdf_files_list);
        emptyFileText=findViewById(R.id.empty_file_text);
        new PDFListLoader().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent readingIntent=new Intent(MainActivity.this,ReadingActivity.class);
                readingIntent.putExtra(FILE_TO_READ,fileList.get(position));
                startActivity(readingIntent);
            }
        });

    }

    private class PDFListLoader extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            findAllPDF(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(fileList!=null && fileList.size()>0){
                BookListArrayAdapter adapter=new BookListArrayAdapter(getApplicationContext(),fileList);
                listView.setAdapter(adapter);
            }else{
                emptyFileText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void findAllPDF (File file){
        File[] allFiles=file.listFiles();
        if(allFiles!=null){
            for(int i=0;i<allFiles.length;i++){
                File item=allFiles[i];
                String name=item.getName();
                if(item.isDirectory() && !name.substring(name.length()-4).equals(".")){
                    findAllPDF(item);
                }else if(name.endsWith(".pdf")){
                    fileList.add(item);
                }
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity Life Cycle","OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity Life Cycle","OnRestart");
    }

}

