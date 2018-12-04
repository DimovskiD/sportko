package com.dimovski.sportko.db.converter;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String arrayToJSON(ArrayList arrayList) {
        String s =  new Gson().toJson(arrayList);
        return s;
    }

    @TypeConverter
    public static ArrayList<String> JSONtoArray(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList ar =  new Gson().fromJson(value, listType);
        return ar;
    }
}