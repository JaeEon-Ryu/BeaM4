package com.example.beam4;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class SortByTimeAdapter extends BaseAdapter {

    private List<SortByTime> item;
    public SortByTimeAdapter(List<SortByTime> item){
        this.item = item;

    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) { return item.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sort_by_time, parent, false);

        TextView txt_date = convertView.findViewById(R.id.date_sort_by_time);
        ImageView img1 = convertView.findViewById(R.id.image1_sort);
        ImageView img2 = convertView.findViewById(R.id.image2_sort);
        ImageView img3 = convertView.findViewById(R.id.image3_sort);
        ImageView img4 = convertView.findViewById(R.id.image4_sort);
        ImageView img5 = convertView.findViewById(R.id.image5_sort);
        ImageView img6 = convertView.findViewById(R.id.image6_sort);

        SortByTime L = item.get(position);
        txt_date.setText(L.getDate());
        img1.setImageResource(L.getImage1());
        img2.setImageResource(L.getImage2());
        img3.setImageResource(L.getImage3());
        img4.setImageResource(L.getImage4());
        img5.setImageResource(L.getImage5());
        img6.setImageResource(L.getImage6());


        return convertView;
    }
}
