package de.b08.moodivation.ui;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date minimizeDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new Date(date.getTime() - 1000*60*60*c.get(Calendar.HOUR_OF_DAY) - 1000*60*c.get(Calendar.MINUTE) - 1000*c.get(Calendar.SECOND) - c.get(Calendar.MILLISECOND));
    }

    public static Date maximizeDate(Date date) {
        Date minimized = minimizeDate(date);
        return new Date(minimized.getTime() + 1000*60*60*24 - 1);
    }

}
