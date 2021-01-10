package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences preferences;
    final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = preferences.getString("test_lang", "не выбрано");
        boolean theme = preferences.getBoolean("theme", false);
        String fontSize = preferences.getString("fontSize", "");

        Configuration config = new Configuration();
        Locale language;
        if (value.equals("Английский") || value.equals("English")) {
            language = new Locale("en");
        } else {
            language = new Locale("ru");
        }
        Locale.setDefault(language);
        config.locale = language;


        if (fontSize.equals("Small") || fontSize.equals("Малый")) {
            config.fontScale = (float) 0.85;
        } else if (fontSize.equals("Normal") || fontSize.equals("Нормальный")) {
            config.fontScale = (float) 1;
        } else {
            config.fontScale = (float) 1.15;
        }

        getBaseContext().getResources().updateConfiguration(config, null);

        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_splash_screen);

        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}