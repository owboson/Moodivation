package de.b08.moodivation.database.interventions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import de.b08.moodivation.database.interventions.entities.RewardCompletionEntity;

@RunWith(AndroidJUnit4.class)
public class InterventionDatabaseTest {

    private static final Date t1 = new Date(1);
    private static final Date t2 = new Date(4);

    private InterventionDatabase db;

    @Before
    public void setup() {
        Context c = ApplicationProvider.getApplicationContext();
        db = Room.databaseBuilder(c, InterventionDatabase.class, "InterventionDatabaseTest").fallbackToDestructiveMigration().build();

        // populate db
        db.rewardCompletionDao().insert(RewardCompletionEntity.createRunningEntity(t1, new Date()));
        db.rewardCompletionDao().insert(RewardCompletionEntity.createStreakEntity(t2, new Date()));
    }

    @Test
    public void testGetLocationsInTimeInterval() {
        assertTrue(db.rewardCompletionDao().runningReferenceDateExists(t1.getTime()));
        assertFalse(db.rewardCompletionDao().runningReferenceDateExists(20000));
        assertTrue(db.rewardCompletionDao().streakReferenceDateExists(t2.getTime()));
        assertFalse(db.rewardCompletionDao().streakReferenceDateExists(20000));
        assertFalse(db.rewardCompletionDao().cyclingReferenceDateExists(20000));
        assertFalse(db.rewardCompletionDao().stepsReferenceDateExists(20000));
    }

    @After
    public void clean() {
        db.clearAllTables();
        db.close();
    }

}
