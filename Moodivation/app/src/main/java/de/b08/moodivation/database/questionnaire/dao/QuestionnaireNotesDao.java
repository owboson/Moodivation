package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.QuestionnaireNotesEntity;

@Dao
public interface QuestionnaireNotesDao {

    @Query("SELECT * FROM QuestionnaireNotesEntity WHERE questionnaireId = :id")
    List<QuestionnaireNotesEntity> getAllNotesForQuestionnaire(String id);

    @Insert
    void insert(QuestionnaireNotesEntity entity);

    @Insert
    void insertAll(List<QuestionnaireNotesEntity> entities);

    @Delete
    void delete(QuestionnaireNotesEntity entity);

    @Delete
    void deleteAll(List<QuestionnaireNotesEntity> entities);

}
