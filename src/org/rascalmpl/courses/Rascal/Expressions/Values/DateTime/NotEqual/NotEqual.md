---
title: "DateTime NotEqual"
keywords: "!="
---

.Synopsis
Not equal operator on datetime values.

.Syntax
`Exp~1~ != Exp~2~`

.Types
| `Exp~1~`      | `Exp~2~`      | `Exp~1~ != Exp~2~`  |
| --- | --- | --- |
| `datetime`     |  `datetime`    | `bool`                |


.Function

.Description
Yields `true` if both arguments are different `datetime` values and `false` otherwise.

.Examples
```rascal-shell
$2010-07-15$ != $2010-07-14$;
$2010-07-15$ != $2010-07-15$;
```

.Benefits

.Pitfalls

