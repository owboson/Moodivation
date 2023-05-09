package de.b08.moodivation.questionnaire;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.b08.moodivation.questionnaire.constraints.Constraint;

/**
 * 
 */
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

}
