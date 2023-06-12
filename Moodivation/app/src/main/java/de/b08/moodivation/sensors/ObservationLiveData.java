package de.b08.moodivation.sensors;

import androidx.lifecycle.LiveData;

public abstract class ObservationLiveData<T> extends LiveData<T> {

    public abstract void post(Observation observation);

}
