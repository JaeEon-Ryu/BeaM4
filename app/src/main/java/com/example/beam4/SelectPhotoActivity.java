package com.example.beam4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SelectPhotoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<Uri> photoGroup = new ArrayList<>();
    private ImageView bigImage;
    private CheckBox checkButton;
    private Button deleteExceptBM;
    private ArrayList<Uri> unselectedPhotoGroup = new ArrayList<>();
    private Uri unselectedPhoto;
    private ArrayList<Uri> selectedPhotoGroup = new ArrayList<>();
    private Uri selectedPhoto;
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
        if (photoFileClass.photoFileArrayList != null) {
            for (int i = 0; i < photoFileClass.photoFileArrayList.size(); i++) {
                photoGroup.add(photoFileClass.photoFileArrayList.get(i));
            }
        }


        for (int i = 0; i < 20; i++) {
            checkedPhotoList.add(false);
        }

        bigImage = findViewById(R.id.bigImage);
        bigImage.setAdjustViewBounds(true);
        setBigImage(0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setItemPrefetchEnabled(true);
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.select_photo_group);
        recyclerView.setLayoutManager(linearLayoutManager);
        final SelectPhotoActivityRowAdapter adapter = new SelectPhotoActivityRowAdapter(this, photoGroup);

        adapter.setOnItemClickListener(new SelectPhotoActivityRowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                whatPhotoPosition = position;
                setBigImage(whatPhotoPosition);
                checkButton.setChecked(checkedPhotoList.get(position));
            }
        });

        recyclerView.setAdapter(adapter);
        SelectPhotoActivityRowDecoration selectPhotoActivityRowDecoration = new SelectPhotoActivityRowDecoration(25,25,30,30);
        recyclerView.addItemDecoration(selectPhotoActivityRowDecoration);

        // checkButtonClick
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnCheckedChangeListener(this);

        // trashcan으로 전송
        deleteExceptBM = findViewById(R.id.deleteExceptBM);
        deleteExceptBM.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int count = photoGroup.size();
                for (int i = 0; i < count; i++) {
                    if(checkedPhotoList.get(i) == false){
                        unselectedPhoto = photoGroup.get(i);
                        unselectedPhotoGroup.add(unselectedPhoto);
                    } else{
                        selectedPhoto = photoGroup.get(i);
                        selectedPhotoGroup.add(selectedPhoto);
                    }
                }

                photoGroup.clear();
                checkedPhotoList.clear();

                photoGroup.addAll(selectedPhotoGroup);

                if(unselectedPhotoGroup != null){
                    photoFileClass.trashFileArrayList.addAll(unselectedPhotoGroup);
                }
                count = selectedPhotoGroup.size();
                for (int i = 0; i < count; i++) {
                    checkedPhotoList.add(false);
                }

                selectedPhotoGroup.clear();
                unselectedPhotoGroup.clear();

                setBigImage(0);
                if(!checkedPhotoList.isEmpty()){
                    Boolean check = checkedPhotoList.get(0);
                    whatPhotoPosition = 0;
                    checkButton.setChecked(check);
                }
                else{
                    checkButton.setChecked(false);
                }

                adapter.notifyDataSetChanged();
//                SelectPhotoActivityRowAdapter adapter = new SelectPhotoActivityRowAdapter(getApplicationContext(), photoGroup);
//                adapter.setOnItemClickListener(new SelectPhotoActivityRowAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//                        setBigImage(position);
//                        checkButton.setChecked(checkedPhotoList.get(position));
//                    }
//                });
//                recyclerView.setAdapter(adapter);
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

    private void setBigImage(int position){
        if(photoGroup.size() != 0){
            Uri uri = photoGroup.get(position);
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Matrix matrix = new Matrix();
            matrix.preRotate(90,0,0);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
            bmp = Bitmap.createScaledBitmap(bmp, 500, 500, false);

            //---------------------------------------------------------------

            // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
            // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.
            bigImage = findViewById(R.id.bigImage);
            bigImage.setImageBitmap(bmp);
        } else{
            bigImage.setImageResource(android.R.color.transparent);
        }

    }
}

