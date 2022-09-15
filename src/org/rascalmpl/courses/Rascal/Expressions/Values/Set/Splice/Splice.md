---
title: Set Splice
keywords:
  - ""*""
---

#### Synopsis

Splice the elements of a set in an enclosing set.

#### Syntax

#### Types


|`Exp` | `Exp~1~`|  `Exp~n~` | `{Exp~1~, ..., Exp, ..., Exp~n~}`  |
| --- | --- | --- | --- |
|`T`   | `T~1~`  |  `T~n~`   | `set[lub(T~1~, ..., T, ...,T~n~)]`     |


#### Function
       
#### Usage

#### Description

The operator `*` splices the elements of a set in an enclosing set.

#### Examples

Consider the following set in which the set `{10, 20, 30}` occurs as set element. It has as type `set[value]`:
```rascal-shell,continue
{1, 2, {10, 20, 30}, 3, 4};
```
The effect of splicing the same set element in the enclosing set gives a flat list of type `set[int]`:
```rascal-shell,continue
{1, 2, *{10, 20, 30}, 3, 4};
```
The same example can be written as:
```rascal-shell,continue
S = {10, 20, 30};
{1, 2, *S, 3, 4};
```

#### Benefits

#### Pitfalls

