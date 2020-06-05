package com.example.beam4;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
    private Button deletePermanently;
    private Button restore_button;
    private ArrayList<Uri> photoGroup = new ArrayList<>();
    private ArrayList<Uri> unselectedPhotoGroup = new ArrayList<>();
    private Uri unselectedPhoto;
    private ArrayList<Uri> selectedPhotoGroup = new ArrayList<>();
    private Uri selectedPhoto;
    public static ArrayList<Boolean> checkedTrashList = new ArrayList<>();
    private TrashCanFragmentAdapter adapter;
    private Button selectButton;
    public static ArrayList<TrashCan> trashData = new ArrayList<>();
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

        trashData.clear();

        if (trashUri.size() > 0) {
            for (int i = 0; i < trashUri.size(); i++) {
                Bitmap img = setBitmap(photoFileClass.trashFileArrayList.get(i));
                checkedTrashList.add(false);
                trashData.add(new TrashCan(img));
            }
        }

        adapter = new TrashCanFragmentAdapter(trashData);

        View view = inflater.inflate(R.layout.fragment_trash_can, container, false);
        gridView = view.findViewById(R.id.gridViewImages);
        gridView.setAdapter(adapter);

//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if(isChecked){
//                checkedPhotoList.set(whatPhotoPosition, true);
//            }else{
//                checkedPhotoList.set(whatPhotoPosition, false);
//            }
//        }

        // 영구삭제
        Button deletePermanently = (Button)view.findViewById(R.id.delete_permanently_btn);
        deletePermanently.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DeletePermanentlyTask().execute();
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


        CheckBox select_check = (CheckBox)view.findViewById(R.id.select_check);
        Button restore_button = (Button)view.findViewById(R.id.restore_btn);

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

    private class DeletePermanentlyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int count = photoFileClass.trashFileArrayList.size();

            for (int i = 0; i < count; i++) {
                //Log.i(this.getClass().getName(),"쳌디버깅중  "+ checkedTrashList);

                if(checkedTrashList.get(i) == false){
                    unselectedPhoto = photoFileClass.trashFileArrayList.get(i);
                    unselectedPhotoGroup.add(unselectedPhoto);
                } else{
                    selectedPhoto = photoFileClass.trashFileArrayList.get(i);
                    selectedPhotoGroup.add(selectedPhoto);
                }
            }

            photoFileClass.trashFileArrayList.clear();
            trashData.clear();
            //Log.i(this.getClass().getName(),"쳌디버깅중  "+ trashData);
            checkedTrashList.clear();

            if (unselectedPhotoGroup.size() > 0) {
                for (int i = 0; i < unselectedPhotoGroup.size(); i++) {
                    Bitmap img = setBitmap(unselectedPhotoGroup.get(i));
                    photoFileClass.trashFileArrayList.add(unselectedPhotoGroup.get(i));
                    trashData.add(new TrashCan(img));
                    checkedTrashList.add(false);
                }
            }

            //selected에 대해서 영구히 삭제하는 function 필요


//            if(unselectedPhotoGroup != null){
//                photoFileClass.trashFileArrayList.addAll(unselectedPhotoGroup);
//            }
//            count = selectedPhotoGroup.size();
//            for (int i = 0; i < count; i++) {
//                checkedPhotoList.add(false);
//            }


            selectedPhotoGroup.clear();
            unselectedPhotoGroup.clear();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

    }

}

