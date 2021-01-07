package Service;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.lab2.ActionType;
import com.example.lab2.TimerActivity;

import com.example.lab2.R;

public class TimerService extends Service{

    private static int count = 0;
    CountDownTimer countDownTimer;
    private SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    private static int[] durations;
    private int MusicId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        MusicId = soundPool.load(this, R.raw.begin, 1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        durations = intent.getIntArrayExtra("list");
        ActionType action = intent.getParcelableExtra(TimerActivity.TYPE_ACTION);
        switch(action){
            case START :
                startTimer();
                break;
            case STOP:
                stopTimer();
                break;
            case NEXT:
                nextTimer();
                break;
            case PREV:
                prevTimer();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopSelf();
    }

    public void startTimer(){
        if(durations != null && durations.length > 0){
            countDownTimer = new CountDownTimer(durations[count] * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTime(millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    count++;
                    if(count < durations.length){
                        countDownTimer.cancel();
                        soundPool.play(MusicId, 1, 1, 0, 0, 1);
                        startTimer();
                    }
                    else{
                        soundPool.play(MusicId, 1, 1, 0, 0, 1);
                        count = 0;
                        updateTime(0);
                        stopSelf();
                    }
                }
            };
        }
        countDownTimer.start();
    }

    public void stopTimer(){
        countDownTimer.cancel();
    }

    public void updateTime(long time){
        Intent intent = new Intent(TimerActivity.BROADCAST_ACTION);
        intent.putExtra(TimerActivity.PARAM_RESULT, time);
        intent.putExtra(TimerActivity.POSITION, count);
        sendBroadcast(intent);
    }

    public void nextTimer(){
        if(count < durations.length - 1){
            count++;
            countDownTimer.cancel();
            startTimer();
        }
    }

    public void prevTimer(){
        if(count > 0){
            count--;
            countDownTimer.cancel();
            startTimer();
        }
    }
}
