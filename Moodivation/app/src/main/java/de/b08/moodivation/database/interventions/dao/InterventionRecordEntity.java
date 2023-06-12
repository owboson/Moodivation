package de.b08.moodivation.database.interventions.dao;

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

    public InterventionRecordEntity(@NonNull String interventionId, @Nullable Date questionnaireAnswerId,
                                    @NonNull Date startTimestamp, @NonNull Date endTimestamp,
                                    boolean afterQuestionnaire) {
        this.interventionId = interventionId;
        this.questionnaireAnswerId = questionnaireAnswerId;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.afterQuestionnaire = afterQuestionnaire;
    }

}
