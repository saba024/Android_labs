package com.example.lab2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import Database.DatabaseHelper;
import Model.Timer;

public class ViewModel extends AndroidViewModel {

    DatabaseViewModel db = new DatabaseViewModel(getApplication());

    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public void addTimer(String timerName, String preparationTime,
                         String workTime,
                         String relaxationTime, String cycleCount,
                         String setCount, String pauseTime, String color)
    {
        Timer timer= new Timer(timerName, preparationTime, workTime, relaxationTime, cycleCount, setCount, pauseTime, color);
        db.addTimer(timer);
    }

    public void updateTimer(Timer timer, String timerName, String preparationTime,
                            String workTime,
                            String relaxationTime, String cycleCount,
                            String setCount, String pauseTime, String color)
    {
        timer.setName(timerName);
        timer.setPrepareTime(preparationTime);
        timer.setDuration(workTime);
        timer.setRestTime(relaxationTime);
        timer.setCycleNumber(cycleCount);
        timer.setSetNumber(setCount);
        timer.setPause(pauseTime);
        timer.setColor(color);
        db.updateTimer(timer);
    }
}
