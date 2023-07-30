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

package de.b08.moodivation.database.questionnaire;

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

import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireEntity;

@RunWith(AndroidJUnit4.class)
public class QuestionnaireDatabaseTest {

    private static final String QUESTIONNAIRE_ID = "qn1";
    private static final String QUESTION_1_ID = "q1";
    private static final String QUESTION_2_ID = "q2";

    private QuestionnaireDatabase db;

    @Before
    public void setup() {
        Context c = ApplicationProvider.getApplicationContext();
        db = Room.databaseBuilder(c, QuestionnaireDatabase.class, "QuestionnaireDatabaseTest").build();

        // populate db
        QuestionnaireEntity qn = new QuestionnaireEntity(QUESTIONNAIRE_ID);
        QuestionEntity q1 = new QuestionEntity(QUESTIONNAIRE_ID, QUESTION_1_ID);
        QuestionEntity q2 = new QuestionEntity(QUESTIONNAIRE_ID, QUESTION_2_ID);
        AnswerEntity a1q1 = new AnswerEntity(QUESTIONNAIRE_ID, QUESTION_1_ID, new Date(0), AnswerEntity.DataType.STRING, "B");
        AnswerEntity a2q1 = new AnswerEntity(QUESTIONNAIRE_ID, QUESTION_1_ID, new Date(1), AnswerEntity.DataType.STRING,"C");

        db.questionnaireDao().insert(qn);
        db.questionDao().insert(q1);
        db.questionDao().insert(q2);
        db.answerDao().insert(a1q1);
        db.answerDao().insert(a2q1);
    }

    @Test
    public void testAnswerDao() {
        List<AnswerEntity> answersQ1 = db.answerDao().getAllAnswersForQuestion(QUESTIONNAIRE_ID, QUESTION_1_ID);

        assertEquals(2, answersQ1.size());
        assertEquals("B", answersQ1.get(0).answer);
        assertEquals("C", answersQ1.get(1).answer);

        List<AnswerEntity> answersQ2 = db.answerDao().getAllAnswersForQuestion(QUESTIONNAIRE_ID, QUESTION_2_ID);

        assertEquals(0, answersQ2.size());
    }

    @Test
    public void testQuestionDao() {
        List<String> questionIds = db.questionDao().getAllQuestionIdsForQuestionnaire(QUESTIONNAIRE_ID);
        assertEquals(Arrays.asList(QUESTION_1_ID, QUESTION_2_ID), questionIds);
    }

    @After
    public void clean() {
        db.clearAllTables();
        db.close();
    }

}
