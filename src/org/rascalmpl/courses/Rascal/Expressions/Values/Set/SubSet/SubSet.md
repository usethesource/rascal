---
title: Set SubSet
keywords:
  - "<="

---

#### Synopsis

Subset operator on set values.

#### Syntax

`Exp~1~ <= Exp~2~`

#### Types


| `Exp~1~`    |  `Exp~2~`    | `Exp~1~ <= Exp~2~`   |
| --- | --- | --- |
| `set[T~1~]` |  `set[T~2~]` | `bool`                 |


#### Function

#### Description

Yields `true` if the value of SetExp~1~ is a subset of the value of SetExp~2~, and `false` otherwise.

#### Examples

```rascal-shell
{1, 2, 3} <= {1, 2, 3, 4};
{1, 2, 3} <= {1, 2, 3};
```

#### Benefits

#### Pitfalls

