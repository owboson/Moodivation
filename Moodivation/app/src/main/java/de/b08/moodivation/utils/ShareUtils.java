package de.b08.moodivation.utils;

import android.content.Context;
import android.content.Intent;

public class ShareUtils {

    public static void shareTextIntent(String message, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        context.startActivity(Intent.createChooser(intent, null));
    }

}
