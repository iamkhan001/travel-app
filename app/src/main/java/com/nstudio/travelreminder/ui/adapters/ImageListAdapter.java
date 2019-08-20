package com.nstudio.travelreminder.ui.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nstudio.travelreminder.R;
import com.nstudio.travelreminder.database.model.Image;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private List<Image> list;

    public ImageListAdapter( List<Image> list) {
        this.list = list;



    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

     holder.imgLuggage.setImageBitmap(list.get(position).getBitmap());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgLuggage;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLuggage = itemView.findViewById(R.id.imgLuggage);
        }
    }

}