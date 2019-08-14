package com.nstudio.travelreminder.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nstudio.travelreminder.R;
import com.nstudio.travelreminder.database.entitiy.Luggage;

import java.io.File;
import java.util.List;

public class LuggageListAdapter extends RecyclerView.Adapter<LuggageListAdapter.MyViewHolder> {

    private List<Luggage> list;
    private Context context;

    public LuggageListAdapter(Context context,List<Luggage> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final String path = list.get(position).getImage();
        File file = new File(path);

        Glide.with(context)
                .load(Uri.fromFile(file))
                .apply(new RequestOptions().override(100, 100))
                .into(holder.imgLuggage);


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
