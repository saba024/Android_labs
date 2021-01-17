package com.example.laba2.Database;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.laba2.Application.TimerApplication;
import com.example.laba2.Constants.DatabaseConstants;


public class DatabaseHelper {

    public static TimerDatabase createInstance(){
        return Room.databaseBuilder(TimerApplication.getContext(),
                TimerDatabase.class, DatabaseConstants.DB_NAME)
                .addMigrations(new SQLMigration(1, 2, new String[]{"CREATE TABLE IF NOT EXISTS `Timer` (`timeAdded` INTEGER NOT NULL, `name` TEXT, `sets` INTEGER NOT NULL, `work` INTEGER NOT NULL, `rest` INTEGER NOT NULL, `mode` INTEGER NOT NULL, `color` TEXT, PRIMARY KEY(`timeAdded`))"}))
                .build();
    }

    public static class SQLMigration extends Migration {
        private final String[] SQL;
        public SQLMigration(int start, int end, String[] SQL){
            super(start, end);
            this.SQL = SQL;
        }
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            for(String str : SQL) database.execSQL(str);
        }

    }
}

