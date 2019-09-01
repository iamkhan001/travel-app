package com.nstudio.travelreminder.ui.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nstudio.travelreminder.R;
import com.nstudio.travelreminder.data.TravelData;
import com.nstudio.travelreminder.database.entitiy.Travel;
import com.nstudio.travelreminder.utils.ColorPicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.MyViewHolder> {

    private List<TravelData> list;
    private SimpleDateFormat formatBase;
    private SimpleDateFormat formatTime;
    private SimpleDateFormat formatMonth;
    private OnTravelClickListener clickListener;

    public JourneyListAdapter(List<TravelData> list,OnTravelClickListener clickListener) {
        this.list = list;
        this.clickListener = clickListener;
        formatBase =  new SimpleDateFormat("dd MMM yy hh:mm a", Locale.ENGLISH);
        formatTime =  new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        formatMonth =  new SimpleDateFormat("MMM yy", Locale.ENGLISH);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TravelData travel = list.get(position);

        holder.tvFrom.setText(travel.getFrom());
        holder.tvTo.setText(travel.getTo());

        String dateFrom = travel.getBoardingTime();

        try {
            Date date = formatBase.parse(dateFrom);

            if (date==null){
                return;
            }

            holder.tvMonth.setText(formatMonth.format(date));
            holder.tvTime.setText(formatTime.format(date));


            String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
            String day          = (String) DateFormat.format("dd",   date);

            holder.tvDay.setText(dayOfTheWeek);
            holder.tvDate.setText(day);

            holder.bgDate.setColor(Color.parseColor(ColorPicker.getColor(day)));



        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvDay,tvMonth,tvFrom,tvTo,tvTime,tvDate,tvBags;
        private GradientDrawable bgDate;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDay = itemView.findViewById(R.id.tvDay);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvTime = itemView.findViewById(R.id.tvTime);
            bgDate = (GradientDrawable) tvDate.getBackground();
            tvBags = itemView.findViewById(R.id.tvLuggageCount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onTravelClick(list.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnTravelClickListener{
        void onTravelClick(TravelData travel);
    }

}
