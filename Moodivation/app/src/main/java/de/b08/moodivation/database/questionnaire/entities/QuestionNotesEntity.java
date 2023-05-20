package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

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

}
