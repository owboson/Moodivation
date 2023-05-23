package de.b08.moodivation.database.location.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import java.util.List;

import de.b08.moodivation.database.location.entities.LocationHistoryEntity;

@Dao
public interface LocationHistoryDao {

    @Insert
    void insert(LocationHistoryEntity entity);

    @Insert
    void insertAll(List<LocationHistoryEntity> entities);

    @Delete
    void delete(LocationHistoryEntity entity);

    @Delete
    void deleteAll(List<LocationHistoryEntity> entities);

}