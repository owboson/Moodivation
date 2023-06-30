package de.b08.moodivation.database.interventions.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"challenge", "referenceDate"})
public class RewardCompletionEntity {

    @NonNull
    public String challenge;

    @NonNull
    public Date completionDate;

    @NonNull
    public Date referenceDate;

    public RewardCompletionEntity(@NonNull String challenge, @NonNull Date referenceDate, @NonNull Date completionDate) {
        this.challenge = challenge;
        this.referenceDate = referenceDate;
        this.completionDate = completionDate;
    }

    public static RewardCompletionEntity createStreakEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("STREAK", referenceDate, completionDate);
    }

    public static RewardCompletionEntity createStepsEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("STEPS", referenceDate, completionDate);
    }

    public static RewardCompletionEntity createRunningEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("Running", referenceDate, completionDate);
    }

    public static RewardCompletionEntity createCyclingEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("Cycling", referenceDate, completionDate);
    }

}
