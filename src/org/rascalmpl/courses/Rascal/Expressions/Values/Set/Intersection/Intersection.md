---
title: "Set Intersection"
keywords: "&"
---

.Synopsis
Intersection of two sets.

.Syntax
`Exp~1~ & Exp~2~`

.Types


| `Exp~1~`    |  `Exp~2~`      | `Exp~1~ & Exp~2~`      |
| --- | --- | --- |
| `set[T~1~]` |  `set[T~2~]`   | `set[lub(T~1~,T~2~)]`  |


.Function

.Description
Returns the intersection of the two set values of _Exp_~1~ and _Exp_~2~.
The intersection consists of the common elements of both sets.

.Examples
```rascal-shell
{1, 2, 3, 4, 5} & {4, 5, 6};
```

.Benefits

.Pitfalls

