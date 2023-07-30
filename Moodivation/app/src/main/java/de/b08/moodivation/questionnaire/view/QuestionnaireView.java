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
import android.widget.LinearLayout;

import com.google.android.material.divider.MaterialDivider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.b08.moodivation.R;
import de.b08.moodivation.questionnaire.Note;
import de.b08.moodivation.questionnaire.Questionnaire;
import de.b08.moodivation.questionnaire.answer.Answer;
import de.b08.moodivation.questionnaire.constraints.Constraint;
import de.b08.moodivation.questionnaire.constraints.EnabledIfSelectedConstraint;
import de.b08.moodivation.questionnaire.question.ChoiceQuestion;
import de.b08.moodivation.questionnaire.question.FreeTextQuestion;
import de.b08.moodivation.questionnaire.question.NumberQuestion;
import de.b08.moodivation.questionnaire.QuestionnaireElement;

public class QuestionnaireView extends LinearLayout {

    private Questionnaire questionnaire;

    private final LinearLayout questionnaireLinearLayout;

    private final Map<String, QuestionView<?,?>> questionViewMap;

    private boolean answered = false;

    private final List<Runnable> updateHandlers;

    public QuestionnaireView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.questionnaire_view, this);

        questionnaireLinearLayout = findViewById(R.id.questionnaireLinearLayout);
        questionViewMap = new HashMap<>();

        updateHandlers = new ArrayList<>();
    }

    public void addUpdateHandler(Runnable r) {
        this.updateHandlers.add(r);
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;

        MaterialDivider lastDivider = null;
        for (QuestionnaireElement e : questionnaire.getQuestionnaireElements()) {
            QuestionView<?,?> questionView = null;
            if (e instanceof NumberQuestion) {
                NumberQuestionView q = new NumberQuestionView(getContext(), null);
                q.setQuestion((NumberQuestion) e);
                questionView = q;
            } else if (e instanceof FreeTextQuestion) {
                FreeTextQuestionView q = new FreeTextQuestionView(getContext(), null);
                q.setQuestion((FreeTextQuestion) e);
                questionView = q;
            } else if (e instanceof ChoiceQuestion) {
                ChoiceQuestionView q = new ChoiceQuestionView(getContext(), null);
                q.setQuestion((ChoiceQuestion) e);
                questionView = q;
            }

            if (questionView != null) {
                questionView.setQuestionnaireId(questionnaire.getId());
                questionnaireLinearLayout.addView(questionView);
                questionViewMap.put(e.getId(), questionView);

                lastDivider = new MaterialDivider(getContext());
                questionnaireLinearLayout.addView(lastDivider);

                questionView.registerUpdateHandler(view -> {
                    checkQuestionnaireAnswered();
                    checkConstraints(view.getQuestion().getId(), view);
                    updateHandlers.forEach(Runnable::run);
                });
            }
        }

        if (lastDivider != null)
            questionnaireLinearLayout.removeView(lastDivider);

        questionnaire.getConstraints().forEach(constraint -> {
            if (questionViewMap.containsKey(constraint.getObservedQuestionId()))
                checkConstraints(constraint.getObservedQuestionId(), questionViewMap.get(constraint.getObservedQuestionId()));
        });

        checkQuestionnaireAnswered();
        updateHandlers.forEach(Runnable::run);
    }

    private void checkQuestionnaireAnswered() {
        answered = questionViewMap.values().stream().allMatch(QuestionView::isAnswered);
    }

    private void checkConstraints(String observedId, QuestionView<?,?> view) {
        List<Constraint> affectedConstraints = questionnaire.getConstraintsForObservedId(observedId);

        if (view instanceof ChoiceQuestionView) {
            ChoiceQuestionView c = (ChoiceQuestionView) view;

            List<String> constrainedIds = affectedConstraints.stream()
                    .map(Constraint::getConstrainedQuestionId)
                    .distinct().collect(Collectors.toList());

            for (String constrainedId : constrainedIds) {
                if (!questionViewMap.containsKey(constrainedId))
                    continue;

                List<String> allowedSelections = affectedConstraints.stream()
                        .filter(a -> a.getConstrainedQuestionId().equals(constrainedId) && (a instanceof EnabledIfSelectedConstraint))
                        .flatMap(a -> ((EnabledIfSelectedConstraint) a).getAllowedSelectionIds().stream())
                        .collect(Collectors.toList());

                boolean anyAllowedSelected = c.getSelectedItemIds().stream().anyMatch(allowedSelections::contains);

                Objects.requireNonNull(questionViewMap.get(constrainedId)).setEnabled(anyAllowedSelected);
            }
        }
    }

    public boolean isAnswered() {
        return answered;
    }

    public List<Answer<?>> getAllAnswers() {
        if (!isAnswered())
            return Collections.emptyList();

        return questionViewMap.values().stream()
                .map(QuestionView::getAnswer)
                .collect(Collectors.toList());
    }

    public List<Note> getAllNotes() {
        if (!isAnswered())
            return Collections.emptyList();

        return questionViewMap.values().stream()
                .map(QuestionView::getNote)
                .filter(n -> n.getValue() != null)
                .collect(Collectors.toList());
    }

    public String getQuestionnaireId() {
        return questionnaire.getId();
    }

}
