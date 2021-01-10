package com.example.lab2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TimerTask;
import java.util.Timer;

import Database.DatabaseHelper;


public class TimerActivity extends AppCompatActivity {

    TextView timerText;
    Button stopStartButton;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    boolean timerStarted = false;
    public final static String PARAM_START_TIME = "start_time";
    public final static String NAME_ACTION = "name";
    public final static String TIME_ACTION = "time";
    public final static String CURRENT_ACTION = "pause";
    public final static String BROADCAST_ACTION = "com.example.lab2";
    ArrayList<String> steps = new ArrayList();
    private TextView position;
    DatabaseHelper databaseHelper;
    BroadcastReceiver receiver;
    int el = 0;
    String value_status_pause = "";
    String value_time_pause = "";
    int value_element_pause;
    ListView timerlist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        stopStartButton = findViewById(R.id.animateToStart);
        timerlist = findViewById(R.id.list_timer);
        position = findViewById(R.id.position);
        timerText = findViewById(R.id.timerText);

        stopStartButton = (Button) findViewById(R.id.startStopButton);

        timer = new Timer();

        receiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onReceive(Context context, Intent intent) {
                if (Objects.equals(intent.getStringExtra(CURRENT_ACTION), "work")) {
                    String task = intent.getStringExtra(NAME_ACTION);
                    String status = intent.getStringExtra(TIME_ACTION);
                    assert status != null;
                    if (status.equals("1")) {
                        workLastSec();
                    } else {
                        workInProgress(task);
                    }
                    position.setText(task);
                    timerText.setText(status);
                } else if (Objects.equals(intent.getStringExtra(CURRENT_ACTION), "clear")) {
                    clear();
                } else {
                    value_status_pause = intent.getStringExtra(NAME_ACTION);
                    value_time_pause = intent.getStringExtra(TIME_ACTION);
                    assert value_time_pause != null;
                    startPause(value_time_pause);
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(receiver, intentFilter);

    }

    public void clear() {
        if (el != 0 && el - 1 - timerlist.getFirstVisiblePosition() < 14 && el - 1 - timerlist.getFirstVisiblePosition() >= 0 && el != 0)
            timerlist.getChildAt(el - timerlist.getFirstVisiblePosition() - 1).setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
        if (el - timerlist.getFirstVisiblePosition() < 14 && el - 1 - timerlist.getFirstVisiblePosition() >= 0)
            timerlist.getChildAt(el - timerlist.getFirstVisiblePosition()).setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
    }

    public void workInProgress(String task) {
        if (check_last_sec) {
            check_last_sec = false;
        }
        value_status_pause = "";
        if (task.equals(getResources().getString(R.string.Finish))) {
            stopStartButton.setOnClickListener(TimerActivity.this::startStopTapped);
            stopStartButton.setBackground(getResources().getDrawable(R.drawable.flag));
        }
    }

    public void workLastSec() {
        el++;
        check_last_sec = true;
        if (el < adapter.getCount()) {
            String[] words = Objects.requireNonNull(adapter.getItem(el)).split(" : ");
            if (words.length == 2)
                newService(words[1], "0");
            else
                newService(words[1], words[2]);
        }
    }


    public void newService(String name, String time) {
        startService(new Intent(this, Timer.class).putExtra(PARAM_START_TIME, time)
                .putExtra(NAME_ACTION, name));
    }

    public void resetTapped(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(timerTask != null)
                {
                    timerTask.cancel();
                    setButtonUI("START", R.color.green);
                    time = 0.0;
                    timerStarted = false;
                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing
            }
        });

        resetAlert.show();

    }

    public void startStopTapped(View view)
    {
        if(timerStarted == false)
        {
            timerStarted = true;
            setButtonUI("STOP", R.color.red);

            startTimer();
        }
        else
        {
            timerStarted = false;
            setButtonUI("START", R.color.green);

            timerTask.cancel();
        }
    }

    private void setButtonUI(String start, int color)
    {
        stopStartButton.setText(start);
        stopStartButton.setTextColor(ContextCompat.getColor(this, color));
    }

    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

}