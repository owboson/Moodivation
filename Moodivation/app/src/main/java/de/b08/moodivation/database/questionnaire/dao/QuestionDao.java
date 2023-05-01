package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.QuestionEntity;

@Dao
public interface QuestionDao {

    @Query("SELECT QuestionEntity.questionId FROM QuestionEntity WHERE QuestionEntity.questionnaireId = :questionnaireId")
    List<String> getAllQuestionIdsForQuestionnaire(String questionnaireId);

    @Insert
    void insert(QuestionEntity entity);

    @Insert
    void insertAll(List<QuestionEntity> entities);

}
