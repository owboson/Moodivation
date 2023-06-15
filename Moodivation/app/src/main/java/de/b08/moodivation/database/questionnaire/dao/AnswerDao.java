package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;

@Dao
public interface AnswerDao {

    @Query("SELECT * FROM AnswerEntity WHERE AnswerEntity.questionId = :questionId AND AnswerEntity.questionnaireId = :questionnaireId")
    List<AnswerEntity> getAllAnswersForQuestion(String questionnaireId, String questionId);

    @Insert
    void insert(AnswerEntity entity);

    @Insert
    void insertAll(List<AnswerEntity> entities);

    @Query("SELECT DISTINCT(timestamp) FROM AnswerEntity")
    List<Date> getAllDates();

    @Query("SELECT * FROM AnswerEntity WHERE timestamp = :timestamp")
    List<AnswerEntity> getAllAnswerEntitiesWithTimestamp(long timestamp);

}
