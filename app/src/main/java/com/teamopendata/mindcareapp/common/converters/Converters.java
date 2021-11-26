package com.teamopendata.mindcareapp.common.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamopendata.mindcareapp.common.model.entity.Task;
import com.teamopendata.mindcareapp.common.Utils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class Converters {
    @TypeConverter
    public String fromTaskList(List<Task> list) {
        if (list == null) return (null);
        Type type = new TypeToken<List<Task>>() {}.getType();
        return new Gson().toJson(list, type);
    }

    @TypeConverter
    public List<Task> toTaskList(String value) {
        if (value == null) return (null);
        Type type = new TypeToken<List<Task>>() {}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public long LocalDateToLong(LocalDate localDate) {
        return Utils.localDateToMilli(localDate);
    }

    @TypeConverter
    public LocalDate LongFromLocalDate(long milli) {
        return Utils.milliToLocalDate(milli);
    }
}
