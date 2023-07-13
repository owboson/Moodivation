package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import de.b08.moodivation.database.questionnaire.entities.InterventionTriggeredAfterQuestionnaireEntity;

@Dao
public interface InterventionTriggeredAfterQuestionnaireDao {

    @Insert
    void insert(InterventionTriggeredAfterQuestionnaireEntity entity);

}
