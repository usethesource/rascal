---
title: List in
keywords:
  - in

---

#### Synopsis

Membership test on list elements.

#### Syntax

`Exp~1~ in Exp~2~`

#### Types

//

| `Exp~1~`           |  `Exp~2~`      | `Exp~1~ in Exp~2~`  |
| --- | --- | --- |
| `T~1~`  <: `T~2~` |  `list[T~2~]`  | `bool`               |


#### Function

#### Description

Yields `true` if the value of Exp~1~ occurs as element in the value of Exp~2~ and `false` otherwise. 
The type of _Exp_~1~ should be compatible with the element type of _Exp_~2~.

#### Examples

```rascal-shell
2 in [1, 2, 3];
4 in [1, 2, 3];
```

#### Benefits

#### Pitfalls

