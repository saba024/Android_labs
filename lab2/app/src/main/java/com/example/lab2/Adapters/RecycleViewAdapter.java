package com.example.lab2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.ItemClickListener;
import com.example.lab2.R;

import java.util.List;

import Model.Timer;

class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public ImageView image;
    public TextView text;
    public TextView position;

    private ItemClickListener itemClickListener;

    public RecycleViewHolder(@NonNull View itemView) {

        super(itemView);
        position = (TextView) itemView.findViewById(R.id.txtId);
        text = (TextView) itemView.findViewById(R.id.txtName);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v, getAdapterPosition());
    }
}

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>
{
    private List<Timer> timerList;
    private Context context;

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView =  inflater.inflate(R.layout.timer_item_activity, parent, false);

        return new RecycleViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        holder.text.setText(timerList.get(position).getName());
        holder.text.setText(timerList.get(position).getName());

        holder.setItemClickListener(new ItemClickListener()
        {
            @Override
            public void OnClick(View view, int position) {
                //Intent intent = new intent
            }
        });
    }

    @Override
    public int getItemCount() {
        return timerList.size();
    }

}