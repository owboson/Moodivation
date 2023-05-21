package de.b08.moodivation.database.location.entities;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class LocationHistoryEntity {

    @NonNull
    @PrimaryKey
    public Date timestamp;

    public float speed;
    public float speedAccuracyMetersPerSecond;
    public double altitude;
    public double latitude;
    public double longitude;
    public float bearing;
    public float bearingAccuracyDegrees;
    public float accuracy;
    public float verticalAccuracyMeters;
    public String provider;

    public LocationHistoryEntity(@NonNull Date timestamp, float speed, float speedAccuracyMetersPerSecond,
                                 double altitude, double latitude, double longitude, float bearing,
                                 float bearingAccuracyDegrees, float accuracy, float verticalAccuracyMeters,
                                 String provider) {
        this.timestamp = timestamp;
        this.speed = speed;
        this.speedAccuracyMetersPerSecond = speedAccuracyMetersPerSecond;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.bearingAccuracyDegrees = bearingAccuracyDegrees;
        this.accuracy = accuracy;
        this.verticalAccuracyMeters = verticalAccuracyMeters;
        this.provider = provider;
    }

    public static List<LocationHistoryEntity> from(@NonNull List<Location> locations) {
        return locations.stream().map(l -> new LocationHistoryEntity(new Date(l.getTime()),
                l.getSpeed(), l.getSpeedAccuracyMetersPerSecond(), l.getAltitude(), l.getLatitude(),
                l.getLongitude(), l.getBearing(), l.getBearingAccuracyDegrees(), l.getAccuracy(),
                l.getVerticalAccuracyMeters(), l.getProvider()))
                .collect(Collectors.toList());
    }

}
