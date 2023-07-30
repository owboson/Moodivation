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

import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;

import de.b08.moodivation.questionnaire.Category;
import de.b08.moodivation.questionnaire.constraints.EnabledIfSelectedConstraint;
import de.b08.moodivation.questionnaire.question.ChoiceQuestion;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;
import de.b08.moodivation.questionnaire.question.FreeTextQuestion;
import de.b08.moodivation.questionnaire.Questionnaire;
import de.b08.moodivation.questionnaire.question.NumberQuestion;

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

        assertEquals(Arrays.asList(f1, f2, f3), questionnaire.getQuestionnaireElements());
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

        assertEquals(Collections.singletonList(s1), questionnaire.getQuestionnaireElements());
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

    @Test
    public void testCategory() throws QuestionnaireParsingException, IOException,
            ParserConfigurationException, SAXException {
        String questionnaireString ="<Questionnaire id=\"1\">\n" +
                "  <Category id=\"1\">\n" +
                "    <Title>X</Title>\n" +
                "  </Category>" +
                "  <NumberQuestion id=\"4\" stepSize=\"1\" fromValue=\"2\" toValue=\"3\" type=\"SLIDER\">\n" +
                "    <Title/>\n" +
                "  </NumberQuestion>\n" +
                "</Questionnaire>";

        Questionnaire questionnaire = QuestionnaireParser.parse(questionnaireString);
        Category category = new Category("X", "1");
        NumberQuestion numberQuestion = new NumberQuestion("", "4", NumberQuestion.Type.SLIDER, 2,3,1);
        assertEquals(Arrays.asList(category, numberQuestion), questionnaire.getQuestionnaireElements());
    }

    @Test
    public void testEnabledIfSelectedConstraint() throws QuestionnaireParsingException, IOException,
            ParserConfigurationException, SAXException {
        String questionnaireString ="<Questionnaire id=\"1\">\n" +
                "  <Constraints>\n" +
                "    <EnabledIfSelectedConstraint constrainedQuestionId=\"4\" observedQuestionId=\"5\">\n" +
                "      <AllowedItem>0</AllowedItem>\n" +
                "    </EnabledIfSelectedConstraint>\n" +
                "  </Constraints>\n" +
                "</Questionnaire>";

        Questionnaire questionnaire = QuestionnaireParser.parse(questionnaireString);

        EnabledIfSelectedConstraint constraint = new EnabledIfSelectedConstraint("4", "5", Collections.singletonList("0"));
        assertEquals(Collections.singletonList(constraint), questionnaire.getConstraints());
    }

}
