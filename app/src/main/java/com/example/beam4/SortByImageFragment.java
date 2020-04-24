package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SortByImageFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<SortByImage> timeData = new ArrayList<>();


        for(int i=0; i<5; i++) {
            timeData.add(new SortByImage("유사도별"+i, R.drawable.want_sleep, R.drawable.want_sleep
                    , R.drawable.want_sleep, R.drawable.want_sleep, R.drawable.want_sleep, R.drawable.want_sleep));
        }

        SortByImageAdapter adapter = new SortByImageAdapter(timeData);
        View view = inflater.inflate(R.layout.fragment_sort_by_image, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_image);
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

}


