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
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SingleSensorObserver extends SensorObserver implements SensorEventListener {


    private static final String TAG = "SingleSensorObserver";
    private int delay = 0;

    public SingleSensorObserver(int sensorId, Context context) {
        super(sensorId, context);
    }

    public void setDelay(int delay) {
        this.delay = delay;
        if (isEnabled()) {
            unregister();
            register();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Observation observation = new Observation(event.values, event.accuracy, event.timestamp);
        getObservations().add(observation);

        if (getObservationLiveData() != null)
            getObservationLiveData().post(observation);

        saveBatchIfNecessary();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    void register() {
        getSensorManager().registerListener(this, getSensor(), delay);
    }

    @Override
    void unregister() {
        getSensorManager().unregisterListener(this, getSensor());
    }

}
