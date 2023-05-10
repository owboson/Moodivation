package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import de.b08.moodivation.questionnaire.answer.Answer;
import de.b08.moodivation.questionnaire.answer.ChoiceAnswer;
import de.b08.moodivation.questionnaire.answer.NumberAnswer;
import de.b08.moodivation.questionnaire.answer.TextAnswer;

@Entity(primaryKeys = {"questionnaireId", "questionId", "timestamp"}/*,
        foreignKeys = {@ForeignKey(entity = QuestionnaireEntity.class, parentColumns = {"id"}, childColumns = {"questionnaireId"})}*/)
public class AnswerEntity {

    public enum DataType {
        STRING, NUMBER, CHOICES
    }

    @NonNull
    public String questionnaireId;

    @NonNull
    public String questionId;

    @NonNull
    public Date timestamp;

    @NonNull
    public String answer;

    @NonNull
    public DataType type;

    public AnswerEntity(@NonNull String questionnaireId, @NonNull String questionId, @NonNull Date timestamp,
                        @NonNull DataType type, @NonNull String answer) {
        this.questionnaireId = questionnaireId;
        this.questionId = questionId;
        this.timestamp = timestamp;
        this.type = type;
        this.answer = answer;
    }

    public static AnswerEntity from(Answer<?> answer, Date now) {
        AnswerEntity.DataType type;
        String answerString;
        if (answer instanceof NumberAnswer) {
            type = AnswerEntity.DataType.NUMBER;
            answerString = Float.toString(((NumberAnswer) answer).getValue());
        } else if (answer instanceof TextAnswer) {
            type = AnswerEntity.DataType.STRING;
            answerString = ((TextAnswer) answer).getValue();
        } else {
            ChoiceAnswer c = (ChoiceAnswer) answer;
            type = AnswerEntity.DataType.CHOICES;

            JSONArray array = new JSONArray();
            c.getValue().forEach(choiceQuestionItem -> {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", choiceQuestionItem.getId());
                    obj.put("value", choiceQuestionItem.getValue());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            answerString = array.toString();
        }

        return new AnswerEntity(answer.getQuestionnaireId(), answer.getQuestionId(), now, type, answerString);
    }

}
