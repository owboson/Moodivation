package de.b08.moodivation.ui;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import de.b08.moodivation.R;

public class ShareButton extends LinearLayout {

    private ImageButton shareBtn;

    public ShareButton(Context context) {
        super(context);

        inflate(context, R.layout.share_button, this);

        shareBtn = findViewById(R.id.shareBtn);
    }

    public ImageButton getShareBtn() {
        return shareBtn;
    }
}
