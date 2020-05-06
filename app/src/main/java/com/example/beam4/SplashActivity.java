package com.example.beam4;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

public class SplashActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSIONS_REQUEST_CODE =100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE//,Manifest.permission.ACCESS_MEDIA_LOCATION
     };
    @Override
    

    protected void onCreate(Bundle savedInstance) {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
       // int accessPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION);

        super.onCreate(savedInstance);

        if (readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
               // && accessPermission == PackageManager.PERMISSION_GRANTED) {
        ){
            String path
                    = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/";
            //String path = path(Environment.getExternalStorageDirectory(),+ "/Camera");
            //final String path = android.os.Environment.DIRECTORY_DCIM+"/Camera";
            //String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera";
            //String path = "Camera";
            //String path = "/sdcard/DCIM/Camera";
            //Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();

            try {
                new DownLoadPhotoTask().execute(path);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "사진 로드 에러", Toast.LENGTH_LONG).show();
                finish();
            }


        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                    //||ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])
                    ) {
                Toast.makeText(getApplicationContext(), "허가가 필요합니다", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults){
        if(requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length){
            boolean check_result = true;

            for(int result : grandResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    check_result = false;
                    break;
                }
            }

            if(check_result){

            } else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                    //|| ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])
                        ){
                    Toast.makeText(getApplicationContext(), "허가가 필요합니다", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "허가가 필요합니다", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private class DownLoadPhotoTask extends AsyncTask<String, Void, ArrayList<Uri>>{

        @Override
        protected ArrayList<Uri> doInBackground(String... path) {
            File directory = new File(path[0]);
            File[] files = directory.listFiles(

                    new FilenameFilter() {
                        public boolean accept(File dir, String filename) {
                            Boolean bOK = false;
                            if(filename.toLowerCase().endsWith(".png")) bOK = true;
                            if(filename.toLowerCase().endsWith(".9.png")) bOK = true;
                            if(filename.toLowerCase().endsWith(".gif")) bOK = true;
                            if(filename.toLowerCase().endsWith(".jpg")) bOK = true;
                            if(filename.toLowerCase().endsWith(".JPG")) bOK = true;
                            return bOK;
                        }
                    });


            ArrayList<Uri> photoFileArrayList = new ArrayList<>();

            if (files != null ) {
                for (File f : files) {
                    Uri uri = Uri.parse("file:///" + f.toString());
                    photoFileArrayList.add(uri);
                }
                Collections.sort(photoFileArrayList, Collections.reverseOrder());
            }
            return photoFileArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Uri> uris) {
            photoFileClass.photoFileArrayList = uris;
            // mainActivity 시작
            startActivity(new Intent(com.example.beam4.SplashActivity.this, MainActivity.class));
            // splash 시간 주는 자리
            finish();
        }
    }
}
