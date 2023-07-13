package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"questionnaireId", "timestamp"}/*,
        foreignKeys = {@ForeignKey(entity = QuestionnaireEntity.class, parentColumns = {"id"}, childColumns = {"questionnaireId"})}*/)
public class InterventionTriggeredAfterQuestionnaireEntity {

    @NonNull
    public String questionnaireId;

    @NonNull
    public Date timestamp;

    public InterventionTriggeredAfterQuestionnaireEntity(@NonNull String questionnaireId, @NonNull Date timestamp) {
        this.questionnaireId = questionnaireId;
        this.timestamp = timestamp;
    }

}
