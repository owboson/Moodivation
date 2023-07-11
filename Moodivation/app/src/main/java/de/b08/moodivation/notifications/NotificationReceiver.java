package de.b08.moodivation.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import de.b08.moodivation.R;
public class NotificationReceiver extends BroadcastReceiver {
    Notification notification;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("morningId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle(context.getResources().getString(R.string.questionnaireNotificationTitle))
                    .setContentText(context.getResources().getString(R.string.morningQuestionnaireNotification))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        } else if (action.equals("dayId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle(context.getResources().getString(R.string.questionnaireNotificationTitle))
                    .setContentText(context.getResources().getString(R.string.noonQuestionnaireNotification))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        } else if (action.equals("eveningId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle(context.getResources().getString(R.string.questionnaireNotificationTitle))
                    .setContentText(context.getResources().getString(R.string.eveningQuestionnaireNotification))
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

