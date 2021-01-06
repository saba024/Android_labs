package com.example.lab2;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseHelper;
import Model.Timer;

public class DatabaseViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;

    private static final int DB_VERSION = 7;

    private static final String DB_NAME = "timerdb";

    private static final String TABLE_NAME = "Timer";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PREPARE_TIME = "prepareTime";
    public static final String DURATION = "duration";
    public static final String REST_TIME = "restTime";
    public static final String CYCLE_NUMBER = "cycleNumber";
    public static final String SET_NUMBER = "setNumber";
    public static final String PAUSE = "pause";
    public static final String COLOR = "color";

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        databaseHelper = new DatabaseHelper(getApplication());
    }

    public void addTimer(Timer timer) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put(NAME, timer.getName());
        val.put(PREPARE_TIME, timer.getPrepareTime());
        val.put(DURATION, timer.getDuration());
        val.put(REST_TIME, timer.getRestTime());
        val.put(CYCLE_NUMBER, timer.getCycleNumber());
        val.put(SET_NUMBER, timer.getSetNumber());
        val.put(PAUSE, timer.getPause());
        val.put(COLOR, timer.getColor());

        db.insert(TABLE_NAME, null, val);

        db.close();
    }

    public int getTimerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Timer getTimer(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor usercursor = db.query(TABLE_NAME, new String[] { ID,
                        NAME, PREPARE_TIME ,
                        DURATION, REST_TIME, CYCLE_NUMBER,
                        SET_NUMBER, PAUSE, COLOR}, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (usercursor != null)
            usercursor.moveToFirst();

        Timer timer = new Timer(Integer.parseInt(usercursor.getString(0)),
                usercursor.getString(1),
                usercursor.getString(2),
                usercursor.getString(3),
                usercursor.getString(4),
                usercursor.getString(5),
                usercursor.getString(6),
                usercursor.getString(7),
                usercursor.getString(8));
        return timer;
    }

    public void deleteTimers()
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("create table Timer ("
                + "id integer primary key autoincrement,"
                + "name Text,"
                + "prepareTime Text,"
                + "duration Text,"
                + "restTime Text,"
                + "cycleNumber Text,"
                + "setNumber Text,"
                + "pause Text,"
                + "color Text" + ");");
    }

    public List<Timer> getAllTimer() {
        List<Timer> worktimeList = new ArrayList<Timer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Timer timer = new Timer();
                timer.setId(Integer.parseInt(cursor.getString(0)));
                timer.setName(cursor.getString(1));
                timer.setPrepareTime(cursor.getString(2));
                timer.setDuration(cursor.getString(4));
                timer.setRestTime(cursor.getString(5));
                timer.setCycleNumber(cursor.getString(6));
                timer.setSetNumber(cursor.getString(7));
                timer.setPause(cursor.getString(8));
                timer.setColor(cursor.getString(9));
                // Adding note to list
                worktimeList.add(timer);
            } while (cursor.moveToNext());
        }

        // return note list
        return worktimeList;
    }


    public int updateTimer(Timer timer) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, timer.getName());
        values.put(PREPARE_TIME, timer.getPrepareTime());
        values.put(DURATION, timer.getDuration());
        values.put(REST_TIME, timer.getRestTime());
        values.put(CYCLE_NUMBER, timer.getCycleNumber());
        values.put(SET_NUMBER, timer.getSetNumber());
        values.put(PAUSE, timer.getPause());
        values.put(COLOR, timer.getColor());

        // updating row
        return db.update(TABLE_NAME, values, ID + " = ?",
                new String[]{String.valueOf(timer.getId())});
    }

    public void deleteTimer(Timer timer) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[] { String.valueOf(timer.getId()) });
        db.close();
    }

}
