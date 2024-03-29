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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static de.b08.moodivation.questionnaire.parser.QuestionnaireParsingException.Reason.*;

import de.b08.moodivation.questionnaire.constraints.Constraint;
import de.b08.moodivation.questionnaire.constraints.EnabledIfSelectedConstraint;
import de.b08.moodivation.questionnaire.Category;
import de.b08.moodivation.questionnaire.question.ChoiceQuestion;
import de.b08.moodivation.questionnaire.question.ChoiceQuestionItem;
import de.b08.moodivation.questionnaire.question.FreeTextQuestion;
import de.b08.moodivation.questionnaire.QuestionnaireElement;
import de.b08.moodivation.questionnaire.Questionnaire;
import de.b08.moodivation.questionnaire.question.NumberQuestion;

/**
 * Parser for questionnaire files
 */
@SuppressWarnings("IOStreamConstructor")
public class QuestionnaireParser {

    private static final String QUESTIONNAIRE_TAG_NAME = "Questionnaire";
    private static final String ID_ATTR = "id";
    private static final String TITLE_TAG_NAME = "Title";
    private static final String FREE_TEXT_QUESTION_TAG_NAME = "FreeTextQuestion";
    private static final String FREE_TEXT_QUESTION_MULTI_LINE_ALLOWED_ATTR = "multiLineAllowed";
    private static final String CHOICE_ITEM_TAG_NAME = "Item";
    private static final String CHOICE_ITEMS_TAG_NAME = "Items";
    private static final String CHOICE_QUESTION_DEFAULT_ITEM_ID_ATTR = "defaultItemId";
    private static final String CHOICE_QUESTION_TAG_NAME = "ChoiceQuestion";
    private static final String CHOICE_ITEM_IS_MODIFIABLE_ATTR = "isModifiable";
    private static final String CHOICE_QUESTION_ALLOWS_MULTI_SELECTION_ATTR = "multiSelectionAllowed";
    private static final String NUMBER_QUESTION_TAG_NAME = "NumberQuestion";
    private static final String NUMBER_QUESTION_STEP_SIZE_ATTR = "stepSize";
    private static final String NUMBER_QUESTION_FROM_VALUE_ATTR = "fromValue";
    private static final String NUMBER_QUESTION_TO_VALUE_ATTR = "toValue";
    private static final String NUMBER_QUESTION_TYPE_ATTR = "type";
    private static final String CATEGORY_TAG_NAME = "Category";
    private static final String CONSTRAINTS_TAG_NAME = "Constraints";
    private static final String ENABLED_IF_SELECTED_CONSTRAINT_TAG_NAME = "EnabledIfSelectedConstraint";
    private static final String CONSTRAINED_QUESTION_ID_ATTR = "constrainedQuestionId";
    private static final String OBSERVED_QUESTION_ID_ATTR = "observedQuestionId";
    private static final String ALLOWED_SELECTION_ITEM_TAG_NAME = "AllowedItem";

    /**
     * Parses a questionnaire represented as a string
     * @param s the questionnaire as a string
     * @return Returns the questionnaire element
     */
    public static Questionnaire parse(String s) throws QuestionnaireParsingException,
            IOException, ParserConfigurationException, SAXException {

        StringReader stringReader = new StringReader(s);
        InputSource source = new InputSource(stringReader);
        return parse(source);
    }

    /**
     * Parses a questionnaire from an input source
     * @param source the questionnaire as a source
     * @return Returns the questionnaire element
     */
    public static Questionnaire parse(InputSource source) throws QuestionnaireParsingException,
            IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(source);

