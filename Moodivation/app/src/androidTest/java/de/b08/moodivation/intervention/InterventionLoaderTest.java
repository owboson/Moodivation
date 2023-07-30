/*
 * MIT License
 *
 * Copyright (c) 2023 RUB-SE-LAB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
