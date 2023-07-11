package de.b08.moodivation.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date minimizeDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new Date(date.getTime() - 1000*60*60*c.get(Calendar.HOUR_OF_DAY) - 1000*60*c.get(Calendar.MINUTE) - 1000*c.get(Calendar.SECOND) - c.get(Calendar.MILLISECOND));
    }

    public static Date maximizeDay(Date date) {
        Date minimized = minimizeDay(date);
        return new Date(minimized.getTime() + 1000*60*60*24 - 1);
    }

    public static Date minimizeMonthAndDay(Date date) {
        Date minimizedDay = minimizeDay(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minimizedDay);
        return new Date(minimizedDay.getTime() - 1000L*60*60*24*(calendar.get(Calendar.DAY_OF_MONTH) - 1));
    }

}
