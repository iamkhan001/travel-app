package com.nstudio.travelreminder.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nstudio.travelreminder.R;
import com.nstudio.travelreminder.database.model.Image;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private List<Image> list;
    private OnImageClickListener onImageClickListener;

    public ImageListAdapter( List<Image> list,OnImageClickListener onImageClickListener) {
        this.list = list;
        this.onImageClickListener = onImageClickListener;
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

        private ImageView imgLuggage,imgRemove;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLuggage = itemView.findViewById(R.id.imgLuggage);
            imgRemove = itemView.findViewById(R.id.imgRemove);

            imgLuggage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickListener.onClick(getAdapterPosition());
                }
            });

            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickListener.onRemove(getAdapterPosition());
                }
            });
        }
    }

    public interface OnImageClickListener{
        void onRemove(int index);
        void onClick(int index);
    }

    @SuppressLint("StaticFieldLeak")
    private static class AsyncLoadImage extends AsyncTask<Void,Void, Bitmap> {

        private String name;
        private ImageView imgPhoto;
        private String root;

        public AsyncLoadImage(Context context, String name, ImageView imgPhoto) {
            this.name = name;
            this.imgPhoto = imgPhoto;
            root = Objects.requireNonNull(context.getExternalFilesDir("")).toString();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            String p = root+"/MyTravels/"+name;
            File file = new File(p);
            if (file.exists()){
                return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(p), 200, 200);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap!=null){
                imgPhoto.setImageBitmap(bitmap);
            }else{
                imgPhoto.setImageResource(R.drawable.ic_image);
            }
        }
    }

}
