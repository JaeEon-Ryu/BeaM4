package com.example.beam4;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.List;

public class TrashCanFragmentAdapter extends BaseAdapter {

    private List<TrashCan> item;
    public TrashCanFragmentAdapter(List<TrashCan> item){
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
                .inflate(R.layout.item_trash_can, parent, false);

        ImageView img = convertView.findViewById(R.id.trashPhoto);

        TrashCan L = item.get(position);
        img.setImageBitmap(L.getImage());

        CheckBox image_check = (CheckBox)convertView.findViewById(R.id.circle_check);
        image_check.setVisibility(View.VISIBLE);
        return convertView;
    }

}
