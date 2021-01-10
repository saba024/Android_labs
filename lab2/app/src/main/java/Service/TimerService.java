package Service;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.lab2.R;
import com.example.lab2.TimerActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService extends Service{

    ScheduledExecutorService service;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    int soundReady;
    int soundFinish;
    double current_time;
    boolean timerStarted = false;
    SoundPool soundPool =  new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

    private String TAG = "BroadcastService";
    public static final String COUNTDOWN_BR = "com.example.backgoundtimercount";
    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;

    SharedPreferences sharedPreferences;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();
        soundReady = soundPool.load(this, R.raw.censore_preview, 1);
        soundFinish = soundPool.load(this, R.raw.final_sound, 1);
        service = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }


    public void run() {
        Intent intent = new Intent(TimerActivity.BROADCAST_ACTION);
        if (name.equals(getResources().getString(R.string.Finish))) {
            intent.putExtra(TimerActivity.CURRENT_ACTION, "work");
            intent.putExtra(TimerActivity.NAME_ACTION, name);
            intent.putExtra(TimerActivity.TIME_ACTION, "");
            sendBroadcast(intent);
        }
        try {
            for (current_time = time; current_time > 0; current_time--) {
                intent.putExtra(TimerActivity.CURRENT_ACTION, "work");
                intent.putExtra(TimerActivity.NAME_ACTION, name);
                intent.putExtra(TimerActivity.TIME_ACTION, Integer.toString((int) current_time));
                sendBroadcast(intent);
                TimeUnit.SECONDS.sleep(1);
                signal((int) current_time);
            }
            intent = new Intent(TimerActivity.BROADCAST_ACTION);
            intent.putExtra(TimerActivity.CURRENT_ACTION, "clear");
            sendBroadcast(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void signal(int time) {
        if (time <= 5) {
            if (time == 1)
                soundPool.play(soundFinish, 1, 1, 0, 0, 1);
            else
                soundPool.play(soundReady, 1, 1, 0, 0, 1);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
