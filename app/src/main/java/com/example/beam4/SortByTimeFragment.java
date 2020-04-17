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

public class SortByTimeFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayList<SortByTime> timeData = new ArrayList<>();


        for(int i=0; i<5; i++) {
            timeData.add(new SortByTime("2020-04-1"+i, R.drawable.want_sleep, R.drawable.want_sleep
                    , R.drawable.want_sleep, R.drawable.want_sleep, R.drawable.want_sleep, R.drawable.ic_add_white_24dp));
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
}


