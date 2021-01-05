package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.util.Locale;

import Database.DatabaseHelper;

public class AppSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Locale language = new Locale(preferences.getString("language", "eu-US"));
        Locale.setDefault(language);
        Configuration config = new Configuration();
        float koefficient = (float)preferences.getInt("size", 1);
        config.locale = language;
        config.fontScale = koefficient / 10;
        getBaseContext().getResources().updateConfiguration(config, null);
        if(preferences.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(preferences.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        addPreferencesFromResource(R.xml.settings);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences preferences, String keyValue) {
        if(preferences.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(preferences.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        Locale language = new Locale(preferences.getString("language", "eu-US"));
        Locale.setDefault(language);
        Configuration config = new Configuration();
        config.locale = language;
        float koefficient = (float)preferences.getInt("size", 1);
        config.fontScale = koefficient / 10;
        getBaseContext().getResources().updateConfiguration(config, null);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    @Override
    public void onClick(View v){
        if(v.getId() == R.id.button_clear)
        {
            DatabaseViewModel db = new DatabaseViewModel(getApplication());
            db.deleteTimers();
            Log.d("delete", "Yes");
        }
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}