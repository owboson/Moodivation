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

package de.b08.moodivation.questionnaire.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import androidx.annotation.Nullable;

import com.google.android.material.slider.Slider;

import de.b08.moodivation.questionnaire.answer.NumberAnswer;
import de.b08.moodivation.questionnaire.question.NumberQuestion;

public class NumberQuestionView extends QuestionView<NumberQuestion, NumberAnswer> {

    private Slider slider;
    private EditText numberBox;

    public NumberQuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setQuestion(NumberQuestion question) {
        super.setQuestion(question);
        if (question == null)
            return;

        if (question.getType() == NumberQuestion.Type.SLIDER) {
            slider = new Slider(getContext());
            slider.setValueFrom(question.getValueFrom());
            slider.setValueTo(question.getValueTo());
            slider.setStepSize(question.getStepSize());
            slider.setValue(question.getValueFrom());
            getQuestionContentView().addView(slider);
            slider.addOnChangeListener((slider1, value, fromUser) -> notifyUpdateHandlers());
        } else {
            numberBox = new EditText(getContext());
            numberBox.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            getQuestionContentView().addView(numberBox);
            numberBox.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus)
                    notifyUpdateHandlers();
            });
        }
    }

    @Override
    public NumberAnswer getAnswer() {
        float returnVal = getQuestion().getType() == NumberQuestion.Type.SLIDER ? slider.getValue()
                : Float.parseFloat(numberBox.getText().toString());

        // TODO: return value for invalid case
        return new NumberAnswer(getQuestion().getId(), getQuestionnaireId(), isEnabled() ? returnVal : getQuestion().getValueFrom() - 1);
    }

    @Override
    public boolean isAnswered() {
        if (!isEnabled())
            return true;

        if (getQuestion().getType() == NumberQuestion.Type.SLIDER) {
            return true;
        } else {
            try {
                float f = Float.parseFloat(numberBox.getText().toString());
                return f <= getQuestion().getValueTo() && f >= getQuestion().getValueFrom();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (slider != null) {
            slider.setEnabled(enabled);
        } else if (numberBox != null){
            numberBox.setEnabled(enabled);
        }
    }

    public void setValue(float value) {
        if (slider != null)
            slider.setValue(value);
        else if (numberBox != null)
            numberBox.setText(Float.toString(value));
    }

}
