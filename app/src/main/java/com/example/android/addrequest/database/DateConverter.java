package com.example.android.addrequest.database;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static Date stringToDate(String dateString){

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

}
