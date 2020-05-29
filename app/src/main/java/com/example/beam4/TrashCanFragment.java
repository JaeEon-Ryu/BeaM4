package com.example.beam4;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;


public class TrashCanFragment extends Fragment {
    private GridView gridViewImages;
    private CheckBox image_check;
    private CheckBox select_button;
    private Button delete_button;
    private Button restore_button;
    private ArrayList<Uri> photoGroup = new ArrayList<>();
    private ArrayList<Uri> unselectedPhotoGroup = new ArrayList<>();
    private Uri unselectedPhoto;
    private ArrayList<Uri> selectedPhotoGroup = new ArrayList<>();
    private Uri selectedPhoto;
    private ArrayList<Boolean> checkedPhotoList = new ArrayList<>();
    private TrashCanFragmentAdapter imageGridAdapter;
    private Button selectButton;
    ArrayList<TrashCan> trashData = new ArrayList<>();
    private GridView gridView;
    private boolean mClick = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Uri> trashUri = new ArrayList<>(photoFileClass.trashFileArrayList);

        if (trashUri.size() > 0) {
            for (int i = 0; i < trashUri.size(); i++) {

                Bitmap img = setBitmap(photoFileClass.trashFileArrayList.get(i));
                checkedPhotoList.add(false);
                trashData.add(new TrashCan(img));
            }
        }

        TrashCanFragmentAdapter adapter = new TrashCanFragmentAdapter(trashData);
        View view = inflater.inflate(R.layout.fragment_trash_can, container, false);
        gridView = view.findViewById(R.id.gridViewImages);
        gridView.setAdapter(adapter);

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
////                intent.putExtra("index", trashData.get(position).getDate());
////               // Log.i(this.getClass().getName(), "exex      =    " + trashData.get(0));
////                startActivity(intent);
//            }
//        });

        CheckBox select_check = (CheckBox)view.findViewById(R.id.select_check);
        Button delete_button = (Button)view.findViewById(R.id.delete_btn);
        Button restore_button = (Button)view.findViewById(R.id.restore_btn);

//        select_check.setOnClickListener(new CheckBox.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent intent = new Intent(getActivity(), TrashCanFragmentAdapter.class);
//                //intent.putExtra("checkInfo",select_check.isChecked());
//                if(select_check.isChecked()){
//                    image_check.setVisibility(View.GONE);
//                    //intent.putExtra("checkInfo",select_check.isChecked());
//                }
//                else{
//                    image_check.setVisibility(View.VISIBLE);
//                    //intent.putExtra("checkInfo",select_check.isChecked());
//                }
//            }
//        });

        return view;
    }



    private Bitmap setBitmap(Uri uri) {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 350, 350, false);
            int degree = RotateBmpClass.GetExifOrientation(uri.getPath());
            Bitmap rotatedBitmap = RotateBmpClass.GetRotatedBitmap(scaledBitmap, degree);
            bmp.recycle();
            bmp = rotatedBitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmp;
    }


}

