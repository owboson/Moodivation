/*
 * MIT License
 *
 * Copyright (c) 2023 RUB-SE-LAB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
