---
title: UnitializedVariable
---

.Synopsis
Use of a variable that has not been initialized.

.Syntax

.Types

.Function
       
.Usage

.Description
A [variable]((Rascal:Declarations-Variable)) has to be initialized before it can be used.
This error is generated when an uninitialzed variable is used.

Remedy: assign a value to the variable before its use:

.Examples
Using the uninitialized variable `x` gives an error:
```rascal-shell,error
x + 5;
```
This can be avoided by first initializing `x`:
```rascal-shell,continue,error
x = 3;
x + 5;
```

.Benefits

.Pitfalls

