package com.example.laba2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.laba2.Adapter.TimerAdapter;
import com.example.laba2.Database.DatabaseHelper;
import com.example.laba2.Database.TimerDatabase;
import com.example.laba2.Model.Timer;

import java.util.List;

public class SavedActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(prefs.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        listView = findViewById(R.id.saved_timers_lv);
        listView.setOnItemClickListener(this);
        addDataToList();
    }

    private void addDataToList(){
        final Handler handler = new Handler();
        new Thread(() -> {
            TimerDatabase db = DatabaseHelper.createInstance();
            List<Timer> timers = db.timerDao().getAll();
            db.close();
            timers.add(0, getTimer("Tabata", 8, 20, 10, "green"));
            handler.post(() -> listView.setAdapter(new TimerAdapter(this, timers)));
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Timer timer = (Timer) adapterView.getItemAtPosition(i);
        SavedTimerRun timerRun = SavedTimerRun.fromTimer(timer);
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(timerRun.toIntent(intent));
    }


    private static Timer getTimer(String name, int sets, int work, int rest, String color){
        Timer t = new Timer();
        t.name = name;
        t.sets = sets;
        t.work = work;
        t.rest = rest;
        t.color = color;
        return t;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Timer timer = (Timer) parent.getItemAtPosition(position);
        new AlertDialog.Builder(this)
                .setTitle(R.string.deletetimer)
                .setPositiveButton("Yes", (di, i1) -> {
                    new Thread(() -> {
                        TimerDatabase database = DatabaseHelper.createInstance();
                        database.timerDao().delete(timer);
                        database.close();
                    }).start();
                    addDataToList();
                })
                .setNegativeButton("No", (dialogI, i1) -> dialogI.dismiss())
                .create().show();
        return true;
    }
}