package com.example.lab1;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;


import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        TextView v = findViewById(R.id.versionName);
        v.setText(BuildConfig.VERSION_NAME);
    }
}