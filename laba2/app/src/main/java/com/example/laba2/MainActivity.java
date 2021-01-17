package com.example.laba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Button saved, createNew, settings;
    private boolean hasLaunchedActivities = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(prefs.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        saved = (Button) findViewById(R.id.main_saved);
        createNew = findViewById(R.id.main_create_new);
        settings = findViewById(R.id.main_settings);
        saved.setOnClickListener(view -> launchActivity(new Intent(this, SavedActivity.class)));
        createNew.setOnClickListener(view -> launchActivity(new Intent(this, CreateActivity.class)));
        settings.setOnClickListener(view -> launchActivity(new Intent(this, SettingsActivity.class)));
    }

    private void launchActivity(Intent intent) {
        if (hasLaunchedActivities) return;
        hasLaunchedActivities = true;
        startActivity(intent);
    }

    public void onStop() {
        super.onStop();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        hasLaunchedActivities = false;
        super.onResume();
    }
}