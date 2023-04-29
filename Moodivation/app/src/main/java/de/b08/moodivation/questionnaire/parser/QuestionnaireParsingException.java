package de.b08.moodivation.questionnaire.parser;

import androidx.annotation.Nullable;

import org.w3c.dom.Node;

/**
 * 
 */
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
        NUMBER_FORMAT_EXCEPTION
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
