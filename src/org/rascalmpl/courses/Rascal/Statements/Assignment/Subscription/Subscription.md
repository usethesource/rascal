---
title: "Subscription"
keywords: "[,],="
---

#### Synopsis

Assign a single element of a structured value.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

Let _V_ be the current value of _Assignable_. The value of _Exp_~1~ is used as index in _V_ and 
the value of _Exp_~2~ replaces the original value at that index position. 
The result is a new value _V_' that is assigned to the _Assignable_.

#### Examples

```rascal-shell
```
Assignable has a list value:
```rascal-shell,continue
L = [10,20,30];
P = L;
L[1] = 200;
```
Observe that `P` is unchanged:
```rascal-shell,continue
P;
```
Assignable has a map value:
```rascal-shell,continue
M = ("abc": 1, "def" : 2);
M["def"] = 3;
```
Assignable has a tuple value:
```rascal-shell,continue
T = <1, "abc", true>;
T[1] = "def";
```
NOTE: See https://github.com/usethesource/rascal/issues/948

#### Benefits

#### Pitfalls

