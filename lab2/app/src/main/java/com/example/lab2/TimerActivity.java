package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



import java.util.List;

import Model.Timer;
import Service.TimerService;

public class TimerActivity extends AppCompatActivity {

    private Timer timer;
    public ListView listview;
    public TextView text_timer;
    public List<String> timerList;
    public ArrayAdapter<String> listViewAdapter;
    public MainViewModel mainViewModel ;
    public Button btnBegin, btnEnd;
    public ConstraintLayout constrLayout;
    Intent intentService;


    BroadcastReceiver br;
    public final static String BROADCAST_ACTION = "ru.startandroid.develop.p0961servicebackbroadcast";
    public final static String PARAM_RESULT = "result";
    public final static String TYPE_ACTION = "action";
    public final static String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainViewModel  = ViewModelProviders.of(this).get(MainViewModel.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(preferences.getString("theme", "D").equals("D"))
        {
            setTheme(R.style.AppThemeDark);
        }
        else if(preferences.getString("theme", "D").equals("L"))
        {
            setTheme(R.style.AppThemeLite);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_page);

        Intent mainintent = this.getIntent();
        this.timer = (Timer) mainintent.getSerializableExtra("timer");
        mainViewModel.initializationTimerIntegerList(timer);
        timerList = mainViewModel.getTimerStringList(timer);

        listview = findViewById(R.id.listView);
        constrLayout = findViewById(R.id.Constraint);
        btnBegin = findViewById(R.id.button_start);
        text_timer = findViewById(R.id.textView_timer);
        btnEnd = findViewById(R.id.button_stop);

        listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, timerList);
        listview.setAdapter(listViewAdapter);
        listview.setBackgroundColor(Color.parseColor(timer.getColor()));
        constrLayout.setBackgroundColor(Color.parseColor(timer.getColor()));

        intentService = new Intent(this, TimerService.class).putExtra("list", mainViewModel.getList());
        btnBegin.setOnClickListener(item -> {
            intentService.putExtra("operationCode", 1);
            startService(intentService);
        });

        btnEnd.setOnClickListener(item -> {
            intentService.putExtra("operationCode", 2);
            startService(intentService);
        });

        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                intentService.putExtra("operationCode", 3).putExtra("position", position);
                startService(intentService);
            }
        });
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int result = intent.getIntExtra(PARAM_RESULT, 0);
                text_timer.setText("" + result);
            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        stopService(intentService);
    }

    @Override
    public void finish() {
        super.finish();
        intentService.putExtra("operationCode", 2);
        startService(intentService);
    }
}