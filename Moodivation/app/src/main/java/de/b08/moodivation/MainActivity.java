package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.notifications.NotificationReceiver;
import de.b08.moodivation.notifications.RandomTimeGenerator;

public class MainActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuestionnaireDatabase.getInstance(getApplicationContext());

        Button digitSpanTaskPreviewBtn = findViewById(R.id.digitSpanTaskPreviewButton);
        digitSpanTaskPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, DigitSpanTask.class));
        });

        Button questionnairePreviewBtn = findViewById(R.id.questionnairePreviewButton);
        questionnairePreviewBtn.setOnClickListener(v -> {
            Intent questionnaireIntent = new Intent(this, QuestionnaireActivity.class);
            questionnaireIntent.putExtra("name", "main");
            MainActivity.this.startActivity(questionnaireIntent);
        });

        Button settingsPreviewBtn = findViewById(R.id.settingsPreviewButton);
        settingsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, SettingsPage.class));
        });


/////////////////        Notifications logic

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        createNotificationChannel();

        // get three random timestamps within the notification intervals
        RandomTimeGenerator timeGenerator = new RandomTimeGenerator();
        LocalTime morningNotif = timeGenerator.generateMorningTimestamp(sharedPreferences);
        LocalTime dayNotif = timeGenerator.generateDayTimestamp(sharedPreferences);
        LocalTime eveningNotif = timeGenerator.generateEveningTimestamp(sharedPreferences);

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        TimeZone zone = TimeZone.getTimeZone("Europe/Berlin");

        // three unique pending intents for three different notifications
        Intent intent1 = new Intent(this, NotificationReceiver.class);
        intent1.setAction("morningId");
        Intent intent2 = new Intent(this, NotificationReceiver.class);
        intent2.setAction("dayId");
        Intent intent3 = new Intent(this, NotificationReceiver.class);
        intent3.setAction("eveningId");

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 1, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, 2, intent3, PendingIntent.FLAG_CANCEL_CURRENT);

        // take hh:mm and transform into milliseconds
        Calendar calendar1 = Calendar.getInstance(zone);
        calendar1.set(Calendar.HOUR_OF_DAY, morningNotif.getHour());
        calendar1.set(Calendar.MINUTE, morningNotif.getMinute());
        if (calendar1.getTimeInMillis() < System.currentTimeMillis()) {
            // The specified time has already passed, add one day to the calendar
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
        }

        Calendar calendar2 = Calendar.getInstance(zone);
        calendar2.set(Calendar.HOUR_OF_DAY, dayNotif.getHour());
        calendar2.set(Calendar.MINUTE, dayNotif.getMinute());
        if (calendar2.getTimeInMillis() < System.currentTimeMillis()) {
            calendar2.add(Calendar.DAY_OF_MONTH, 1);
        }

        Calendar calendar3 = Calendar.getInstance(zone);
        calendar3.set(Calendar.HOUR_OF_DAY, eveningNotif.getHour());
        calendar3.set(Calendar.MINUTE, eveningNotif.getMinute());
        if (calendar3.getTimeInMillis() < System.currentTimeMillis()) {
            calendar3.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent3);


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel with a unique ID, name, and importance level
            NotificationChannel channel = new NotificationChannel("channelId", "Notification channel", NotificationManager.IMPORTANCE_HIGH);

            // Set a description for the channel
            channel.setDescription("notifications");

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}