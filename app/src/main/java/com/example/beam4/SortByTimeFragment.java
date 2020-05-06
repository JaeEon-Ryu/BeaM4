package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.UriMatcher;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SortByTimeFragment extends Fragment {

    ArrayList<SortByTime> timeData = new ArrayList<>();
    ArrayList<Uri> photoGroup = new ArrayList<>();
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    int[] timeIndex = new int[10000]; //
    String[] timeString = new String[10000];


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // uri 정보 받아오기
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd", Locale.KOREA);
        String dateTimeCurrent = "";
        String dateTimePast = "days";


//        File file = new File("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + \"/Camera/\"");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd");
//        formatter.format(file.lastModified());


        int days = -1;

        if (photoFileClass.photoFileArrayList != null) {
            for (int i = 0; i < 15; i++) { // photoFileClass.photoFileArrayList.size()
                photoGroup.add(photoFileClass.photoFileArrayList.get(i));
                // 사진정보 받기
                try {
                    //Log.i(this.getClass().getName(),"uri가져오기 =   " + photoFileClass.photoFileArrayList.get(i));
                    ExifInterface exif = new ExifInterface(photoFileClass.photoFileArrayList.get(i).getPath());
                    //Log.i(this.getClass().getName(),"exif정보 =   " + exif);
                    dateTimeCurrent = exif.getAttribute(ExifInterface.TAG_DATETIME);
                    if (dateTimeCurrent != null){
                        dateTimeCurrent = dateTimeCurrent.substring(0,10);
                    }
                    //Log.i(this.getClass().getName(),i + "번째 dateTime = " + dateTimeCurrent);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(this.getClass().getName(),"dateTime = 에러" );
                }

                if (dateTimeCurrent == null){

                }
                else if (dateTimeCurrent == dateTimePast){   // 이전 사진과 날짜가 같은 경우
                    timeIndex[days]+=1;
                    Log.i(this.getClass().getName(),"timeInfoIndex = " +timeIndex[days]);
                }
                else{ // 이전 사진과 날짜가 다른 경우
                    days++;
                    timeIndex[days]+=1;
                    timeString[days]=dateTimeCurrent;
                }
                dateTimePast = dateTimeCurrent;
            }
        }

        setBitmapArrayList(photoGroup);
            for(int i=0; i<bitmapArrayList.size(); i++){
        }

        /*
        알고리즘
        for 날짜의 개수
            if 날짜에 해당하는 사진들의 개수>5
                맨 마지막에 + 모양 삽입
            else
                사진넣기
         */

        /*
        날짜별로 사진들을 분류하는 배열을 가지려면?
        1차원배열 날짜array
        2차원배열 날짜array의 index, 사진들을 넣는 배열
         */

        Bitmap[] timeDataImg = new Bitmap[6];
        for(int i=0; i<timeString.length; i++){ // 날짜 개수만큼 반복
            for(int reset=0; reset<6; reset++){
                timeDataImg[reset] = null;
            }
            for(int num=0; num<timeIndex[i]; num++){    // 해당 날짜에 대한 사진들 추가
                timeDataImg[num]=bitmapArrayList.get(i);// 이부분 수정해야함

            }
            timeData.add(new SortByTime(timeString[i],timeDataImg[0],timeDataImg[1],timeDataImg[2],
                    timeDataImg[3],timeDataImg[4],timeDataImg[5]));
        }


        SortByTimeAdapter adapter = new SortByTimeAdapter(timeData);
        View view = inflater.inflate(R.layout.fragment_sort_by_time, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_time);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }

    private ListView listView;

    private void setBitmapArrayList(ArrayList<Uri> photoUriGroup) {
        bitmapArrayList.clear();
        for(Uri uri : photoUriGroup){
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
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


