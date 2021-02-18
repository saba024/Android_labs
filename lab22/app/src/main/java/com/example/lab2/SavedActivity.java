package com.example.lab2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab2.Adapter.RecycleViewAdapter;
import com.example.lab2.Database.DatabaseHelper;
import com.example.lab2.Database.TimerDatabase;
import com.example.lab2.Model.Timer;

import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    List<Timer> timerList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecycleViewAdapter adapter;


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

        initData();

        recyclerView = (RecyclerView) findViewById(R.id.list_timer);
        adapter = new RecycleViewAdapter(timerList, getBaseContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void initData()
    {
        TimerDatabase db = DatabaseHelper.createInstance();
        List<Timer> timers = db.timerDao().getAll();
        db.close();
        timers.add(0, getTimer("Tabata", 8, 20, 10, "green"));
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
                .setTitle("Delete Timer")
                .setPositiveButton("Yes", (di, i1) -> {
                    new Thread(() -> {
                        TimerDatabase database = DatabaseHelper.createInstance();
                        database.timerDao().delete(timer);
                        database.close();
                    }).start();
                    initData();
                })
                .setNegativeButton("No", (dialogI, i1) -> dialogI.dismiss())
                .create().show();
        return true;
    }
}