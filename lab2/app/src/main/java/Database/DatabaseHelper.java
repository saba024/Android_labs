package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Model.Timer;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    private static final int DB_VERSION = 7;

    private static final String DB_NAME = "timerdb";

    private static final String TABLE_NAME = "Timer";


    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
