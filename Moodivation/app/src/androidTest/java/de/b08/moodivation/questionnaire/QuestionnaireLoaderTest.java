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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import de.b08.moodivation.questionnaire.question.ChoiceQuestion;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;

@RunWith(AndroidJUnit4.class)
public class QuestionnaireLoaderTest {

    @Test
    public void testQuestionnaire() throws Exception {
        Map<String, QuestionnaireBundle> questionnaireBundleMap =
                QuestionnaireLoader.loadFromAssets(InstrumentationRegistry.getInstrumentation().getContext());

        assertTrue(questionnaireBundleMap.containsKey("test"));

        ChoiceQuestionItem i1 = new ChoiceQuestionItem("X", "1");
        ChoiceQuestionItem i2 = new ChoiceQuestionItem("Y", "2");
        ChoiceQuestionItem i3 = new ChoiceQuestionItem("X", "3");
        ChoiceQuestionItem i4 = new ChoiceQuestionItem("Y", "4");

        ChoiceQuestion expectedChoiceQuestionDE = new ChoiceQuestion("XYZ_DE", "1", Arrays.asList(i1, i2, i3, i4),
                false, "2");
        Questionnaire expectedQuestionnaireDE = new Questionnaire("", "0", Collections.singletonList(expectedChoiceQuestionDE));

        ChoiceQuestion expectedChoiceQuestionEN = new ChoiceQuestion("XYZ_EN", "1", Arrays.asList(i1, i2, i3, i4),
                false, "2");
        Questionnaire expectedQuestionnaireEN = new Questionnaire("", "0", Collections.singletonList(expectedChoiceQuestionEN));

        QuestionnaireBundle questionnaireBundle = questionnaireBundleMap.get("test");

        assertNotNull(questionnaireBundle);
        assertEquals(new HashSet<>(Arrays.asList("en", "de")), questionnaireBundle.getLanguages());
        assertEquals(expectedQuestionnaireEN, questionnaireBundle.getQuestionnaire("en"));
        assertEquals(expectedQuestionnaireDE, questionnaireBundle.getQuestionnaire("de"));
    }

    @Test
    public void testQuestionnaireWithXMLComment() throws Exception {
        Map<String, QuestionnaireBundle> questionnaireBundleMap =
                QuestionnaireLoader.loadFromAssets(InstrumentationRegistry.getInstrumentation().getContext());

        assertTrue(questionnaireBundleMap.containsKey("testComment"));

        ChoiceQuestionItem i1 = new ChoiceQuestionItem("X", "1");
        ChoiceQuestionItem i2 = new ChoiceQuestionItem("Y", "2");
        ChoiceQuestionItem i3 = new ChoiceQuestionItem("X", "3");
        ChoiceQuestionItem i4 = new ChoiceQuestionItem("Y", "4");

        ChoiceQuestion expectedChoiceQuestionDE = new ChoiceQuestion("XYZ_DE", "1", Arrays.asList(i1, i2, i3, i4),
                false, "2");
        Questionnaire expectedQuestionnaireDE = new Questionnaire("", "1", Collections.singletonList(expectedChoiceQuestionDE));

        ChoiceQuestion expectedChoiceQuestionEN = new ChoiceQuestion("XYZ_EN", "1", Arrays.asList(i1, i2, i3, i4),
                false, "2");
        Questionnaire expectedQuestionnaireEN = new Questionnaire("", "1", Collections.singletonList(expectedChoiceQuestionEN));

        QuestionnaireBundle questionnaireBundle = questionnaireBundleMap.get("testComment");

        assertNotNull(questionnaireBundle);
        assertEquals(new HashSet<>(Arrays.asList("en", "de")), questionnaireBundle.getLanguages());
        assertEquals(expectedQuestionnaireEN, questionnaireBundle.getQuestionnaire("en"));
        assertEquals(expectedQuestionnaireDE, questionnaireBundle.getQuestionnaire("de"));
    }

}
