package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class QuestionnaireEntity {

    @NonNull
    @PrimaryKey
    public String id;

    public QuestionnaireEntity(@NonNull String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnaireEntity)) return false;
        QuestionnaireEntity that = (QuestionnaireEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
