package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.Adapters.RecycleViewAdapter;

import java.util.List;

import Model.Timer;

public class MainActivity extends AppCompatActivity{

    List<Timer> timerList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecycleViewAdapter recycleViewAdapter;
    DatabaseViewModel databaseViewModel;
    Button buttonAddTimer, buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_item_activity);
        databaseViewModel.getAllTimer();
        recyclerView = (RecyclerView)findViewById(R.id.list_timer);
        recycleViewAdapter = new RecycleViewAdapter(timerList, getBaseContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter((recycleViewAdapter));
        buttonAddTimer = (Button) findViewById(R.id.buttonAddTimer);
        buttonSettings = (Button)findViewById(R.id.btnSettings);

        buttonAddTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTimerActivity.class);
                startActivity(intent);
            }
        });

        buttonSettings.setOnClickListener(i -> {
            Intent Settings = new Intent(this, AppSettingsActivity.class);
            startActivityForResult(Settings, 1);
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }


}
