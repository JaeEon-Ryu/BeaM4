package com.example.beam4;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URI;
import java.net.URL;

public class SortByTime {
    // private Image store_img;
    private String date;
    private Bitmap image1;
    private Bitmap image2;
    private Bitmap image3;
    private Bitmap image4;
    private Bitmap image5;
    private Bitmap image6;


    public SortByTime(String date, Bitmap image1, Bitmap image2,
                      Bitmap image3, Bitmap image4,
                      Bitmap image5, Bitmap image6) {
        // this.store_img = store_img;
        this.date=date;
        this.image1=image1;
        this.image2=image2;
        this.image3=image3;
        this.image4=image4;
        this.image5=image5;
        this.image6=image6;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Bitmap getImage1() { return image1; }

    public void setImage1(Bitmap image1) {
        this.image1 = image1;
    }

    public Bitmap getImage2() {
        return image2;
    }

    public void setImage2(Bitmap image2) {
        this.image2 = image2;
    }

    public Bitmap getImage3() {
        return image3;
    }

    public void setImage3(Bitmap image3) {
        this.image3 = image3;
    }

    public Bitmap getImage4() {
        return image4;
    }

    public void setImage4(Bitmap image4) {
        this.image4 = image4;
    }

    public Bitmap getImage5() {
        return image5;
    }

    public void setImage5(Bitmap image5) {
        this.image5 = image5;
    }

    public Bitmap getImage6() {
        return image6;
    }

    public void setImage6(Bitmap image6) {
        this.image6 = image6;
    }

}
