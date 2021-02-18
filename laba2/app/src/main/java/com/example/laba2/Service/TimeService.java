package com.example.laba2.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;

import com.example.laba2.Constants.Constants;
import com.example.laba2.R;
import com.example.laba2.SavedTimerRun;
import com.example.laba2.TimerActivity;
import com.pixplicity.easyprefs.library.Prefs;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeService extends Service {

    SoundPool soundPool;
    int soundFinish;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate() {
        super.onCreate();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();
        soundFinish = soundPool.load(this, R.raw.beep, 1);
    }

    public static String formatTime(int seconds) {
        SimpleDateFormat df = new SimpleDateFormat(Constants.timeFormat, Locale.US);
        return df.format(new Date(seconds * 1000));
    }

    public static int getUpdatedTime(int currentTime, int update, Context paramContext){
        return getUpdatedValue(currentTime + update, Constants.MIN_TIME, Constants.MAX_TIME, paramContext);
    }

    public static int getUpdatedSets(int currentSets, int update, Context paramContext){
        return getUpdatedValue(currentSets + update, Constants.MIN_SETS, Constants.MAX_SETS, paramContext);
    }

    private static int getUpdatedValue(int result, int min, int max, Context paramContext){
        if(result < min){
            return min;
        } else if(result > max){
            return max;
        }
        return result;
    }

    public static int getMode(){
        return 2;
    }

    public static boolean isModeManualSets(){
        return getMode() == 2;
    }

    public static int[] getValues(int[] data, boolean longClick, Context context){
        int sets = data[0]; //data -> 0 = sets, 1 = work, 2 = rest, 3 = res
        int workTime = data[1];
        int restTime = data[2];
        if (data[3] == R.id.plus1) {
            sets = TimeService.getUpdatedSets(sets, longClick ? 5 : 1, context);
        } else if (data[3] == R.id.plus2) {
            workTime = TimeService.getUpdatedTime(workTime, longClick ? 60 : 1, context);
        } else if (data[3] == R.id.plus3) {
            restTime = TimeService.getUpdatedTime(restTime, longClick ? 60 : 1, context);
        } else if (data[3] == R.id.minus1) {
            sets = TimeService.getUpdatedSets(sets, longClick ? -5 : -1, context);
        } else if (data[3] == R.id.minus2) {
            workTime = TimeService.getUpdatedTime(workTime, longClick ? -60 : -1, context);
        } else if (data[3] == R.id.minus3) {
            restTime = TimeService.getUpdatedTime(restTime, longClick ? -60 : -1, context);
        }
        return new int[]{sets, workTime, restTime};
    }

    public static void start(Context context, SavedTimerRun t){
        Prefs.putInt(Constants.KEY_SETS, t.sets);
        Prefs.putInt(Constants.KEY_WORK, t.work);
        Prefs.putInt(Constants.KEY_REST, t.rest);
        context.startActivity(new Intent(context, TimerActivity.class));
    }

    public void vibrate(int time)
    {
        if(time < 7){
            soundPool.play(soundFinish,1,1,0,0,1 );
        }
    }

}
