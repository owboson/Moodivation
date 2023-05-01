package de.b08.moodivation.questionnaire.parser;

import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;

import de.b08.moodivation.questionnaire.question.ChoiceQuestion;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;
import de.b08.moodivation.questionnaire.question.FreeTextQuestion;
import de.b08.moodivation.questionnaire.Questionnaire;
import de.b08.moodivation.questionnaire.question.NumberQuestion;

/**
 * 
 */
public class QuestionnaireParserTest {

    @Test
    public void testChoiceQuestion() throws QuestionnaireParsingException,
            IOException, ParserConfigurationException, SAXException {
        String questionnaireString ="<Questionnaire id=\"0\">\n" +
                                    "  <Title>ABC</Title>\n" +
                                    "  <ChoiceQuestion id=\"1\">\n" +
                                    "    <Title>XYZ</Title>\n" +
                                    "    <Items>\n" +
                                    "      <Item id=\"1\">X</Item>\n" +
                                    "      <Item id=\"2\">Y</Item>\n" +
                                    "    </Items>\n" +
                                    "  </ChoiceQuestion>\n" +
                                    "</Questionnaire>";

        Questionnaire questionnaire = QuestionnaireParser.parse(questionnaireString);

        ChoiceQuestionItem i1 = new ChoiceQuestionItem("X", "1");
        ChoiceQuestionItem i2 = new ChoiceQuestionItem("Y", "2");
        ChoiceQuestion expectedChoiceQuestion = new ChoiceQuestion("XYZ", "1", Arrays.asList(i1, i2),
                false, null);
        Questionnaire expectedQuestionnaire = new Questionnaire("ABC", "0", Collections.singletonList(expectedChoiceQuestion));

        assertEquals(expectedQuestionnaire, questionnaire);
    }

    @Test
    public void testChoiceQuestionWithDefaultItem() throws QuestionnaireParsingException,
            IOException, ParserConfigurationException, SAXException {
        String questionnaireString ="<Questionnaire id=\"0\">\n" +
                                    "  <Title/>\n" +
                                    "  <ChoiceQuestion defaultItemId=\"2\" id=\"1\">\n" +
                                    "    <Title>XYZ</Title>\n" +
                                    "    <Items>\n" +
                                    "      <Item id=\"1\">X</Item>\n" +
                                    "      <Item id=\"2\">Y</Item>\n" +
                                    "    </Items>\n" +
                                    "    <Items>\n" +
                                    "      <Item id=\"3\">X</Item>\n" +
                                    "      <Item id=\"4\">Y</Item>\n" +
                                    "    </Items>\n" +
                                    "  </ChoiceQuestion>\n" +
                                    "</Questionnaire>";

        Questionnaire questionnaire = QuestionnaireParser.parse(questionnaireString);

        ChoiceQuestionItem i1 = new ChoiceQuestionItem("X", "1");
        ChoiceQuestionItem i2 = new ChoiceQuestionItem("Y", "2");
        ChoiceQuestionItem i3 = new ChoiceQuestionItem("X", "3");
        ChoiceQuestionItem i4 = new ChoiceQuestionItem("Y", "4");

        ChoiceQuestion expectedChoiceQuestion = new ChoiceQuestion("XYZ", "1", Arrays.asList(i1, i2, i3, i4),
                false, "2");
        Questionnaire expectedQuestionnaire = new Questionnaire("", "0", Collections.singletonList(expectedChoiceQuestion));

        assertEquals(expectedQuestionnaire, questionnaire);
    }

    @Test
    public void testChoiceQuestionItemIdNotUnique() {
        String questionnaireString ="<Questionnaire id=\"0\">\n" +
                                    "  <Title>ABC</Title>\n" +
                                    "  <ChoiceQuestion id=\"1\">\n" +
                                    "    <Title>XYZ</Title>\n" +
                                    "    <Items>\n" +
                                    "      <Item id=\"1\">X</Item>\n" +
                                    "      <Item id=\"1\">Y</Item>\n" +
                                    "    </Items>\n" +
                                    "  </ChoiceQuestion>\n" +
                                    "</Questionnaire>";

        QuestionnaireParsingException ex = assertThrows(QuestionnaireParsingException.class,
                () -> QuestionnaireParser.parse(questionnaireString));
        assertEquals(QuestionnaireParsingException.Reason.ID_NOT_UNIQUE, ex.getReason());
        assertEquals("Y", ex.getNode().getTextContent());
    }

    @Test
    public void testChoiceQuestionNoDefaultItemWithId() {
        String questionnaireString ="<Questionnaire id=\"0\">\n" +
                                    "  <Title>ABC</Title>\n" +
                                    "  <ChoiceQuestion defaultItemId=\"10\" id=\"1\">\n" +
                                    "    <Title>XYZ</Title>\n" +
                                    "    <Items>\n" +
                                    "      <Item id=\"1\">X</Item>\n" +
                                    "      <Item id=\"2\">Y</Item>\n" +
                                    "    </Items>\n" +
                                    "  </ChoiceQuestion>\n" +
                                    "</Questionnaire>";

        QuestionnaireParsingException ex = assertThrows(QuestionnaireParsingException.class,
                () -> QuestionnaireParser.parse(questionnaireString));
        assertEquals(QuestionnaireParsingException.Reason.CHOICE_QUESTION_NO_DEFAULT_ITEM_WITH_ID, ex.getReason());
        assertEquals("ChoiceQuestion", ((Element)ex.getNode()).getTagName());
    }

