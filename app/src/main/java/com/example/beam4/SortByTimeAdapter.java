package com.example.beam4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
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

        /*
        SortByTime L = item.get(position);
        for(int i=0; i<item.size(); i++){
            ImageView img1 = convertView.findViewById(R.id.image1_sort);
            img1.setImageResource(L.getImage1());
        }*/

        //TextView txt_date = convertView.findViewById(R.id.date_sort_by_time);
        ImageView img1 = convertView.findViewById(R.id.image1_sort);
        ImageView img2 = convertView.findViewById(R.id.image2_sort);
        ImageView img3 = convertView.findViewById(R.id.image3_sort);
        ImageView img4 = convertView.findViewById(R.id.image4_sort);
        ImageView img5 = convertView.findViewById(R.id.image5_sort);
        ImageView img6 = convertView.findViewById(R.id.image6_sort);

        SortByTime L = item.get(position);
        txt_date.setText(L.getDate());
        img1.setImageBitmap(L.getImage1());
        img2.setImageBitmap(L.getImage2());
        img3.setImageBitmap(L.getImage3());
        img4.setImageBitmap(L.getImage4());
        img5.setImageBitmap(L.getImage5());
        img6.setImageDrawable(L.getImage6());

        return convertView;
    }




}
