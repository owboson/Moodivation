package de.b08.moodivation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.time.LocalDateTime;
import java.util.Map;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.notifications.NotificationReceiver;
import de.b08.moodivation.notifications.RandomTimeGenerator;
import de.b08.moodivation.sensors.SensorConstants;
import de.b08.moodivation.services.LocationService;
import de.b08.moodivation.services.SensorService;

public class MainActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    private LocalTime morningNotif;
    private LocalTime dayNotif;
    private LocalTime eveningNotif;

    private TimeZone zone;
    private AlarmManager alarmManager;

    PendingIntent pendingIntent1;
    PendingIntent pendingIntent2;
    PendingIntent pendingIntent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuestionnaireDatabase.getInstance(getApplicationContext());

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        preset_sharedPreferences();

        Button digitSpanTaskPreviewBtn = findViewById(R.id.digitSpanTaskPreviewButton);
        digitSpanTaskPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, DigitSpanTask.class));
        });
        /////////////////        Notifications logic

        SensorConstants.presetSensorSharedPreferencesIfRequired(getApplicationContext());
        startService(new Intent(getApplicationContext(), SensorService.class));

        startForegroundService(new Intent(getApplicationContext(), LocationService.class));

        System.out.println(sharedPreferences.getInt("allow_notifs", 1));
        set_notifs(this.sharedPreferences, getApplicationContext(), (AlarmManager) getSystemService(ALARM_SERVICE));

        Button questionnairePreviewBtn = findViewById(R.id.questionnairePreviewButton);
        questionnairePreviewBtn.setOnClickListener(v -> {
            if (sharedPreferences.getInt("allow_questionnaire_data_collection", 0) == 1) {
                Intent questionnaireIntent = new Intent(this, QuestionnaireActivity.class);
                questionnaireIntent.putExtra("name", "main");
                MainActivity.this.startActivity(questionnaireIntent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Allow data collection to be able to complete the questionnaire")
                        .setTitle("Settings")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        Button settingsPreviewBtn = findViewById(R.id.settingsPreviewButton);
        settingsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, SettingsPage.class));
        });

        Button recordsPreviewBtn = findViewById(R.id.recordsPreviewButton);
        recordsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, RecordsPage.class));
        });

        Button chartsPreviewBtn = findViewById(R.id.chartsPreviewButton);
        chartsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, DevelopmentVisualization.class));
        });
    }

    protected void set_notifs(SharedPreferences sharedPreferences, Context context, AlarmManager alarmManager) {
        if (sharedPreferences.getInt("allow_notifs", 1) == 1 && sharedPreferences.getInt("allow_questionnaire_data_collection", 1) == 1) {
            int periodicity = sharedPreferences.getInt("questionnaire_periodicity", 3);
            int interval = sharedPreferences.getInt("questionnaire_periodicity_details", 3);
            createNotificationChannel(context);

            // get three random timestamps within the notification intervals
            RandomTimeGenerator timeGenerator = new RandomTimeGenerator();
            morningNotif = timeGenerator.generateMorningTimestamp(sharedPreferences);
            dayNotif = timeGenerator.generateDayTimestamp(sharedPreferences);
            eveningNotif = timeGenerator.generateEveningTimestamp(sharedPreferences);

            // Get the AlarmManager service
            zone = TimeZone.getTimeZone("Europe/Berlin");

            if (periodicity == 1) {
                if (interval == 0) {
                    setup_morning(context, alarmManager);
                } else if (interval == 1) {
                    setup_day(context, alarmManager);
                } else if (interval == 2) {
                    setup_evening(context, alarmManager);
                }
            } else if (periodicity == 2) {
                if (interval == 0) {
                    setup_morning(context, alarmManager);
                    setup_day(context, alarmManager);
                } else if (interval == 1) {
                    setup_morning(context, alarmManager);
                    setup_evening(context, alarmManager);
                } else if (interval == 2) {
                    setup_day(context, alarmManager);
                    setup_evening(context, alarmManager);
                }
            }else if (periodicity == 3) {
                setup_morning(context, alarmManager);
                setup_day(context, alarmManager);
                setup_evening(context, alarmManager);
            }
            System.out.println(morningNotif);
            System.out.println(dayNotif);
            System.out.println(eveningNotif);

            System.out.println(pendingIntent1);
            System.out.println(pendingIntent2);
            System.out.println(pendingIntent3);
        }
    }

    private void setup_morning(Context context, AlarmManager alarmManager) {

        Intent intent1 = new Intent(context, NotificationReceiver.class);
        intent1.setAction("morningId");
        pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar1 = Calendar.getInstance(zone);
        calendar1.set(Calendar.HOUR_OF_DAY, morningNotif.getHour());
        calendar1.set(Calendar.MINUTE, morningNotif.getMinute());
        if (calendar1.getTimeInMillis() < System.currentTimeMillis()) {
            // The specified time has already passed, add one day to the calendar
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);

    }

    private void setup_day(Context context, AlarmManager alarmManager) {

        Intent intent2 = new Intent(context, NotificationReceiver.class);
        intent2.setAction("dayId");
        pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar2 = Calendar.getInstance(zone);
        calendar2.set(Calendar.HOUR_OF_DAY, dayNotif.getHour());
        calendar2.set(Calendar.MINUTE, dayNotif.getMinute());
        if (calendar2.getTimeInMillis() < System.currentTimeMillis()) {
            calendar2.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);

    }

    private void setup_evening(Context context, AlarmManager alarmManager) {

        Intent intent3 = new Intent(context, NotificationReceiver.class);
        intent3.setAction("eveningId");
        pendingIntent3 = PendingIntent.getBroadcast(context, 2, intent3, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar3 = Calendar.getInstance(zone);
        calendar3.set(Calendar.HOUR_OF_DAY, eveningNotif.getHour());
        calendar3.set(Calendar.MINUTE, eveningNotif.getMinute());
        if (calendar3.getTimeInMillis() < System.currentTimeMillis()) {
            calendar3.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent3);
    }

    protected void unset_notification_intents() {
        System.out.println("pendingIntents");
        if (pendingIntent1 != null) {
            alarmManager.cancel(pendingIntent1);
        } else if (pendingIntent2 != null) {
            alarmManager.cancel(pendingIntent2);
        } else if (pendingIntent3 != null) {
            alarmManager.cancel(pendingIntent3);
        }


    }

    private void preset_sharedPreferences() {
        if (!sharedPreferences.contains("morning_from")) {
            editor.putString("morning_from", "9:0");
        }
        if (!sharedPreferences.contains("morning_to")) {
            editor.putString("morning_to", "10:30");
        }
        if (!sharedPreferences.contains("day_from")) {
            editor.putString("day_from", "13:30");
        }
        if (!sharedPreferences.contains("day_to")) {
            editor.putString("day_to", "14:30");
        }
        if (!sharedPreferences.contains("evening_from")) {
            editor.putString("evening_from", "18:0");
        }
        if (!sharedPreferences.contains("evening_to")) {
            editor.putString("evening_to", "19:30");
        }
        if (!sharedPreferences.contains("allow_data_collection")) {
            editor.putInt("allow_data_collection", 0);
        }
        if (!sharedPreferences.contains("google_fit_collection")) {
            editor.putInt("google_fit_collection", 0);
        }
        if (!sharedPreferences.contains("allow_notifs")) {
            editor.putInt("allow_notifs", 0);
        }
        if (!sharedPreferences.contains("allow_questionnaire_data_collection")) {
            editor.putInt("allow_questionnaire_data_collection", 1);
        }
        if (!sharedPreferences.contains("questionnaire_periodicity")) {
            editor.putInt("questionnaire_periodicity", 3);
        }
        if (!sharedPreferences.contains("questionnaire_periodicity_details")) {
            editor.putInt("questionnaire_periodicity", 0);
        }
        if (!sharedPreferences.contains("allow_digit_span_collection")) {
            editor.putInt("allow_digit_span_collection", 1);
        }

        editor.commit();

        Map<String, ?> allValues = sharedPreferences.getAll();

// Iterate over the map and print out the key-value pairs
        for (Map.Entry<String, ?> entry : allValues.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Log.d("TAG", key + ": " + value.toString());
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel with a unique ID, name, and importance level
            NotificationChannel channel = new NotificationChannel("channelId", "Notification channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("notifications");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}