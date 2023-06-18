# Questionnaire File Specification

## Questionnaire File

Questionnaire files may only contain a single questionnaire. Each questionnaire must have an id. 

### Title 

The title is specified using `<Title>Your title here</Title>` and must be a child node of the element that it refers to. The following elements can have a title

- `Questionnaire` (optional)
- `Category` (mandatory)
- `NumberQuestion` (mandatory)
- `ChoiceQuestion` (mandatory)
- `FreeTextQuestion` (mandatory)

### Category

Each category must have an id. Categories can be placed between questions to indicate that the following questions belong the most recent category.

## Types of questions

Generally, each question must have an unique id among the other questions of the questionnaire.

### ChoiceQuestion

A choice question should contain an `Items` child element, which specifies the selectable items as childs. Each `Item` element must have an id (must be unique within the other items).

```
<ChoiceQuestion id="2.1">
  <Title>My title</Title>
  <Items>
    <Item id="0">Yes</Item>
    <Item id="1">No</Item>
  </Items>
</ChoiceQuestion>
  ``` 
### NumberQuestion

For a number question one can specify whether it should be displayed as a slider (`type="SLIDER"`) or as a text box (`type="BOX"`). Additionally, one can specify the minimum allowed value (`fromValue="1"`, default `Float.MIN_VALUE`), the maximum allowed value (`toValue="9"`, default `Float.MAX_VALUE`), and the step size (`stepSize=0"`, default value 0, `stepSize=0` means that its continuous, otherwise discrete).

```
<NumberQuestion id="7.1" fromValue="0" stepSize="1" toValue="9" type="SLIDER">
  <Title>My title</Title>
</NumberQuestion>
```

### FreeTextQuestion

For the free text question, you can specify whether multiple input lines are graphically supported (default: false). Note that this does not prevent the user from pasting in text with line breaks.
```
<FreeTextQuestion id="1" multiLineAllowed="true">
  <Title>My title</Title>
</FreeTextQuestion>
```
## Constraints

The constraints should be specified in a `<Constraints>` element (which needs to be a direct child of the questionnaire element). Currently the only supported constraint is `EnabledIfSelectedConstraint` (so the observed question must be a ChoiceQuestion).

### EnabledIfSelectedConstraint

The question that should be constrained must be specified using the `constrainedQuestionId`, while the question that is being obeserved must be specified using `observedQuestionId`. Only if one of the allowed items is selected in the ChoiceQuestion with id `observedQuestionId`, then the question with id `constrainedQuestionId` is enabled (can be answered).

```
<Constraints>
  <EnabledIfSelectedConstraint constrainedQuestionId="2.2" observedQuestionId="2.1">
    <AllowedItem>0</AllowedItem>
  </EnabledIfSelectedConstraint>
</Constraints>
```

## Example questionnaire

An example can be found [here](https://github.com/RUB-SE-LAB/23B08/blob/main/Moodivation/app/src/main/assets/questionnaires/main.en.questionnaire.xml).
