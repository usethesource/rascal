---
title: Title
---

#### Synopsis

The display (presentation) name of a concept.

#### Syntax

```
---
title: _ConceptName_
---
```

#### Description

A concept has actually three names:

* Its file name, i.e., the name of the `.md` file in which the concept is described.
* Its _full_ name, the path name from the root concept to the current concept file.
* Its displayed title, its name  as its appears as section heading and in the table of contents.

The title metadata in the file header defines the presentation name of a concept and may be an arbitrary text. 

Concept names are used to refer to other concepts: when unambiguous its file name or display name can be used to refer to another concept, otherwise a sufficient part of its full name has to be given to make it unambiguous.

The title is *mandatory* for every concept file.

#### Examples

The first line of this concept is:

``````
---
title: What a title
---
``````



