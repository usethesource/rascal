---
title: "String Concatenation"
keywords: "+"
---

.Synopsis
Concatenate two strings.

.Syntax
`Exp~1~ + Exp~2~`

.Types


| `Exp~1~` | `Exp~2~` | `Exp~1~ + Exp~2~`  |
| --- | --- | --- |
| `str`     | `str`     | `str`                |


.Function

.Details

.Description

Concatenates the string values of _Exp_~1~ and _Exp_~2~.

Note that to concatenate other types of values into a string, you can use ((Values-String)) interpolation.

.Examples
```rascal-shell
"abc" + "def";
```

.Benefits

.Pitfalls

