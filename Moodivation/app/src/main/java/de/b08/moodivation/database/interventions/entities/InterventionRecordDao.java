package de.b08.moodivation.database.interventions.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.interventions.dao.InterventionRecordEntity;

@Dao
public interface InterventionRecordDao {

    @Insert
    void insert(InterventionRecordEntity entity);

    @Insert
    void insertAll(List<InterventionRecordEntity> entities);

    @Query("SELECT * FROM InterventionRecordEntity")
    List<InterventionRecordEntity> getAllRecords();

}
