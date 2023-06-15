package de.b08.moodivation.services;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class TravelledDistanceLiveDataManager {

    private static TravelledDistanceLiveDataManager instance;
    private final List<TravelledDistanceLiveData> liveDataObjects;

    public static TravelledDistanceLiveDataManager getInstance() {
        if (instance == null) {
            instance = new TravelledDistanceLiveDataManager();
        }

        return instance;
    }

    private TravelledDistanceLiveDataManager() {
        liveDataObjects = new ArrayList<>();
    }

    public static class TravelledDistanceLiveData extends LiveData<Double> {

        private TravelledDistanceLiveData() {}

        public void addDistance(Double val) {
            double current = getValue() == null ? 0 : getValue();
            postValue(current + val);
        }

        public void reset() {
            postValue(0D);
        }

    }

    public TravelledDistanceLiveData create() {
        TravelledDistanceLiveData travelledDistanceLiveData = new TravelledDistanceLiveData();
        liveDataObjects.add(travelledDistanceLiveData);
        return travelledDistanceLiveData;
    }

    public void addDistance(Double val) {
        liveDataObjects.forEach(l -> l.addDistance(val));
    }

}
