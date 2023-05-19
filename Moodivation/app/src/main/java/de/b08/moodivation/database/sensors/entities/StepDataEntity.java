package de.b08.moodivation.database.sensors.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

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

}
