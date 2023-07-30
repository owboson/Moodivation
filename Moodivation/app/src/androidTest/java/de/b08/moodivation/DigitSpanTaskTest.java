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

package de.b08.moodivation;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;

@RunWith(AndroidJUnit4.class)
public class DigitSpanTaskTest {

    @Rule
    public ActivityScenarioRule<DigitSpanTask> activityScenarioRule = new ActivityScenarioRule<>(DigitSpanTask.class);

    QuestionnaireDatabase database;

    @Before
    public void init() {
        database = QuestionnaireDatabase.getInstance(InstrumentationRegistry.getInstrumentation().getContext());
        database.clearAllTables();
    }

    @Test
    public void testTask() {
        activityScenarioRule.getScenario().onActivity(activity -> activity.setDelayMillis(0));

        // start digit span task
        onView(withId(R.id.start)).perform(click());

        AtomicReference<List<Integer>> numbers = new AtomicReference<>();
        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        for (Integer i : numbers.get()) {
            onView(withId(getButtonIdForNumber(i))).perform(click());
        }

        onView(withId(R.id.start)).perform(click());

        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        for (Integer i : numbers.get()) {
            onView(withId(getButtonIdForNumber(i))).perform(click());
        }

        onView(withId(R.id.start)).perform(click());

        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        for (Integer i : numbers.get()) {
            onView(withId(getButtonIdForNumber(i))).perform(click());
        }

        onView(withId(R.id.start)).perform(click());

        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        for (Integer i : numbers.get()) {
            onView(withId(getButtonIdForNumber(i))).perform(click());
        }

        activityScenarioRule.getScenario().onActivity(activity -> {
            assertEquals(DigitSpanTask.INITIAL_UPPER_BOUND + 3, activity.getUserMax());
        });
    }

    @Test
    public void testTaskWrongNumber() {
        activityScenarioRule.getScenario().onActivity(activity -> activity.setDelayMillis(0));

        // start digit span task
        onView(withId(R.id.start)).perform(click());

        AtomicReference<List<Integer>> numbers = new AtomicReference<>();
        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        for (Integer i : numbers.get()) {
            onView(withId(getButtonIdForNumber(i))).perform(click());
        }

        onView(withId(R.id.start)).perform(click());

        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });

        onView(withId(getButtonIdForNumber(numberOtherThan(numbers.get().get(0))))).perform(click());

        AtomicInteger userMax = new AtomicInteger();
        activityScenarioRule.getScenario().onActivity(activity -> {
            userMax.set(activity.getUserMax());
            assertEquals(DigitSpanTask.INITIAL_UPPER_BOUND, activity.getUserMax());
        });

        onView(withId(R.id.start)).check(matches(isNotEnabled()));
        for (int i = 0; i < 10; i++) {
            onView(withId(getButtonIdForNumber(i))).check(matches(isNotEnabled()));
        }

        AtomicReference<String> resString = new AtomicReference<>();
        activityScenarioRule.getScenario().onActivity(activity -> {
            resString.set(activity.getResources().getString(R.string.digitSpanFinalRes).replace("%NUM%", "" + userMax.get()));
        });
        onView(withId(R.id.instructionField)).check(matches(withText(resString.get())));
    }

    @Test
    public void testStoreResultInDatabase() {
        // assert tables are empty
        database.clearAllTables();
        assertTrue(database.digitSpanTaskResDao().getAllResEntities().isEmpty());

        activityScenarioRule.getScenario().onActivity(activity -> activity.setDelayMillis(0));

        // start digit span task
        onView(withId(R.id.start)).perform(click());

        AtomicReference<List<Integer>> numbers = new AtomicReference<>();
        // click correct numbers
        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        for (Integer i : numbers.get()) {
            onView(withId(getButtonIdForNumber(i))).perform(click());
        }

        onView(withId(R.id.start)).perform(click());

        // click correct numbers
        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        for (Integer i : numbers.get()) {
            onView(withId(getButtonIdForNumber(i))).perform(click());
        }

        onView(withId(R.id.start)).perform(click());

        // click wrong number
        activityScenarioRule.getScenario().onActivity(activity -> {
            numbers.set(activity.getNumbers());
        });
        onView(withId(getButtonIdForNumber(numberOtherThan(numbers.get().get(0))))).perform(click());

        // get user result
        AtomicInteger userMax = new AtomicInteger();
        activityScenarioRule.getScenario().onActivity(activity -> {
            userMax.set(activity.getUserMax());
            assertEquals(DigitSpanTask.INITIAL_UPPER_BOUND + 1, activity.getUserMax());
        });

        // check start button and number buttons are disabled
        onView(withId(R.id.start)).check(matches(isNotEnabled()));
        for (int i = 0; i < 10; i++) {
            onView(withId(getButtonIdForNumber(i))).check(matches(isNotEnabled()));
        }

        // check result string is displayed
        AtomicReference<String> resString = new AtomicReference<>();
        activityScenarioRule.getScenario().onActivity(activity -> {
            resString.set(activity.getResources().getString(R.string.digitSpanFinalRes).replace("%NUM%", "" + userMax.get()));
        });
        onView(withId(R.id.instructionField)).check(matches(withText(resString.get())));

        // check everything is stored in the database
        assertEquals(1, database.digitSpanTaskResDao().getAllResEntities().size());
        assertEquals((Integer) (DigitSpanTask.INITIAL_UPPER_BOUND + 1), database.digitSpanTaskResDao().getAllResEntities().get(0).result);
    }

    private int numberOtherThan(int i) {
        return (i+1) % 10;
    }

    private int getButtonIdForNumber(int number) {
        switch (number) {
            case 0:
                return R.id.button_0;
            case 1:
                return R.id.button_1;
            case 2:
                return R.id.button_2;
            case 3:
                return R.id.button_3;
            case 4:
                return R.id.button_4;
            case 5:
                return R.id.button_5;
            case 6:
                return R.id.button_6;
            case 7:
                return R.id.button_7;
            case 8:
                return R.id.button_8;
            case 9:
                return R.id.button_9;
        }

        throw new IllegalArgumentException();
    }

}
