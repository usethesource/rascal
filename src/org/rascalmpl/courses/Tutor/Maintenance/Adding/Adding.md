---
title: Adding
---

#### Synopsis

Add a concept to a course

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

There are basically two scenarios for adding a new concept, depending on where its contents come from.

If the concept is part of a "course":

* Select a concept that is suitable as parent for the new concept.
* Create a subdirectory, say `SubConcept` for the new subconcept.
* Create the file `SubConcept/SubConcept.md` or `SubConcept/index.md` using your favourite editor.
* Add the name and other information about the new concept.
* Save the file.

If a concept is a Rascal source module:

* Simply write the Rascal module in `folder/MyModule.rsc`
* Add @synopsis, @description, @examples, etc. tags to every notable declaration in the module
* Save the file.

If a concept is a ((Concept)) or ((Rascal:Module)) container only:

* Create the folder `MyConcept`
* Add new `SubConcept`s as above or new ((Rascal:Module))s, as above.
* an `index.md` file will be generated for the folder

#### Examples

#### Benefits

#### Pitfalls

