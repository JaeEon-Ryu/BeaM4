package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;



public class TrashCanFragment extends Fragment {

    static final String TAG = "TrashCanFragmentAdapter";

    private CheckBox checkBox;

    private CheckBox select_button;
    private Button delete_button;
    private Button restore_button;

    private ArrayList<Uri> photoGroup = new ArrayList<>();
    private ArrayList<Uri> unselectedPhotoGroup = new ArrayList<>();
    private Uri unselectedPhoto;
    private ArrayList<Uri> selectedPhotoGroup = new ArrayList<>();
    private Uri selectedPhoto;
    private ArrayList<Boolean> checkedPhotoList = new ArrayList<>();
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

    private int count;
    public static Bitmap[] thumbnails;
    public static boolean[] thumbnailsSelection;
    public static String[] arrPath;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Uri> imageIDs = new ArrayList<>(photoFileClass.trashFileArrayList);

        /*
        columns: String[] image 데이터와 주소값 담고 있음   = Hashmap data_list
        orderBy: String 주소값을 담고 있음
        image_column_index: int 주소값의 index를 담고 있음
        */
        this.count = imageIDs.size();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        this.thumbnailsSelection = new boolean[this.count];

        ArrayList<HashMap<String, Object>> data_list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        for (int i = 0; i < count; i++) {
            hashMap.put("", imageIDs.get(i));
            data_list.add(hashMap);
        }

        if (imageIDs != null) {
            for (int i = 0; i < count; i++) {
                photoGroup.add(photoFileClass.trashFileArrayList.get(i));
            }
        }

        for (int i = 0; i < count; i++) {
            checkedPhotoList.add(false);
        }

        TrashCanFragmentAdapter imageAdapter = new TrashCanFragmentAdapter(this.getActivity(), imageIDs);
        View view = inflater.inflate(R.layout.activity_trash_can, container, false);
        // getActivity().setContentView(R.layout.activity_trash_can);
        GridView gridView = (GridView)view.findViewById(R.id.gridViewImages);
        gridView.setAdapter(imageAdapter);

        select_button = (CheckBox)view.findViewById(R.id.select_btn);
        delete_button = (Button)view.findViewById(R.id.delete_btn);
        restore_button = (Button)view.findViewById(R.id.restore_btn);
        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final int len = count;
                int cnt = 0;
                String selectImages = "";
                for (int i = 0; i < count; i++)
                {
                    if (thumbnailsSelection[i]){
                        cnt++;
                        selectImages = selectImages + arrPath[i] + "|";
                    }
                }
            }
        });
        return view;
    }
}


