package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SortByTimeFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayList<SortByTime> data = new ArrayList<>();

        data.add(new SortByTime("2020-04-11",R.drawable.sample1,R.drawable.sample2
                ,R.drawable.sample3,R.drawable.sample4,R.drawable.sample1,R.drawable.ic_add_white_24dp));

        data.add(new SortByTime("2020-04-12",R.drawable.sample1,R.drawable.sample1
                ,R.drawable.sample3,R.drawable.sample4,R.drawable.sample3,R.drawable.sample3));

        data.add(new SortByTime("2020-04-13",R.drawable.sample4,R.drawable.sample4
                ,R.drawable.sample4,R.drawable.sample4,R.drawable.sample4,R.drawable.sample4));

        data.add(new SortByTime("2020-04-14",R.drawable.sample1,R.drawable.sample2
                ,R.drawable.sample3,R.drawable.sample4,R.drawable.sample1,R.drawable.sample2));

        data.add(new SortByTime("2020-04-15",R.drawable.sample1,R.drawable.sample1
                ,R.drawable.sample1,R.drawable.sample1,R.drawable.sample1,R.drawable.ic_add_white_24dp));


        SortByTimeAdapter adapter = new SortByTimeAdapter(data);
        View view = inflater.inflate(R.layout.fragment_sort_by_time, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_time);
        listView.setAdapter(adapter);

        return view;

    }

    private ListView listView;
}


