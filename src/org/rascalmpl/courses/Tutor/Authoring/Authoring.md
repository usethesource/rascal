---
title: Authoring
---

.Synopsis
Creating and writing a course for the Rascal Tutor.

.Syntax

.Types

.Function

.Description
The life cycle of a course consists of the following steps:

* A new course, say `MyCourse`, is created. This is achieved by:
  ** Creating a subdirectory named `MyCourse` in the `courses` directory of the current Rascal project.
  ** Creating a file `MyCourse/MyCourse.concept`. This is the root concept of the new course.
* The contents of the course are created by populating the course with subconcepts of the root concept.
* A subconcept, say `CoolIdea` is created by:
  ** Creating a subdirectory `CoolIdea` of its parent concept.
  ** Creating a file `CoolIdea/CoolIdea.concept` that describes the concept.
  
* Renaming/moving/deleting concepts is done at the directory/file level.

Concepts are represented as directories for the following reasons:

* To represent subconcepts as subdirectories.
* To contain all figures and other files that are included in the concept. In this way:
  ** A complete concept can be easily moved or renamed as a single unit.
  ** Name clashes between included files per concept are avoided.

.Examples

.Benefits
You can use your favourite editor and standard system command to author a course.

.Pitfalls
There is no editing support or incremental course compilation available (yet).
