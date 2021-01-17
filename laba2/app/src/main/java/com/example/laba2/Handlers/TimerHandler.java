package com.example.laba2.Handlers;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.laba2.Service.TimeService;
import com.example.laba2.TimerActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerHandler {
    private TextView timerText;
    private CountDownTimer timer;
    private boolean running;
    private Context context;
    private Handler timerHandler;

    public TimerHandler(TextView timerText, int seconds, timerInterface timerInterface, Context paramContext){
        this.timerText = timerText;
        this.context = paramContext;
        running = true;
        timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long l) {
                updateTimer(l);
            }

            @Override
            public void onFinish() {
                if(!running) return;
                running = false;
                timerInterface.onFinish();
            }
        };
        timer.start();
    }

    public TimerHandler(TextView time){
        timerHandler = new Handler(Looper.getMainLooper());
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
                timerHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    public void stop(){
        if(running)
            timer.cancel();
        running = false;
    }

    private void updateTimer(long millis){
        int time = (int) millis / 1000;
        if(!running || !TimerActivity.isRunning) {
            timer.cancel();
            return;
        }
        timerText.setText(TimeService.formatTime(time));
        if(time < 4){
            if(time == 1){
                new Handler().postDelayed(() -> {
                    timerText.setText(TimeService.formatTime(0));
                }, 950);
            }
        }
    }
    public static interface timerInterface{
        void onFinish();
    }

}

