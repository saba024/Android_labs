package com.example.laba2.Application;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

public class TimerApplication extends Application{

    private static Application mApp;

    public void onCreate() {
        super.onCreate();

        mApp = this;

        setupPrefs(this);
    }

    public static Context getContext(){
        return mApp.getApplicationContext();
    }

    private static void setupPrefs(Context context){
        new Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
