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

package de.b08.moodivation.questionnaire.constraints;

import java.util.Objects;

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
