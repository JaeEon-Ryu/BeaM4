package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Environment;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private GridView gridViewImages;
    private TrashCanFragmentAdapter imageGridAdapter;
    private CheckBox checkButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Uri> imageIDs = new ArrayList<>(photoFileClass.photoFileArrayList);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_trash_can, null);

        // 데이터를 가지고 있는 리스트 생성
        ArrayList<HashMap<String, Object>> data_list = new ArrayList<HashMap<String, Object>>();
        int count = imageIDs.size();
        for (int i = 0; i < count; i++) {
            // 항목 하나를 구성하기 위해서 필요한 데이터를 해시 맵에 담는다.
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("", imageIDs.get(i));
            data_list.add(hashMap);
        }

        /*
        // 해시 맵 객체에 데이터를 저장할 때 사용한 이름
        String key = "image";

        //데이터를 셋팅할 뷰의 아이디
        int id = R.id.samplePhoto;
        */

        //select, cancel btn


        gridViewImages = (GridView)view.findViewById(R.id.gridViewImages);
        imageGridAdapter = new TrashCanFragmentAdapter(this.getActivity(), imageIDs);
        gridViewImages.setAdapter(imageGridAdapter);

        //imageGridAdapter.notifyDataSetChanged();

        return view;

        // return inflater.inflate(R.layout.fragment_trash_can, container, false);
    }


}

