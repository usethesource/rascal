---
title: Reference to Concept
---

#### Synopsis

Refer to a concept in this course or another course.

#### Syntax

``````
((DisplayName))
((ParentConceptName-ConceptName))
((FullDashSeparatedPathTo-ConceptName))
((Course))
((Course:ConceptName))
((Course:ParentConceptName-ConceptName))
((Course:FullDashSeparatedPathTo-ConceptName))
((ModuleName))
((a::b::ModuleName))
((Course:a::b::ModuleName))
((module:ModuleName))
((module:a::b::ModuleName))
((Course:module:a::b::ModuleName))
((package:a::b))
((Course:package:a::b))
``````

#### Types

#### Function

#### Description

Above are all the ways a concept can be linked:
* The concept name, which is equal to its file name is leading.
* The parent concept name can be used to disambiguate
* Otherwise the full path to the root of the course can be used, where `/` is replaced by `-`
* Rascal modules can be referenced by name and by their fully qualified name
* To disambiguate modules from concept names, the `module` and `package` prefixes come in handy.


#### Examples

The concept name of this concept is `ReferenceToConcept` while its title is `Reference to Concept` (note the spaces).

We can create a reference to the `InlineMarkup` concept in the current course in the following ways:

* `\((Inline Markup))` (using the display name) gives ((Inline Markup)).
* `\((Markup-InlineMarkup))` (using its parent concept name and concept name) gives ((Markup-InlineMarkup)).
* `\[see inline markup]((Tutor:Markup-InlineMarkup))` gives [see inline markup]((Tutor:Markup-InlineMarkup))

Here is a reference to another course:

* `\[If statement]((Rascal:Statements-If))` gives [If statement]((Rascal:Statements-If)).

#### Benefits

* Links are checked at compile-time
* Ambiguous links are first tried in the local course scope before an ambiguity is reported
* Ambiguous links have helpful error messages with the options to choose from 

#### Pitfalls



