package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"questionnaireId", "questionId"},
        foreignKeys = {@ForeignKey(entity = QuestionnaireEntity.class, childColumns = {"questionnaireId"}, parentColumns = {"id"})})
public class QuestionEntity {

    @NonNull
    public String questionnaireId;

    @NonNull
    public String questionId;

    public QuestionEntity(@NonNull String questionnaireId, @NonNull String questionId) {
        this.questionnaireId = questionnaireId;
        this.questionId = questionId;
    }

}
