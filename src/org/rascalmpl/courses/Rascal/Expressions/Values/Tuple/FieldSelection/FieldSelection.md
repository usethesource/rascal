---
title: Tuple FieldSelection
keywords:
  - "."
  - getter
  - get

---

#### Synopsis

Select a field from a  node by its field name.

#### Syntax

`Exp . Name`

#### Types


| `Exp`                                 | `Name` | `Exp . Name` |
| --- | --- | --- |
|`tuple[T~1~ L~1~, ..., T~n~ L~n~`] | L~i~ | T~i~  |


#### Function

#### Description

Field selection applies to nodes with names keyword fields.
_Exp_ should evaluate to a node with field _Name_ and returns the value of that field.
_Name_ stands for itself and is not evaluated.

#### Examples

```rascal-shell
tuple[int a, str b] T = <1,"hello">;
T.a
T.b
```

#### Benefits

#### Pitfalls

