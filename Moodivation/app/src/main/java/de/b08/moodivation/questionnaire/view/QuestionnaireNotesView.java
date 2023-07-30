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
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import de.b08.moodivation.R;

public class QuestionnaireNotesView extends LinearLayout {

    private final EditText questionnaireNotesTextBox;
    private final TextView questionnaireNotesTitle;

    public QuestionnaireNotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.questionnaire_notes_view, this);
        questionnaireNotesTextBox = findViewById(R.id.questionnaireNotesTextBox);
        questionnaireNotesTitle = findViewById(R.id.questionnaireNotesTitle);
        questionnaireNotesTextBox.setHint(R.string.questionnaireNoteViewEditTextHint);
    }

    public void setTitle(@StringRes int resId) {
        questionnaireNotesTitle.setText(resId);
    }

    public void setTitle(String title) {
        questionnaireNotesTitle.setText(title);
    }

    public void setTitleVisible(boolean visible) {
        questionnaireNotesTitle.setVisibility(visible ? VISIBLE : GONE);
        questionnaireNotesTitle.setEnabled(visible);
    }

    public void setText(String text) {
        questionnaireNotesTextBox.setText(text);
    }

    public Editable getText() {
        return questionnaireNotesTextBox.getText();
    }

    public EditText getQuestionnaireNotesTextBox() {
        return questionnaireNotesTextBox;
    }
}
