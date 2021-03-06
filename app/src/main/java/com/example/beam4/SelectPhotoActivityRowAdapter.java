package com.example.beam4;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectPhotoActivityRowAdapter extends RecyclerView.Adapter<SelectPhotoActivityRowAdapter.ViewHolder> {
    Context context = null;
    private ArrayList<Bitmap> photoIds;


    SelectPhotoActivityRowAdapter(Context context, ArrayList<Bitmap> photoIds){
        this.context = context;
        this.photoIds = photoIds;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.samplePhoto);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(onItemClickListener != null){
                            onItemClickListener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
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
        Bitmap bmp = photoIds.get(position);


        //---------------------------------------------------------------

        // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
        // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.
        viewHolder.photo.setAdjustViewBounds(true);
        viewHolder.photo.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return  photoIds.size();
    }

}
