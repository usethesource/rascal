---
title: "Set NotEqual"
keywords: "!="
---

#### Synopsis

Not equal operator on set values.

#### Syntax

`Exp~1~ != Exp~2~`

#### Types


| `Exp~1~`    |  `Exp~2~`    | `Exp~1~ != Exp~2~`  |
| --- | --- | --- |
| `set[T~1~]` |  `set[T~2~]` | `bool`                |


#### Function

#### Description

Yields `true` if both arguments are unequal sets and `false` otherwise.

#### Examples

```rascal-shell
{1, 2, 3} != {3, 2, 1};
{1, 2, 3} != {1, 2};
```

#### Benefits

#### Pitfalls

