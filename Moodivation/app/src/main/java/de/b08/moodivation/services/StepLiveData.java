package de.b08.moodivation.services;

import de.b08.moodivation.sensors.Observation;
import de.b08.moodivation.sensors.ObservationLiveData;

public class StepLiveData extends ObservationLiveData<Integer> {

    private static StepLiveData instance;

    private StepLiveData() {}

    public static StepLiveData getInstance() {
        if (instance == null) {
            instance = new StepLiveData();
        }
        return instance;
    }

    @Override
    public void post(Observation observation) {
        int current = getValue() == null ? 0: getValue();
        postValue(current + 1);
    }

}
