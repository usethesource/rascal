---
title: Tuple LessThanOrEqual
keywords:
  - <=

---

#### Synopsis

Less than or equal operator on tuple values.

#### Syntax

`Exp~1~ <= Exp~2~`

#### Types


| `Exp~1~`                      |  `Exp~2~`                      | `Exp~1~ <= Exp~2~`  |
| --- | --- | --- |
| `tuple[ T~11~, T~12~, ... ]` |  `tuple[ T~21~, T~22~, ... ]` | `bool`                |


#### Function

#### Description

Yields `true` if 

*  both tuples are equal, or
*  the left-most element in the tuple value of _Exp~1~_ that differs from the corresponding element in the tuple 
value of _Exp_~2~ is less than that element in _Exp_~2~.


Otherwise the result if `false`.

#### Examples

```rascal-shell
<1, "abc", true> <= <1, "abc", true>;
<1, "abc", true> <= <1, "def", true>;
```

#### Benefits

#### Pitfalls

