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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import de.b08.moodivation.QuestionnaireActivity;
import de.b08.moodivation.R;
public class NotificationReceiver extends BroadcastReceiver {
    Notification notification;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent questionnaireIntent = new Intent(context, QuestionnaireActivity.class);
        questionnaireIntent.putExtra("name", "main");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, questionnaireIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String action = intent.getAction();
        if (action.equals("morningId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle(context.getResources().getString(R.string.questionnaireNotificationTitle))
                    .setContentText(context.getResources().getString(R.string.morningQuestionnaireNotification))
                    .setSmallIcon(R.drawable.moodivation_icon_foreground)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        } else if (action.equals("dayId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle(context.getResources().getString(R.string.questionnaireNotificationTitle))
                    .setContentText(context.getResources().getString(R.string.noonQuestionnaireNotification))
                    .setSmallIcon(R.drawable.moodivation_icon_foreground)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        } else if (action.equals("eveningId")) {
            notification = new NotificationCompat.Builder(context, "channelId")
                    .setContentTitle(context.getResources().getString(R.string.questionnaireNotificationTitle))
                    .setContentText(context.getResources().getString(R.string.eveningQuestionnaireNotification))
                    .setSmallIcon(R.drawable.moodivation_icon_foreground)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        }
        System.out.println(notification);
        if (notification != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

    }
}

