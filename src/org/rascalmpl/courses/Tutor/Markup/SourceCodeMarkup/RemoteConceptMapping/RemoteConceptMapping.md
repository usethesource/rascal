---
title: Remote Concept Mapping
---

.Synopsis
Include the concepts in a Rascal source module in a course.

.Syntax

.Types

.Function
       
.Usage

.Description

Each Rascal source file defines a module `M` that may declare concepts `M/c__~1~`, `M/c__~2~`, ....
as indicated by embedded `@doc` annotations.
How are these concepts included in a Tutor course?

The answer is a remote concept file.

Where an ordinary concept `C` is defined by a subdirectory `C` and a concept file `C/C.concept` 
a remote concept is defined by `C/C.remote` that contains the location of the source file, say `CLoc`.

`Cloc` will be parsed, all its concepts will be extracted and all these
concepts become subconcepts of `C`. 

Moving the documentation for a library file is as simple as 
moving `C` to another position in the concept hierarchy.

.Examples
The `DateTime` library module is defined in the [Libraries]((Libraries)) course by
the subdirectory `Libraries/Prelude/DateTime` and the file `DateTime/DateTime.remote`.
The latter contains a single line with the location of the DateTime module:

```rascal
|std:///DateTime.rsc|
```


This establishes, for instance, that the function `now` that is declared in the `DateTime.rsc` library file
will become the concept `Libraries/Prelude/DateTime/now`.

.Benefits

.Pitfalls

*  Be carefull to avoid spaces, since the value reader does not like them.

