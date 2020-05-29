/*
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

public class SortByImageAdapter extends BaseAdapter {

    private List<SortByImage> item;
    public SortByImageAdapter(List<SortByImage> item){
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
                .inflate(R.layout.item_sort_by_image, parent, false);

        ImageView img1 = convertView.findViewById(R.id.image1_sort);
        ImageView img2 = convertView.findViewById(R.id.image2_sort);
        ImageView img3 = convertView.findViewById(R.id.image3_sort);
        ImageView img4 = convertView.findViewById(R.id.image4_sort);
        ImageView img5 = convertView.findViewById(R.id.image5_sort);
        ImageView img6 = convertView.findViewById(R.id.image6_sort);
        ImageView img7 = convertView.findViewById(R.id.image7_sort);
        ImageView img8 = convertView.findViewById(R.id.image8_sort);
        ImageView img9 = convertView.findViewById(R.id.image9_sort);
        ImageView img10 = convertView.findViewById(R.id.image10_sort);
        ImageView img11 = convertView.findViewById(R.id.image11_sort);
        ImageView img12 = convertView.findViewById(R.id.image12_sort);


        SortByImage L = item.get(position);

        img1.setImageBitmap(L.getImage1());
        img2.setImageBitmap(L.getImage2());
        img3.setImageBitmap(L.getImage3());
        img4.setImageBitmap(L.getImage4());
        img5.setImageBitmap(L.getImage5());
        img6.setImageBitmap(L.getImage6());
        img7.setImageBitmap(L.getImage7());
        img8.setImageBitmap(L.getImage8());
        img9.setImageBitmap(L.getImage9());
        img8.setImageBitmap(L.getImage10());
        img9.setImageBitmap(L.getImage11());
        img10.setImageDrawable(L.getImage12());

        return convertView;
    }

}
*/