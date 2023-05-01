package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.QuestionnaireEntity;

@Dao
public interface QuestionnaireDao {

    @Query("SELECT * FROM QuestionnaireEntity")
    List<QuestionnaireEntity> getAllQuestionnaires();

    @Insert
    void insert(QuestionnaireEntity entity);

    @Insert
    void insertAll(List<QuestionnaireEntity> entities);

}
