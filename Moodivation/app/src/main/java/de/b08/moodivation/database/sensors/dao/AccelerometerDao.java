package de.b08.moodivation.database.sensors.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.sensors.entities.AccelerometerDataEntity;

@Dao
public interface AccelerometerDao {

    @Insert
    void insert(AccelerometerDataEntity entity);

    @Insert
    void insertAll(List<AccelerometerDataEntity> entities);

    @Delete
    void delete(AccelerometerDataEntity entity);

    @Delete
    void deleteAll(List<AccelerometerDataEntity> entities);

    @Query("SELECT * FROM AccelerometerDataEntity WHERE timestamp >= :from AND timestamp <= :to")
    List<AccelerometerDataEntity> getAllEntitiesInTimeInterval(long from, long to);

}
