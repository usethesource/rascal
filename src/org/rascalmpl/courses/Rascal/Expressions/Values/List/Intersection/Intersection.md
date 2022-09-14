---
title: "List Intersection"
keywords: "&"
---

.Synopsis
Intersection of two lists.

.Syntax
`Exp~1~ & Exp~2~`

.Types

//

| `Exp~1~`     |  `Exp~2~`      | `Exp~1~ & Exp~2~`       |
| --- | --- | --- |
| `list[T~1~]` |  `list[T~2~]`  | `list[lub(T~1~,T~2~)]`  |


.Function

.Details

.Description
Returns the intersection of the two list values of  _Exp_~1~ and _Exp_~2~, i.e.,
the list value of _Exp_~1~ with all elements removed that do not occur in the list value of _Exp_~2~.

.Examples
```rascal-shell
[1, 2, 3, 4, 5] & [4, 5, 6];
```

.Benefits

.Pitfalls

