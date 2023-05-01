package de.b08.moodivation.database.questionnaire;

import android.content.Context;

import androidx.room.BuiltInTypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.b08.moodivation.database.questionnaire.dao.QuestionDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionnaireDao;
import de.b08.moodivation.database.questionnaire.entities.QuestionEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireEntity;
import de.b08.moodivation.database.questionnaire.dao.AnswerDao;
import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.database.typeconverter.DateConverter;

@Database(entities = {AnswerEntity.class, QuestionEntity.class, QuestionnaireEntity.class},
          exportSchema = true,
          version = 1)
@TypeConverters(value = {DateConverter.class},
                builtInTypeConverters = @BuiltInTypeConverters(enums = BuiltInTypeConverters.State.ENABLED))
public abstract class QuestionnaireDatabase extends RoomDatabase {

    private static volatile QuestionnaireDatabase instance = null;

    public static QuestionnaireDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (QuestionnaireDatabase.class) {
                instance = Room.databaseBuilder(context, QuestionnaireDatabase.class,
                        "QuestionnaireDatabase").build();
                return instance;
            }
        }
        return instance;
    }

    public abstract QuestionnaireDao questionnaireDao();

    public abstract QuestionDao questionDao();

    public abstract AnswerDao answerDao();

}
