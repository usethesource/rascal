---
title: UnguardedAppend
---

.Synopsis
Append statement occurs outside a for/while statement.

.Syntax

.Types

.Function
       
.Usage

.Description
The [append]((Rascal:Statements-Append)) statement can be used inside a loop statement.
This error is generated when append occurs outside a loop.

Remedy: use List concatenation to append an element to a list outside a loop.

.Examples
This is the typical use of `append`:
```rascal-shell
for(int i <- [1..5]) append i*i;
```
Using append outside a loop gives an error:
```rascal-shell,error
append 3;
```


.Benefits

.Pitfalls

