package com.example.beam4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;


public class SelectPhotoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<Uri> photoGroup = new ArrayList<>();
    protected ImageView bigImage;
    private CheckBox checkButton;
    private Button deleteExceptBM;
    private ArrayList<Uri> unselectedPhotoGroup = new ArrayList<>();
    private Uri unselectedPhoto;
    private ArrayList<Uri> selectedPhotoGroup = new ArrayList<>();
    private Uri selectedPhoto;
    private ArrayList<Boolean> checkedPhotoList = new ArrayList<>();
    private int whatPhotoPosition;
    private SelectPhotoActivityRowAdapter adapter;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        photoGroup.clear();
        selectedPhotoGroup.clear();
        unselectedPhotoGroup.clear();

        if (photoFileClass.photoFileArrayList != null) {
            for (int i = 0; i < 7; i++) {
                photoGroup.add(photoFileClass.photoFileArrayList.get(i));
            }
        }

        for (int i = 0; i < 25; i++) {
            checkedPhotoList.add(false);
        }

        setBitmapArrayList(photoGroup);

        bigImage = findViewById(R.id.bigImage);
        bigImage.setAdjustViewBounds(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setItemPrefetchEnabled(true);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.select_photo_group);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SelectPhotoActivityRowAdapter(com.example.beam4.SelectPhotoActivity.this, bitmapArrayList);
        adapter.setOnItemClickListener(new SelectPhotoActivityRowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                whatPhotoPosition = position;
                new SetBigImageTask().execute(whatPhotoPosition);
                checkButton.setChecked(checkedPhotoList.get(position));
            }
        });

        recyclerView.setAdapter(adapter);

        SelectPhotoActivityRowDecoration selectPhotoActivityRowDecoration = new SelectPhotoActivityRowDecoration(25,25,30,30);
        recyclerView.addItemDecoration(selectPhotoActivityRowDecoration);

        new SetBigImageTask().execute(0);
        // checkButtonClick
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnCheckedChangeListener(this);

        // trashcan으로 전송
        deleteExceptBM = findViewById(R.id.deleteExceptBM);
        deleteExceptBM.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DeletePhotoTask().execute();
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
    private class SetBigImageTask extends AsyncTask<Integer, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            int position = integers[0];
            if(photoGroup.size() != 0){
                Uri uri = photoGroup.get(position);
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(com.example.beam4.SelectPhotoActivity.this.getContentResolver(), uri);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 500, 500, false);
                    int degree = RotateBmpClass.GetExifOrientation(uri.getPath());
                    Bitmap rotatedBitmap = RotateBmpClass.GetRotatedBitmap(scaledBitmap, degree);
                    bmp.recycle();
                    bmp = rotatedBitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return bmp;
            } else{
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap == null){
                bigImage.setImageResource(android.R.color.transparent);
            } else {
                bigImage = findViewById(R.id.bigImage);
                bigImage.setImageBitmap(bitmap);
            }
        }
    }

    private class DeletePhotoTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
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
            setBitmapArrayList(photoGroup);

            if(unselectedPhotoGroup != null){
                photoFileClass.trashFileArrayList.addAll(unselectedPhotoGroup);
            }
            count = selectedPhotoGroup.size();
            for (int i = 0; i < count; i++) {
                checkedPhotoList.add(false);
            }

            selectedPhotoGroup.clear();
            unselectedPhotoGroup.clear();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new SetBigImageTask().execute(0);
            if(!checkedPhotoList.isEmpty()){
                Boolean check = checkedPhotoList.get(0);
                whatPhotoPosition = 0;
                checkButton.setChecked(check);
            }
            else{
                checkButton.setChecked(false);
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void setBitmapArrayList(ArrayList<Uri> photoUriGroup) {
        bitmapArrayList.clear();
        for(Uri uri : photoUriGroup){
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(com.example.beam4.SelectPhotoActivity.this.getContentResolver(), uri);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 250, 250, false);
                int degree = RotateBmpClass.GetExifOrientation(uri.getPath());
                Bitmap rotatedBitmap = RotateBmpClass.GetRotatedBitmap(scaledBitmap, degree);
                bmp.recycle();
                bmp = rotatedBitmap;
                bitmapArrayList.add(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

