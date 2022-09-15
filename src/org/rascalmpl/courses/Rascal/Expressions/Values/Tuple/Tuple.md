---
title: Tuple
keywords:
  - <
  - >

---

#### Synopsis

Tuple values.

#### Syntax

`< Exp~1~, Exp~2~, ... >`

#### Types


| `Exp~1~`  | `Exp~2~`  |  ...  | `< Exp~1~, Exp~2~, ... >`  |
| --- | --- | --- | --- |
| `T~1~`    |  T~2~     | ...   | `tuple[T~1~, T~2~, ... ]`  |


#### Function

#### Description

A tuple is a sequence of elements with the following properties:

*  Each element in a tuple (may) have a different type.

*  Each element of a tuple may have a label that can be used to select that element of the tuple.

*  Each tuple is fixed-width, i.e., has the same number of elements.


Tuples are represented by the type `tuple[T~1~ L~1~, T~2~ L~2~, ...]`, 
where _T_~1~, _T_~2~, ... are arbitrary types and _L_~1~, _L_~2~, ... are optional labels. 

The following operators are provided for tuples:
(((TOC)))

#### Examples

```rascal-shell
tuple[str first, str last, int age] P = <"Jo","Jones",35>;
P.first;
P.first = "Bo";
```

#### Benefits

#### Pitfalls

