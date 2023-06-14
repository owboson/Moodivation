package de.b08.moodivation.services;


import android.location.Location;

import androidx.lifecycle.LiveData;

public class LocationLiveData extends LiveData<Location> {

    private static LocationLiveData instance;

    private LocationLiveData() {}

    public static LocationLiveData getInstance() {
        if (instance == null) {
            instance = new LocationLiveData();
        }
        return instance;
    }

    public void update(Location location) {
        postValue(location);
    }

}
