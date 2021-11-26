package com.teamopendata.mindcareapp.common;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.teamopendata.mindcareapp.R;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public final class Utils {
    private Utils() {
    }


    public static String getDayString(DayOfWeek day) {
        return day.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

    public static long localDateToMilli(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDate milliToLocalDate(long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()).toLocalDate();
    }

    public static Toast buildSaveToast(@NonNull View view, @NonNull Context context, @NonNull LayoutInflater inflater) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(inflater.inflate(R.layout.toast_record_save, view.findViewById(R.id.toast_record_save)));
        toast.setMargin(0, 0);
        return toast;
    }

    public static String LocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDate StringToLocalDate(String text) {
        String[] date = text.split("-");
        return LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
    }


}