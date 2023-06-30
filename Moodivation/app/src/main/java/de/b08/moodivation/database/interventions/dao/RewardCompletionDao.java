package de.b08.moodivation.database.interventions.dao;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;

import de.b08.moodivation.database.interventions.entities.RewardCompletionEntity;

@Dao
public interface RewardCompletionDao {

    @Insert
    void insert(RewardCompletionEntity entity);

    @Query("SELECT COUNT(*) FROM RewardCompletionEntity WHERE challenge = :challenge AND referenceDate = :referenceDate")
    int referenceDateExists(String challenge, long referenceDate);

    @Ignore
    default boolean cyclingReferenceDateExists(long referenceDate) {
        return referenceDateExists("Cycling", referenceDate) != 0;
    }

    @Ignore
    default boolean runningReferenceDateExists(long referenceDate) {
        return referenceDateExists("Running", referenceDate) != 0;
    }

    @Ignore
    default boolean stepsReferenceDateExists(long referenceDate) {
        return referenceDateExists("STEPS", referenceDate) != 0;
    }

    @Ignore
    default boolean streakReferenceDateExists(long referenceDate) {
        return referenceDateExists("STREAK", referenceDate) != 0;
    }

}
