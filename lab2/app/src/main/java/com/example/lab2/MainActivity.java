package com.example.lab2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import java.util.ArrayList;
import java.util.List;
import Model.Timer;

public class MainActivity extends AppCompatActivity{

    public final List<Timer> timerList = new ArrayList<Timer>();
    public ArrayAdapter<Timer> listViewAdapter;
    Button btnAdd, btnClear;
    public ListView listView;
    public MainViewModel mainViewModel;


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
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        btnAdd   = findViewById(R.id.button_add);
        timerList.addAll(mainViewModel.getWorktimelist());
        listViewAdapter = new ArrayAdapter<Timer>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, timerList);
        listView.setAdapter(listViewAdapter);
        registerForContextMenu(listView);
        btnAdd.setOnClickListener(item -> {
            Intent intent = new Intent(this, AddTimerActivity.class);
            this.startActivityForResult(intent, 1000);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings :
                Intent intent = new Intent(this, AppSettingsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle(R.string.select);

        menu.add(0, 111 , 0, R.string.view_timer);
        menu.add(0, 222 , 2, R.string.edit_timer);
        menu.add(0, 444, 4, R.string.delete_timer);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Timer chosenTimer = (Timer) listView.getItemAtPosition(menuInfo.position);

        if(item.getItemId() == 111){
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra("timer", (Parcelable) chosenTimer);
            this.startActivityForResult(intent, 1000);
        }
        else if(item.getItemId() == 222 ){
            Intent intent = new Intent(this, AddTimerActivity.class);
            intent.putExtra("timer", (Parcelable) chosenTimer);
            this.startActivityForResult(intent, 10000);
        }
        else if(item.getItemId() == 444){
            new AlertDialog.Builder(this)
                    .setMessage(chosenTimer.getName() + "Press Yes if you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mainViewModel.deleteTimer(chosenTimer);
                            timerList.remove(chosenTimer);
                            listViewAdapter.notifyDataSetChanged();
                            btnClear.notify();
                            btnAdd.notify();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            return false;
        }
        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}
