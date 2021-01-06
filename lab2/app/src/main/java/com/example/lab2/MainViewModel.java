package com.example.lab2;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Database.DatabaseHelper;
import Model.Timer;

public class MainViewModel extends AndroidViewModel {

    List<Integer> worktimelist = new ArrayList<Integer>();
    DatabaseViewModel db = new DatabaseViewModel(getApplication());

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void addTimer(String name, String prepareTime, String duration,
                         String restTime, String cycleNumber,
                         String setNumber, String pause, String color)
    {
        Timer timer= new Timer(name, prepareTime, duration, restTime, cycleNumber, setNumber, pause, color);
        db.addTimer(timer);
    }

    public void updateTimer(Timer timer, String name, String preparationTime, String duration,
                            String relaxationTime, String cycleCount,
                            String setCount, String pauseTime, String color)
    {
        timer.setName(name);
        timer.setPrepareTime(preparationTime);
        timer.setDuration(duration);
        timer.setRestTime(relaxationTime);
        timer.setCycleNumber(cycleCount);
        timer.setSetNumber(setCount);
        timer.setPause(pauseTime);
        timer.setColor(color);
        db.updateTimer(timer);
    }

    public List<Timer> getWorktimelist()
    {
        return db.getAllTimer();
    }

    public void deleteTimers()
    {
        db.deleteTimers();
    }

    public void deleteTimer(Timer timer)  {
        db.deleteTimer(timer);
        worktimelist.remove(timer);
    }

    public int[] getList()
    {
        int count = worktimelist.size();
        int[] timers = new int[count];
        for(int i = 0; i < count; i++)
        {
            timers[i] = worktimelist.get(i);
        }
        return timers;
    }

    public void initializationTimerIntegerList(Timer timer)
    {
        int countCycle = Integer.parseInt(timer.getCycleNumber());
        int countSet = Integer.parseInt(timer.getSetNumber());
        int count = 1;
        for(int i = 0; i < countSet; i++)
        {
            worktimelist.add(Integer.parseInt(timer.getPrepareTime()) * 1000);
            count++;
            for(int j = 0; j < countCycle; j++)
            {
                worktimelist.add(Integer.parseInt(timer.getDuration()) * 1000);
                count++;
                worktimelist.add(Integer.parseInt(timer.getRestTime()) * 1000);
                count++;
            }
            worktimelist.add(Integer.parseInt(timer.getPause()) * 1000);
            count++;
        }
    }

    public List<String> getTimerStringList(Timer timer)
    {
        List<String> stringTimerList = new ArrayList<String>();
        List<String> phase = new ArrayList<String>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        switch (preferences.getString("language", "eu-US"))
        {
            case "ru":
                phase.add(". Подготовка : ");
                phase.add(". Работа : ");
                phase.add(". Отдых : ");
                phase.add(". Пауза : ");
                break;
            case "en-US":
                phase.add(". Preparation : ");
                phase.add(". WorkingProcess : ");
                phase.add(". RestTime : ");
                phase.add(". Pause : ");
                break;
        }
        int countCycle = Integer.parseInt(timer.getCycleNumber());
        int length = Integer.parseInt(timer.getSetNumber());
        int position = 1;
        for(int i = 0; i < length; i++)
        {
            stringTimerList.add(position + phase.get(0) + timer.getPrepareTime());
            position++;
            for(int k = 0; k < countCycle; k++)
            {
                stringTimerList.add(position + phase.get(2) + timer.getDuration());
                position++;
                stringTimerList.add(position + phase.get(3)  + timer.getRestTime());
                position++;
            }
            stringTimerList.add(position + phase.get(4) + timer.getPause());
            position++;
        }
        return stringTimerList;
    }

    public Configuration getConfiguration()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        float koefficient = (float)preferences.getInt("size", 1);
        Locale local = new Locale(preferences.getString("language", "eu-US"));
        Locale.setDefault(local);
        Configuration config = new Configuration();
        config.locale = local;
        config.fontScale = koefficient / 10;
        return config;
    }
}