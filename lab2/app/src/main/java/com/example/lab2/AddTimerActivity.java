package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;



import java.util.ArrayList;

import Model.Timer;

public class AddTimerActivity extends AppCompatActivity {

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private EditText editName, editPrepare, editDuration, editRestTime, editCycleNumber, editSetNumber, editPause;
    private Button buttonConfirm;
    private Timer timer;
    private int state;
    public ViewModel mainViewModel;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getString("theme", "D").equals("D")) {
            setTheme(R.style.AppThemeDark);
        } else if (prefs.getString("theme", "D").equals("L")) {
            setTheme(R.style.AppThemeLite);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timer);
        mainViewModel = ViewModelProviders.of(this).get(ViewModel.class);


        this.editName = (EditText) this.findViewById(R.id.title);
        this.editPrepare = (EditText) this.findViewById(R.id.prepare);
        this.editDuration = (EditText) this.findViewById(R.id.work_duration_spinner);
        this.editRestTime = (EditText) this.findViewById(R.id.relaxation);
        this.editCycleNumber = (EditText) this.findViewById(R.id.cycle_number);
        this.editSetNumber = (EditText) this.findViewById(R.id.set_number);
        this.editPause = (EditText) this.findViewById(R.id.pause);

        this.buttonConfirm = (Button) findViewById(R.id.btnEdit);
        this.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSaveClicked();
                buttonCancelClicked();
            }
        });


        Intent intent = this.getIntent();
        this.timer = (Timer) intent.getSerializableExtra("timer");
        if (timer == null) {
            this.state = MODE_CREATE;
        } else {
            this.state = MODE_EDIT;
            this.editName.setText(timer.getName());
            this.editPrepare.setText(timer.getPrepareTime());
            this.editDuration.setText(timer.getDuration());
            this.editRestTime.setText(timer.getRestTime());
            this.editCycleNumber.setText(timer.getCycleNumber());
            this.editSetNumber.setText(timer.getSetNumber());
            this.editPause.setText(timer.getPause());
            color = timer.getColor();
        }
    }

    public void buttonSaveClicked() {
        String name = this.editName.getText().toString();
        String preparation = this.editPrepare.getText().toString();
        String work = this.editDuration.getText().toString();
        String relaxation = this.editRestTime.getText().toString();
        String cycle = this.editCycleNumber.getText().toString();
        String set = this.editSetNumber.getText().toString();
        String pause = this.editPause.getText().toString();

        if (name.equals("") || preparation.equals("") || work.equals("") || relaxation.equals("") || cycle.equals("")
                || set.equals("") || pause.equals("") || color == null) {
            Toast.makeText(getApplicationContext(),
                    "Please enter empty edit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (state == MODE_CREATE) {
            mainViewModel.addTimer(name, preparation, work, relaxation, cycle, set, pause, color);
        } else {
            mainViewModel.updateTimer(timer, name, preparation, work, relaxation, cycle, set, pause, color);
        }
        this.onBackPressed();
    }


    public void buttonCancelClicked() {
        this.onBackPressed();
    }


}