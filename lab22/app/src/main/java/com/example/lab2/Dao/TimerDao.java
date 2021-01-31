package com.example.lab2.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lab2.Model.Timer;

import java.util.List;

@Dao
public interface TimerDao {
    @Query("SELECT * FROM timer")
    List<Timer> getAll();

    @Insert
    void insertAll(Timer... workouts);

    @Delete
    void delete(Timer workout);

    @Delete
    void deleteAll();
}
