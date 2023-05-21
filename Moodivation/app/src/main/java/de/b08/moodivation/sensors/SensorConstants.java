package de.b08.moodivation.sensors;

import android.content.Context;
import android.content.SharedPreferences;

public interface SensorConstants {

    String ACCELEROMETER_SETTING_PREFIX = "accelerometerSetting";
    String STEP_DETECTOR_SETTING_PREFIX = "stepDetectorSetting";

    String ENABLED_SETTING_SUFFIX = "Enabled";
    String FREQUENCY_SETTING_SUFFIX = "Frequency";

    String ACCELEROMETER_ENABLED_SETTING = ACCELEROMETER_SETTING_PREFIX + ENABLED_SETTING_SUFFIX;
    String STEP_DETECTOR_ENABLED_SETTING = STEP_DETECTOR_SETTING_PREFIX + ENABLED_SETTING_SUFFIX;


    String ACCELEROMETER_FREQUENCY_SETTING = ACCELEROMETER_SETTING_PREFIX + FREQUENCY_SETTING_SUFFIX;

    int ACCELEROMETER_DEFAULT_FREQUENCY_VALUE = SensorDelay.FASTEST.getValue();
    boolean ACCELEROMETER_DEFAULT_ENABLED_VALUE = false;
    boolean STEP_DETECTOR_DEFAULT_ENABLED_VALUE = false;

    static void presetSensorSharedPreferencesIfRequired(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        if (sharedPreferences.contains(ACCELEROMETER_ENABLED_SETTING) && sharedPreferences.contains(ACCELEROMETER_FREQUENCY_SETTING)
                && sharedPreferences.contains(STEP_DETECTOR_ENABLED_SETTING))
            return;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(ACCELEROMETER_ENABLED_SETTING, ACCELEROMETER_DEFAULT_ENABLED_VALUE);
        editor.putBoolean(STEP_DETECTOR_ENABLED_SETTING, STEP_DETECTOR_DEFAULT_ENABLED_VALUE);

        editor.putInt(ACCELEROMETER_FREQUENCY_SETTING, ACCELEROMETER_DEFAULT_FREQUENCY_VALUE);

        editor.apply();
    }

}
