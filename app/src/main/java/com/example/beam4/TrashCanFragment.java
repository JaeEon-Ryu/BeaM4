package com.example.beam4;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
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
                trashData.add(new TrashCan(img,false));
            }
        }

        adapter = new TrashCanFragmentAdapter(trashData);

        View view = inflater.inflate(R.layout.fragment_trash_can, container, false);
        gridView = view.findViewById(R.id.gridViewImages);
        gridView.setAdapter(adapter);

        /*
        //선택,취소
        CheckBox select_check = (CheckBox)view.findViewById(R.id.select_check);
        select_check.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_check.isChecked()){
                    TrashCanFragmentAdapter.image_check.setVisibility(View.VISIBLE);
                    Log.i(this.getClass().getName(),"디버깅중  "+ TrashCanFragmentAdapter.image_check);
                }
                else{
                    TrashCanFragmentAdapter.image_check.setVisibility(View.INVISIBLE);
                }
            }
        });*/

        // 영구삭제
        Button deletePermanently = (Button)view.findViewById(R.id.delete_permanently_btn);
        deletePermanently.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DeletePermanentlyTask().execute();
            }
        });

        // 복원
        Button restore = (Button)view.findViewById(R.id.restore_btn);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { new RestoreTask().execute(); }
        });

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

            if (selectedPhotoGroup.size() > 0) {
                for (int i = 0; i < selectedPhotoGroup.size(); i++) {
                    String path = selectedPhotoGroup.get(i).getPath();
                    Log.i(this.getClass().getName(),"디버깅중  "+ selectedPhotoGroup.get(i).getPath());
                    Log.i(this.getClass().getName(),"디버깅중  "+ path);

                    File file = new File(path);
                    if(file.exists()) {
                        boolean isDelete = file.delete();
                        if(isDelete) {
                            Log.e("file delete ?", String.valueOf(isDelete));
                            MediaScanner mediaScanner = new MediaScanner(getContext(), file);
//                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//
//                            intent.setData(Uri.fromFile(file);
//
//                            sendBroadcast(intent);
//                            Intent intent = new Intent();
//                            intent.setAction();
//                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//                            IntentSender(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path))
//                            BroadcastReceiver reciver = new BroadcastReceiver() {
//                                @Override
//                                public void onReceive(Context context, Intent intent) {
//                                    String action = intent.getAction();
//                                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//                                }
//                            }
//                            reciver.
//                            Uri uri = null;
//                            if (type == PublicVariable.MEDIA_TYPE_IMAGE) {
//                                uri = Images.Media.EXTERNAL_CONTENT_URI;
//                            } else {
//                                uri = Video.Media.EXTERNAL_CONTENT_URI;
//                            }
//                            String selection = Images.Media.DATA + " = ?";
//                            String[] selectionArgs = {path}; // 실제 파일의 경로
//                            int count = resolver.delete(uri, selection, selectionArgs);
//
                         }
                    }
                }
            }

            if (unselectedPhotoGroup.size() > 0) {
                for (int i = 0; i < unselectedPhotoGroup.size(); i++) {
                    Bitmap img = setBitmap(unselectedPhotoGroup.get(i));
                    photoFileClass.trashFileArrayList.add(unselectedPhotoGroup.get(i));
                    trashData.add(new TrashCan(img,false));
                    checkedTrashList.add(false);
                }
            }
            selectedPhotoGroup.clear();
            unselectedPhotoGroup.clear();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

    }

    private class RestoreTask extends AsyncTask<Void, Void, Void> {

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

            if (selectedPhotoGroup.size() > 0) {
                for (int i = 0; i < selectedPhotoGroup.size(); i++) {
                    photoFileClass.photoFileArrayList.add(selectedPhotoGroup.get(i));
                    //Log.i(this.getClass().getName(),"디버깅중  "+ selectedPhotoGroup.get(i));
                }
            }

            if (unselectedPhotoGroup.size() > 0) {
                for (int i = 0; i < unselectedPhotoGroup.size(); i++) {
                    Bitmap img = setBitmap(unselectedPhotoGroup.get(i));
                    photoFileClass.trashFileArrayList.add(unselectedPhotoGroup.get(i));
                    trashData.add(new TrashCan(img,false));
                    checkedTrashList.add(false);
                }
            }

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

