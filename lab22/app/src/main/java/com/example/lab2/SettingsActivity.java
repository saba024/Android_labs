package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import java.util.Locale;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        LoadSettings();

    }

    public void LoadSettings()
    {
        loadLocale();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean chk_night = sharedPreferences.getBoolean("NIGHT", false);
        if (chk_night)
        {
            getListView().setBackgroundColor(Color.parseColor("#222222"));
            setTheme(R.style.AppThemeDark);
        }
        else
            {
                getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                setTheme(R.style.AppThemeLite);
            }

        CheckBoxPreference check_night = (CheckBoxPreference) findPreference("NIGHT");
        check_night.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean yes = (boolean)newValue;
                if (yes)
                {
                    getListView().setBackgroundColor(Color.parseColor("#222222"));
                }
                else
                {
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return true;

            }
        });

        ListPreference listPreference = (ListPreference) findPreference("LANGUAGE");
        String language = sharedPreferences.getString("LANGUAGE", String.valueOf(false));
        if ("1".equals(language))
        {
            setlocale("en");
            recreate();
        }
        else if ("2".equals(language))
        {
            setlocale("ru");
            recreate();
        }
    }

    public void setlocale(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My lang", lang);
        editor.apply();
    }

    public void loadLocale()
    {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My lang", "");
        setlocale(language);
    }

    @Override
    protected void onResume() {
        LoadSettings();
        super.onResume();
    }
}