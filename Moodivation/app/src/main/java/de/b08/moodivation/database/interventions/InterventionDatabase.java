package de.b08.moodivation.database.interventions;

import android.content.Context;

import androidx.room.BuiltInTypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;
import de.b08.moodivation.database.interventions.dao.InterventionRecordDao;
import de.b08.moodivation.database.typeconverter.DateConverter;


@Database(entities = {InterventionRecordEntity.class},
        exportSchema = true,
        version = 2)
@TypeConverters(value = {DateConverter.class},
        builtInTypeConverters = @BuiltInTypeConverters(enums = BuiltInTypeConverters.State.ENABLED))
public abstract class InterventionDatabase extends RoomDatabase {

    private static volatile InterventionDatabase instance = null;

    public static InterventionDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (InterventionDatabase.class) {
                instance = Room.databaseBuilder(context, InterventionDatabase.class,
                                "InterventionDatabase").fallbackToDestructiveMigration()
                        .build();
                return instance;
            }
        }
        return instance;
    }

    public abstract InterventionRecordDao interventionRecordDao();

}
