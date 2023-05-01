package de.b08.moodivation.database.typeconverter;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date fromLong(Long time) {
        return time == null ? null : new Date(time);
    }

}
