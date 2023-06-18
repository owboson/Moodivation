package de.b08.moodivation.database.interventions.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"interventionId", "startTimestamp", "endTimestamp"})
public class InterventionRecordEntity {

    @NonNull
    public String interventionId;

    /**
     * date when questionnaire was answered (null if intervention/activity was recorded without
     * being proposed by the app after a questionnaire)
     */
    @Nullable
    public Date questionnaireAnswerId;

    @NonNull
    public Date startTimestamp;

    @NonNull
    public Date endTimestamp;

    public boolean afterQuestionnaire;

    public Float rating;

    public String feedback;

    public InterventionRecordEntity(@NonNull String interventionId, @Nullable Date questionnaireAnswerId,
                                    @NonNull Date startTimestamp, @NonNull Date endTimestamp,
                                    boolean afterQuestionnaire, Float rating, String feedback) {
        this.interventionId = interventionId;
        this.questionnaireAnswerId = questionnaireAnswerId;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.afterQuestionnaire = afterQuestionnaire;
        this.rating = rating;
        this.feedback = feedback;
    }

    public InterventionRecordEntity cloneEntity() {
        return new InterventionRecordEntity(interventionId, questionnaireAnswerId == null ? null : new Date(questionnaireAnswerId.getTime()),
                new Date(startTimestamp.getTime()), new Date(endTimestamp.getTime()), afterQuestionnaire, rating, feedback);
    }

}
