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

package de.b08.moodivation.questionnaire.question;

import java.util.Objects;

import de.b08.moodivation.questionnaire.QuestionnaireElement;

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
