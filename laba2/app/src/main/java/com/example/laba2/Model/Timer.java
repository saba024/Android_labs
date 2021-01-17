package com.example.laba2.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Timer {

    @PrimaryKey
    public long timeAdded;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "sets")
    public int sets;

    @ColumnInfo(name = "work")
    public int work;

    @ColumnInfo(name = "rest")
    public int rest;

    @ColumnInfo(name = "mode")
    public int mode;

    @ColumnInfo(name = "color")
    public String color;
}
