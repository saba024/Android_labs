package com.example.lab2.Database;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Converter {

    @TypeConverter
    public static List<Integer> fromString(String value) {
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<Integer> list) {
        return new Gson().toJson(list);
    }

}

