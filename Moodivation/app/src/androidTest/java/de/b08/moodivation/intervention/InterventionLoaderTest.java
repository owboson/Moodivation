package de.b08.moodivation.intervention;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class InterventionLoaderTest {

    @Test
    public void testRunningIntervention() {
        List<InterventionBundle> interventionBundles = InterventionLoader.
                getAllInterventions(InstrumentationRegistry.getInstrumentation().getContext());

        Intervention runningEN = new Intervention("RunningIntervention_Internal", "Running",
                "How about going for a run?", null,
                Arrays.asList(Intervention.DataType.ELAPSED_TIME, Intervention.DataType.SPEED, Intervention.DataType.TRAVELLED_DISTANCE),
                new InterventionOptions(false, false));

        Intervention runningDE = new Intervention("RunningIntervention_Internal", "Laufen",
                "Wie wäre es mit einer Runde Laufen?", null,
                Arrays.asList(Intervention.DataType.ELAPSED_TIME, Intervention.DataType.SPEED, Intervention.DataType.TRAVELLED_DISTANCE),
                new InterventionOptions(false, false));

        assertEquals(1, interventionBundles.size());
        InterventionBundle interventionBundle = interventionBundles.get(0);
        assertEquals("RunningIntervention_Internal", interventionBundle.getId());
        assertEquals(2, interventionBundle.getInterventionMap().keySet().size());
        assertTrue(interventionBundle.getInterventionMap().containsValue(runningDE));
        assertTrue(interventionBundle.getInterventionMap().containsValue(runningEN));
    }

}
