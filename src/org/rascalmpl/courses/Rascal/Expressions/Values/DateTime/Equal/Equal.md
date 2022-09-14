---
title: "DateTime Equal"
keywords: "=="
---

.Synopsis
Equality on datetime values.

.Syntax
`Exp~1~ == Exp~2~`

.Types

//

| `Exp~1~`      | `Exp~2~`      | `Exp~1~ == Exp~2~`  |
| --- | --- | --- |
| `datetime`     |  `datetime`    | `bool`                |


.Function

.Details

.Description
Yields `true` if both arguments are identical `datetime` values and `false` otherwise.

.Examples
```rascal-shell
$2010-07-15$ == $2010-07-15$;
$2010-07-15$ == $2010-07-14$;
```

.Benefits

.Pitfalls

