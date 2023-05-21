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
