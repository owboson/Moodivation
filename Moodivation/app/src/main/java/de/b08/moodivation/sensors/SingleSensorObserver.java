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
