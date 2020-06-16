package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SortByImageFragment extends Fragment {
    private static final String TAG = "OcvTest1";
    ArrayList<Uri> photoGroup = new ArrayList<>();
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    ArrayList<SortByImage> sortByImages = new ArrayList<>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");

        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV is not loaded!");
        } else {
            Log.d(TAG, "OpenCV is loaded!");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // uri 정보 받아오기

        if (photoFileClass.photoFileArrayList != null) {
            for (int idx = 0; idx < photoFileClass.photoFileArrayList.size(); idx++) {
                photoGroup.add(photoFileClass.photoFileArrayList.get(idx));
            }
        }

        // 각 행마다 이미지들 넣기
        //setBitmapArrayList(photoGroup);

        // 유사도별 사진 나누기
        new sortbyImageOpenCV().execute(photoGroup);

        ArrayList<ArrayList<Uri>> imgUriArrayList = new ArrayList<>();
        imgUriArrayList.addAll(photoFileClass.openCVFileArrayList);
        Uri[] uriDataImg = new Uri[11];
        Drawable plusOn;

        for(ArrayList<Uri> imgUris : imgUriArrayList){
            for (int reset = 0; reset < 11; reset++) {
                uriDataImg[reset] = null;
            }
            plusOn = null;
            for (int i = 0; i < imgUris.size(); i++) {
                if(i==11){   // 이 부분에 사진 12개일 때 + 표시 하도록
                    Drawable plusImage = getResources().getDrawable(R.drawable.ic_add_white_24dp);
                    plusOn = plusImage;
                    break;
                }
                uriDataImg[i] = imgUris.get(i);
            }
            SortByImage sortByImage = new SortByImage();
            sortByImage.setImage1(uriDataImg[0]);
            sortByImage.setImage1(uriDataImg[1]);
            sortByImage.setImage1(uriDataImg[2]);
            sortByImage.setImage1(uriDataImg[3]);
            sortByImage.setImage1(uriDataImg[4]);
            sortByImage.setImage1(uriDataImg[5]);
            sortByImage.setImage1(uriDataImg[6]);
            sortByImage.setImage1(uriDataImg[7]);
            sortByImage.setImage1(uriDataImg[8]);
            sortByImage.setImage1(uriDataImg[9]);
            sortByImage.setImage1(uriDataImg[10]);
            sortByImage.setImage1(uriDataImg[11]);
            sortByImage.setImage12(plusOn);
            sortByImages.add(sortByImage);
        }

        SortByImageAdapter sortByImageAdapter = new SortByImageAdapter(sortByImages);
        View view = inflater.inflate(R.layout.fragment_sort_by_image, container, false);
        ListView listView = view.findViewById(R.id.list_view_image);
        listView.setAdapter(sortByImageAdapter);
        return view;
    }

    private void setBitmapArrayList(ArrayList<Uri> photoUriGroup) {
        bitmapArrayList.clear();
        for (Uri uri : photoUriGroup) {
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(this.getActivity()).getContentResolver(), uri);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 1000, 1000, false);
                int degree = RotateBmpClass.GetExifOrientation(uri.getPath());
                Bitmap rotatedBitmap = RotateBmpClass.GetRotatedBitmap(scaledBitmap, degree);
                bmp.recycle();
                bmp = rotatedBitmap;
                bitmapArrayList.add(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class sortbyImageOpenCV extends AsyncTask<ArrayList<Uri>, Void, ArrayList<ArrayList<Uri>>>{

        @Override
        protected ArrayList<ArrayList<Uri>> doInBackground(ArrayList<Uri>... arrayLists) {
            ArrayList<ArrayList<Uri>> resultArrayList = new ArrayList();

            ArrayList<Uri> uriArrayList = new ArrayList<>();
            ArrayList<Uri> resultUriArrayList = new ArrayList<>();
            ArrayList<Uri> notGoodResultUriArrayList = new ArrayList<>();
            ArrayList<Mat> notSameMatArrayList = new ArrayList<>();

            uriArrayList.addAll(arrayLists[0]);

            // openCV 유사도 체크
            ArrayList<Mat> matArrayList = new ArrayList<Mat>();
            ArrayList<String> pathArrayList = new ArrayList<>();
            for (int i = 0; i < uriArrayList.size(); i++){
                String path = getImagePathFromURI(uriArrayList.get(i));
                pathArrayList.add(path);
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            for (int i = 0; i< pathArrayList.size(); i++){
                bitmapArrayList.add(BitmapFactory.decodeFile(pathArrayList.get(i), options));
            }
            for(int i = 0; i < bitmapArrayList.size(); i++){
                Mat mat = new Mat();
                Utils.bitmapToMat(bitmapArrayList.get(i), mat);
                matArrayList.add(mat);
            }

            // while(리스트가 비었는가?)
            while(!matArrayList.isEmpty()){;
                // resultBitmapArrayList 첫번째 값은 matArrayList 첫번째 값을
                // bitmap 으로 바꾸어 넣음
                Mat firstMat = matArrayList.get(0);
                resultUriArrayList.add(uriArrayList.get(0));
                Log.d("check the process","checking openCV matching is working");
                for (int i = 1; i < matArrayList.size(); i++) {
                    Log.d("check the process","checking openCV matching is working " + i);
                    Mat comparedMat = matArrayList.get(i);
                    // 히스토그램 매칭 실행
                    Mat hsvImg1 = new Mat();
                    Mat hsvImg2 = new Mat();

                    Mat histFirstMat = firstMat.clone();
                    Mat histComparedMat = comparedMat.clone();

                    Imgproc.cvtColor(histFirstMat, hsvImg1, Imgcodecs.IMREAD_ANYCOLOR);
                    Imgproc.cvtColor(histComparedMat, hsvImg2, Imgcodecs.IMREAD_ANYCOLOR);

                    List<Mat> matList1 = new ArrayList<Mat>();
                    List<Mat> matList2 = new ArrayList<Mat>();

                    matList1.add(hsvImg1);
                    matList2.add(hsvImg2);

                    MatOfFloat range = new MatOfFloat(0,255);
                    MatOfInt histSize = new MatOfInt(50);
                    MatOfInt channels = new MatOfInt((0));

                    Mat histogram1 = new Mat();
                    Mat histogram2 = new Mat();

                    Imgproc.calcHist(matList1, channels, new Mat(), histogram1, histSize, range);
                    Imgproc.calcHist(matList2, channels, new Mat(), histogram2, histSize, range);

                    Core.normalize(histogram1, histogram1, 0, 1, Core.NORM_MINMAX, -1, new Mat());
                    Core.normalize(histogram2, histogram2, 0, 1, Core.NORM_MINMAX, -1, new Mat());

                    /*
                     * correlation: the higher the metric, the more accurate the match ">0.9"
                     * chi_square: the lower the metric, the more accurate the match "<0.1"
                     * intersection: the higher the metric, the more accurate the match ">1.5"
                     * bhattacharyya: the lower the metric, the more accurate the match "<0.3"
                     * */
                    double correlation, chi_square, intersection, bhattacharyya;
                    correlation = Imgproc.compareHist(histogram1, histogram2, 0);
                    chi_square = Imgproc.compareHist(histogram1, histogram2, 1);
                    intersection = Imgproc.compareHist(histogram1, histogram2, 2);
                    bhattacharyya = Imgproc.compareHist(histogram1, histogram2, 3);

                    int count = 0;
                    boolean histResult = false;

                    if(correlation > 0.9) count++;
                    if(chi_square < 0.1) count++;
                    if(intersection > 1.5) count++;
                    if(bhattacharyya < 0.3) count++;

                    // 히스토그램 매칭 결과 확인
                    if(count >= 3){
                        histResult = true;
                    }


                    boolean matchResult = false;
                    // 피처 매칭 실행
                    if(false){

                        ORB detector = ORB.create();

                        // keypoint 와 description 생성
                        MatOfKeyPoint mainKeyPoint = new MatOfKeyPoint();
                        MatOfKeyPoint subKeyPoint = new MatOfKeyPoint();
                        Mat mainDescriptor = new Mat();
                        Mat subDescriptor = new Mat();

                        detector.detectAndCompute(firstMat, new Mat(), mainKeyPoint, mainDescriptor);
                        detector.detectAndCompute(comparedMat, new Mat(), subKeyPoint, subDescriptor);

                        // Matcher
                        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);;

                        MatOfDMatch matches = new MatOfDMatch();
                        matcher.match(mainDescriptor, subDescriptor, matches);

                        List<DMatch> dMatchList = matches.toList();
                        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
                        for (int j = 0; j < dMatchList.size(); j++) {
                            if(dMatchList.get(j).distance <= 40){
                                good_matches.add(dMatchList.get(j));
                            }
                        }
                        if(good_matches.size() > 20){
                            matchResult = true;
                        }
                    }

                    // 결과로 리스트를 분류
                    Mat mat = matArrayList.get(i);
                    if(histResult || (!histResult && matchResult)) {
                        resultUriArrayList.add(uriArrayList.get(i));
                    } else{
                        notGoodResultUriArrayList.add(uriArrayList.get(i));
                        notSameMatArrayList.add(mat);
                    }
                    mat.release();
                }
                Log.d("check the process","checking openCV matching is done " );
                // matArrayList 초기화 및 notSameMatArrayList로 갱신
                matArrayList.clear();
                matArrayList.addAll(notSameMatArrayList);
                resultArrayList.add(resultUriArrayList);
                resultUriArrayList.clear();
                notSameMatArrayList.clear();
                uriArrayList.clear();
                uriArrayList.addAll(notGoodResultUriArrayList);
            }
            return resultArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<Uri>> arrayLists) {
            photoFileClass.openCVFileArrayList.addAll(arrayLists);
        }
    }

    private String getImagePathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);

        if(cursor == null){
            return contentUri.getPath();
        } else{
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imgPath = cursor.getString(idx);
            cursor.close();
            return imgPath;
        }
    }
}
