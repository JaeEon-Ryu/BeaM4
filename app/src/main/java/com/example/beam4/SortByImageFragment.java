package com.example.beam4;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SortByImageFragment extends Fragment {
    private static final String TAG = "OcvTest1";
    ArrayList<Uri> photoGroup = new ArrayList<>();
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    ArrayList<SortByImage> sortByImages = new ArrayList<>();
    public static SortByImageAdapter sortByImageAdapter;

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
        // new sortbyImageOpenCV().execute(photoGroup);

        ArrayList<ArrayList<Uri>> resultArrayList = new ArrayList<ArrayList<Uri>>();
        ArrayList<Uri> uriArrayList = new ArrayList<>();

        uriArrayList.addAll(photoGroup);

        // openCV 유사도 체크
        ArrayList<Mat> matArrayList = new ArrayList<Mat>();
        ArrayList<String> pathArrayList = new ArrayList<>();
        for (int i = 0; i < uriArrayList.size(); i++){
            String path = uriArrayList.get(i).getPath();
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
        while(!matArrayList.isEmpty()){
            ArrayList<Uri> resultUriArrayList = new ArrayList<>();
            ArrayList<Uri> notGoodResultUriArrayList = new ArrayList<>();
            ArrayList<Mat> notSameMatArrayList = new ArrayList<>();
            // matArrayList 첫번째 mat을 firstMat으로 선정
            Mat firstMat = matArrayList.get(0);
            // 첫번재 mat의 uri를 resultUriArrayList에 넣음
            resultUriArrayList.add(uriArrayList.get(0));
            Log.d("check the process","checking openCV matching is working");
            for (int i = 1; i < matArrayList.size(); i++) {
                Log.d("check the process","checking openCV matching is working " + i);
                // matArrayList 1 ~ matArrayList.size 까지 비교하기
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

                hsvImg1.release();
                hsvImg2.release();

                histFirstMat.release();
                histComparedMat.release();

                histogram1.release();
                histogram2.release();

                matList1.clear();
                matList2.clear();


                boolean matchResult = false;
                // 피처 매칭 실행
                if(!histResult){

                    ORB detector1 = ORB.create();
                    ORB detector2 = ORB.create();

                    // keypoint 와 description 생성
                    MatOfKeyPoint mainKeyPoint = new MatOfKeyPoint();
                    MatOfKeyPoint subKeyPoint = new MatOfKeyPoint();
                    Mat mainDescriptor = new Mat();
                    Mat subDescriptor = new Mat();

                    detector1.detectAndCompute(firstMat, new Mat(), mainKeyPoint, mainDescriptor);
                    detector2.detectAndCompute(comparedMat, new Mat(), subKeyPoint, subDescriptor);
                    Log.d("check descriptor", "onCreateView: " + mainDescriptor + "  " + subDescriptor);
                    if (mainDescriptor.isContinuous() == true && subDescriptor.isContinuous() == true ) {
                        // Matcher
                        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

                        MatOfDMatch matches = new MatOfDMatch();
                        matcher.match(mainDescriptor, subDescriptor, matches);

                        List<DMatch> dMatchList = matches.toList();
                        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
                        for (int j = 0; j < dMatchList.size(); j++) {
                            if (dMatchList.get(j).distance <= 40) {
                                good_matches.add(dMatchList.get(j));
                            }
                        }
                        if (good_matches.size() > SimilaritySetter.getSimilarity()) {
                            matchResult = true;
                        }
                    }
                }

                // 결과로 리스트를 분류
                if(histResult || (!histResult && matchResult)) {
                    resultUriArrayList.add(uriArrayList.get(i));
                } else{
                    notGoodResultUriArrayList.add(uriArrayList.get(i));
                    notSameMatArrayList.add(comparedMat);
                }
                // comparedMat.release();
            }
            Log.d("check the process","checking openCV matching is done " );
            // matArrayList 초기화 및 notSameMatArrayList로 갱신

            matArrayList = notSameMatArrayList;
            Log.d("check the process","checking resultUriArrayList "  + resultUriArrayList);
            if(resultUriArrayList.size() >= 2){
                resultArrayList.add(resultUriArrayList);
            }
            Log.d("check the process","checking resultUriArrayList "  + resultArrayList);

            uriArrayList = notGoodResultUriArrayList;
            firstMat.release();
        }
        Log.d("check the process","checking resultArrayList "  + resultArrayList);

        photoFileClass.openCVFileArrayList = resultArrayList;
        ArrayList<ArrayList<Uri>> imgUriArrayList;
        imgUriArrayList = photoFileClass.openCVFileArrayList;
        Log.d(TAG, "check OpenCVFileArrayLsit: " + imgUriArrayList);
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
            sortByImage.setImage2(uriDataImg[1]);
            sortByImage.setImage3(uriDataImg[2]);
            sortByImage.setImage4(uriDataImg[3]);
            sortByImage.setImage5(uriDataImg[4]);
            sortByImage.setImage6(uriDataImg[5]);
            sortByImage.setImage7(uriDataImg[6]);
            sortByImage.setImage8(uriDataImg[7]);
            sortByImage.setImage9(uriDataImg[8]);
            sortByImage.setImage10(uriDataImg[9]);
            sortByImage.setImage11(uriDataImg[10]);
            sortByImage.setImage12(plusOn);
            sortByImages.add(sortByImage);
        }

        sortByImageAdapter = new SortByImageAdapter(getContext(), sortByImages);
        View view = inflater.inflate(R.layout.fragment_sort_by_image, container, false);
        ListView listView = view.findViewById(R.id.list_view_image);
        listView.setAdapter(sortByImageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                intent.putExtra("indexFromSortByImageFragment", position);
                startActivity(intent);
            }
        });

        return view;
    }

//    private Bitmap setBitmap(Uri uri) {
//        Bitmap bmp = null;
//        try {
//            bmp = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
//            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 250, 250, false);
//            int degree = RotateBmpClass.GetExifOrientation(uri.getPath());
//            Bitmap rotatedBitmap = RotateBmpClass.GetRotatedBitmap(scaledBitmap, degree);
//            bmp.recycle();
//            bmp = rotatedBitmap;
//            return bmp;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bmp;
//    }

//    private String getImagePathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
//
//        if(cursor == null){
//            return contentUri.getPath();
//        } else{
//            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String imgPath = cursor.getString(idx);
//            cursor.close();
//            return imgPath;
//        }
//    }
}