    @Test
    public void testQuestionnaireNoId() {
        String questionnaireString = "<Questionnaire></Questionnaire>";

        QuestionnaireParsingException ex = assertThrows(QuestionnaireParsingException.class,
                () -> QuestionnaireParser.parse(questionnaireString));
        assertEquals(QuestionnaireParsingException.Reason.NO_ID, ex.getReason());
        assertEquals("Questionnaire", ((Element)ex.getNode()).getTagName());
    }

    @Test
    public void testQuestionnaireNoTitle() throws QuestionnaireParsingException, IOException,
            ParserConfigurationException, SAXException {
        String questionnaireString = "<Questionnaire id=\"1\"></Questionnaire>";

        Questionnaire questionnaire = QuestionnaireParser.parse(questionnaireString);
        assertEquals(Optional.empty(), questionnaire.getTitle());
    }

    @Test
    public void testFreeTextQuestion() throws QuestionnaireParsingException, IOException,
            ParserConfigurationException, SAXException {
        String questionnaireString ="<Questionnaire id=\"1\">\n" +
                                    "  <FreeTextQuestion id=\"1\" multiLineAllowed=\"true\">\n" +
                                    "    <Title/>\n" +
                                    "  </FreeTextQuestion>\n" +
                                    "  <FreeTextQuestion id=\"2\" multiLineAllowed=\"false\">\n" +
                                    "    <Title/>\n" +
                                    "  </FreeTextQuestion>\n" +
                                    "  <FreeTextQuestion id=\"3\">\n" +
                                    "    <Title/>\n" +
                                    "  </FreeTextQuestion>\n" +
                                    "</Questionnaire>";

        Questionnaire questionnaire = QuestionnaireParser.parse(questionnaireString);

        FreeTextQuestion f1 = new FreeTextQuestion("", "1", true);
        FreeTextQuestion f2 = new FreeTextQuestion("", "2", false);
        FreeTextQuestion f3 = new FreeTextQuestion("", "3", false);

        assertEquals(Arrays.asList(f1, f2, f3), questionnaire.getQuestions());
    }

    @Test
    public void testNumberQuestion() throws QuestionnaireParsingException, IOException,
            ParserConfigurationException, SAXException {
        String questionnaireString ="<Questionnaire id=\"1\">\n" +
                                    "  <NumberQuestion id=\"4\" stepSize=\"1\" fromValue=\"2\" toValue=\"3\" type=\"SLIDER\">\n" +
                                    "    <Title/>\n" +
                                    "  </NumberQuestion>\n" +
                                    "</Questionnaire>";

        Questionnaire questionnaire = QuestionnaireParser.parse(questionnaireString);

        NumberQuestion s1 = new NumberQuestion("", "4", NumberQuestion.Type.SLIDER,2, 3, 1);

        assertEquals(Collections.singletonList(s1), questionnaire.getQuestions());
    }

    @Test
    public void testNumberQuestionWithTextAsFromValue() {
        String questionnaireString ="<Questionnaire id=\"1\">\n" +
                "  <NumberQuestion id=\"4\" stepSize=\"1\" fromValue=\"ABC\" toValue=\"1\">\n" +
                "    <Title/>\n" +
                "  </NumberQuestion>\n" +
                "</Questionnaire>";

        QuestionnaireParsingException ex = assertThrows(QuestionnaireParsingException.class,
                () -> QuestionnaireParser.parse(questionnaireString));
        assertEquals(QuestionnaireParsingException.Reason.NUMBER_FORMAT_EXCEPTION, ex.getReason());
        assertEquals("ABC", ex.getNode().getAttributes().getNamedItem("fromValue").getTextContent());
    }

    @Test
    public void testNumberQuestionToValueLessThenFromValue() {
        String questionnaireString ="<Questionnaire id=\"1\">\n" +
                "  <NumberQuestion id=\"4\" stepSize=\"1\" fromValue=\"2\" toValue=\"1\">\n" +
                "    <Title/>\n" +
                "  </NumberQuestion>\n" +
                "</Questionnaire>";

        QuestionnaireParsingException ex = assertThrows(QuestionnaireParsingException.class,
                () -> QuestionnaireParser.parse(questionnaireString));
        assertEquals(QuestionnaireParsingException.Reason.TO_VALUE_LESS_THAN_OR_EQUAL_TO_FROM_VALUE, ex.getReason());
        assertEquals("4", ex.getNode().getAttributes().getNamedItem("id").getTextContent());
    }

    @Test
    public void testNumberQuestionWithInvalidType() {
        String questionnaireString ="<Questionnaire id=\"1\">\n" +
                "  <NumberQuestion id=\"4\" stepSize=\"1\" fromValue=\"2\" toValue=\"3\" type=\"A\">\n" +
                "    <Title/>\n" +
                "  </NumberQuestion>\n" +
                "</Questionnaire>";

        QuestionnaireParsingException ex = assertThrows(QuestionnaireParsingException.class,
                () -> QuestionnaireParser.parse(questionnaireString));
        assertEquals(QuestionnaireParsingException.Reason.NUMBER_QUESTION_INVALID_TYPE, ex.getReason());
        assertEquals("A", ex.getNode().getAttributes().getNamedItem("type").getTextContent());
    }

}
