package de.b08.moodivation.questionnaire.constraints;

import java.util.List;
import java.util.Objects;

/**
 * 
 */
public class EnabledIfSelectedConstraint extends Constraint {

    private final List<String> allowedSelectionIds;

    public EnabledIfSelectedConstraint(String constrainedQuestionId, String observedQuestionId, List<String> allowedSelectionIds) {
        super(constrainedQuestionId, observedQuestionId);
        this.allowedSelectionIds = allowedSelectionIds;
    }

    public List<String> getAllowedSelectionIds() {
        return allowedSelectionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnabledIfSelectedConstraint)) return false;
        EnabledIfSelectedConstraint that = (EnabledIfSelectedConstraint) o;
        return Objects.equals(allowedSelectionIds, that.allowedSelectionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedSelectionIds);
    }
}
