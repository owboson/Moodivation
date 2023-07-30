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

package de.b08.moodivation.questionnaire;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.b08.moodivation.QuestionnaireActivity;
import de.b08.moodivation.R;
import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.questionnaire.answer.ChoiceAnswer;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;
import de.b08.moodivation.questionnaire.view.ChoiceQuestionView;
import de.b08.moodivation.questionnaire.view.NumberQuestionView;
import de.b08.moodivation.questionnaire.view.QuestionView;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;

import static org.junit.Assert.*;

import android.view.View;
import android.widget.RadioButton;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuestionnaireActivityTest {

    @Rule
    public ActivityScenarioRule<QuestionnaireActivity> activityScenarioRule = new ActivityScenarioRule<>(QuestionnaireActivity.class);

    QuestionnaireDatabase database;

    @Before
    public void init() {
        database = QuestionnaireDatabase.getInstance(InstrumentationRegistry.getInstrumentation().getContext());
        database.clearAllTables();
    }

    @Test
    public void testEnabledOnlyIfSelectedConstraint() {
        onView(withQuestionId("1.3")).perform(setSliderValueAction(50));
        onView(withId(R.id.saveBtn)).check(matches(isNotEnabled()));

        onView(withQuestionId("2.1")).perform(setChoiceAction("0", true));
        onView(withQuestionId("2.2")).check(matches(isEnabled()));
        onView(withQuestionId("2.3")).check(matches(isEnabled()));

        onView(withQuestionId("2.1")).perform(setChoiceAction("1", true));
        onView(withQuestionId("2.2")).check(matches(isNotEnabled()));
        onView(withQuestionId("2.3")).check(matches(isNotEnabled()));

        onView(withQuestionId("3.1")).perform(setChoiceAction("0", true));
        onView(withQuestionId("5.1")).perform(setChoiceAction("0", true));

        onView(withId(R.id.saveBtn)).check(matches(isEnabled()));
    }

    @Test
    public void testDatabase() {
        database.clearAllTables();

        float question13SliderValue = 60;
        String question13Note = "xyz";
        String questionnaireNote = "questionnaireNote";
        String question51OtherPlace = "secret";

        onView(withQuestionId("1.3")).perform(setSliderValueAction(question13SliderValue));

        onView(withQuestionId("2.1")).perform(setChoiceAction("1", true));
        onView(withQuestionId("2.2")).check(matches(isNotEnabled()));
        onView(withQuestionId("2.3")).check(matches(isNotEnabled()));

        onView(withQuestionId("3.1")).perform(setChoiceAction("0", true));

        // set custom answer to modifiable choice question item
        onView(withQuestionId("5.1")).perform(setChoiceAction("7", true));
        onView(allOf(withParent(withParent(withParent(withParent(withQuestionId("5.1"))))), radioButtonWithText("Other")))
                .perform(replaceRadioButtonText(question51OtherPlace));

        // add note to 1.3
        onView(allOf(withParent(withParent(withParent(withQuestionId("1.3")))), withId(R.id.addNoteButton))).perform(click());
        onView(withId(R.id.questionnaireNotesTextBox)).perform(typeText(question13Note));
        // click OK on dialog
        onView(withId(android.R.id.button1)).perform(click());
        // check note was stored
        onView(withQuestionId("1.3")).check(matches(hasNote(question13Note)));

        // add questionnaire note
        onView(withId(R.id.addNoteBtn)).perform(click());
        onView(withId(R.id.questionnaireNotesTextBox)).perform(typeText(questionnaireNote));
        onView(withId(android.R.id.button1)).perform(click());
        activityScenarioRule.getScenario().onActivity(activity -> assertEquals(questionnaireNote, activity.getNote()));

        // save
        onView(withId(R.id.saveBtn)).check(matches(isEnabled()));
        onView(withId(R.id.saveBtn)).perform(click());

        // assert question note was stored
        assertTrue(database.questionNotesDao().getAllNotesForQuestionnaire("1").stream()
                .anyMatch(e -> e.questionId.equals("1.3") && Objects.equals(e.notes, question13Note)));

        // assert questionnaire note was stored
        assertTrue(database.questionnaireNotesDao().getAllNotesForQuestionnaire("1").stream()
                .anyMatch(e -> e.questionnaireId.equals("1") && Objects.equals(e.notes, questionnaireNote)));

        assertEquals(1, database.answerDao().getAllDates().size());
        Date saveDate = database.answerDao().getAllDates().get(0);

        List<AnswerEntity> answerEntities = database.answerDao().getAllAnswerEntitiesWithTimestamp(saveDate.getTime());
        assertTrue(answerEntities.stream().allMatch(e -> {
            switch (e.questionId) {
                case "1.3":
                    return e.answer.equals(Float.toString(question13SliderValue));
                case "2.1":
                    return AnswerEntity.from(new ChoiceAnswer("2.1", "1", Collections.singletonList(new ChoiceQuestionItem("No", "1"))), saveDate).equals(e);
                case "3.1":
                    return AnswerEntity.from(new ChoiceAnswer("3.1", "1", Collections.singletonList(new ChoiceQuestionItem("Yes", "0"))), saveDate).equals(e);
                case "5.1":
                    return AnswerEntity.from(new ChoiceAnswer("5.1", "1", Collections.singletonList(new ChoiceQuestionItem(question51OtherPlace, "7"))), saveDate).equals(e);
            }
            return true;
        }));
    }

    public ViewAction setSliderValueAction(float value) {
        return new ViewAction() {
            @Override
            public String getDescription() {
                return "set slider value to " + value;
            }

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(NumberQuestionView.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                NumberQuestionView numberQuestionView = (NumberQuestionView) view;
                numberQuestionView.setValue(value);
            }
        };
    }

    public ViewAction setChoiceAction(String choice, boolean selected) {
        return new ViewAction() {
            @Override
            public String getDescription() {
                return "set choice value to " + choice;
            }

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(ChoiceQuestionView.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                ChoiceQuestionView choiceQuestionView = (ChoiceQuestionView) view;
                choiceQuestionView.selectItemWithId(choice, selected);
            }
        };
    }

    public BaseMatcher<View> withQuestionId(String id) {
        return new BaseMatcher<View>() {
            @Override
            public boolean matches(Object actual) {
                if (!(actual instanceof QuestionView<?, ?>))
                    return false;

                QuestionView<?,?> questionView = (QuestionView<?, ?>) actual;
                return Objects.equals(questionView.getQuestion().getId(), id);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("matches question views with id " + id);
            }
        };
    }

    public BaseMatcher<View> radioButtonWithText(String text) {
        return new BaseMatcher<View>() {
            @Override
            public boolean matches(Object actual) {
                if (!(actual instanceof RadioButton))
                    return false;

                RadioButton radioButton = (RadioButton) actual;
                return radioButton.getText().toString().equals(text);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("matches radio buttons with text " + text);
            }
        };
    }

    public ViewAction replaceRadioButtonText(String text) {
        return new ViewAction() {
            @Override
            public String getDescription() {
                return "type radio button text: " + text;
            }

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RadioButton.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                RadioButton radioButton = (RadioButton) view;
                radioButton.setText(text);
            }
        };
    }

    public BaseMatcher<View> hasNote(String note) {
        return new BaseMatcher<View>() {
            @Override
            public boolean matches(Object actual) {
                if (actual instanceof QuestionView<?, ?>)
                    return Objects.equals(((QuestionView<?, ?>) actual).getNote().getValue(), note);

                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("matches question views with note " + note);
            }
        };
    }

}