        return parse(document);
    }

    /**
     * Parses a questionnaire in a file
     * @param f the questionnaire file
     * @return Returns the questionnaire element
     */
    public static Questionnaire parse(File f) throws IOException,
            QuestionnaireParsingException, ParserConfigurationException, SAXException {

        return parse(new InputSource(new FileInputStream(f)));
    }

    private static Questionnaire parse(Document document) throws QuestionnaireParsingException {
        if (document.getChildNodes().getLength() != 1)
            throw new QuestionnaireParsingException(document, NO_ROOT_ELEMENT);

        Node firstChildNode = document.getFirstChild();
        if (firstChildNode.getNodeType() != Node.ELEMENT_NODE)
            throw new QuestionnaireParsingException(document, QUESTIONNAIRE_NOT_ROOT_ELEMENT);

        Element el = (Element) firstChildNode;

        if (!el.getTagName().equalsIgnoreCase(QUESTIONNAIRE_TAG_NAME)) {
            throw new QuestionnaireParsingException(el, QUESTIONNAIRE_NOT_ROOT_ELEMENT);
        }

        return parseQuestionnaire(el);
    }

    private static Questionnaire parseQuestionnaire(Element questionnaire) throws QuestionnaireParsingException {
        List<QuestionnaireElement> questionnaireElements = new ArrayList<>();
        List<Constraint> constraints = new ArrayList<>();

        Optional<String> title = parseTitle(questionnaire);
        String id = parseAttributeOrElseThrow(questionnaire, ID_ATTR, Function.identity(),
                () -> new QuestionnaireParsingException(questionnaire, NO_ID));

        for (int i = 0; i < questionnaire.getChildNodes().getLength(); i++) {
            Node childNode = questionnaire.getChildNodes().item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && !childNode.getNodeName().equalsIgnoreCase(TITLE_TAG_NAME)) {
                if (childNode.getNodeName().equalsIgnoreCase(CONSTRAINTS_TAG_NAME)) {
                    constraints.addAll(parseConstraints((Element) childNode));
                    continue;
                }

                QuestionnaireElement q = parseQuestionnaireElement((Element) childNode);
                if (questionnaireElements.stream().anyMatch(x -> Objects.equals(x.getId(), q.getId())))
                    throw new QuestionnaireParsingException(questionnaire, ID_NOT_UNIQUE);

                questionnaireElements.add(q);
            }
        }

        return new Questionnaire(title.orElse(null), id, questionnaireElements, constraints);
    }

    private static QuestionnaireElement parseQuestionnaireElement(Element questionnaireElement)
            throws QuestionnaireParsingException {

        if (questionnaireElement.getTagName().equalsIgnoreCase(CHOICE_QUESTION_TAG_NAME)) {
            return parseChoiceQuestion(questionnaireElement);
        } else if (questionnaireElement.getTagName().equalsIgnoreCase(FREE_TEXT_QUESTION_TAG_NAME)) {
            return parseFreetextQuestion(questionnaireElement);
        } else if (questionnaireElement.getTagName().equalsIgnoreCase(NUMBER_QUESTION_TAG_NAME)) {
            return parseNumberQuestion(questionnaireElement);
        } else if (questionnaireElement.getTagName().equalsIgnoreCase(CATEGORY_TAG_NAME)){
            return parseCategory(questionnaireElement);
        } else {
            throw new QuestionnaireParsingException(questionnaireElement, UNKNOWN_ELEMENT);
        }
    }

    private static List<Constraint> parseConstraints(Element constraintsElement)
            throws QuestionnaireParsingException {

        List<Constraint> constraints = new ArrayList<>();

        for (int i = 0; i < constraintsElement.getChildNodes().getLength(); i++) {
            Node childNode = constraintsElement.getChildNodes().item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element childElement = (Element) childNode;
            if (childElement.getTagName().equalsIgnoreCase(ENABLED_IF_SELECTED_CONSTRAINT_TAG_NAME))
                constraints.add(parseEnabledIfSelectedConstraint(childElement));
        }

        return constraints;
    }

    private static EnabledIfSelectedConstraint parseEnabledIfSelectedConstraint(Element constraintElement)
            throws QuestionnaireParsingException {

        String constrainedQuestionId = parseAttributeOrElseThrow(constraintElement, CONSTRAINED_QUESTION_ID_ATTR, Function.identity(),
                () -> new QuestionnaireParsingException(constraintElement, CONSTRAINT_NO_CONSTRAINED_ID));

        String observedQuestionId = parseAttributeOrElseThrow(constraintElement, OBSERVED_QUESTION_ID_ATTR, Function.identity(),
                () -> new QuestionnaireParsingException(constraintElement, CONSTRAINT_NO_OBSERVED_ID));

        List<String> allowedIds = new ArrayList<>();

        for (int i = 0; i < constraintElement.getChildNodes().getLength(); i++) {
            Node childNode = constraintElement.getChildNodes().item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            Element childElement = (Element) childNode;

            if (childElement.getTagName().equalsIgnoreCase(ALLOWED_SELECTION_ITEM_TAG_NAME)) {
                allowedIds.add(childElement.getTextContent());
            }
        }

        return new EnabledIfSelectedConstraint(constrainedQuestionId, observedQuestionId, allowedIds);
    }

    private static Category parseCategory(Element questionnaireElement)
            throws QuestionnaireParsingException {

        String title = parseTitle(questionnaireElement)
                .orElseThrow(() -> new QuestionnaireParsingException(questionnaireElement, NO_TITLE));

        String id = parseAttributeOrElseThrow(questionnaireElement, ID_ATTR, Function.identity(),
                () -> new QuestionnaireParsingException(questionnaireElement, NO_ID));

        return new Category(title, id);
    }

    private static ChoiceQuestion parseChoiceQuestion(Element questionnaireElement)
            throws QuestionnaireParsingException {

        String title = parseTitle(questionnaireElement)
                .orElseThrow(() -> new QuestionnaireParsingException(questionnaireElement, NO_TITLE));

        List<Node> items = new ArrayList<>();
        for (int i = 0; i < questionnaireElement.getChildNodes().getLength(); i++) {
            Node n = questionnaireElement.getChildNodes().item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element e = (Element) n;
            if (e.getTagName().equalsIgnoreCase(CHOICE_ITEM_TAG_NAME)) {
                throw new QuestionnaireParsingException(e, CHOICE_QUESTION_ITEM_NOT_ENCLOSED_IN_ITEMS_TAG);
            } else if (e.getTagName().equalsIgnoreCase(CHOICE_ITEMS_TAG_NAME)) {
                List<Node> childNodes = IntStream.range(0, e.getChildNodes().getLength())
                        .mapToObj(e.getChildNodes()::item).collect(Collectors.toList());
                items.addAll(childNodes);
            }
        }

        List<ChoiceQuestionItem> choiceQuestionItems = new ArrayList<>();
        for (Node itemNode : items) {
            Optional<ChoiceQuestionItem> choiceQuestionItem = parseChoiceQuestionItem(itemNode);

            if (!choiceQuestionItem.isPresent())
                continue;

            if (choiceQuestionItems.stream().map(ChoiceQuestionItem::getId).anyMatch(i -> Objects.equals(i, choiceQuestionItem.get().getId())))
                throw new QuestionnaireParsingException(itemNode, ID_NOT_UNIQUE);

            choiceQuestionItems.add(choiceQuestionItem.get());
        }

        boolean multiSelectionAllowed = parseAttribute(questionnaireElement, CHOICE_QUESTION_ALLOWS_MULTI_SELECTION_ATTR,
                Boolean::valueOf, false);

        String id = parseAttributeOrElseThrow(questionnaireElement, ID_ATTR, Function.identity(),
                () -> new QuestionnaireParsingException(questionnaireElement, NO_ID));

        String defaultItemId = parseAttribute(questionnaireElement, CHOICE_QUESTION_DEFAULT_ITEM_ID_ATTR,
                Function.identity(), null);

        if (defaultItemId != null && choiceQuestionItems.stream().noneMatch(i -> defaultItemId.equals(i.getId())))
            throw new QuestionnaireParsingException(questionnaireElement, CHOICE_QUESTION_NO_DEFAULT_ITEM_WITH_ID);

        return new ChoiceQuestion(title, id, choiceQuestionItems, multiSelectionAllowed, defaultItemId);
    }

    private static Optional<ChoiceQuestionItem> parseChoiceQuestionItem(Node itemNode)
            throws QuestionnaireParsingException {

        if (itemNode.getNodeType() != Node.ELEMENT_NODE)
            return Optional.empty();

        Element el = (Element) itemNode;
        if (!el.getTagName().equalsIgnoreCase(CHOICE_ITEM_TAG_NAME))
            return Optional.empty();

        String id = parseAttributeOrElseThrow(el, ID_ATTR, Function.identity(),
                () -> new QuestionnaireParsingException(itemNode, NO_ID));

        boolean modifiable = parseAttribute(el, CHOICE_ITEM_IS_MODIFIABLE_ATTR,
                Boolean::valueOf, false);

        return Optional.of(new ChoiceQuestionItem(el.getTextContent().trim(), id, modifiable));
    }

    private static FreeTextQuestion parseFreetextQuestion(Element questionnaireElement)
            throws QuestionnaireParsingException {

        String title = parseTitle(questionnaireElement)
                .orElseThrow(() -> new QuestionnaireParsingException(questionnaireElement, NO_TITLE));

        boolean multiLineAllowed = parseAttribute(questionnaireElement, FREE_TEXT_QUESTION_MULTI_LINE_ALLOWED_ATTR,
                Boolean::valueOf, false);

        String id = parseAttributeOrElseThrow(questionnaireElement, ID_ATTR, Function.identity(),
                () -> new QuestionnaireParsingException(questionnaireElement, NO_ID));

        return new FreeTextQuestion(title, id, multiLineAllowed);
    }

    private static NumberQuestion parseNumberQuestion(Element questionnaireElement)
            throws QuestionnaireParsingException {

        String title = parseTitle(questionnaireElement)
                .orElseThrow(() -> new QuestionnaireParsingException(questionnaireElement, NO_TITLE));

        try {
            float stepSize = parseAttribute(questionnaireElement, NUMBER_QUESTION_STEP_SIZE_ATTR,
                    Float::parseFloat, 0F);

            float fromValue = parseAttribute(questionnaireElement, NUMBER_QUESTION_FROM_VALUE_ATTR,
                    Float::parseFloat, Float.MIN_VALUE);

            float toValue = parseAttribute(questionnaireElement, NUMBER_QUESTION_TO_VALUE_ATTR,
                    Float::parseFloat, Float.MAX_VALUE);

            NumberQuestion.Type type = parseAttribute(questionnaireElement, NUMBER_QUESTION_TYPE_ATTR,
                    NumberQuestion.Type::valueOf, NumberQuestion.Type.SLIDER);

            if (toValue <= fromValue)
                throw new QuestionnaireParsingException(questionnaireElement, TO_VALUE_LESS_THAN_OR_EQUAL_TO_FROM_VALUE);

            String id = parseAttributeOrElseThrow(questionnaireElement, ID_ATTR, Function.identity(),
                    () -> new QuestionnaireParsingException(questionnaireElement, NO_ID));

            return new NumberQuestion(title, id, type, fromValue, toValue, stepSize);
        } catch (NumberFormatException ex) {
            throw new QuestionnaireParsingException(questionnaireElement, NUMBER_FORMAT_EXCEPTION, ex);
        } catch (IllegalArgumentException ex) {
            throw new QuestionnaireParsingException(questionnaireElement, NUMBER_QUESTION_INVALID_TYPE, ex);
        }
    }

    private static <R> R parseAttribute(Element e, String attrName, Function<String, R> parseFunc, R defaultValue) {
        return e.hasAttribute(attrName) ? parseFunc.apply(e.getAttribute(attrName)) : defaultValue;
    }

    private static <R,T extends Throwable> R parseAttributeOrElseThrow(Element e, String attrName, Function<String, R> parseFunc,
                                                                       Supplier<T> throwableSupplier) throws T {
        if (!e.hasAttribute(attrName))
            throw throwableSupplier.get();
        return parseFunc.apply(e.getAttribute(attrName));
    }

    private static Optional<String> parseTitle(Node n) throws QuestionnaireParsingException {
        String title = null;
        for (int i = 0; i < n.getChildNodes().getLength(); i++) {
            Node child = n.getChildNodes().item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element childElement = (Element) child;
            if (!childElement.getTagName().equalsIgnoreCase(TITLE_TAG_NAME))
                continue;

            if (title != null)
                throw new QuestionnaireParsingException(n, MULTIPLE_TITLES);

            title = childElement.getTextContent();
        }
        return title == null ? Optional.empty() : Optional.of(title);
    }

}
