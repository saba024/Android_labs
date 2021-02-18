package com.example.lab2.Service;

import android.app.Service;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.lab2.R;

public class TimerService extends Service {

    private String TAG = "TimerService";
    private int seconds;
    public static final String COUNTDOWN_BR = "com.example.lab2";
    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;
    SoundPool soundPool;
    int soundFinish;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();
        soundFinish = soundPool.load(this, R.raw.beep, 1);
        countDownTimer = new CountDownTimer(getSeconds(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra("countdown", millisUntilFinished);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void vibrate(int time)
    {
        if(time < 5){
            soundPool.play(soundFinish,1,1,0,0,1 );
        }
    }
}
