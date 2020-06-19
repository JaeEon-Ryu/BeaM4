package com.example.beam4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.opencv.android.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SelectPhotoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<Uri> photoGroup = new ArrayList<>();
    protected ImageView bigImage;
    private CheckBox checkButton;
    private Button backspace;
    private Button deleteExceptBM;
    private ArrayList<Uri> unselectedPhotoGroup = new ArrayList<>();
    private Uri unselectedPhoto;
    private ArrayList<Uri> selectedPhotoGroup = new ArrayList<>();
    private Uri selectedPhoto;
    private ArrayList<Boolean> checkedPhotoList = new ArrayList<>();
    private int whatPhotoPosition;
    private SelectPhotoActivityRowAdapter adapter;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    private Boolean isDeleted=false;
    private Boolean timeFlag = false;
    private Boolean imageFlag = false;
    private Button recommendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        photoGroup.clear();
        selectedPhotoGroup.clear();
        unselectedPhotoGroup.clear();

        Intent intent = getIntent();
//        Log.i(this.getClass().getName(),"지금inent 들어온 것 =   "+ intent.getStringExtra("index"));
//        Log.i(this.getClass().getName(),"지금inent 들어온 것 =   "+ intent.getExtras().getInt("indexFromSortByImageFragment"));
        String dateTime = intent.getStringExtra("index");
        int indexFromSortByImageFragment = intent.getExtras().getInt("indexFromSortByImageFragment");

        Log.i(this.getClass().getName(),"깃발 dateTime 상태 =   "+ dateTime);
        // 어디서 왔는지 구별하기 위한 요소
        if(dateTime != null){
            timeFlag = true;
            imageFlag = false;
        }
        else{
            imageFlag = true;
            timeFlag = false;
        }

        Log.i(this.getClass().getName(),"깃발 timeFlag 상태 =   "+ timeFlag);
        Log.i(this.getClass().getName(),"깃발 imageFlag 상태 =   "+ imageFlag);
        if(timeFlag){   // SortByTimeFragment 에서 넘어오 데이터들
            if(dateTime.equals("시간 정보 없음")){
                String[] IndexArray = SortByTimeFragment.nullIndex.split(",");
                for (int i = 0; i < IndexArray.length; i++) {
                    int idx = Integer.parseInt(IndexArray[i]);
                    photoGroup.add(photoFileClass.photoFileArrayList.get(idx));
                    checkedPhotoList.add(false);
                }
            }
            else{
                for(hourlyPhotography s : SortByTimeFragment.timeList) {
                    if (s.getTimeString().equals(dateTime)) {
                        String[] IndexArray = s.getTimeIndex().split(",");
                        for (int i = 0; i < IndexArray.length; i++) {
                            int idx = Integer.parseInt(IndexArray[i]);
                            photoGroup.add(photoFileClass.photoFileArrayList.get(idx));
                            checkedPhotoList.add(false);
                        }
                        break;
                    }
                }
            }
        } else{ // SortByImage에서 넘어온 데이터들
            if(indexFromSortByImageFragment != -1){
                ArrayList<Uri> uriArrayList = photoFileClass.openCVFileArrayList.get(indexFromSortByImageFragment);
                for (int i = 0; i<uriArrayList.size(); i++){
                    photoGroup.add(uriArrayList.get(i));
                    checkedPhotoList.add(false);
                }
            }
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

        //back button
        backspace = findViewById(R.id.back_btn);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDeleted){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    //SortByTimeFragment.timeAdapter.notifyDataSetChanged();
                }
                else {
                    onBackPressed();
                }
            }
        });

        // trashcan으로 전송
        deleteExceptBM = findViewById(R.id.deleteExceptBM);
        deleteExceptBM.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DeletePhotoTask().execute();
            }
        });
        // 추천 버튼
        recommendBtn = findViewById(R.id.btn_recommend);
        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecommandPhotoTask().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isDeleted){
            SortByTimeFragment.timeAdapter.notifyDataSetChanged();
            SortByImageFragment.sortByImageAdapter.notifyDataSetChanged();

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

        }
        else {
            super.onBackPressed();
        }
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
                    for(int j=0; j<photoFileClass.photoFileArrayList.size(); j++){
                        if(unselectedPhoto == photoFileClass.photoFileArrayList.get(j)){
                            photoFileClass.photoFileArrayList.remove(j);
                        }
                    }
                } else{
                    selectedPhoto = photoGroup.get(i);
                    selectedPhotoGroup.add(selectedPhoto);
                }
            }

            photoGroup.clear();
            checkedPhotoList.clear();

            photoGroup.addAll(selectedPhotoGroup);
            for (int i = 0; i<selectedPhotoGroup.size(); i++){
                String path = selectedPhotoGroup.get(i).getPath();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);

                Mat mat = new Mat();
                Utils.bitmapToMat(bitmap, mat);

                ORB detector = ORB.create();
                Mat descriptor = new Mat();
                detector.detectAndCompute(mat, new Mat(), new MatOfKeyPoint(), descriptor);
                recommendPhotoClass.descRcmdArrayList.add(descriptor);
            }
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

            isDeleted = true;
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

            //Log.i(this.getClass().getName(),"데이터 전송 : 바뀌었다고 알림      =    ");

        }
    }
    private class RecommandPhotoTask extends AsyncTask<Void, Void, Void> {

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<String> pathArrayList = new ArrayList<>();
            for (int i =0; i<photoGroup.size(); i++){
                String path = photoGroup.get(i).getPath();
                pathArrayList.add(path);
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
            for (int i =0; i<pathArrayList.size(); i++){
                Bitmap bitmap = BitmapFactory.decodeFile(pathArrayList.get(i), options);
                bitmapArrayList.add(bitmap);
            }

            ArrayList<Mat> matArrayList = new ArrayList<>();
            for (int i = 0; i < bitmapArrayList.size(); i++){
                Mat mat = new Mat();
                Utils.bitmapToMat(bitmapArrayList.get(i), mat);
                matArrayList.add(mat);
            }

            ORB detector = ORB.create();
            MatOfKeyPoint mainKeyPoint = new MatOfKeyPoint();
            Mat mainDescriptor = new Mat();
            int max = -1;
            Mat maxDescriptor = new Mat();
            maxDescriptor = null;
            detector.detectAndCompute(matArrayList.get(0), new Mat(), mainKeyPoint, mainDescriptor);
            int recommendPhotoListSize = recommendPhotoClass.descRcmdArrayList.size();
            if(recommendPhotoListSize > 0){
                for (int i = 0; i<recommendPhotoListSize; i++) {
                    Mat subDescriptor = recommendPhotoClass.descRcmdArrayList.get(i);
                    if (mainDescriptor.isContinuous() && subDescriptor.isContinuous()) {
                        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

                        MatOfDMatch matches = new MatOfDMatch();
                        matcher.match(mainDescriptor, subDescriptor, matches);

                        List<DMatch> dMatchList = matches.toList();
                        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
                        for (int j = 0; j < dMatchList.size(); j++) {
                            if (dMatchList.get(j).distance <= 40) {
                                good_matches.add(dMatchList.get(j));
                            }
                        }
                        if (good_matches.size() > max) {
                            maxDescriptor = subDescriptor.clone();
                        }
                    }
                }

            } else {
                SelectPhotoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "추천 리스트 정보가 없습니다!!", Toast.LENGTH_LONG).show();
                        // 추천 리스트 정보 없음
                    }
                });

            }
            if (maxDescriptor != null){
                int recommend = -1;
                max = -1;
                for (int i = 0; i<matArrayList.size(); i++){
                    detector.detectAndCompute(matArrayList.get(i), new Mat(), mainKeyPoint, mainDescriptor);
                    if (mainDescriptor.isContinuous() && maxDescriptor.isContinuous()){
                        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

                        MatOfDMatch matches = new MatOfDMatch();
                        matcher.match(mainDescriptor, maxDescriptor, matches);

                        List<DMatch> dMatchList = matches.toList();
                        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
                        for (int j = 0; j < dMatchList.size(); j++) {
                            if (dMatchList.get(j).distance <= 40) {
                                good_matches.add(dMatchList.get(j));
                            }
                        }
                        if (good_matches.size() > max) {
                            recommend = i;
                        }
                    }
                }
                SelectPhotoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "추천을 완료하였습니다!", Toast.LENGTH_LONG).show();
                    }
                });

                // 추천 리스트 정보 없음
                whatPhotoPosition = recommend;
                new SetBigImageTask().execute(whatPhotoPosition);
                checkButton.setChecked(checkedPhotoList.get(whatPhotoPosition));
            }
            return null;
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

