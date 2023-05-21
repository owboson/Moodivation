package de.b08.moodivation.database.sensors;

import android.content.Context;

import androidx.room.BuiltInTypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.b08.moodivation.database.sensors.dao.AccelerometerDao;
import de.b08.moodivation.database.sensors.dao.StepDao;
import de.b08.moodivation.database.sensors.entities.AccelerometerDataEntity;
import de.b08.moodivation.database.sensors.entities.StepDataEntity;
import de.b08.moodivation.database.typeconverter.DateConverter;

@Database(entities = {AccelerometerDataEntity.class, StepDataEntity.class},
        exportSchema = true,
        version = 1)
@TypeConverters(value = {DateConverter.class},
        builtInTypeConverters = @BuiltInTypeConverters(enums = BuiltInTypeConverters.State.ENABLED))
public abstract class SensorDatabase extends RoomDatabase {

    private static volatile SensorDatabase instance = null;

    public static SensorDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (SensorDatabase.class) {
                instance = Room.databaseBuilder(context, SensorDatabase.class,
                        "SensorDatabase").build();
                return instance;
            }
        }
        return instance;
    }

    public abstract AccelerometerDao accelerometerDataDao();

    public abstract StepDao stepDataDao();

}



