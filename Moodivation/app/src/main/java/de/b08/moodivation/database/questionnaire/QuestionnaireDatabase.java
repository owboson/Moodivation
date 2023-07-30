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

import androidx.room.BuiltInTypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.b08.moodivation.database.questionnaire.dao.DigitSpanTaskResDao;
import de.b08.moodivation.database.questionnaire.dao.InterventionTriggeredAfterQuestionnaireDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionnaireDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionNotesDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionnaireNotesDao;
import de.b08.moodivation.database.questionnaire.entities.DigitSpanTaskResEntity;
import de.b08.moodivation.database.questionnaire.entities.InterventionTriggeredAfterQuestionnaireEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionNotesEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireEntity;
import de.b08.moodivation.database.questionnaire.dao.AnswerDao;
import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireNotesEntity;
import de.b08.moodivation.database.typeconverter.DateConverter;

@Database(entities = {AnswerEntity.class, QuestionEntity.class,
          QuestionnaireEntity.class, DigitSpanTaskResEntity.class, QuestionNotesEntity.class,
          QuestionnaireNotesEntity.class, InterventionTriggeredAfterQuestionnaireEntity.class},
          exportSchema = true,
          version = 2)
@TypeConverters(value = {DateConverter.class},
                builtInTypeConverters = @BuiltInTypeConverters(enums = BuiltInTypeConverters.State.ENABLED))
public abstract class QuestionnaireDatabase extends RoomDatabase {

    private static volatile QuestionnaireDatabase instance = null;

    public static QuestionnaireDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (QuestionnaireDatabase.class) {
                instance = Room.databaseBuilder(context, QuestionnaireDatabase.class,
                        "QuestionnaireDatabase").fallbackToDestructiveMigration()
                        .build();
                return instance;
            }
        }
        return instance;
    }

    public abstract QuestionnaireDao questionnaireDao();

    public abstract QuestionDao questionDao();

    public abstract AnswerDao answerDao();

    public abstract DigitSpanTaskResDao digitSpanTaskResDao();

    public abstract QuestionNotesDao questionNotesDao();

    public abstract QuestionnaireNotesDao questionnaireNotesDao();

    public abstract InterventionTriggeredAfterQuestionnaireDao interventionTriggeredAfterQuestionnaireDao();

}
