package com.example.user.pdfreader;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_TO_READ="FILE_TO_READ";
    private ListView listView;
    private ArrayList<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileList=new ArrayList<>();
        listView=(ListView) findViewById(R.id.pdf_files_list);
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
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view= super.getView(position, convertView, parent);
                        TextView text=(TextView)view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                for(File file :fileList){
                    adapter.add(file.getName());
                }
                listView.setAdapter(adapter);
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
}

