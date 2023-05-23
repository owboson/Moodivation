package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.QuestionNotesEntity;

@Dao
public interface QuestionNotesDao {

    @Query("SELECT * FROM QuestionNotesEntity WHERE questionnaireId = :id")
    List<QuestionNotesEntity> getAllNotesForQuestionnaire(String id);

    @Insert
    void insert(QuestionNotesEntity entity);

    @Insert
    void insertAll(List<QuestionNotesEntity> entities);

    @Delete
    void delete(QuestionNotesEntity entity);

    @Delete
    void deleteAll(List<QuestionNotesEntity> entities);

}
