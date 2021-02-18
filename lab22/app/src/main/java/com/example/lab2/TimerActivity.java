package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.lab2.Service.TimerService;

public class TimerActivity extends AppCompatActivity {

    String name;
    int set;
    int work;
    int rest;
    String TAG = "Main";
    TextView textView, status, sets;
    private boolean isWorking;
    public static boolean isRunning;
    TimerService timerService;

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
        setContentView(R.layout.activity_timer);
        textView = findViewById(R.id.intervaltime);
        status = findViewById(R.id.status);
        sets = findViewById(R.id.sets);
        if (getIntent() != null)
        {
            name = getIntent().getStringExtra("name");
            set = getIntent().getIntExtra("set", 8);
            work = getIntent().getIntExtra("work", 30);
            rest = getIntent().getIntExtra("rest", 15);
        }

        Intent intent = new Intent(this, TimerService.class);
        updateStatus(true);
        startService(intent);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            update(intent);
        }
    };

    @Override
    protected void onResume() {
        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.COUNTDOWN_BR));
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, TimerService.class));
        super.onDestroy();
    }

    private void update(Intent intent)
    {
        if(intent.getExtras() != null)
        {
            long millisUntilFinished = intent.getLongExtra("countdown", work);
            textView.setText(Long.toString(millisUntilFinished / 1000));

        }
    }

    private void updateStatus(boolean working){
        isWorking = working;
        isRunning = true;
        timerService.setSeconds(working?work:set);
        sets.setText(String.valueOf(set));
        status.setBackground(ContextCompat.getDrawable(this, working ? R.color.work : R.color.rest));
        status.setText(getResources().getString(working ? R.string.work : R.string.rest));
    }
}