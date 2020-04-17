package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        timeData.add(new SortByTime("2020-04-11",R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp
                ,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_add_white_24dp));

        timeData.add(new SortByTime("2020-04-12",R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp
                ,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp));

        timeData.add(new SortByTime("2020-04-13",R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp
                ,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp));

        timeData.add(new SortByTime("2020-04-14",R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp
                ,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp));

        timeData.add(new SortByTime("2020-04-15",R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp
                ,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_mood_black_24dp,R.drawable.ic_add_white_24dp));


        SortByTimeAdapter adapter = new SortByTimeAdapter(timeData);
        View view = inflater.inflate(R.layout.fragment_sort_by_time, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_time);
        listView.setAdapter(adapter);

        return view;

    }

    private ListView listView;
}


