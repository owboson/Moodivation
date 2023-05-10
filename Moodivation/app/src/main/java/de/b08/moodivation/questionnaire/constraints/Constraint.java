package de.b08.moodivation.questionnaire.constraints;

import java.util.Objects;

/**
 * 
 */
public class Constraint {

    private final String constrainedQuestionId;
    private final String observedQuestionId;

    public Constraint(String constrainedQuestionId, String observedQuestionId) {
        this.constrainedQuestionId = constrainedQuestionId;
        this.observedQuestionId =observedQuestionId;
    }

    public String getConstrainedQuestionId() {
        return constrainedQuestionId;
    }

    public String getObservedQuestionId() {
        return observedQuestionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constraint)) return false;
        Constraint that = (Constraint) o;
        return Objects.equals(constrainedQuestionId, that.constrainedQuestionId) && Objects.equals(observedQuestionId, that.observedQuestionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constrainedQuestionId, observedQuestionId);
    }
}
