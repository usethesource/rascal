---
title: Reference to Concept
---

.Synopsis
Refer to a concept in this course or another course.

.Syntax
* `(( DisplayName ))`
* `(( ParentConceptName-ConceptName ))`
* `\[LinkText]((Course))`
* `\[LinkText]((Course:ParentConceptName-ConceptName))`

.Types

.Function

.Description

Recall that a concept is known under two names, its _concept name_ (the file name of the concept) and its _display name_ (the name as displayed in the text).

The first two forms create a link to a concept in the current course.

The remaining two forms create a link to a concept in another course.

The first creates a link to a complete course, the second to a specific concept in an external course.


.Examples

The concept name of this concept is `ReferenceToConcept` while its display name is `Reference to Concept` (note the spaces).

We can create a reference to the `InlineMarkup` concept in the current course in the following ways:

* `\((Inline Markup))` (using the display name) gives ((Inline Markup)).
* `\((Markup-InlineMarkup))` (using its parent concept name and concept name) gives ((Markup-InlineMarkup)).
* `\[see inline markup]((Tutor:Markup-InlineMarkup))` gives [see inline markup]((Tutor:Markup-InlineMarkup))

Here is a reference to another course:

* `\[If statement]((Rascal:Statements-If))` gives [If statement]((Rascal:Statements-If)).

.Benefits

.Pitfalls
Note the `/` before the course name in refernces to another course.

