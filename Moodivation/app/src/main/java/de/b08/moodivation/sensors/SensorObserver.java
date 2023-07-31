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
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public abstract class SensorObserver {

    private static final int BATCH_SIZE = 10000;
    private static final int TRIES = 3;
    private static final String TAG = "SensorObserver";
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private Function<List<Observation>, Boolean> batchStoreFunc;
    private LinkedList<Observation> observations;
    private final List<OnDataStoredHandler> onDataStoredHandlers;

    private ObservationLiveData<?> observationLiveData;

    private boolean enabled = false;

    public SensorObserver(int sensorId, Context context) {
        onDataStoredHandlers = new ArrayList<>();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorId);

        if (sensor == null) {
            Log.w(TAG, String.format("no sensor with id %d", sensorId));
            return;
        }

        Log.i(TAG, String.format("sensor %s, vendor %s, version %s", sensor.getName(),sensor.getVendor(), sensor.getVersion()));
        observations = new LinkedList<>();
    }

    public void setBatchStoreFunc(Function<List<Observation>, Boolean> batchStoreFunc) {
        this.batchStoreFunc = batchStoreFunc;
    }

    abstract void register();

    abstract void unregister();

    public void setEnabled(boolean enabled) {
        if (sensor == null)
            return;

        this.enabled = enabled;
        if (enabled) {
            register();
        } else {
            unregister();
        }
    }

    protected void saveBatchIfNecessary() {
        if (getObservations().size() > BATCH_SIZE) {
            saveBatch();
        }
    }

    public void saveBatch() {
        if (getObservations() == null)
            return;

        LinkedList<Observation> observationsCopy = new LinkedList<>(getObservations());
        getObservations().clear();

        Handler mainThread = new Handler(Looper.getMainLooper());
        AsyncTask.execute(() -> {
            if (observationsCopy.isEmpty())
                return;

            for (int i = 0; i < TRIES; i++) {
                boolean returnVal = getBatchStoreFunc().apply(observationsCopy);
                if (returnVal) {
                    mainThread.post(() -> onDataStoredHandlers.forEach(OnDataStoredHandler::onDataStored));
                    return;
                }
            }
        });
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Function<List<Observation>, Boolean> getBatchStoreFunc() {
        return batchStoreFunc;
    }

    public LinkedList<Observation> getObservations() {
        return observations;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public ObservationLiveData<?> getObservationLiveData() {
        return observationLiveData;
    }

    public void setObservationLiveData(ObservationLiveData<?> observationLiveData) {
        this.observationLiveData = observationLiveData;
    }

    @FunctionalInterface
    public interface OnDataStoredHandler {
        void onDataStored();
    }

    public void addOnDataStoredHandler(OnDataStoredHandler handler) {
        onDataStoredHandlers.add(handler);
    }
}
