package com.example.beam4;

import android.graphics.Bitmap;

public class TrashCan {

    private Bitmap image;
    private Boolean isChecked;

    public TrashCan(Bitmap image, Boolean isChecked) {
        this.image = image;
        this.isChecked=isChecked; }

    public Bitmap getImage() { return image; }
    public Boolean getIsChecked() { return isChecked; }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    public void setIsChecked(Boolean isChecked) {this.isChecked =isChecked; }
}
