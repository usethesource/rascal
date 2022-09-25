---
title: Authoring
---

#### Synopsis

Creating and writing a course for the Rascal Tutor.

#### Syntax

#### Types

#### Function

#### Description

The life cycle of a course consists of the following steps:

* A new course, say `MyCourse`, is created. This is achieved by:
  * Creating a subdirectory named `MyCourse` in the `courses` directory of the current Rascal project.
  * Creating a file `MyCourse/MyCourse.md`. This is the root concept of the new course.
* The contents of the course are created by populating the course with subconcepts of the root concept.
* A subconcept, say `CoolIdea` is created by:
  * Creating a subdirectory `CoolIdea` of its parent concept.
  * Creating a file `CoolIdea/CoolIdea.md` that describes the concept.
  * Alternatively, a file `CoolIdea/index.md` works too.
  * Renaming/moving/deleting concepts is done at the directory/file level.

Concepts are represented as directories for the following reasons:

* To represent subconcepts as subdirectories.
* To contain all figures and other files that are included in the concept. In this way:
  ** A complete concept can be easily moved or renamed as a single unit.
  ** Name clashes between included files per concept are avoided.

#### Examples

#### Benefits

* You can use your favourite Markdown editor and standard system commands to author a course.
* The output of the Tutor compiler is Markdown/HTML that you can process further with any tool you like
* Links to other concepts are all _relative_ to a root directory with all the courses. So `../../Course/A/index.md` would be a link generated from a link to `A` in `/Course/Z/index.md`. This makes it easy to include courses in a website at any subfolder.
* The index that is generated for every project with several courses can be reused by other projects to generate consistent cross-referencing. For example a project `flybytes` which depends on the `rascal` project which contains the `Library` course, could use `Library:IO` to refer to the documentation about the `IO` module in the standard library of Rascal.

#### Pitfalls

* There is no editing support or incremental course compilation available (yet).
* Special features like code execution and cross-referencing concepts are not implemented by generic Markdown editors, so you have to wait and see what happens until you compile the course.
* All images are referenced from `/assets/` so images have to be installed there on any website you want to include the documentation in.