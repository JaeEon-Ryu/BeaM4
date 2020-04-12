package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.GridView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;



public class TrashCanFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private GridView gridViewImages;
    private TrashCanFragmentAdapter imageGridAdapter;

    // 그리드뷰 항목에 셋팅한 데이터
    private int[] imageIDs = new int[] {
            R.drawable.sample1,
            R.drawable.sample2,
            R.drawable.sample3,
            R.drawable.sample4,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 상진수 추가 입력, SelectPhotoActivity에서 선택된 포토 리스트 전송
        ArrayList<Integer> unselectedPhotoGroup = getArguments().getIntegerArrayList("unselectedPhotoGroup");


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_trash_can, null);

        // 데이터를 가지고 있는 리스트 생성
        ArrayList<HashMap<String, Object>> data_list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < imageIDs.length; i++) {
            // 항목 하나를 구성하기 위해서 필요한 데이터를 해시 맵에 담는다.
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("", imageIDs[i]);
            data_list.add(map);
        }

        /*
        // 해시 맵 객체에 데이터를 저장할 때 사용한 이름
        String key = "image";

        //데이터를 셋팅할 뷰의 아이디
        int id = R.id.samplePhoto;
        */

        gridViewImages = (GridView)view.findViewById(R.id.gridViewImages);
        imageGridAdapter = new TrashCanFragmentAdapter(this.getActivity(), imageIDs);
        gridViewImages.setAdapter(imageGridAdapter);

        imageGridAdapter.notifyDataSetChanged();

        return view;

        // return inflater.inflate(R.layout.fragment_trash_can, container, false);
    }
}

