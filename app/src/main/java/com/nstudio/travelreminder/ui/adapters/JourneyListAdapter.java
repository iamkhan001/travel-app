package com.nstudio.travelreminder.ui.adapters;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nstudio.travelreminder.R;
import com.nstudio.travelreminder.database.entitiy.Travel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.MyViewHolder> {

    private List<Travel> list;
    private SimpleDateFormat formatBase;
    private SimpleDateFormat formatTime;
    private SimpleDateFormat formatMonth;

    public JourneyListAdapter(List<Travel> list) {
        this.list = list;
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

        Travel travel = list.get(position);

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

            String time = day+"\n"+dayOfTheWeek;
            holder.tvDay.setText(time);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvDay,tvMonth,tvFrom,tvTo,tvTime;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDay = itemView.findViewById(R.id.tvDay);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvTime = itemView.findViewById(R.id.tvTime);

        }
    }

}
