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

package de.b08.moodivation.questionnaire;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.b08.moodivation.questionnaire.constraints.Constraint;

public class Questionnaire {

    private final List<QuestionnaireElement> questionnaireElements;
    private final List<Constraint> constraints;
    private final String title;
    private final String id;

    public Questionnaire(String title, String id, List<QuestionnaireElement> questionnaireElements) {
        this(title, id, questionnaireElements, Collections.emptyList());
    }

    public Questionnaire(String title, String id, List<QuestionnaireElement> questionnaireElements,
                         List<Constraint> constraints) {
        this.title = title;
        this.questionnaireElements = questionnaireElements;
        this.id = id;
        this.constraints = constraints;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public List<QuestionnaireElement> getQuestionnaireElements() {
        return questionnaireElements;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Questionnaire)) return false;
        Questionnaire that = (Questionnaire) o;
        return Objects.equals(questionnaireElements, that.questionnaireElements) && Objects.equals(constraints, that.constraints)
                && Objects.equals(title, that.title) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionnaireElements, constraints, title, id);
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public List<Constraint> getConstraintsForObservedId(String observedId) {
        return constraints.stream().filter(c -> c.getObservedQuestionId().equals(observedId))
                .collect(Collectors.toList());
    }

}
