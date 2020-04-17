package com.example.beam4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class TrashCanFragmentAdapter extends BaseAdapter {

    Context context = null;

    // 이미지 파일들의 리소스 ID를 담는 배열
    ArrayList<Uri> imageIDs = null;

    public TrashCanFragmentAdapter(Context context, ArrayList<Uri> imageIDs) {
        this.context = context;
        this.imageIDs = imageIDs;
    }

    @Override
    public int getCount() {
        return (null != imageIDs)? imageIDs.size(): 0;
    }

    @Override
    public Object getItem(int i) {
        return (null != imageIDs)? imageIDs.get(i): 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ImageView imageView = null;

        if (null != convertView)
            imageView = (ImageView)convertView;
        else {
            // GridView 뷰를 구성할 ImageView 뷰의 비트맵을 정의합니다.
            // 그리고 그것의 크기를 320*240으로 줄입니다.
            // 크기를 줄이는 이유는 메모리 부족 문제를 막을 수 있기 때문입니다.
            Uri uri = imageIDs.get(position);
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Matrix matrix = new Matrix();
            matrix.preRotate(90,0,0);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
            bmp = Bitmap.createScaledBitmap(bmp, 320, 320, false);

            //---------------------------------------------------------------

            // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
            // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bmp);

            // ImageClickListener
        }
        return imageView;
    }
}
