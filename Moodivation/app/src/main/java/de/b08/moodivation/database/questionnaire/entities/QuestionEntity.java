package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionEntity)) return false;
        QuestionEntity that = (QuestionEntity) o;
        return questionnaireId.equals(that.questionnaireId) && questionId.equals(that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionnaireId, questionId);
    }
}
