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

import com.devilcat.leato.models.HomeDataModel;
import com.devilcat.leato.R;
import com.devilcat.leato.views.Detail;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private final Context context;
    private List<HomeDataModel> dataList;

    public HomeAdapter(Context context, List<HomeDataModel> dataList) {

        this.context = context;
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home, parent, false);
        return new HomeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        holder.title.setText(dataList.get(position).getTitle());
        holder.details.setText(dataList.get(position).getDetails());
        holder.period.setText(dataList.get(position).getPeriod());
        holder.lang.setText(dataList.get(position).getLang());

        holder.recCard.setOnClickListener(view -> {

            Intent intent = new Intent(context, Detail.class);
            intent.putExtra("title", dataList.get(holder.getAdapterPosition()).getTitle());
            intent.putExtra("details", dataList.get(holder.getAdapterPosition()).getDetails());
            intent.putExtra("period", dataList.get(holder.getAdapterPosition()).getPeriod());
            intent.putExtra("lang", dataList.get(holder.getAdapterPosition()).getLang());

            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {

        return dataList.size();

    }

    public void searchData(ArrayList<HomeDataModel> searchList){

        dataList = searchList;
        notifyDataSetChanged();

    }

    static class HomeViewHolder extends RecyclerView.ViewHolder{

        TextView title,details,period,lang;
        CardView recCard;

        public HomeViewHolder(@NonNull View itemView) {

            super(itemView);
            recCard = itemView.findViewById(R.id.recCard);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            period = itemView.findViewById(R.id.period);
            lang = itemView.findViewById(R.id.language);

        }

    }

}