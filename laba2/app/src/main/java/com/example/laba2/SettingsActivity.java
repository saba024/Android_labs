package com.example.laba2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laba2.Dao.TimerDao;
import com.example.laba2.Database.DatabaseHelper;
import com.example.laba2.Database.TimerDatabase;

import java.util.Locale;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Locale language = new Locale(preferences.getString("language", "eu-US"));
        Locale.setDefault(language);
        Configuration configuration = new Configuration();
        float koefficient = (float)preferences.getInt("size", 1);
        configuration.locale = language;
        configuration.fontScale = koefficient / 10;
        getBaseContext().getResources().updateConfiguration(configuration, null);
        if(preferences.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(preferences.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
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
            TimerDatabase db = DatabaseHelper.createInstance();
            db.timerDao().deleteAll();
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