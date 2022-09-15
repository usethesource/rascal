---
title: DateTime LessThanOrEqual
keywords:
  - <=

---

#### Synopsis

Less than or equal operator on datetime values.

#### Syntax

`Exp~1~ <= Exp~2~`

#### Types

//

| `Exp~1~`      | `Exp~2~`      | `Exp~1~ <= Exp~2~`  |
| --- | --- | --- |
| `datetime`     |  `datetime`    | `bool`                |


#### Function

#### Description

Yields `true` if the `datetime` value of Exp~1~ is earlier in time than the `datetime` value
of _Exp_~2~ or if the values of _Exp_~1~ and _Exp_~2~ are equal, and `false` otherwise.

#### Examples

```rascal-shell
$2010-07-15$ <= $2010-07-15$;
$2011-07-15$ <= $2010-07-14$;
```

#### Benefits

#### Pitfalls

