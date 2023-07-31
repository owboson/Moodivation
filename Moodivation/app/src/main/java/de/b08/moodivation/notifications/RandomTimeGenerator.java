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

package de.b08.moodivation.notifications;

import android.content.SharedPreferences;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;


public class RandomTimeGenerator {
    private static Random random = new Random();
    private LocalDate lastGeneratedDateMorning;
    private LocalDate lastGeneratedDateDay;
    private LocalDate lastGeneratedDateEvening;
    private LocalTime cachedTimestampMorning;
    private LocalTime cachedTimestampDay;
    private LocalTime cachedTimestampEvening;


    public RandomTimeGenerator() {
        this.lastGeneratedDateMorning = null;
        this.cachedTimestampMorning = null;
        random = new Random();

    }

    public LocalTime generateMorningTimestamp(SharedPreferences sharedPreferences) {
        String start = sharedPreferences.getString("morning_from", "9:0");
        String end = sharedPreferences.getString("morning_to", "10:30");
        LocalDate currentDate = LocalDate.now();
        if (!currentDate.equals(lastGeneratedDateMorning)) {
            this.cachedTimestampMorning = getRandomTime(start, end);
            this.lastGeneratedDateMorning = currentDate;
        }
        return cachedTimestampMorning;
    }

    public LocalTime generateDayTimestamp(SharedPreferences sharedPreferences) {
        String start = sharedPreferences.getString("day_from", "13:30");
        String end = sharedPreferences.getString("day_to", "14:30");
        LocalDate currentDate = LocalDate.now();
        if (!currentDate.equals(lastGeneratedDateDay)) {
            this.cachedTimestampDay = getRandomTime(start, end);
            this.lastGeneratedDateDay = currentDate;
        }
        return cachedTimestampDay;
    }

    public LocalTime generateEveningTimestamp(SharedPreferences sharedPreferences) {
        String start = sharedPreferences.getString("evening_from", "13:0");
        String end = sharedPreferences.getString("evening_to", "10:30");
        LocalDate currentDate = LocalDate.now();
        if (!currentDate.equals(lastGeneratedDateEvening)) {
            this.cachedTimestampEvening = getRandomTime(start, end);
            this.lastGeneratedDateEvening = currentDate;
        }
        return cachedTimestampEvening;
    }

    private static LocalTime getRandomTime(String start, String end) {
        LocalTime startTime = LocalTime.parse(formatTimestamp(start.concat(":00")));
        LocalTime endTime = LocalTime.parse(formatTimestamp(end.concat(":00")));

        // Get the range of minutes between the two timestamps
        int minutesRange = endTime.toSecondOfDay() / 60 - startTime.toSecondOfDay() / 60;

        // Generate a random number within the range of minutes
        int randomMinutes = random.nextInt(minutesRange);

        // Add the random number of minutes to the start time
        return startTime.plusMinutes(randomMinutes);
    }

    public static String formatTimestamp(String timestamp) {
        String[] parts = timestamp.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        String hourString = String.format("%02d", hour);
        String minuteString = String.format("%02d", minute);

        return hourString + ":" + minuteString;
    }

}
