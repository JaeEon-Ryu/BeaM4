package com.example.beam4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;


public class TrashCanFragmentAdapter extends BaseAdapter {

    static final String TAG = "TrashCanFragmentAdapter";

    private LayoutInflater mInflater;
    private ArrayList<Bitmap> photoIDs;
    private ViewHolder viewHolder;
    Context context = null;
    ArrayList<Uri> imageIDs = new ArrayList<>();


    public TrashCanFragmentAdapter(Context context, ArrayList<Uri> imageIDs) {
        Log.d(TAG, "TrashCanFragmentAdapter");
        mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        // this.photoIDs = photoIDs; // bitmap
        this.imageIDs = imageIDs; // uri
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkbox;
        int id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // sean
            Log.d(TAG, "RecyclerView.ViewHolder");
            // imageView = itemView.findViewById(R.id.thumbImage);
            imageView = itemView.findViewById(R.id.Image);
            checkbox = itemView.findViewById(R.id.CheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
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


    @Override
    public int getCount() {
        return (null != imageIDs)? imageIDs.size(): 0;
    }

    @Override
    public Object getItem(int position) {
        return (null != imageIDs)? imageIDs.get(position): 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        // ViewHolder holder;
        Log.d(TAG,"getView");

        if (null == convertView) {
            /*
            Uri uri = imageIDs.get(position);
            Bitmap bmp = null;
            */

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_trash_can_row, parent, false);

            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.Image);
            viewHolder.checkbox = (CheckBox)convertView.findViewById(R.id.CheckBox);

            convertView.setTag(viewHolder);

            /*
            try {
                bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Matrix matrix = new Matrix();
            matrix.preRotate(90,0,0);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
            bmp = Bitmap.createScaledBitmap(bmp, 250, 250, false);

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bmp);
            */

            // ImageClickListener
        }

        else {
            imageView = (ImageView)convertView;
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.checkbox.setId(position);
            viewHolder.imageView.setId(position);
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    int id = checkBox.getId();
                    if (TrashCanFragment.thumbnailsSelection[id]){
                        checkBox.setChecked(false);
                        TrashCanFragment.thumbnailsSelection[id] = false;
                    }
                    else {
                        checkBox.setChecked(true);
                        TrashCanFragment.thumbnailsSelection[id] = true;
                    }
                }
            });
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    // sean
                    Intent intent = new Intent(v.getContext(), TrashCanFragment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                    intent.setDataAndType(Uri.parse("file://" + TrashCanFragment.arrPath[id]), "image/*");
                }
            });
            viewHolder.imageView.setImageBitmap(TrashCanFragment.thumbnails[position]);
            viewHolder.checkbox.setChecked(TrashCanFragment.thumbnailsSelection[position]);
            viewHolder.id = position;
            return convertView;
        }
        return imageView;
    }
}


