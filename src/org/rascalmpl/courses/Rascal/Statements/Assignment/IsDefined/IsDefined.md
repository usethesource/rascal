---
title: "IsDefined"
keywords: "?"
---

.Synopsis

Assign but replace if value is not defined.

.Syntax

.Types

.Function
       
.Usage

.Description

First the value of _Exp_~1~ is determined and if that is defined it is assigned to _Assignable_. 
Otherwise, the value of _Exp_~2~ is assigned to _Assignable_.

Values which can be undefined are:
* in ((Values-Map))s where the key is _not_ set
* values of ((Annotation Declarations)) which are not present.
* values of ((Declaratons-Function))'s keyword parameters which have not been provided, but are set to default.
* values of ((Constructor))'s keyword parameters which have not been provided, but are computed by defaults.

No other values can be used in an undefined state, so the ? operator does not make sense on undefined or uninitialized variables for example.

.Examples
```rascal-shell
M = ("Andy": 1, "Brian" : 2);
```
Using an `isDefined` assignable can we increment a non-existing entry:
```rascal-shell,continue
M["SomebodyElse"] ? 0 += 1;
M["SomebodyElse"];
```
And if we increment an existing entry the ? has no effect:
```rascal-shell,continue
M["Andy"] ? 0 += 1;
M["Andy"]
```

.Benefits

* short notation that inline initialization of map values, keyword fields or annotations without having to write a lot of boilerplate if-then-else statements. 

.Pitfalls

