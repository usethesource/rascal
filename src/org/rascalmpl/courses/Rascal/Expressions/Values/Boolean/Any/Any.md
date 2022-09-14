---
title: "Boolean Any"
keywords: "any"
---

.Synopsis
Any combination of argument values is true.

.Syntax
`any ( Exp~1~, Exp~2~, ... )`

.Types

//


| `Exp~1~` | `Exp~2~` | ... | `any ( Exp~1~, Exp~2~, ... )`  |
| --- | --- | --- | --- |
|`bool`     | `bool`    | ... | `bool`                           |


.Function

.Description
Yields `true` when at least one combination of values of _Exp_~i~ is true.

.Examples
```rascal-shell
any(int n <- [1 .. 10], n % 2 == 0);
```

.Benefits

.Pitfalls

