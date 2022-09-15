---
title: Number Conditional
keywords:
  - "?"
  - ":"

---

#### Synopsis

Conditional expression for numeric values.

#### Syntax

`Exp~1~ ? Exp~2~ : Exp~3~`

#### Types


| `Exp~1~`   | `Exp~2~`  |  `Exp~3~` | `Exp~1~ ? Exp~2~ : Exp~3~`   |
| --- | --- | --- | --- |
|  `bool`     | `int`      |  `int`     | `int`                           |
|  `bool`     | `int`      |  `real`    | `real`                          |
|  `bool`     | `real`     |  `real`    | `real`                          |


#### Function

#### Description

If the value of _Exp_ is `true` then the value of _Exp_~1~ else the value of _Exp_~2~.

#### Examples

```rascal-shell
(3 > 2) ? 10 : 20
(3 > 20) ? 10 : 20
```

#### Benefits

#### Pitfalls

