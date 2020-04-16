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

public class SelectPhotoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<String> photoGroup = new ArrayList<>();
    private ImageView bigImage;
    private CheckBox checkButton;
    private Button deleteExceptBM;
    private ArrayList<String> unselectedPhotoGroup = new ArrayList<>();
    private ArrayList<String> selectedPhotoGroup = new ArrayList<>();
    private ArrayList<Boolean> checkedPhotoList = new ArrayList<>();
    private int whatPhotoPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        photoGroup.clear();
        selectedPhotoGroup.clear();
        unselectedPhotoGroup.clear();

        for (int i = 0; i < 20; i++) {
            photoGroup.add(photoFileClass.photoFileArrayList.get(i));
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

                setBigImage(0);

                SelectPhotoActivityRowAdapter adapter = new SelectPhotoActivityRowAdapter(getApplicationContext(), photoGroup);
                adapter.setOnItemClickListener(new SelectPhotoActivityRowAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        setBigImage(position);
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

    private void setBigImage(int position){
        Uri uri = Uri.parse("file:///" + photoGroup.get(position));
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
    }
}

