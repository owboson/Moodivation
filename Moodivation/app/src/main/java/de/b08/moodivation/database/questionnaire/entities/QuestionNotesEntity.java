package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;
import java.util.Objects;

import de.b08.moodivation.questionnaire.Note;

@Entity(primaryKeys = {"timestamp", "questionnaireId", "questionId"})
public class QuestionNotesEntity {

    @NonNull
    public Date timestamp;

    @NonNull
    public String questionnaireId;

    @NonNull
    public String questionId;

    public String notes;

    public QuestionNotesEntity(@NonNull String questionnaireId, @NonNull Date timestamp, @NonNull String questionId, String notes) {
        this.questionnaireId = questionnaireId;
        this.timestamp = timestamp;
        this.questionId = questionId;
        this.notes = notes;
    }

    public static QuestionNotesEntity from(Note note, Date now) {
        if (note.getQuestionId() == null)
            return null;
        return new QuestionNotesEntity(note.getQuestionnaireId(), now, note.getQuestionId(), note.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionNotesEntity)) return false;
        QuestionNotesEntity that = (QuestionNotesEntity) o;
        return timestamp.equals(that.timestamp) && questionnaireId.equals(that.questionnaireId) && questionId.equals(that.questionId) && Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, questionnaireId, questionId, notes);
    }
}
