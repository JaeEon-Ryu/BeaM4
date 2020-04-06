package com.example.beam4;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectPhotoActivityRowDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int right;
    private int top;
    private int bottom;

    public SelectPhotoActivityRowDecoration(int left, int right, int top, int bottom){
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = this.left;
        outRect.right = this.right;
        outRect.top = this.top;
        outRect.bottom = this.bottom;
    }
}
