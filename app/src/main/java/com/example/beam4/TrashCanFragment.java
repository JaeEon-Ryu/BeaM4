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
    // 그리드뷰 항목에 셋팅한 데이터
    private int[] images = new int[] {
            R.drawable.sample1,
            R.drawable.sample2,
            R.drawable.sample3,
            R.drawable.sample4,
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 뷰의 주소값을 담을 참조 변수
        GridView gridViewImages = (GridView)getView().findViewById(R.id.gridViewImages);
        // 데이터를 가지고 있는 리스트 생성
        ArrayList<HashMap<String, Object>> data_list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            // 항목 하나를 구성하기 위해서 필요한 데이터를 해시 맵에 담는다.
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("", images[i]);

            data_list.add(map);
        }
        // 해시 맵 객체에 데이터를 저장할 때 사용한 이름
        String key = "image";
        // 데이터를 셋팅할 뷰의 아이디
        int id = R.id.samplePhoto;
        // 어댑터 생성
        // ArrayAdapter imageGridAdapter = new ArrayAdapter(this., data_list, R.layout.activity_trash_can_row, key, id);
        //gridViewImages.setAdapter(imageGridAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trash_can, container, false);
    }
}

