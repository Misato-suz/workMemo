package com.example.workMemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButtonInput;
    private ImageButton imageButtonList;
    private Button buttonArchive;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Java xml　の結びつけ
        imageButtonInput = findViewById(R.id.imageInput);
        imageButtonList = findViewById(R.id.imageList);
        buttonArchive = findViewById(R.id.buttonArchiveFromMain);

        System.out.println("This is main activity.");
        if (FileProcessor.checkFile(getApplicationContext())) {
            FileProcessor.readFile(getApplicationContext());
        }
        FileProcessor.checkFile("1", getApplicationContext());


        //imageButtonListとimageButtonInputにOnClickListenerを付ける
        imageButtonList.setOnClickListener(this);
        imageButtonInput.setOnClickListener(this);
        buttonArchive.setOnClickListener(this);
        //spinnerにはonClickListenerを付けない！！


    }

    @Override
    public void onClick(View view) {
        if (view == imageButtonInput){
            Intent intentInput = new Intent(MainActivity.this, InputActivity.class);
            startActivity(intentInput);
        }else if ( view == imageButtonList){
            Intent intentList = new Intent(MainActivity.this, com.example.workMemo.ListWorkActivity.class);
            startActivity(intentList);
        }else if (view == buttonArchive){
            Intent intentArchive = new Intent(MainActivity.this, ArchiveActivity.class);
            startActivity(intentArchive);
        }
    }
}