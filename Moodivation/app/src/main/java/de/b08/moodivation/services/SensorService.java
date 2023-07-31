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

package de.b08.moodivation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.stream.Collectors;

import de.b08.moodivation.Rewards;
import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.entities.RewardCompletionEntity;
import de.b08.moodivation.database.sensors.SensorDatabase;
import de.b08.moodivation.database.sensors.entities.AccelerometerDataEntity;
import de.b08.moodivation.database.sensors.entities.StepDataEntity;
import de.b08.moodivation.sensors.SensorConstants;
import de.b08.moodivation.sensors.SingleSensorObserver;
import de.b08.moodivation.sensors.SingleTriggerSensorObserver;
import de.b08.moodivation.utils.DateUtils;

/**
 * Handles all sensors for which data is being stored in the database
 */
public class SensorService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "SensorService";
    private SharedPreferences sharedPreferences;

    private SensorDatabase sensorDatabase;

    private SingleSensorObserver accelerometerSensorObserver;
    private SingleTriggerSensorObserver stepTriggerSensorObserver;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        sensorDatabase = SensorDatabase.getInstance(getApplicationContext());

        accelerometerSensorObserver = new SingleSensorObserver(Sensor.TYPE_ACCELEROMETER, getApplicationContext());
        accelerometerSensorObserver.setBatchStoreFunc(observations -> {
            sensorDatabase.accelerometerDataDao().insertAll(observations.stream()
                    .map(o -> new AccelerometerDataEntity(new Date(o.getTimestamp()), o.getValues()[0],o.getValues()[1],o.getValues()[2], (byte) o.getAccuracy()))
                    .collect(Collectors.toList())
            );
            return true;
        });

        accelerometerSensorObserver.setDelay(sharedPreferences.getInt(SensorConstants.ACCELEROMETER_FREQUENCY_SETTING,
                SensorConstants.ACCELEROMETER_DEFAULT_FREQUENCY_VALUE));
        accelerometerSensorObserver.setEnabled(sharedPreferences.getBoolean(SensorConstants.ACCELEROMETER_ENABLED_SETTING,
                SensorConstants.ACCELEROMETER_DEFAULT_ENABLED_VALUE));

        stepTriggerSensorObserver = new SingleTriggerSensorObserver(Sensor.TYPE_STEP_DETECTOR, getApplicationContext());
        stepTriggerSensorObserver.setBatchStoreFunc(observations -> {
            sensorDatabase.stepDataDao().insertAll(observations.stream()
                    .map(o -> new StepDataEntity(new Date(o.getTimestamp())))
                    .collect(Collectors.toList())
            );
            return true;
        });
        stepTriggerSensorObserver.setObservationLiveData(StepLiveData.getInstance());
        stepTriggerSensorObserver.addOnDataStoredHandler(() -> {
            if (Rewards.stepsCompleted(getApplicationContext())
                    && !InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                    .stepsReferenceDateExists(DateUtils.minimizeDay(new Date()).getTime())) {
                InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                        .insert(RewardCompletionEntity.createStepsEntity(DateUtils.minimizeDay(new Date()), new Date()));
            }
        });

        stepTriggerSensorObserver.setEnabled(sharedPreferences.getBoolean(SensorConstants.STEP_DETECTOR_ENABLED_SETTING,
                SensorConstants.STEP_DETECTOR_DEFAULT_ENABLED_VALUE));

        stepTriggerSensorObserver.addOnDataStoredHandler(() -> System.out.println("something "));
    }

    @Override
    public void onDestroy() {
        accelerometerSensorObserver.saveBatch();
        stepTriggerSensorObserver.saveBatch();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        accelerometerSensorObserver.setEnabled(sharedPreferences.getBoolean(SensorConstants.ACCELEROMETER_ENABLED_SETTING,
                SensorConstants.ACCELEROMETER_DEFAULT_ENABLED_VALUE));
        stepTriggerSensorObserver.setEnabled(sharedPreferences.getBoolean(SensorConstants.STEP_DETECTOR_ENABLED_SETTING,
                SensorConstants.STEP_DETECTOR_DEFAULT_ENABLED_VALUE));
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SensorConstants.ACCELEROMETER_ENABLED_SETTING:
                accelerometerSensorObserver.setEnabled(sharedPreferences.getBoolean(SensorConstants.ACCELEROMETER_ENABLED_SETTING,
                        SensorConstants.ACCELEROMETER_DEFAULT_ENABLED_VALUE));
                break;
            case SensorConstants.ACCELEROMETER_FREQUENCY_SETTING:
                accelerometerSensorObserver.setDelay(sharedPreferences.getInt(SensorConstants.ACCELEROMETER_FREQUENCY_SETTING,
                        SensorConstants.ACCELEROMETER_DEFAULT_FREQUENCY_VALUE));
                break;
            case SensorConstants.STEP_DETECTOR_ENABLED_SETTING:
                stepTriggerSensorObserver.setEnabled(sharedPreferences.getBoolean(SensorConstants.STEP_DETECTOR_ENABLED_SETTING,
                        SensorConstants.STEP_DETECTOR_DEFAULT_ENABLED_VALUE));
                break;
            default:
                // ignore
                break;
        }
    }

    public static boolean isSensorAvailable(int sensorId, Context context) {
        return ((SensorManager) context.getSystemService(SENSOR_SERVICE)).getDefaultSensor(sensorId) != null;
    }

}
