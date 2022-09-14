---
title: MissingReturn
---

.Synopsis
A return statement is missing from a function body.

.Syntax

.Types

.Function
       
.Usage

.Description
Functions return some value (except functions that have return type `void`).
This error is generated when a function body does not return a value.

Remedies:

*  Add a [Rascal:Return] statement to the function body.
*  Rewrite the function so that the function body becomes a single expression and you can use the abbreviated function format, see [$Rascal:Declarations/Function].

.Examples
Here is an incorrect definition of function `triple`:
```rascal-shell,error
int triple(int x) {
   x * 3;
}
triple(5)
```
It should look like this:
```rascal-shell
int triple(int x) {
   return x * 3;
}
triple(5)
```
This is another solution using the abbreviated function format:
```rascal-shell
int triple(int x) = x * 3;
triple(5)
```

.Benefits

.Pitfalls

