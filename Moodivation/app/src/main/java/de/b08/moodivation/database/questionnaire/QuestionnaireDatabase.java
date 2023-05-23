package de.b08.moodivation.database.questionnaire;

import android.content.Context;

import androidx.room.BuiltInTypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.b08.moodivation.database.questionnaire.dao.DigitSpanTaskResDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionnaireDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionNotesDao;
import de.b08.moodivation.database.questionnaire.dao.QuestionnaireNotesDao;
import de.b08.moodivation.database.questionnaire.entities.DigitSpanTaskResEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionNotesEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireEntity;
import de.b08.moodivation.database.questionnaire.dao.AnswerDao;
import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireNotesEntity;
import de.b08.moodivation.database.typeconverter.DateConverter;

@Database(entities = {AnswerEntity.class, QuestionEntity.class,
          QuestionnaireEntity.class, DigitSpanTaskResEntity.class, QuestionNotesEntity.class,
          QuestionnaireNotesEntity.class},
          exportSchema = true,
          version = 3)
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

    public abstract DigitSpanTaskResDao digitSpanTaskResDao();

    public abstract QuestionNotesDao questionNotesDao();

    public abstract QuestionnaireNotesDao questionnaireNotesDao();

}
