package com.example.lab2;

import android.content.Intent;

import com.example.lab2.Model.Timer;

public class TimeRun {
    public String name;
    public int sets;
    public int work;
    public int rest;
    public int mode;
    public String color;

    public static TimeRun fromTimer(Timer t){
        TimeRun r = new TimeRun();
        r.name = t.name;
        r.sets = t.sets;
        r.work = t.work;
        r.rest = t.rest;
        r.mode = t.mode;
        r.color = t.color;
        return r;
    }

    public static TimeRun fromIntent(Intent i){
        TimeRun r = new TimeRun();
        r.name = i.getStringExtra("name");
        r.sets = i.getIntExtra("sets", 8);
        r.work = i.getIntExtra("work", 30);
        r.rest = i.getIntExtra("rest", 20);
        r.mode = i.getIntExtra("mode", 0);
        r.color = i.getStringExtra("color");
        return r;
    }

    public Intent toIntent(Intent i){
        i.putExtra("name", name);
        i.putExtra("sets", sets);
        i.putExtra("work", work);
        i.putExtra("rest", rest);
        i.putExtra("mode", mode);
        i.putExtra("color", color);
        return i;
    }
}