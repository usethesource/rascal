---
title: "Boolean IsDefined"
keywords: "?"
---

.Synopsis
Test whether the value of an expression is defined.

.Syntax
`Exp ?`

.Types

//

| `Exp` | `Exp ?`  |
| --- | --- |
|  `T`   |   `bool`      |


.Function

.Description

If no exception is generated during the evaluation of _Exp_, 
the result is `true`. Otherwise, it is `false`.

.Examples

```rascal-shell
T = ("a" : 1, "b" : 2);
T["b"]?
T["c"]?
L = [10, 20, 30];
L[1]?
L[5]?
```

.Benefits

.Pitfalls

