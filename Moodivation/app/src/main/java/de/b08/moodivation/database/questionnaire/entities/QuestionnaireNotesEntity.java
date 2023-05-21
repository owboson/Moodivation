package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"timestamp", "questionnaireId"})
public class QuestionnaireNotesEntity {

    @NonNull
    public Date timestamp;

    @NonNull
    public String questionnaireId;

    public String notes;

    public QuestionnaireNotesEntity(@NonNull String questionnaireId, @NonNull Date timestamp, String notes) {
        this.questionnaireId = questionnaireId;
        this.timestamp = timestamp;
        this.notes = notes;
    }

}
