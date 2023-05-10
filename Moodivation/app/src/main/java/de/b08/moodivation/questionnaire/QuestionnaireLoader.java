package de.b08.moodivation.questionnaire;

import android.content.Context;
import android.os.AsyncTask;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.database.questionnaire.entities.QuestionEntity;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireEntity;
import de.b08.moodivation.questionnaire.parser.QuestionnaireParser;
import de.b08.moodivation.questionnaire.parser.QuestionnaireParsingException;

public class QuestionnaireLoader {

    public static Map<String, QuestionnaireBundle> loadFromAssets(Context context) throws Exception {
        String[] paths = context.getAssets().list("questionnaires/");
        Set<String> names = Arrays.stream(paths).map(s -> s.split("\\.")[0])
                .collect(Collectors.toSet());

        Map<String, QuestionnaireBundle> questionnaires = new HashMap<>();
        for (String name : names) {
            Map<String, Questionnaire> questionnaireMap = new HashMap<>();
            Arrays.stream(paths).filter(p -> p.startsWith(name + ".")).forEach(p -> {
                try {
                    InputStream in = context.getAssets().open("questionnaires/" + p);
                    Questionnaire q = QuestionnaireParser.parse(new InputSource(in));
                    String lang = p.replaceFirst(name + "\\.", "").split("\\.")[0];
                    questionnaireMap.put(lang, q);
                } catch (IOException | QuestionnaireParsingException |
                         ParserConfigurationException | SAXException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            });
            if (questionnaireMap.values().stream().map(q -> q.getId()).distinct().count() > 1)
                throw new Exception("multiple questionnaires with same id");

            if (!questionnaireMap.values().isEmpty()) {
                addToDatabase(questionnaireMap.values().stream().findFirst().get(), context);
                questionnaires.put(name, new QuestionnaireBundle(name, questionnaireMap));
            }
        }
        return questionnaires;
    }

    public static void addToDatabase(Questionnaire questionnaire, Context context) {
        QuestionnaireDatabase questionnaireDatabase = QuestionnaireDatabase.getInstance(context);
        AsyncTask.execute(() -> {
            if (questionnaireDatabase.questionnaireDao().getAllQuestionnaires().stream().noneMatch(q -> q.id.equals(questionnaire.getId()))) {
                questionnaireDatabase.questionnaireDao().insert(new QuestionnaireEntity(questionnaire.getId()));

                questionnaireDatabase.questionDao().insertAll(
                        questionnaire.getQuestionnaireElements().stream()
                                .filter(q -> !(q instanceof Category))
                                .map(q -> new QuestionEntity(questionnaire.getId(), q.getId()))
                                .collect(Collectors.toList())
                );
            }
        });
    }

}
