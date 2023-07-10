package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.Date;
import java.util.Objects;

@Entity(primaryKeys = {"timestamp"})
public class DigitSpanTaskResEntity {

    @NonNull
    public Date timestamp;

    @NonNull
    public Boolean afterNoonQuestionnaire;

    @NonNull
    public Integer result;

    @Nullable
    public Date questionnaireAnswerId;

    public DigitSpanTaskResEntity(@NonNull Date timestamp, @NonNull Boolean afterNoonQuestionnaire,
                                  @NonNull Integer result, @Nullable Date questionnaireAnswerId) {
        this.timestamp = timestamp;
        this.afterNoonQuestionnaire = afterNoonQuestionnaire;
        this.result = result;
        this.questionnaireAnswerId = questionnaireAnswerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DigitSpanTaskResEntity)) return false;
        DigitSpanTaskResEntity that = (DigitSpanTaskResEntity) o;
        return timestamp.equals(that.timestamp) && afterNoonQuestionnaire.equals(that.afterNoonQuestionnaire) && result.equals(that.result) && Objects.equals(questionnaireAnswerId, that.questionnaireAnswerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, afterNoonQuestionnaire, result, questionnaireAnswerId);
    }
}
