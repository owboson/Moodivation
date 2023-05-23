package de.b08.moodivation.sensors;

import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import java.util.Arrays;

public enum SensorDelay {

    FASTEST(SensorManager.SENSOR_DELAY_FASTEST),
    UI(SensorManager.SENSOR_DELAY_UI),
    GAME(SensorManager.SENSOR_DELAY_GAME),
    NORMAL(SensorManager.SENSOR_DELAY_NORMAL);

    private int value;

    /**
     * @param val Either set to one of {@link SensorManager#SENSOR_DELAY_NORMAL}, {@link SensorManager#SENSOR_DELAY_UI},
     *            {@link SensorManager#SENSOR_DELAY_GAME}, {@link SensorManager#SENSOR_DELAY_FASTEST} or the delay
     *            in milliseconds.
     */
    private SensorDelay(int val) {
        this.value = val;
    }

    @NonNull
    public int getValue() {
        return value;
    }

    public static SensorDelay getDelayFor(int value) {
        return Arrays.stream(values()).filter(d -> d.value == value).findFirst().orElse(FASTEST);
    }

}
