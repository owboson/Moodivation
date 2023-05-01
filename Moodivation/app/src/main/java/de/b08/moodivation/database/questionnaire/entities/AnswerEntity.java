package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Date;

@Entity(primaryKeys = {"questionnaireId", "questionId", "timestamp"},
        foreignKeys = {@ForeignKey(entity = QuestionnaireEntity.class, parentColumns = {"id"}, childColumns = {"questionnaireId"})})
public class AnswerEntity {

    public enum DataType {
        STRING, NUMBER, STRING_LIST
    }

    @NonNull
    public String questionnaireId;

    @NonNull
    public String questionId;

    @NonNull
    public Date timestamp;

    @NonNull
    public String answer;

    @NonNull
    public DataType type;

    public AnswerEntity(@NonNull String questionnaireId, @NonNull String questionId, @NonNull Date timestamp,
                        @NonNull DataType type, @NonNull String answer) {
        this.questionnaireId = questionnaireId;
        this.questionId = questionId;
        this.timestamp = timestamp;
        this.type = type;
        this.answer = answer;
    }

}
