---
title: "String GreaterThan"
keywords: ">"
---

.Synopsis
Greater than operator on string values.

.Syntax
`Exp~1~ > Exp~2~`

.Types


| `Exp~1~` | `Exp~2~` | `Exp~1~ > Exp~2~`  |
| --- | --- | --- |
| `str`     |  `str`    | `bool`                |


.Function

.Description
Yields `true` if the string value of _Exp_~1~ is strictly lexicographically greater
than the string value of _Exp_~2~, and `false` otherwise.

.Examples
```rascal-shell
"abcdef" > "abc";
"defghi" > "abcdef";
"a" > "abc";
```

.Benefits

.Pitfalls

