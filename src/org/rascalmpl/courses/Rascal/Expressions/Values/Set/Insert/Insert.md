---
title: "Set Insert"
keywords: "+"
---

.Synopsis
Add an element to a set.

.Syntax
`Exp~1~ + Exp~2~`

.Types


| `Exp~1~`    |  `Exp~2~`    | `Exp~1~ + Exp~2~`       |
| --- | --- | --- |
| `set[T~1~]` |  `T~2~`      | `set[lub(T~1~,T~2~)]`   |
| `T~1~`      |  `set[T~2~]` | `set[lub(T~1~,T~2~)]`   |


.Function

.Details

.Description

The `+` operator will add elements to sets.

.Examples
```rascal-shell
{1, 2, 3} + 4;
1 + { 2, 3, 4};
{1} + 1;
1 + {1};
```

.Benefits

.Pitfalls

*  if both operands of `+` are a set then it acts as ((Set-Union)).

