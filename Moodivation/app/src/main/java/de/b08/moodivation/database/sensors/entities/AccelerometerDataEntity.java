package de.b08.moodivation.database.sensors.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccelerometerDataEntity)) return false;
        AccelerometerDataEntity that = (AccelerometerDataEntity) o;
        return Float.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0 && Float.compare(that.z, z) == 0 && accuracy == that.accuracy && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, x, y, z, accuracy);
    }
}
