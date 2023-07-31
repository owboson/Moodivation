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
