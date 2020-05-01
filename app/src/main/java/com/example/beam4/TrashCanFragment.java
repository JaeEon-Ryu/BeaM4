package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Environment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;



public class TrashCanFragment extends Fragment {
    private GridView gridViewImages;
    private CheckBox checkBox;
    private CheckBox select_button;
    private Button delete_button;
    private Button restore_button;
    private ArrayList<Uri> photoGroup = new ArrayList<>();
    private ArrayList<Uri> unselectedPhotoGroup = new ArrayList<>();
    private Uri unselectedPhoto;
    private ArrayList<Uri> selectedPhotoGroup = new ArrayList<>();
    private Uri selectedPhoto;
    private ArrayList<Boolean> checkedPhotoList = new ArrayList<>();
    private TrashCanFragmentAdapter imageGridAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Uri> imageIDs = new ArrayList<>(photoFileClass.trashFileArrayList);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_trash_can, null);

        // 데이터를 가지고 있는 리스트 생성
        ArrayList<HashMap<String, Object>> data_list = new ArrayList<HashMap<String, Object>>();
        int count = imageIDs.size();
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("", imageIDs.get(i));
            data_list.add(hashMap);
        }

        if (imageIDs != null) {
            for (int i = 0; i < count; i++) {
                photoGroup.add(photoFileClass.trashFileArrayList.get(i));
            }
        }

        for (int i = 0; i < count; i++) {
            checkedPhotoList.add(false);
        }


        gridViewImages = (GridView)view.findViewById(R.id.gridViewImages);
        select_button = (CheckBox)view.findViewById(R.id.select_btn);
        //delete_button = (Button)view.findViewById(R.id.delete_btn);
        restore_button = (Button)view.findViewById(R.id.restore_btn);
        // select_button.setOnCheckedChangeListener();



        imageGridAdapter = new TrashCanFragmentAdapter(this.getActivity(), imageIDs);
        gridViewImages.setAdapter(imageGridAdapter);


        return view;
    }


}

