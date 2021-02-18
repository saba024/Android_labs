package com.example.lab2.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.lab2.Constants.DatabaseConstants;
import com.example.lab2.Dao.TimerDao;
import com.example.lab2.Model.Timer;

@Database(version = DatabaseConstants.VERSION, entities = {Timer.class})
@TypeConverters(Converter.class)
public abstract  class TimerDatabase extends RoomDatabase {
    public abstract TimerDao timerDao();
}

