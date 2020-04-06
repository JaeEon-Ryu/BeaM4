package com.example.beam4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectPhotoActivityRowAdapter extends RecyclerView.Adapter<SelectPhotoActivityRowAdapter.ViewHolder> {

    private ArrayList<Integer> photoIds;
    SelectPhotoActivityRowAdapter(ArrayList<Integer> photoIds){
        this.photoIds = photoIds;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.samplePhoto);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_select_photo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.photo.setImageResource(photoIds.get(position));
    }

    @Override
    public int getItemCount() {
        return  photoIds.size();
    }


}
