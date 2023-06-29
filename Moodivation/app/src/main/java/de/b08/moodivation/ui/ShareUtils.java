package de.b08.moodivation.ui;

import android.app.Activity;
import android.content.Intent;

public class ShareUtils {

    public static void shareTextIntent(String message, Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        activity.startActivity(Intent.createChooser(intent, null));
    }

}
