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

package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;

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
                    array.put(obj);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            answerString = array.toString();
        }

        return new AnswerEntity(answer.getQuestionnaireId(), answer.getQuestionId(), now, type, answerString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerEntity)) return false;
        AnswerEntity that = (AnswerEntity) o;
        return questionnaireId.equals(that.questionnaireId) && questionId.equals(that.questionId) && timestamp.equals(that.timestamp) && answer.equals(that.answer) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionnaireId, questionId, timestamp, answer, type);
    }
}
