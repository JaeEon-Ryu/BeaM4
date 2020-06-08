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

    public static CheckBox image_check;

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

        image_check = (CheckBox)convertView.findViewById(R.id.circle_check);
        image_check.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_check.isChecked()){
                    //Log.i(this.getClass().getName(),"이미지   "+ trashFragment.trashData);
                    TrashCanFragment.checkedTrashList.set(position,true);
                    //Log.i(this.getClass().getName(),"이미지체크되었음  "+ position);
                }
                else{
                    TrashCanFragment.checkedTrashList.set(position,false);
                }
            }
        });



//        adapter = new SelectPhotoActivityRowAdapter(com.example.beam4.SelectPhotoActivity.this, bitmapArrayList);
//        adapter.setOnItemClickListener(new SelectPhotoActivityRowAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                whatPhotoPosition = position;
//                new SetBigImageTask().execute(whatPhotoPosition);
//                checkButton.setChecked(checkedPhotoList.get(position));
//            }
//        });
//        recyclerView.setAdapter(adapter);


        return convertView;
    }

}
