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

import com.devilcat.leato.models.JavaModel;
import com.devilcat.leato.views.JavaDetail;
import com.devilcat.leato.R;

import java.util.ArrayList;
import java.util.List;

public class JavaAdapter extends RecyclerView.Adapter<JavaAdapter.MyViewHolder> {

    private final Context context;
    private List<JavaModel> dataList;

    public JavaAdapter(Context context, List<JavaModel> dataList) {

        this.context = context;
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_java, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(dataList.get(position).getTitle());
        holder.details.setText(dataList.get(position).getDetails());
        holder.period.setText(dataList.get(position).getPeriod());

        holder.recCard.setOnClickListener(view -> {

            Intent intent = new Intent(context, JavaDetail.class);
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

    public void searchData(ArrayList<JavaModel> searchList){

        dataList = searchList;
        notifyDataSetChanged();

    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,details,period;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            recCard = itemView.findViewById(R.id.recCard);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            period = itemView.findViewById(R.id.period);

        }

    }

}