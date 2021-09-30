package com.example.workMemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ArchiveActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button buttonBack;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        buttonBack = findViewById(R.id.buttonBackArchive);
        listView = findViewById(R.id.listViewArchive);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(fileList());

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonBack){
            Intent intentBack = new Intent(ArchiveActivity.this, MainActivity.class);
            startActivity(intentBack);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] fileList = fileList();
        String fileName = fileList[i];
        System.out.println("filename : " + fileName);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.linearLayout,FileListFragment.newInstance(fileName,"2"));
        transaction.addToBackStack(null);
        listView.setVisibility(View.GONE);
        buttonBack.setVisibility(View.GONE);
        transaction.commit();
    }

    
}