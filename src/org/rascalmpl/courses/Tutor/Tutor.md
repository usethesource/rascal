---
title: Tutor
---

#### Synopsis

The RascalTutor compiler can be used to create, maintain and follow (interactive) documentation.

#### Syntax

#### Types

#### Function

#### Description

The RascalTutor is an interactive ((Authoring)) and learning environment intended to create and follow interactive courses related to the Rascal language.
It is based on the following principles and ideas:

* The basic notion is a ((Concept)). Each concept has a _name_ and contains a fixed set of subsections that describe it.
* A course is a _concept tree_:
  The central subject of the course is the root of the concept tree, and all subtrees further explain their parent concept.
* A Rascal code module is a concept as well. The declarations it contains are not sub-concepts but rather sub-sections of that concept.
* A folder with Rascal modules is also a concept. If it has an `index.md` file this is used to document it, otherwise an `index.md` file is generated.

A _student_ using a course can:

* Browse through the table of contents of the concept tree of the course.
* Search for concepts and specific terms using a search box.
* Read individual concepts.
* Follow links to other concepts.

An _author_ of a course can:

* Create a new course.
* Create a new concept in a course.
* Edit a concept directly using a standard text editor.
* Add code examples that are actually executed at "compile time" of the course. The code examples either simulate the interaction with the Rascal REPL, or they are just highlighted code.
* Recompile the course.
* Inspect the warnings that are generated for the whole course in order to 
  control the quality of the concept descriptions.
* Create links between concepts (in different courses)
* Inline images in the same folder/directory as the concept
* USe Rascal code to create (static) visuals

  (((TODO:fix asciidoctor ref)))
The actual markup used is an extension of Docusaurus, see https://docusaurus.io/ and
in most cases we directly refer to 

* https://docusaurus.io/docs/markdown-features for the "commonmark" compliant features of Docusaurus
* https://docusaurus.io/docs/markdown-features/react for the MDX/React extensibility features of Docusaurus

The following topics will be described here:
(((TOC)))

#### Examples

#### Benefits

#### Pitfalls

