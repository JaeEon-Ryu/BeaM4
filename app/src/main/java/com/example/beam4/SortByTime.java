package com.example.beam4;

import android.graphics.drawable.Icon;

public class SortByTime {
    // private Image store_img;
    private String date;
    private int image1;
    private int image2;
    private int image3;
    private int image4;
    private int image5;
    private int image6;


    public SortByTime(String date, int image1, int image2,
                      int image3, int image4,
                      int image5, int image6) {
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


    public int getImage1() {
        return image1;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }


    public int getImage2() {
        return image2;
    }

    public void setImage2(int image2) {
        this.image2 = image2;
    }

    public int getImage3() {
        return image3;
    }

    public void setImage3(int image3) {
        this.image3 = image3;
    }

    public int getImage4() {
        return image4;
    }

    public void setImage4(int image4) {
        this.image4 = image4;
    }

    public int getImage5() {
        return image5;
    }

    public void setImage5(int image5) {
        this.image5 = image5;
    }

    public int getImage6() {
        return image6;
    }

    public void setImage6(int image6) {
        this.image6 = image6;
    }

}
