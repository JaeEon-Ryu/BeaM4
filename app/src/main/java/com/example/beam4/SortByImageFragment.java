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

public class SortByImageFragment extends Fragment {

    ArrayList<SortByImage> imageData = new ArrayList<>();
    ArrayList<Uri> photoGroup = new ArrayList<>();
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    public static ArrayList<String> imageList = new ArrayList<>();

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

        if (photoFileClass.photoFileArrayList != null) {
            for (int idx = 0; idx < photoFileClass.photoFileArrayList.size(); idx++) { // photoFileClass.photoFileArrayList.size()
                String idxString = Integer.toString(idx);
                photoGroup.add(photoFileClass.photoFileArrayList.get(idx));
                // 사진정보 받기
                try {
                    ExifInterface exif = new ExifInterface(photoFileClass.photoFileArrayList.get(idx).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        View view = inflater.inflate(R.layout.fragment_sort_by_image, container, false);
        return view;
    }
}

/*
        // 각 행마다 이미지들 넣기
        setBitmapArrayList(photoGroup);
        Bitmap[] imageDataImg = new Bitmap[5];
        Drawable plusOn;

        for (hourlyPhotography s : imageList) {
            for (int reset = 0; reset < 5; reset++) {
                timeDataImg[reset] = null;
            }
            plusOn = null;
            //Log.i(this.getClass().getName(),"TTest  :   " + s.getTimeIndex().split(","));
            String[] indexArray = s.getTimeIndex().split(",");
            for (int i = 0; i < indexArray.length; i++) {
                if (i == 5) {   // 이 부분에 사진 6개일 때 + 표시 하도록
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

        SortByImageAdapter adapter = new SortByImageAdapter(imageData);
        View view = inflater.inflate(R.layout.fragment_sort_by_image, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_image);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                intent.putExtra("index", imageData.get(position).getDate());
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
}

*/