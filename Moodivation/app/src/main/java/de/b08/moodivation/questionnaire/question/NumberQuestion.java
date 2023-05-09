package de.b08.moodivation.questionnaire.question;

import java.util.Objects;

import de.b08.moodivation.questionnaire.QuestionnaireElement;

/**
 * 
 */
public class NumberQuestion extends QuestionnaireElement {

    public enum Type {
        /** Displayed using a slider */
        SLIDER,
        /** Displayed using a number box */
        BOX
    }

    private final float valueFrom;
    private final float valueTo;
    private final float stepSize;
    private final Type type;

    public NumberQuestion(String title, String id, Type type, float valueFrom, float valueTo, float stepSize) {
        super(title, id);
        this.type = type;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
        this.stepSize = stepSize;
    }

    /**
     *  0 => continuous <br>
     * >0 => discrete
     */
    public float getStepSize() {
        return stepSize;
    }

    public float getValueFrom() {
        return valueFrom;
    }

    public float getValueTo() {
        return valueTo;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumberQuestion)) return false;
        if (!super.equals(o)) return false;
        NumberQuestion that = (NumberQuestion) o;
        return Float.compare(that.valueFrom, valueFrom) == 0 && Float.compare(that.valueTo, valueTo) == 0
                && Float.compare(that.stepSize, stepSize) == 0 && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), valueFrom, valueTo, stepSize, type);
    }
}
