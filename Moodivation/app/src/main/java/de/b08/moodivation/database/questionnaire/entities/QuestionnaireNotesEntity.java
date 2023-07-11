package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnaireNotesEntity)) return false;
        QuestionnaireNotesEntity that = (QuestionnaireNotesEntity) o;
        return timestamp.equals(that.timestamp) && questionnaireId.equals(that.questionnaireId) && Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, questionnaireId, notes);
    }
}
