package de.b08.moodivation.database.sensors.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;
import java.util.Objects;

/**
 * Each entry is a single step
 */
@Entity(primaryKeys = {"timestamp"})
public class StepDataEntity {

    @NonNull
    public Date timestamp;

    public StepDataEntity(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StepDataEntity)) return false;
        StepDataEntity that = (StepDataEntity) o;
        return timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }
}
