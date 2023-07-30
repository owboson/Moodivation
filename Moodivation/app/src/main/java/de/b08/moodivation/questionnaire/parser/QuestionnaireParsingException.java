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

package de.b08.moodivation.questionnaire.parser;

import androidx.annotation.Nullable;

import org.w3c.dom.Node;

public class QuestionnaireParsingException extends Exception {

    public enum Reason {
        NO_TITLE,
        NO_ID,
        ID_NOT_UNIQUE,
        UNKNOWN_ELEMENT,
        MULTIPLE_TITLES,
        CHOICE_QUESTION_NO_DEFAULT_ITEM_WITH_ID,
        CHOICE_QUESTION_ITEM_NOT_ENCLOSED_IN_ITEMS_TAG,
        QUESTIONNAIRE_NOT_ROOT_ELEMENT,
        NO_ROOT_ELEMENT,
        TO_VALUE_LESS_THAN_OR_EQUAL_TO_FROM_VALUE,
        NUMBER_QUESTION_INVALID_TYPE,
        NUMBER_FORMAT_EXCEPTION,
        CONSTRAINT_NO_CONSTRAINED_ID,
        CONSTRAINT_NO_OBSERVED_ID
    }

    private final Node node;
    private final Reason reason;

    public QuestionnaireParsingException(Node node, Reason reason) {
        super();
        this.node = node;
        this.reason = reason;
    }

    public QuestionnaireParsingException(Node node, Reason reason, Throwable cause) {
        super(cause);
        this.node = node;
        this.reason = reason;
    }

    public Node getNode() {
        return node;
    }

    public Reason getReason() {
        return reason;
    }

    @Nullable
    @Override
    public String getMessage() {
        return "Reason: " + reason.name();
    }

}
