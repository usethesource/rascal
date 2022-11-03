---
title: Node FieldSelection
keywords:
  - "."
  - getter
  - get

---

#### Synopsis

Select a field from a node by its field name.

#### Syntax

`Exp . Name`

#### Types


| `Exp`                                 | `Name` | `Exp . Name` |
| --- | --- | --- |
|`node` |  `L~i~` | `T~i~`         |


#### Function

#### Description

Field selection applies to nodes with keyword fields.
_Exp_ should evaluate to a tuple with field _Name_ and returns the value of that field.
_Name_ stands for itself and is not evaluated.

#### Examples

```rascal-shell
tuple[int key, str val] T = <1, "abc">;
T.val;
```

#### Benefits

#### Pitfalls

