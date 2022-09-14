---
title: "List NotEqual"
keywords: "!="
---

.Synopsis
Not equal operator on lists.

.Syntax
`Exp~1~ != Exp~2~`

.Types

//

| `Exp~1~`     |  `Exp~2~`     | `Exp~1~ != Exp~2~`  |
| --- | --- | --- |
| `list[T~1~]` |  `list[T~2~]` | `bool`                |


.Function

.Description
Yields `true` if both arguments are unequal lists and `false` otherwise.

.Examples
```rascal-shell
[1, 2, 3] != [3, 2, 1];
[1, 2, 3] != [1, 2, 3];
```

.Benefits

.Pitfalls

