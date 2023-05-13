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
        this.random = new Random();

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

    public static void main(String[] args) {}

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
