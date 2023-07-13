package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.DigitSpanTaskResEntity;

@Dao
public interface DigitSpanTaskResDao {

    @Insert
    void insert(DigitSpanTaskResEntity entity);

    @Insert
    void insertAll(List<DigitSpanTaskResEntity> entities);

    @Query("SELECT * FROM DigitSpanTaskResEntity")
    List<DigitSpanTaskResEntity> getAllResEntities();

}
