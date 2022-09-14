---
title: UndeclaredModuleProvider
---

.Synopsis
A scheme is used in a location for wich no provider has been registered.

.Syntax

.Types

.Function
       
.Usage

.Description

[Locations]((Rascal:Values-Location)) provide very flexible ways to access files and external data sources.
There any many protocols (called _schemes_) that can be used (e.g., `file`, `http`, `home` and many others).
This error is generated when an unknown scheme is used.

Remedy: Use an existing scheme.

.Examples
Misspelling the scheme for Rascal's standard library (which is `std`) gives an error when the location is used:
```rascal-shell,error
import IO;
readFileLines(|standard:///demo/basic/Hello.rsc|);
```
This is fixed by using the proper scheme name:
```rascal-shell,continue,error
readFileLines(|std:///demo/basic/Hello.rsc|);
```

.Benefits

.Pitfalls

