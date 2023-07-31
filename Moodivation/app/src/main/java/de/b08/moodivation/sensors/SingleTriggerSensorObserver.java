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
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;

public class SingleTriggerSensorObserver extends SensorObserver {

    private static final String TAG = "SingleSensorObserver";

    private final TriggerEventListener triggerEventListener;


    public SingleTriggerSensorObserver(int sensorId, Context context) {
        super(sensorId, context);

        triggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                SingleTriggerSensorObserver.this.onTrigger(event);
            }
        };
    }

    private void onTrigger(TriggerEvent event) {
        Observation observation = new Observation(event.values, 0, event.timestamp);
        getObservations().add(observation);

        if (getObservationLiveData() != null)
            getObservationLiveData().post(observation);

        saveBatchIfNecessary();
    }

    void register() {
        getSensorManager().requestTriggerSensor(triggerEventListener, getSensor());
    }

    void unregister() {
        getSensorManager().cancelTriggerSensor(triggerEventListener, getSensor());
    }

}
