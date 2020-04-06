package com.example.beam4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectPhotoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Integer> photoGroup = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        /*
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         */

        photoGroup.clear();
        int  [] photoId = {
                R.drawable.sample1, R.drawable.sample2, R.drawable.sample3, R.drawable.sample4
        };

        for (int i = 0; i < photoId.length; i++) {
            photoGroup.add(photoId[i]);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.select_photo_group);
        recyclerView.setLayoutManager(linearLayoutManager);
        SelectPhotoActivityRowAdapter adapter = new SelectPhotoActivityRowAdapter(photoGroup);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SelectPhotoActivityRowDecoration(25,25,30,30));
    }
}

