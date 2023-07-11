package de.b08.moodivation.database.location;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.b08.moodivation.database.location.entities.LocationHistoryEntity;

@RunWith(AndroidJUnit4.class)
public class LocationDatabaseTest {

    private static final Date t1 = new Date(1);
    private static final Date t2 = new Date(4);

    private final LocationHistoryEntity e1 = new LocationHistoryEntity(t1, 0,0,0,0,0,0,0,0,0,null);
    private final LocationHistoryEntity e2 = new LocationHistoryEntity(t2, 0,0,0,0,0,0,0,0,0,null);

    private LocationDatabase db;

    @Before
    public void setup() {
        Context c = ApplicationProvider.getApplicationContext();
        db = Room.databaseBuilder(c, LocationDatabase.class, "LocationDatabaseTest").fallbackToDestructiveMigration().build();

        // populate db
        db.locationHistoryDao().insertAll(Arrays.asList(e1, e2));
    }

    @Test
    public void testGetLocationsInTimeInterval() {
        List<LocationHistoryEntity> entities0To5 = db.locationHistoryDao().getLocationsInTimeInterval(0, 5);
        assertEquals(2, entities0To5.size());
        assertTrue(entities0To5.contains(e1));
        assertTrue(entities0To5.contains(e2));

        List<LocationHistoryEntity> entities2To5 = db.locationHistoryDao().getLocationsInTimeInterval(2, 5);
        assertEquals(1, entities2To5.size());
        assertFalse(entities2To5.contains(e1));
        assertTrue(entities2To5.contains(e2));

        List<LocationHistoryEntity> entities2To3 = db.locationHistoryDao().getLocationsInTimeInterval(2, 3);
        assertEquals(0, entities2To3.size());
    }

    @After
    public void clean() {
        db.clearAllTables();
        db.close();
    }

}
