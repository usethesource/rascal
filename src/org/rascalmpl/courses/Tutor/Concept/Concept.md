---
title: Concept
details:
  - Name
  - Synopsis
  - Syntax
  - Types
  - Function
  - Details
  - Description
  - Examples
  - Benefits
  - Pitfalls
---

#### Synopsis

A _concept_ is the basic building block of a course. 

#### Syntax

* `module a/b/Concept` in `a/b/Concept.rsc`
* Or a file `a/b/Concept/Concept.md`, `a/b/Concept/index.md`:
   ``````
   ---
   title: Concept Title
   keywords:
      - keyword1
      - keyword2
   ---

   #### Synopsis
   
   This is the synopsis.

   #### Description

   #### Types

   #### Function

   #### Examples

   #### Benefits

   #### Pitfalls
   ``````

All sections are optional, but not the title header. It is always recommended to have at least a Synopsis and some Examples. Empty sections are removed by the preprocessor.

#### Description

A concept describes a separate entity (idea, artefact, function, rule) that is relevant in the scope of the course in which it appears.
It consists of the named sections above that are described separately. 
Each section starts with a keyword that should appear at the begin of a line.

Here is a brief summary of the sections of a concept:
(((TOC)))


