package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuestionnaireEntity {

    @NonNull
    @PrimaryKey
    public String id;

    public QuestionnaireEntity(@NonNull String id) {
        this.id = id;
    }

}
