package com.example.laba2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.laba2.Model.Timer;
import com.example.laba2.R;

import java.util.List;

public class TimerAdapter extends ArrayAdapter<Timer>
{
    public TimerAdapter(Context context, List<Timer> timers){
        super(context, 0, timers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Timer t = getItem(position);
        return getConvertedView(convertView, parent, t);
    }

    @SuppressLint("SetTextI18n")
    private View getConvertedView(View v, ViewGroup parent, Timer t){
        if(v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.timer_item, parent, false);
            TextView name = v.findViewById(R.id.timer_item_name);
            TextView sets = v.findViewById(R.id.timer_item_sets);
            name.setText(t.name);
            sets.setText(t.sets + " " + getContext().getString(R.string.sets).toLowerCase());
        }
        return v;
    }
}