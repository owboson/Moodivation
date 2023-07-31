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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.b08.moodivation.ui.EditableCheckBox;
import de.b08.moodivation.ui.EditableRadioButton;
import de.b08.moodivation.questionnaire.answer.ChoiceAnswer;
import de.b08.moodivation.questionnaire.question.ChoiceQuestion;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;

public class ChoiceQuestionView extends QuestionView<ChoiceQuestion, ChoiceAnswer> {

    private final RadioGroup radioGroup;

    private final List<CheckBox> checkBoxes;
    private final List<RadioButton> radioButtons;

    public ChoiceQuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        radioGroup = new RadioGroup(context, attrs);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioGroup.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        getQuestionContentView().addView(radioGroup);

        checkBoxes = new ArrayList<>();
        radioButtons = new ArrayList<>();
    }

    @Override
    public void setQuestion(ChoiceQuestion choiceQuestion) {
        super.setQuestion(choiceQuestion);
        if (choiceQuestion == null)
            return;

        for (int i = 0; i < choiceQuestion.getItems().size(); i++) {
            if (choiceQuestion.isMultiSelectionAllowed())
                addCheckBox(choiceQuestion.getItems().get(i), i);
            else
                addRadioButton(choiceQuestion.getItems().get(i), i);
        }

        if (!choiceQuestion.isMultiSelectionAllowed())
            radioGroup.setOnCheckedChangeListener((a,b) -> notifyUpdateHandlers());
    }

    private void addCheckBox(ChoiceQuestionItem item, int index) {
        EditableCheckBox box = new EditableCheckBox(getContext());
        box.setText(item.getValue());
        box.setId(index);
        getQuestionContentView().addView(box);
        box.setEditable(item.isModifiable());
        checkBoxes.add(box);
        box.setOnCheckedChangeListener((a,b) -> notifyUpdateHandlers());
    }

    private void addRadioButton(ChoiceQuestionItem item, int index) {
        EditableRadioButton btn = new EditableRadioButton(getContext());
        btn.setText(item.getValue());
        btn.setId(index);
        btn.setEditable(item.isModifiable());
        radioGroup.addView(btn);
        radioButtons.add(btn);
    }

    @Override
    public ChoiceAnswer getAnswer() {
        if (!isEnabled()) {
            return new ChoiceAnswer(getQuestion().getId(), getQuestionnaireId(), Collections.emptyList());
        }

        if (getQuestion().isMultiSelectionAllowed()) {
            List<ChoiceQuestionItem> selectedItems = checkBoxes.stream().filter(CompoundButton::isChecked)
                    .map(b -> {
                        ChoiceQuestionItem item = getQuestion().getItems().get(checkBoxes.indexOf(b));
                        if (item.isModifiable()) {
                            return new ChoiceQuestionItem(b.getText().toString(), item.getId(), item.isModifiable());
                        } else {
                            return item;
                        }
                    })
                    .collect(Collectors.toList());

            return new ChoiceAnswer(getQuestion().getId(), getQuestionnaireId(), Collections.unmodifiableList(selectedItems));
        } else {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1)
                throw new RuntimeException();

            List<ChoiceQuestionItem> selectedItem = radioButtons.stream().filter(b -> b.getId() == selectedId)
                    .map(b -> {
                        ChoiceQuestionItem item = getQuestion().getItems().get(radioButtons.indexOf(b));
                        if (item.isModifiable()) {
                            return new ChoiceQuestionItem(b.getText().toString(), item.getId(), item.isModifiable());
                        } else {
                            return item;
                        }
                    })
                    .collect(Collectors.toList());

            return new ChoiceAnswer(getQuestion().getId(), getQuestionnaireId(), selectedItem);
        }
    }

    @Override
    public boolean isAnswered() {
        if (!isEnabled())
            return true;

        if (getQuestion().isMultiSelectionAllowed()) {
            return checkBoxes.stream().anyMatch(CompoundButton::isChecked);
        } else {
            return radioGroup.getCheckedRadioButtonId() != -1;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (radioGroup != null) {
            radioGroup.setEnabled(enabled);
            radioButtons.forEach(r -> r.setEnabled(enabled));
        } else {
            checkBoxes.forEach(b -> b.setEnabled(enabled));
        }
    }

    public List<String> getSelectedItemIds() {
        if (getQuestion().isMultiSelectionAllowed()) {
            return checkBoxes.stream().filter(CompoundButton::isChecked)
                    .map(c -> getQuestion().getItems().get(checkBoxes.indexOf(c)).getId())
                    .collect(Collectors.toList());
        } else {
            return radioButtons.stream()
                    .filter(RadioButton::isChecked)
                    .map(s -> getQuestion().getItems()
                    .get(radioButtons.indexOf(s)).getId())
                    .collect(Collectors.toList());
        }
    }

    public void selectItemWithId(String id, boolean selected) {
        ChoiceQuestionItem questionItem = getQuestion().getItems().stream().filter(c -> Objects.equals(c.getId(), id)).findAny().get();
        int index = getQuestion().getItems().indexOf(questionItem);

        if (getQuestion().isMultiSelectionAllowed()) {
            checkBoxes.get(index).setChecked(selected);
        } else {
            radioButtons.get(index).setChecked(selected);
        }
    }

}
