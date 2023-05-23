package de.b08.moodivation.database.location;

import android.content.Context;

import androidx.room.BuiltInTypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.b08.moodivation.database.location.dao.LocationHistoryDao;
import de.b08.moodivation.database.location.entities.LocationHistoryEntity;
import de.b08.moodivation.database.typeconverter.DateConverter;

@Database(entities = {LocationHistoryEntity.class},
        exportSchema = true,
        version = 1)
@TypeConverters(value = {DateConverter.class},
        builtInTypeConverters = @BuiltInTypeConverters(enums = BuiltInTypeConverters.State.ENABLED))
public abstract class LocationDatabase extends RoomDatabase {

    private static volatile LocationDatabase instance = null;

    public static LocationDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LocationDatabase.class) {
                instance = Room.databaseBuilder(context, LocationDatabase.class,
                        "LocationDatabase").build();
                return instance;
            }
        }
        return instance;
    }

    public abstract LocationHistoryDao locationHistoryDao();

}
