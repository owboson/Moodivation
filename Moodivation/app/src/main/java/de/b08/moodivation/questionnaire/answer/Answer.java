package de.b08.moodivation.questionnaire.answer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;

/**
 * 
 */
public class Answer<T> {

    private final T value;
    private final String questionId;
    private final String questionnaireId;

    public Answer(String questionId, String questionnaireId, T value) {
        this.value = value;
        this.questionId = questionId;
        this.questionnaireId = questionnaireId;
    }

    public T getValue() {
        return value;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public static Answer<?> from(AnswerEntity entity) {
        try {
            switch (entity.type) {
                case NUMBER:
                    return new NumberAnswer(entity.questionId, entity.questionnaireId, Float.parseFloat(entity.answer));
                case STRING:
                    return new TextAnswer(entity.questionId, entity.questionnaireId, entity.answer);
                case CHOICES:
                    JSONArray array = new JSONArray(entity.answer);
                    List<ChoiceQuestionItem> choices = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        choices.add(new ChoiceQuestionItem((String) object.get("value"), (String) object.get("id")));
                    }
                    return new ChoiceAnswer(entity.questionId, entity.questionnaireId, choices);
                default:
                    throw new IllegalArgumentException();
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
