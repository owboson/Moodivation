package de.b08.moodivation.ui;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.color.MaterialColors;

import de.b08.moodivation.R;

public class DescribedChronometerView extends DescribedValueView {

    private final Chronometer chronometer;

    public DescribedChronometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        chronometer = new Chronometer(getContext());
        replaceValueView(chronometer);

        setDescriptionText(R.string.chronometerDescriptionText);
    }

    @NonNull
    public Chronometer getChronometer() {
        return chronometer;
    }

    public void start() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void resume() {
        chronometer.start();
    }

    public void stop() {
        chronometer.stop();
    }

}
