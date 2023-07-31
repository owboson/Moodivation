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
