package de.b08.moodivation.database.sensors.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"timestamp"})
public class AccelerometerDataEntity {

    @NonNull
    public Date timestamp;

    public float x;
    public float y;
    public float z;
    public byte accuracy;

    public AccelerometerDataEntity(@NonNull Date timestamp, float x, float y, float z, byte accuracy) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.accuracy = accuracy;
    }

}
