---
title: Tutor
sidebar_position: 11
---

#### Synopsis

The Rascal Tutor compiler can be used to create, maintain and follow (interactive) documentation.

#### Syntax

#### Types

#### Function

#### Description

The RascalTutor is an interactive ((Authoring)) and learning environment intended to create and follow interactive courses related to the Rascal language.
It is based on the following principles and ideas:

* [Write]((Authoring)) standard Markdown in normal files and folders that can be edited using any Markdown editor.
* Generates Docusauris Markdown files in a file hierarchy that can be included easily in a static markdown website.
* The basic notion is a ((Concept)). Each concept has a _name_ and contains a fixed set of subsections that describe it.
* A course is a _concept tree_:
  The central subject of the course is the root of the concept tree, and all subtrees further explain their parent concept.
* A Rascal code module is a ((Concept)) to the tutur compiler as well, in order to provide ((API)) documentation for every Rascal module. The declarations it contains are not sub-concepts but rather sub-sections of that concept.
* A folder with Rascal modules is also a ((Concept). If it has an `index.md` file this is used to document it, otherwise an `index.md` file is generated.

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
* Use Rascal code to create (static) visuals

The actual markup used is an extension of Docusaurus, see https://docusaurus.io/ and
in most cases we directly refer to 

* https://docusaurus.io/docs/markdown-features for the "commonmark" compliant features of Docusaurus
* https://docusaurus.io/docs/markdown-features/react for the MDX/React extensibility features of Docusaurus

The following topics will be described here:

(((TOC)))

#### Examples

#### Benefits

* The tutor compiler generates docusaurus-compatible markdown, which is general Markdown unless people write text that depends on docusaurus-specific extensions. This means that downstream processing can be done by many different Markdown tools.
* The input syntax is also very close to standard Markdown, so typical Markdown editors will do a good job in helping you write documentation. We also selected the `.md` extension for this purpose.
* The tutor compiler allows to include runnable code examples that must succeed in order for to the documentation to be released. This makes the documentation another source of valuable tests for the documented Rascal code, and it guarantees that users can copy paste from the examples without hazard.
* **privacy** Tutor does not send or retain information about the user at the server side. Even interactive question answering and the progress that is measured are stored client-side only.

#### Pitfalls

* We have to run the tutor compiler manually to find out about possible errors. There is no IDE support yet.
* The Tutor compiler is not incremental yet. It will re-compile everything from scratch even if nothing has changed.
* Downstream tools, such as Docusaurus, may detect issues that the tutor compiler does not detect. For example broken links that are not ((Concept)) links will not be detected early. This means you may have to go back and fix the documentation, release it in a `jar` and then try the downstream tool again.
* The interactive ((QuestionMarkup)) part of the compiler is currently under maintenance and therefore unavailable.