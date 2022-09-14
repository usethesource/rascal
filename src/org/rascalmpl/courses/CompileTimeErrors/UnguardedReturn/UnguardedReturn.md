---
title: UnguardedReturn
---

.Synopsis
A return statement occurs outside a function body.

.Syntax

.Types

.Function
       
.Usage

.Description
A [return]((Rascal:Statements-Return)) statement is used to return a value from a function.
It is an error to use it outside a function body.

.Examples
```rascal-shell
int triple(int n) { return 3 * n; }
triple(5);
```
Using return outside a function body gives an error:
```rascal-shell,error
return 3;
```
.Benefits

.Pitfalls

