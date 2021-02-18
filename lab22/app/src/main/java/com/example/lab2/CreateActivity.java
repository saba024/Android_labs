package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lab2.Database.DatabaseHelper;
import com.example.lab2.Database.TimerDatabase;
import com.example.lab2.Model.Timer;

import java.util.List;

public class CreateActivity extends AppCompatActivity {

    private int setsCount = 8, workTime = 30, restTime = 20;
    private EditText sets, work, rest;
    Button save, start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(prefs.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        sets = findViewById(R.id.editset);
        work = findViewById(R.id.editwork);
        rest = findViewById(R.id.editrest);
        start = findViewById(R.id.btnstart);
        save = findViewById(R.id.save);

        start.setOnClickListener(view -> {
            TimeRun t = new TimeRun();
            t.sets = setsCount;
            t.work = workTime;
            t.rest = restTime;
            startActivity(new Intent(this, TimerActivity.class));
        });

        save.setOnClickListener(view -> {
            Handler handler = new Handler(getMainLooper());
            new Thread(() -> {
                TimerDatabase db = DatabaseHelper.createInstance();
                List<Timer> timers = db.timerDao().getAll();
                Timer t = new Timer();
                t.name = "Timer1" + " " + (timers.size() + 1);
                t.sets = setsCount;
                t.work = workTime;
                t.rest = restTime;
                db.timerDao().insertAll(t);
                db.close();
                handler.post(this::finish);
            }).start();
        });
    }

    public void onStop() {
        super.onStop();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

}