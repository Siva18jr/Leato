package com.devilcat.leato.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.devilcat.leato.models.PythonModel;
import com.devilcat.leato.views.PythonDetail;
import com.devilcat.leato.R;

import java.util.ArrayList;
import java.util.List;

public class PythonAdapter extends RecyclerView.Adapter<PythonAdapter.MyViewHolders> {

    private final Context context;
    private List<PythonModel> dataList;

    public PythonAdapter(Context context, List<PythonModel> dataList) {

        this.context = context;
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_python, parent, false);
        return new MyViewHolders(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolders holder, int position) {

        holder.title.setText(dataList.get(position).getTitle());
        holder.details.setText(dataList.get(position).getDetails());
        holder.period.setText(dataList.get(position).getPeriod());

        holder.recCard.setOnClickListener(view -> {

            Intent intent = new Intent(context, PythonDetail.class);
            intent.putExtra("title", dataList.get(holder.getAdapterPosition()).getTitle());
            intent.putExtra("details", dataList.get(holder.getAdapterPosition()).getDetails());
            intent.putExtra("period", dataList.get(holder.getAdapterPosition()).getPeriod());
            intent.putExtra("key", dataList.get(holder.getAdapterPosition()).getKey());

            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {

        return dataList.size();

    }

    public void searchData(ArrayList<PythonModel> searchList){

        dataList = searchList;
        notifyDataSetChanged();

    }

    static class MyViewHolders extends RecyclerView.ViewHolder{

        TextView title,details,period;
        CardView recCard;

        public MyViewHolders(@NonNull View itemView) {

            super(itemView);
            recCard = itemView.findViewById(R.id.recCard);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            period = itemView.findViewById(R.id.period);

        }

    }

}