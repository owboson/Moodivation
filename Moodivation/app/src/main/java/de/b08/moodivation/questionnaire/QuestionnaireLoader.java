package de.b08.moodivation.questionnaire;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

    private static Map<String, QuestionnaireBundle> questionnaires;

    public static Map<String, QuestionnaireBundle> loadQuestionnaires(Context context) throws Exception {
        if (questionnaires == null) {
            questionnaires = loadFromAssets(context);
        }
        return questionnaires;
    }

    public static Map<String, QuestionnaireBundle> loadFromAssets(Context context) throws Exception {
        return loadFromAssets(context, true);
    }

    public static Map<String, QuestionnaireBundle> loadFromAssets(Context context, boolean storeInDatabase) throws Exception {
        String[] paths = context.getAssets().list("questionnaires/");
        Set<String> names = Arrays.stream(paths).map(s -> s.split("\\.")[0])
                .collect(Collectors.toSet());

        Map<String, QuestionnaireBundle> questionnaires = new HashMap<>();
        for (String name : names) {
            Map<String, Questionnaire> questionnaireMap = new HashMap<>();
            Arrays.stream(paths).filter(p -> p.startsWith(name + ".")).forEach(p -> {
                try {
                    InputStream in = context.getAssets().open("questionnaires/" + p);
                    String xmlString = IOUtils.toString(in, StandardCharsets.UTF_8);
                    xmlString = xmlString.replaceAll("<!--(?:.|[\\r\\t\\n])*-->", "");
                    Questionnaire q = QuestionnaireParser.parse(xmlString);
                    String lang = p.replaceFirst(name + "\\.", "").split("\\.")[0];
                    questionnaireMap.put(lang, q);
                } catch (IOException | QuestionnaireParsingException |
                         ParserConfigurationException | SAXException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            });
            if (questionnaireMap.values().stream().map(Questionnaire::getId).distinct().count() > 1)
                throw new Exception("multiple questionnaires with same id");

            if (!questionnaireMap.values().isEmpty()) {
                if (storeInDatabase)
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
