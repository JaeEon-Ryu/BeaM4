package com.example.beam4;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class RotateBmpClass {
    public synchronized static int GetExifOrientation(String filepath){
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch(IOException e){
            e.printStackTrace();
        }

        if(exif != null){
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if(orientation != -1){
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degee){
        if(degee != 0 && bitmap != null){
            Matrix m = new Matrix();
            m.setRotate(degee, (float)bitmap.getWidth()/2, (float) bitmap.getHeight()/2);
            try{
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
                if(bitmap != rotatedBitmap){
                    bitmap.recycle();
                    bitmap = rotatedBitmap;
                }
            } catch(OutOfMemoryError e){
                e.printStackTrace();
                return bitmap;
            }
        }
        return bitmap;
    }
}
