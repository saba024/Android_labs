package com.example.laba2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.laba2.Constants.Constants;
import com.example.laba2.Handlers.TimerHandler;
import com.example.laba2.Service.TimeService;
import com.pixplicity.easyprefs.library.Prefs;

public class TimerActivity extends AppCompatActivity {

    private Chronometer elapsedtime;
    private Button cancel, finishset;
    private TextView time, status, intervaltime, sets;


    private TimerHandler timeHandler;

    private int currSet;
    private boolean isWorking;
    private boolean hasResumed;
    private boolean hasFinished;

    public static boolean isRunning;


    private void init(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(preferences.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(preferences.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        setContentView(R.layout.activity_timer);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        sets = findViewById(R.id.sets);
        intervaltime = findViewById(R.id.intervaltime);
        elapsedtime = findViewById(R.id.totaltime);
        cancel = findViewById(R.id.cancel);
        finishset = findViewById(R.id.finishset);

        cancel.setOnLongClickListener(view -> {
            endActivity();
            return true;
        });

        finishset.setOnClickListener(view -> {
            updateStatus(!isWorking);
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startTimer();
    }

    private void startTimer(){
        isRunning = true;
        timeHandler = new TimerHandler(time);
        elapsedtime.setBase(SystemClock.elapsedRealtime());
        elapsedtime.start();
        currSet = TimeService.isModeManualSets() ? 0 : Prefs.getInt(Constants.KEY_SETS, Constants.DEF_SETS) + 1;
        updateStatus(true);
    }

    private void endActivity(){
        if(hasFinished) return;
        hasFinished = true;
        isRunning = false;
        stopHandlers(true);
        finish();
    }

    private void updateStatus(boolean working){
        stopHandlers(false);
        isWorking = working;
        isRunning = true;
        checkIfFinished(working);
        newSet(working);
        sets.setText(String.valueOf(currSet));
        status.setBackground(ContextCompat.getDrawable(this, working ? R.color.work : R.color.rest));
        status.setText(getResources().getString(working ? R.string.work : R.string.rest));
    }

    private void checkIfFinished(boolean working){
        if(working && (TimeService.isModeManualSets() ? ++currSet : --currSet) == 0)
            endActivity();
    }

    private void newSet(boolean working){
        timeHandler = new TimerHandler(intervaltime, working ? Prefs.getInt(Constants.KEY_WORK, Constants.DEF_WORKTIME) : Prefs.getInt(Constants.KEY_REST, Constants.DEF_RESTTIME), () -> updateStatus(!working), this);
    }

    private void stopHandlers(boolean isEnd){
        if(timeHandler != null) timeHandler.stop();
        if(isEnd && timeHandler != null) timeHandler.stop();
    }

    public void onDestroy() {
        super.onDestroy();
        endActivity();
    }

    public void onPause() {
        this.hasResumed = false;
        new Handler().postDelayed(() -> {
            if(!hasResumed) endActivity();
        }, 9 * 1000);
        super.onPause();
    }

    public void onResume() {
        hasResumed = true;
        super.onResume();
    }

    public void onStop(){
        super.onStop();
        new Handler().postDelayed(() -> {
            if(!hasResumed) endActivity();
        }, 9 * 1000);
    }
    public void onStart(){
        super.onStart();
        hasResumed = true;
    }
}