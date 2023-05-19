package de.b08.moodivation.database.sensors.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.sensors.entities.StepDataEntity;

@Dao
public interface StepDao {

    @Insert
    void insert(StepDataEntity entity);

    @Insert
    void insertAll(List<StepDataEntity> entities);

    @Delete
    void delete(StepDataEntity entity);

    @Delete
    void deleteAll(List<StepDataEntity> entities);

    @Query("SELECT * FROM StepDataEntity WHERE timestamp >= :from AND timestamp <= :to")
    List<StepDataEntity> getAllEntitiesInTimeInterval(long from, long to);

    @Query("SELECT COUNT(*) FROM StepDataEntity WHERE timestamp >= :from AND timestamp <= :to")
    int getStepsInTimeInterval(long from, long to);

}
