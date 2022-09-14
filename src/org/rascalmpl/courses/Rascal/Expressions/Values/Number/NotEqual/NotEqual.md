---
title: "Number NotEqual"
keywords: "!="
---

.Synopsis
Not equal operator on numeric values.

.Syntax
`Exp~1~ != Exp~2~`

.Types
| `Exp~1~`  |  `Exp~2~` | `Exp~1~ != Exp~2~`   |
| --- | --- | --- |
| `int`      |  `int`     | `bool`                 |
| `int`      |  `real`    | `bool`                 |
| `real`     |  `real`    | `bool`                 |


.Function

.Description
Yields `true` if the value of both arguments is numerically unequal, and `false` otherwise.

.Examples
```rascal-shell
12 != 13
12 != 12
12 != 13.0
12.0 != 13
3.14 != 3
3.14 != 3.14
```

.Benefits

.Pitfalls

