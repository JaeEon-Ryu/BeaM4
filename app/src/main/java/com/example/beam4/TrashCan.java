package com.example.beam4;

import android.graphics.Bitmap;

public class TrashCan {
    // private Image store_img;
    private Bitmap image;

    public TrashCan(Bitmap image) {
        this.image=image;
    }
    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
