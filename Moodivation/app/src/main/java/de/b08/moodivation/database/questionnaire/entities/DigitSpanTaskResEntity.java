package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"timestamp"})
public class DigitSpanTaskResEntity {

    @NonNull
    public Date timestamp;

    @NonNull
    public Boolean afterNoonQuestionnaire;

    @NonNull
    public Integer result;

    public DigitSpanTaskResEntity(@NonNull Date timestamp, @NonNull Boolean afterNoonQuestionnaire,
                                  @NonNull Integer result) {
        this.timestamp = timestamp;
        this.afterNoonQuestionnaire = afterNoonQuestionnaire;
        this.result = result;
    }

}
