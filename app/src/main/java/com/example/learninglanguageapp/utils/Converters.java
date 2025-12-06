package com.example.learninglanguageapp.utils;


import androidx.room.TypeConverter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromList(List<String> list) {
        if (list == null) return null;
        return new JSONArray(list).toString();
    }

    @TypeConverter
    public List<String> toList(String value) {
        List<String> list = new ArrayList<>();
        if (value == null) return list;

        try {
            JSONArray json = new JSONArray(value);
            for (int i = 0; i < json.length(); i++) {
                list.add(json.getString(i));
            }
        } catch (Exception ignored) {}

        return list;
    }
}
