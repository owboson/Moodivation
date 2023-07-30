/*
 * MIT License
 *
 * Copyright (c) 2023 RUB-SE-LAB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
