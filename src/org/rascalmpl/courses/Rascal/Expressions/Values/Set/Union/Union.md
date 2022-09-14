---
title: "Set Union"
keywords: "+"
---

.Synopsis
Union of two set values.

.Syntax

.Types

| `Exp~1~`    |  `Exp~2~`    | `Exp~1~ + Exp~2~`       |
| --- | --- | --- |
| `set[T~1~]` |  `set[T~2~]` | `set[lub(T~1~,T~2~)]`   |


.Description
The `+` operator computes set union if both operands are sets. If one of the operands is not a set, it acts as ((Set-Insert)) instead.

.Examples
```rascal-shell
{1, 2, 3} + {4, 5, 6};
{1,2,3} + {2,3,4};
{1, 2, 3} + {3};
{2} + { 2, 3, 4};
```

