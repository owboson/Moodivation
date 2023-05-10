package de.b08.moodivation.questionnaire.answer;

import java.util.List;

import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;

/**
 * 
 */
public class ChoiceAnswer extends Answer<List<ChoiceQuestionItem>> {

    public ChoiceAnswer(String questionId, String questionnaireId, List<ChoiceQuestionItem> value) {
        super(questionId, questionnaireId, value);
    }

}
