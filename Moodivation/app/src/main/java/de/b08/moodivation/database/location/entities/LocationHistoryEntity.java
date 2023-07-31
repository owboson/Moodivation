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

package de.b08.moodivation.database.location.entities;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationHistoryEntity)) return false;
        LocationHistoryEntity that = (LocationHistoryEntity) o;
        return Float.compare(that.speed, speed) == 0 && Float.compare(that.speedAccuracyMetersPerSecond, speedAccuracyMetersPerSecond) == 0 && Double.compare(that.altitude, altitude) == 0 && Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Float.compare(that.bearing, bearing) == 0 && Float.compare(that.bearingAccuracyDegrees, bearingAccuracyDegrees) == 0 && Float.compare(that.accuracy, accuracy) == 0 && Float.compare(that.verticalAccuracyMeters, verticalAccuracyMeters) == 0 && timestamp.equals(that.timestamp) && Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, speed, speedAccuracyMetersPerSecond, altitude, latitude, longitude, bearing, bearingAccuracyDegrees, accuracy, verticalAccuracyMeters, provider);
    }
}
