package de.b08.moodivation.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void minimizeDayTest() {
        Date date = new Date(1000); // = 01.01.1970 01:00:01
        Date minimizedDay = DateUtils.minimizeDay(date);

        long hourInMillis = 1000 * 60 * 60;
        assertEquals(-hourInMillis, minimizedDay.getTime());

        assertEquals(-hourInMillis, DateUtils.minimizeDay(minimizedDay).getTime());
    }

    @Test
    public void maximizeDayTest() {
        Date date = new Date(1000); // = 01.01.1970 01:00:01
        Date maximizedDay = DateUtils.maximizeDay(date);

        long dayInMillis = 1000 * 60 * 60 * 24;
        long hourInMillis = 1000 * 60 * 60;
        assertEquals(dayInMillis - 1 - hourInMillis, maximizedDay.getTime());

        assertEquals(dayInMillis - 1 - hourInMillis, DateUtils.maximizeDay(maximizedDay).getTime());
    }

    @Test
    public void minimizeMonthAndDayTest() {
        long dayInMillis = 1000 * 60 * 60 * 24;
        Date date = new Date(dayInMillis * 10); // = 11.01.1970 01:00:00
        Date minimizedDate = DateUtils.minimizeMonthAndDay(date);

        long hourInMillis = 1000 * 60 * 60;
        assertEquals(-hourInMillis, minimizedDate.getTime());

        assertEquals(-hourInMillis, DateUtils.minimizeMonthAndDay(minimizedDate).getTime());
    }

}
