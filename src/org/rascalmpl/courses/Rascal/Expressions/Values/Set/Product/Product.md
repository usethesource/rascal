---
title: "Set Product"
keywords: "*"
---

.Synopsis
The product of two set values.

.Syntax
`Exp~1~ * Exp~2~`

.Types


| `Exp~1~`    |  `Exp~2~`    | `Exp~1~ * Exp~2~`  |
| --- | --- | --- |
| `set[T~1~]` |  `set[T~2~]` | `rel[T~1~,T~2~]`   |


.Function

.Details

.Description
Yields a relation resulting from the product of the values of _Exp_~1~ and _Exp_~2~. It contains a tuple for each combination of values from both arguments.

.Examples
```rascal-shell
{1, 2, 3} * {4, 5, 6};
```
A card deck can be created as follows:
```rascal-shell
{"clubs", "hearts", "diamonds", "spades"} * {1,2,3,4,5,6,7,8,9,10,11,12,13};
```

.Benefits

.Pitfalls

