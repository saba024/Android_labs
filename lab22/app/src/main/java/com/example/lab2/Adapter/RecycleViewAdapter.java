package com.example.lab2.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.Interface.ItemClickListener;
import com.example.lab2.Model.Timer;
import com.example.lab2.R;
import com.example.lab2.TimerActivity;

import java.util.List;


class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView textView;
    public TextView setnumber;
    private ItemClickListener itemClickListener;

    public RecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        //textView = (TextView) R.id.timer_name ;
        textView = itemView.findViewById(R.id.timer_name);
        setnumber = itemView.findViewById(R.id.timer_item_sets);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(itemView, getAdapterPosition());
    }
}

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>
{

    private List<Timer> timerList;
    private Context context;

    public RecycleViewAdapter(List<Timer> timerList, Context context)
    {
        this.timerList = timerList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.timer_item, parent, false);
        return new RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        holder.textView.setText(timerList.get(position).getName());
        holder.setnumber.setText(timerList.get(position).getSets());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, TimerActivity.class);
                intent.putExtra("name", timerList.get(position).getName());
                intent.putExtra("set", timerList.get(position).getSets());
                intent.putExtra("work", timerList.get(position).getWork());
                intent.putExtra("rest", timerList.get(position).getRest());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timerList.size();
    }
}
