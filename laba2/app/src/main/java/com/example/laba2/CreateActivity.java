package com.example.laba2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.laba2.Database.DatabaseHelper;
import com.example.laba2.Database.TimerDatabase;
import com.example.laba2.Model.Timer;
import com.example.laba2.Service.TimeService;

import java.util.List;

public class CreateActivity extends AppCompatActivity {

    private Button[] plusMinusBtns;
    private Button save, start;
    private SwitchCompat hr;
    private TextView sets, work, rest;
    private final View.OnClickListener plusMinusBtnListener = view -> plusMinusUpdates(view.getId(), false);
    private int setsCount = 8, workTime = 30, restTime = 20;

    private boolean plusMinusUpdates(int id, boolean longClick){
        int[] data = TimeService.getValues(new int[]{setsCount, workTime, restTime, id}, longClick, this);
        setsCount = data[0];
        workTime = data[1];
        restTime = data[2];
        setTexts();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
        setClickListeners();
        setTexts();
    }

   private void init(){
        plusMinusBtns = new Button[]{
                findViewById(R.id.minus1),
                findViewById(R.id.minus2),
                findViewById(R.id.minus3),
                findViewById(R.id.plus1),
                findViewById(R.id.plus2),
                findViewById(R.id.plus3)
        };
        start = findViewById(R.id.create_new_start);
        save = findViewById(R.id.create_new_save);
        sets = findViewById(R.id.sets_count);
        work = findViewById(R.id.work_count);
        rest = findViewById(R.id.rest_count);
    }

    private void setClickListeners(){
        for(Button btn : plusMinusBtns){
            btn.setOnClickListener(plusMinusBtnListener);
        }
        start.setOnClickListener(view -> {
            SavedTimerRun t = new SavedTimerRun();
            t.sets = setsCount;
            t.work = workTime;
            t.rest = restTime;
            TimeService.start(this, t);
        });
        save.setOnClickListener(view -> {
            Handler handler = new Handler(getMainLooper());
            new Thread(() -> {
                TimerDatabase db = DatabaseHelper.createInstance();
                List<Timer> timers = db.timerDao().getAll();
                Timer t = new Timer();
                t.name = getString(R.string.custom) + " " + (timers.size() + 1);
                t.sets = setsCount;
                t.work = workTime;
                t.rest = restTime;
                db.timerDao().insertAll(t);
                db.close();
                handler.post(this::finish);
            }).start();
        });
    }

    private void setTexts(){
        sets.setText(String.valueOf(setsCount));
        work.setText(TimeService.formatTime(workTime));
        rest.setText(TimeService.formatTime(restTime));
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