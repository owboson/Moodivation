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
