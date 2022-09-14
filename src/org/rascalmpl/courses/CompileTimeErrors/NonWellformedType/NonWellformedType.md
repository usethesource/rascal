---
title: NonWellformedType
---

.Synopsis
A type in a declaration is not wellformed.

.Syntax

.Types

.Function
       
.Usage

.Description
Types should respect some structural rules and this error signals a violation of these rules.
Examples of violations are:

*  a non-parametric type has parameters.
*  a parametric type has an incorrect number of parameters.


Remedy: correct the type.

.Examples
```rascal-shell,error
bool[int] x;
list[int,str] l;
map[str, int, int]  m;
set[int,str] s;
```
.Benefits

.Pitfalls

