package de.b08.moodivation.database.interventions.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;

@Dao
public interface InterventionRecordDao {

    @Insert
    void insert(InterventionRecordEntity entity);

    @Insert
    void insertAll(List<InterventionRecordEntity> entities);

    @Query("SELECT * FROM InterventionRecordEntity")
    List<InterventionRecordEntity> getAllRecords();

    @Update
    void update(InterventionRecordEntity entity);

    @Delete
    void delete(InterventionRecordEntity entity);

}
