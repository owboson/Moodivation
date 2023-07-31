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

package de.b08.moodivation.questionnaire.answer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;

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
