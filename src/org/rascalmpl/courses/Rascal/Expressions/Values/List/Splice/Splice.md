---
title: "List Splice"
keywords: "*"
---

#### Synopsis

Splice the elements of a list in an enclosing list.

#### Syntax

#### Types

//


|`Exp` | `Exp~1~`|  `Exp~n~` | `[Exp~1~, ..., Exp, ..., Exp~n~]`  |
| --- | --- | --- | --- |
|`T`   | `T~1~`  |  `T~n~`   | `list[lub(T~1~, ..., T, ...,T~n~)]`     |


#### Function
       
#### Usage

#### Description

The operator `*` splices the elements of a list in an enclosing list.

#### Examples

Consider the following list in which the list `[10, 20, 30]` occurs as list element. It has as type `list[value]`:
```rascal-shell
[1, 2, [10, 20, 30], 3, 4];
```
The effect of splicing the same list element in the enclosing list gives a flat list of type `list[int]`:
```rascal-shell,continue
[1, 2, *[10, 20, 30], 3, 4];
```
The same example can be written as:
```rascal-shell,continue
L = [10, 20, 30];
[1, 2, *L, 3, 4];
```

#### Benefits

in which nested lists are handled.

#### Pitfalls

