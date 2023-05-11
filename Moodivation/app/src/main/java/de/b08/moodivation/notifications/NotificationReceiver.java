package de.b08.moodivation.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import de.b08.moodivation.MainActivity;
import de.b08.moodivation.QuestionnaireActivity;
import de.b08.moodivation.R;
public class NotificationReceiver extends BroadcastReceiver {
    Notification notification;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("morningId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle("Questionnaire Reminder")
                    .setContentText("Please fill out your morning questionnaire")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        } else if (action.equals("dayId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle("Questionnaire Reminder")
                    .setContentText("Please fill out your noon questionnaire")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        } else if (action.equals("eveningId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle("Questionnaire Reminder")
                    .setContentText("Please fill out your evening questionnaire")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        }
        System.out.println(notification);
        if (notification != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

    }
}

