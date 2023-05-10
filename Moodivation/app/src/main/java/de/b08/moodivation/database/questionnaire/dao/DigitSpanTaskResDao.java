package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.DigitSpanTaskResEntity;

@Dao
public interface DigitSpanTaskResDao {

    @Insert
    void insert(DigitSpanTaskResEntity entity);

    @Insert
    void insertAll(List<DigitSpanTaskResEntity> entities);

}
