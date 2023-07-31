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
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

import de.b08.moodivation.R;
import de.b08.moodivation.questionnaire.answer.TextAnswer;
import de.b08.moodivation.questionnaire.question.FreeTextQuestion;

public class FreeTextQuestionView extends QuestionView<FreeTextQuestion, TextAnswer> {

    private final EditText answerTextField;

    public FreeTextQuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.free_text_question_view, getQuestionContentView());
        answerTextField = findViewById(R.id.freeTextQuestionAnswerTextField);
        answerTextField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                notifyUpdateHandlers();
        });
    }

    @Override
    public TextAnswer getAnswer() {
        return new TextAnswer(getQuestion().getId(), getQuestionnaireId(),
                isEnabled() ? answerTextField.getText().toString() : "");
    }

    @Override
    public boolean isAnswered() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (answerTextField != null)
            answerTextField.setEnabled(enabled);
    }

}
