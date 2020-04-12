package com.example.beam4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;

public class SelectPhotoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Toolbar toolbar;
    private ArrayList<Integer> photoGroup = new ArrayList<>();
    private ImageView bigImage;
    private CheckBox checkButton;
    private Button deleteExceptBM;
    private ArrayList<Integer> unselectedPhotoGroup = new ArrayList<>();
    private ArrayList<Integer> selectedPhotoGroup = new ArrayList<>();
    private ArrayList<Boolean> checkedPhotoList = new ArrayList<>();
    private int whatPhotoPosition;
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
        selectedPhotoGroup.clear();
        unselectedPhotoGroup.clear();

        final int [] photoId = {
                R.drawable.sample1, R.drawable.sample2, R.drawable.sample3, R.drawable.sample4
        };

        for (int i = 0; i < photoId.length; i++) {
            photoGroup.add(photoId[i]);
        }

        for (int i = 0; i < photoId.length; i++) {
            checkedPhotoList.add(false);
        }

        bigImage = findViewById(R.id.bigImage);
        bigImage.setImageResource(photoGroup.get(0));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.select_photo_group);
        recyclerView.setLayoutManager(linearLayoutManager);
        final SelectPhotoActivityRowAdapter adapter = new SelectPhotoActivityRowAdapter(photoGroup);

        adapter.setOnItemClickListener(new SelectPhotoActivityRowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                whatPhotoPosition = position;
                bigImage.setImageResource(photoGroup.get(position));
                checkButton.setChecked(checkedPhotoList.get(position));
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SelectPhotoActivityRowDecoration(25,25,30,30));

        // checkButtonClick
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnCheckedChangeListener(this);

        // trashcan으로 전송
        deleteExceptBM = findViewById(R.id.deleteExceptBM);
        deleteExceptBM.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                for (int i = 0; i < photoGroup.size(); i++) {
                    if(checkedPhotoList.get(i) == false){
                        unselectedPhotoGroup.add(photoGroup.get(i));
                    } else{
                        selectedPhotoGroup.add(photoGroup.get(i));
                    }
                }
                // 데이터베이스로 관리
//                Fragment fragment = new TrashCanFragment();
//                Bundle bundle = new Bundle(1);
//                bundle.putIntegerArrayList("unselectedPhotoGroup", unselectedPhotoGroup);
//                fragment.setArguments(bundle);

                photoGroup.clear();
                checkedPhotoList.clear();
                for (int i = 0; i < selectedPhotoGroup.size(); i++) {
                    photoGroup.add(selectedPhotoGroup.get(i));
                }
                for (int i = 0; i < selectedPhotoGroup.size(); i++) {
                    checkedPhotoList.add(false);
                }
                selectedPhotoGroup.clear();
                unselectedPhotoGroup.clear();

                bigImage.setImageResource(photoGroup.get(0));
                SelectPhotoActivityRowAdapter adapter = new SelectPhotoActivityRowAdapter(photoGroup);
                adapter.setOnItemClickListener(new SelectPhotoActivityRowAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        whatPhotoPosition = position;
                        bigImage.setImageResource(photoGroup.get(position));
                        checkButton.setChecked(checkedPhotoList.get(position));
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            checkedPhotoList.set(whatPhotoPosition, true);
        }else{
            checkedPhotoList.set(whatPhotoPosition, false);
        }
    }

}

