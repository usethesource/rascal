---
title: NonVoidTypeRequired
---

.Synopsis

A type other than `void` is needed.

.Syntax

.Types

.Function
       
.Usage

.Description

This error is generated when a value is needed, so an expression of any type but the `void` type.

The most prominent examples are splicing for 
[list]((Rascal:List-Splice)) and [set]((Rascal:Set-Splice)).

Remedy: replace the expression of type `void` by an expression that computes a value.

.Examples
First define a dummy function that returns void:
```rascal-shell,error
void dummy() { return; }
[1, *dummy(), 2]
{1, *dummy(), 2}
```
A solution could be:

```rascal-shell
int dummy() { return 17; }
[1, *dummy(), 2]
{1, *dummy(), 2}
```

.Benefits

.Pitfalls

