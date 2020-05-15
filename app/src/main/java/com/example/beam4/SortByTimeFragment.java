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
import android.media.Image;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class SortByTimeFragment extends Fragment {

    ArrayList<SortByTime> timeData = new ArrayList<>();
    ArrayList<Uri> photoGroup = new ArrayList<>();
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    String nullIndex="";
    ArrayList<hourlyPhotography> timeList = new ArrayList<>();

    private ListView listView;

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

        if (photoFileClass.photoFileArrayList != null) {
            for (int idx = 0; idx < photoFileClass.photoFileArrayList.size(); idx++) { // photoFileClass.photoFileArrayList.size()
                String idxString = Integer.toString(idx);
                photoGroup.add(photoFileClass.photoFileArrayList.get(idx));
                // 사진정보 받기
                try {
                    //Log.i(this.getClass().getName(),"uri가져오기 =   " + photoFileClass.photoFileArrayList.get(i));
                    ExifInterface exif = new ExifInterface(photoFileClass.photoFileArrayList.get(idx).getPath());
                    //Log.i(this.getClass().getName(),"exif정보 =   " + exif);
                    dateTimeCurrent = exif.getAttribute(ExifInterface.TAG_DATETIME);
                    if (dateTimeCurrent != null) {
                        dateTimeCurrent = dateTimeCurrent.substring(0, 10);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(this.getClass().getName(), "dateTime = 에러");
                }

                boolean addFlag = true;
                if (dateTimeCurrent == null) {
                    if (nullIndex.isEmpty()){
                        nullIndex += idxString;
                    }
                    else {
                        nullIndex += "," + idxString;
                    }
                }
                else {
                    for(hourlyPhotography s:timeList){
                        if(s.getTimeString().equals(dateTimeCurrent) == true){
                            String temp = s.getTimeIndex();
                            s.setTimeIndex(temp+","+idxString);
                            addFlag = false;
                            break;
                        }
                    }
                    if(addFlag){
                        timeList.add(new hourlyPhotography(dateTimeCurrent,idxString));

                    }
                }
            }
        }

        ///////////////////////////////////////////////////////////
        //정렬
        class dateCompare implements Comparator<hourlyPhotography>{
            @Override
            public int compare(hourlyPhotography o1, hourlyPhotography o2) {

                if(o1.getTimeString().compareTo(o2.getTimeString()) <= 1){
                    return 1;
                }
                else if(o1.getTimeString().compareTo(o2.getTimeString()) >= -1){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }
        Collections.sort(timeList, new dateCompare());
        //////////////////////////////////////////////////////////

        setBitmapArrayList(photoGroup);
        Bitmap[] timeDataImg = new Bitmap[5];
        Drawable plusOn;

        //Log.i(this.getClass().getName(),"TTest  :  hourly전 " );

        for(hourlyPhotography s: timeList) {
            for (int reset = 0; reset < 5; reset++) {
                timeDataImg[reset] = null;
            }
            plusOn = null;
            //Log.i(this.getClass().getName(),"TTest  :   " + s.getTimeIndex().split(","));
            String[] indexArray = s.getTimeIndex().split(",");
            for(int i=0; i<indexArray.length; i++) {
                if(i==5){   // 이 부분에 사진 6개일 때 + 표시 하도록
                    Drawable plusImage = getResources().getDrawable(R.drawable.ic_add_white_24dp);
                    plusOn = plusImage;
                    break;
                }
                int idxInt = Integer.parseInt(indexArray[i].toString());
                timeDataImg[i] = bitmapArrayList.get(idxInt);
            }
            timeData.add(new SortByTime(s.getTimeString(), timeDataImg[0], timeDataImg[1], timeDataImg[2],
                    timeDataImg[3], timeDataImg[4], plusOn));

        }

        ///////////////////////////////////////////////////////////

        // 시간정보 null 처리
        if (nullIndex.length() > 1) {
            String[] nullArray = nullIndex.split(",");
            for (int reset = 0; reset < 5; reset++) {
                timeDataImg[reset] = null;
            }
            plusOn = null;
            for (int i = 0; i < nullArray.length; i++) {
                if (i == 5) {   // 이 부분에 사진 6개일 때 + 표시 하도록
                    Drawable plusImage = getResources().getDrawable(R.drawable.ic_add_white_24dp);
                    plusOn = plusImage;
                    break;
                }
                int idxInt = Integer.parseInt(nullArray[i].toString());
                timeDataImg[i] = bitmapArrayList.get(idxInt);
            }
            timeData.add(new SortByTime("시간 정보가 없습니다.", timeDataImg[0], timeDataImg[1], timeDataImg[2],
                    timeDataImg[3], timeDataImg[4], plusOn));
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


    private void setBitmapArrayList(ArrayList<Uri> photoUriGroup) {
        bitmapArrayList.clear();
        for (Uri uri : photoUriGroup) {
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

    private void setBitmap(Bitmap imageTest, Uri uri) {
        bitmapArrayList.clear();
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 250, 250, false);
            int degree = RotateBmpClass.GetExifOrientation(uri.getPath());
            Bitmap rotatedBitmap = RotateBmpClass.GetRotatedBitmap(scaledBitmap, degree);
            bmp.recycle();
            bmp = rotatedBitmap;
            imageTest = bmp;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


class hourlyPhotography {

    private String timeString;
    private String timeIndex;

    public hourlyPhotography(String timeString, String timeIndex) {
        this.timeString = timeString;
        this.timeIndex = timeIndex;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public void setTimeIndex(String timeIndex) {
        this.timeIndex = timeIndex;
    }

    public String getTimeIndex() {
        return this.timeIndex;
    }

    public String getTimeString() {
        return this.timeString;
    }

}

